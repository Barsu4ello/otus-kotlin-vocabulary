package ru.gorbunov.vocabulary.repo.inmemory

import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.repo.*
import ru.gorbunov.vocabulary.repo.common.IRepoWordInitializable
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class WordRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { UUID.randomUUID().toString() },
) : WordRepoBase(), IRepoWord, IRepoWordInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, WordEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(words: Collection<VcblWord>) = words.map { word ->
        val entity = WordEntity(word)
        require(entity.id != null)
        cache.put(entity.id, entity)
        word
    }

    override suspend fun createWord(rq: DbWordRequest): IDbWordResponse = tryWordMethod {
        val key = randomUuid()
        val word = rq.word.copy(id = VcblWordId(key))
        val entity = WordEntity(word)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbWordResponseOk(word)
    }

    override suspend fun readWord(rq: DbWordIdRequest): IDbWordResponse = tryWordMethod {
        val key = rq.id.takeIf { it != VcblWordId.NONE }?.asString() ?: return@tryWordMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbWordResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateWord(rq: DbWordRequest): IDbWordResponse = tryWordMethod {
        val rqWord = rq.word
        val id = rqWord.id.takeIf { it != VcblWordId.NONE } ?: return@tryWordMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldWord = cache.get(key)?.toInternal()
            when {
                oldWord == null -> errorNotFound(id)
                else -> {
                    val newWord = rqWord.copy()
                    val entity = WordEntity(newWord)
                    cache.put(key, entity)
                    DbWordResponseOk(newWord)
                }
            }
        }
    }


    override suspend fun deleteWord(rq: DbWordIdRequest): IDbWordResponse = tryWordMethod {
        val id = rq.id.takeIf { it != VcblWordId.NONE } ?: return@tryWordMethod errorEmptyId
        val key = id.asString()

        mutex.withLock {
            val oldWord = cache.get(key)?.toInternal()
            when {
                oldWord == null -> errorNotFound(id)
                else -> {
                    cache.invalidate(key)
                    DbWordResponseOk(oldWord)
                }
            }
        }
    }

    /**
     * Поиск слов по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchWord(rq: DbWordFilterRequest): IDbWordsResponse = tryWordsMethod {
        val result: List<VcblWord> = cache.asMap().asSequence()
            .filter { entry ->
                rq.ownerId.takeIf { it != VcblUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .filter { entry ->
                rq.partOfSpeech.takeIf { it != VcblPartOfSpeech.NONE }?.let {
                    it.name == entry.value.partOfSpeech
                } ?: true
            }
            .filter { entry ->
                rq.searchString.takeIf { it.isNotBlank() }?.let {
                    val isContainEng = entry.value.english?.contains(it) ?: false
                    val isContainRu = entry.value.russian?.contains(it) ?: false
                    isContainEng || isContainRu
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        DbWordsResponseOk(result)
    }
}