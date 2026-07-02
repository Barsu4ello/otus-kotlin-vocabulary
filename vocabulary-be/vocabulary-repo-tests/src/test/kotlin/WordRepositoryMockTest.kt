package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.repo.DbWordResponseOk
import ru.gorbunov.vocabulary.common.repo.DbWordsResponseOk
import kotlinx.coroutines.test.runTest
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.repo.DbWordFilterRequest
import ru.gorbunov.vocabulary.common.repo.DbWordIdRequest
import ru.gorbunov.vocabulary.common.repo.DbWordRequest
import ru.gorbunov.vocabulary.stubs.VcblWordStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class WordRepositoryMockTest {
    private val repo = WordRepositoryMock(
        invokeCreateWord = { DbWordResponseOk(VcblWordStub.prepareResult { english = "create" }) },
        invokeReadWord = { DbWordResponseOk(VcblWordStub.prepareResult { english = "read" }) },
        invokeUpdateWord = { DbWordResponseOk(VcblWordStub.prepareResult { english = "update" }) },
        invokeDeleteWord = { DbWordResponseOk(VcblWordStub.prepareResult { english = "delete" }) },
        invokeSearchWord = { DbWordsResponseOk(listOf(VcblWordStub.prepareResult { english = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createWord(DbWordRequest(VcblWord()))
        assertIs<DbWordResponseOk>(result)
        assertEquals("create", result.data.english)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readWord(DbWordIdRequest(VcblWord()))
        assertIs<DbWordResponseOk>(result)
        assertEquals("read", result.data.english)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateWord(DbWordRequest(VcblWord()))
        assertIs<DbWordResponseOk>(result)
        assertEquals("update", result.data.english)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteWord(DbWordIdRequest(VcblWord()))
        assertIs<DbWordResponseOk>(result)
        assertEquals("delete", result.data.english)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchWord(DbWordFilterRequest())
        assertIs<DbWordsResponseOk>(result)
        assertEquals("search", result.data.first().english)
    }

}