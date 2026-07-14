package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.repo.DbWordIdRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErrWithData
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.repo.common.IRepoWordInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoWordDeleteTest {
    abstract val repo: IRepoWordInitializable
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = VcblWordId("word-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteWord(DbWordIdRequest(deleteSucc.id,  lock = lockOld))
        assertIs<DbWordResponseOk>(result)
        assertEquals(deleteSucc.english, result.data.english)
        assertEquals(deleteSucc.russian, result.data.russian)
        assertEquals(deleteSucc.partOfSpeech, result.data.partOfSpeech)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readWord(DbWordIdRequest(notFoundId))

        assertIs<DbWordResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteWord(DbWordIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbWordResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitWords("delete") {
        override val initObjects: List<VcblWord> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}