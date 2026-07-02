package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.repo.DbWordRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.repo.common.IRepoWordInitializable
import ru.gorbunov.vocabulary.repo.common.WordRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoWordUpdateTest {
    abstract val repo: IRepoWordInitializable
    protected open val updateSucc = initObjects[0]
    protected val updateIdNotFound = VcblWordId("word-repo-update-not-found")

    private val reqUpdateSucc by lazy {
        VcblWord(
            id = updateSucc.id,
            english = "dog",
            russian = "собака",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            ownerId = VcblUserId("owner-123"),
        )
    }

    private val reqUpdateNotFound = VcblWord(
        id = updateIdNotFound,
        english = "cat",
        russian = "кот",
        partOfSpeech = VcblPartOfSpeech.NOUN,
        ownerId = VcblUserId("owner-123"),
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateWord(DbWordRequest(reqUpdateSucc))
        assertIs<DbWordResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.english, result.data.english)
        assertEquals(reqUpdateSucc.russian, result.data.russian)
        assertEquals(reqUpdateSucc.partOfSpeech, result.data.partOfSpeech)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateWord(DbWordRequest(reqUpdateNotFound))
        assertIs<DbWordResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitWords("update") {
        override val initObjects: List<VcblWord> = listOf(
            createInitTestModel("update"),
        )
    }
}