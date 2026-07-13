package ru.gorbunov.vocabulary.backend.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import ru.gorbunov.vocabulary.backend.repo.cassandra.model.WordCassandraDTO
import ru.gorbunov.vocabulary.backend.repo.cassandra.model.WordPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.common.repo.DbWordFilterRequest
import ru.gorbunov.vocabulary.common.repo.DbWordIdRequest
import ru.gorbunov.vocabulary.common.repo.DbWordRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.common.repo.DbWordsResponseOk
import ru.gorbunov.vocabulary.common.repo.IDbWordResponse
import ru.gorbunov.vocabulary.common.repo.IDbWordsResponse
import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.common.repo.WordRepoBase
import ru.gorbunov.vocabulary.common.repo.errorEmptyId
import ru.gorbunov.vocabulary.common.repo.errorNotFound
import ru.gorbunov.vocabulary.common.repo.errorRepoConcurrency
import ru.gorbunov.vocabulary.repo.common.IRepoWordInitializable
import java.net.InetAddress
import java.net.InetSocketAddress
import java.util.UUID

class RepoWordCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val dc: String = "dc1",
    private val randomUuid: () -> String = { UUID.randomUUID().toString() },
) : WordRepoBase(), IRepoWord, IRepoWordInitializable {
    
    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(WordPartOfSpeech::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter(dc)
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .withKeyspace(keyspaceName)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private val dao by lazy {
        mapper.wordDao(keyspaceName, WordCassandraDTO.TABLE_NAME)
    }

    fun clear() = dao.deleteAll()

    override fun save(words: Collection<VcblWord>): Collection<VcblWord> = runBlocking {
        // Запускаем все запросы параллельно в текущем корутин-контексте
        words.map { word ->
            dao.create(WordCassandraDTO(word)).asDeferred()
        }.awaitAll()
        words
    }

    override suspend fun createWord(rq: DbWordRequest): IDbWordResponse = tryWordMethod {
        val new = rq.word.copy(id = VcblWordId(randomUuid()), lock = VcblWordLock(randomUuid()))
        dao.create(WordCassandraDTO(new)).await()
        DbWordResponseOk(new)
    }

    override suspend fun readWord(rq: DbWordIdRequest): IDbWordResponse = tryWordMethod {
        if (rq.id == VcblWordId.NONE) return@tryWordMethod errorEmptyId
        val res = dao.read(rq.id.asString()).await() ?: return@tryWordMethod errorNotFound(rq.id)
        DbWordResponseOk(res.toWordModel())
    }

    override suspend fun updateWord(rq: DbWordRequest): IDbWordResponse = tryWordMethod {
        val idStr = rq.word.id.asString()
        val prevLock = rq.word.lock.asString()
        val new = rq.word.copy(lock = VcblWordLock(randomUuid()))
        val dto = WordCassandraDTO(new)

        val res: AsyncResultSet = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(WordCassandraDTO.COLUMN_LOCK) }
            ?.getString(WordCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            // Два варианта почти эквивалентны, выбирайте который вам больше подходит
            isSuccess -> DbWordResponseOk(new)
            // res.wasApplied() -> DbWordResponse.success(dao.read(idStr).await()?.toWordModel())
            resultField == null -> errorNotFound(rq.word.id)
            else -> errorRepoConcurrency(
                oldWord = dao.read(idStr).await()?.toWordModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was denied for update but the same object was not found in db at further request"
                ),
                expectedLock = rq.word.lock
            )
        }
    }

    override suspend fun deleteWord(rq: DbWordIdRequest): IDbWordResponse = tryWordMethod {
        val idStr = rq.id.asString()
        val prevLock = rq.lock.asString()
        val oldWord = dao.read(idStr).await()?.toWordModel() ?: return@tryWordMethod errorNotFound(rq.id)

        val res = dao.delete(idStr, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(WordCassandraDTO.COLUMN_LOCK) }
            ?.getString(WordCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            isSuccess -> DbWordResponseOk(oldWord)
            resultField == null -> errorNotFound(rq.id)
            else -> errorRepoConcurrency(
                dao.read(idStr).await()?.toWordModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was successfully read but was denied for delete"
                ),
                rq.lock
            )
        }
    }

    override suspend fun searchWord(rq: DbWordFilterRequest): IDbWordsResponse = tryWordsMethod {
        val found = dao.search(rq).await()
        DbWordsResponseOk(found.map { it.toWordModel() })
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }
}