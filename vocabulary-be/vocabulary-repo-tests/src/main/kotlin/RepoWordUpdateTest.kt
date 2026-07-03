package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.common.repo.DbWordRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErr
import ru.gorbunov.vocabulary.common.repo.DbWordResponseErrWithData
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
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = VcblWordId("word-repo-update-not-found")
    protected val lockBad = VcblWordLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = VcblWordLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        VcblWord(
            id = updateSucc.id,
            english = "dog",
            russian = "собака",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            ownerId = VcblUserId("owner-123"),
            lock = initObjects.first().lock, // lockOld из BaseInitWords
        )
    }

    private val reqUpdateNotFound = VcblWord(
        id = updateIdNotFound,
        english = "cat",
        russian = "кот",
        partOfSpeech = VcblPartOfSpeech.NOUN,
        ownerId = VcblUserId("owner-123"),
        lock = initObjects.first().lock,
    )

    private val reqUpdateConc by lazy {
        VcblWord(
            id = updateConc.id,
            english = "cat",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            ownerId = VcblUserId("owner-123"),
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateWord(DbWordRequest(reqUpdateSucc))
        assertIs<DbWordResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.english, result.data.english)
        assertEquals(reqUpdateSucc.russian, result.data.russian)
        assertEquals(reqUpdateSucc.partOfSpeech, result.data.partOfSpeech)
        assertEquals(lockOld, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateWord(DbWordRequest(reqUpdateNotFound))
        assertIs<DbWordResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateWord(DbWordRequest(reqUpdateConc))
        assertIs<DbWordResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitWords("update") {
        override val initObjects: List<VcblWord> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}