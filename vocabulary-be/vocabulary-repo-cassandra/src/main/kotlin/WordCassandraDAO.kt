package ru.gorbunov.vocabulary.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.*
import ru.gorbunov.vocabulary.backend.repo.cassandra.model.WordCassandraDTO
import ru.gorbunov.vocabulary.backend.repo.cassandra.model.WordCassandraDTO.Companion.COLUMN_LOCK
import ru.gorbunov.vocabulary.common.repo.DbWordFilterRequest
import java.util.concurrent.CompletionStage

@Dao
interface WordCassandraDAO {

    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: WordCassandraDTO): CompletionStage<WordCassandraDTO>

    @Select
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(id: String): CompletionStage<WordCassandraDTO?>

    @Update(customIfClause = "${COLUMN_LOCK} = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: WordCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "${COLUMN_LOCK} = :prevLock", entityClass = [WordCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @Query("TRUNCATE ${WordCassandraDTO.TABLE_NAME}")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun deleteAll()

    @QueryProvider(providerClass = WordCassandraSearchProvider::class, entityHelpers = [WordCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun search(filter: DbWordFilterRequest): CompletionStage<Collection<WordCassandraDTO>>
}