package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.repo.DbWordRequest
import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.repo.common.IRepoWordInitializable
import kotlin.test.*

abstract class RepoWordCreateTest {
    abstract val repo: IRepoWordInitializable
    protected open val uuidNew = VcblWordId("10000000-0000-0000-0000-000000000001")

    private val createObj = VcblWord(
        english = "cat",
        russian = "кот",
        partOfSpeech = VcblPartOfSpeech.NOUN,
        ownerId = VcblUserId("owner-123"),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createWord(DbWordRequest(createObj))
        val expected = createObj
        assertIs<DbWordResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.english, result.data.english)
        assertEquals(expected.russian, result.data.russian)
        assertEquals(expected.partOfSpeech, result.data.partOfSpeech)
        assertNotEquals(VcblWordId.NONE, result.data.id)
    }

    companion object : BaseInitWords("create") {
        override val initObjects: List<VcblWord> = emptyList()
    }
}