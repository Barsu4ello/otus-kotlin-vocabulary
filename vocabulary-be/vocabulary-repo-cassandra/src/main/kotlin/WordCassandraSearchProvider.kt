package ru.gorbunov.vocabulary.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.datastax.oss.driver.api.querybuilder.select.Select
import ru.gorbunov.vocabulary.backend.repo.cassandra.model.WordCassandraDTO
import ru.gorbunov.vocabulary.backend.repo.cassandra.model.toTransport
import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.repo.DbWordFilterRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class WordCassandraSearchProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<WordCassandraDTO>
) {
    fun search(filter: DbWordFilterRequest): CompletionStage<Collection<WordCassandraDTO>> {

        var select = entityHelper.selectStart().allowFiltering()
        if (filter.searchString.isNotBlank()) {
            // Внимание! При использовании LIKE необходимо использовать SASI индексы.
            // При использовании SASI индекса типа StandardAnalyzer происходит токенизация текста по пробелам.
            // Оператор LIKE в этом случае должен быть НЕ LIKE '%<токен>%' а LIKE '<токен>%'
            select = select
                .whereColumn(detectLanguage(filter.searchString))
                .like(QueryBuilder.literal("${filter.searchString}%"))
        }
        if (filter.ownerId != VcblUserId.NONE) {
            select = select
                .whereColumn(WordCassandraDTO.COLUMN_OWNER_ID)
                .isEqualTo(QueryBuilder.literal(filter.ownerId.asString(), context.session.context.codecRegistry))
        }
        if (filter.partOfSpeech != VcblPartOfSpeech.NONE) {
            select = select
                .whereColumn(WordCassandraDTO.COLUMN_PART_OF_SPEECH)
                .isEqualTo(QueryBuilder.literal(filter.partOfSpeech.toTransport(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    private fun detectLanguage(searchString: String): String =
        when {
            searchString.matches(Regex("^[a-zA-Z]+$")) -> WordCassandraDTO.COLUMN_ENGLISH
            searchString.matches(Regex("^[а-яА-ЯёЁ]+$")) -> WordCassandraDTO.COLUMN_RUSSIAN
            else -> throw IllegalArgumentException("Unsupported language")
        }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<WordCassandraDTO>()
        private val future = CompletableFuture<Collection<WordCassandraDTO>>()
        val stage: CompletionStage<Collection<WordCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}