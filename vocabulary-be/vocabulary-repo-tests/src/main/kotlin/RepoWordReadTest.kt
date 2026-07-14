package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.repo.DbWordIdRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.repo.common.IRepoWordInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoWordReadTest {
    abstract val repo: IRepoWordInitializable
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readWord(DbWordIdRequest(readSucc.id))

        assertIs<DbWordResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readWord(DbWordIdRequest(notFoundId))

        assertIs<DbWordResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitWords("read") {
        override val initObjects: List<VcblWord> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = VcblWordId("word-repo-read-notFound")

    }
}