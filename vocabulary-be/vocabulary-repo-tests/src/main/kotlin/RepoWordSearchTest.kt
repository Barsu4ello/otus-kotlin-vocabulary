package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.repo.DbWordFilterRequest
import ru.gorbunov.vocabulary.common.repo.DbWordsResponseOk
import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.repo.common.IRepoWordInitializable
import ru.gorbunov.vocabulary.repo.common.WordRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoWordSearchTest {
    abstract val repo: IRepoWordInitializable
    protected open val initializedObjects: List<VcblWord> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchWord(DbWordFilterRequest(ownerId = searchOwnerId))
        assertIs<DbWordsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchPartOfSpeech() = runRepoTest {
        val result = repo.searchWord(DbWordFilterRequest(partOfSpeech = VcblPartOfSpeech.VERB))
        assertIs<DbWordsResponseOk>(result)
        val expected = listOf(initializedObjects[2], initializedObjects[4]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    @Test
    fun searchEnglish() = runRepoTest {
        val result = repo.searchWord(DbWordFilterRequest(searchString = "run"))
        assertIs<DbWordsResponseOk>(result)
        val expected = initializedObjects[2]
        assertEquals(expected, result.data.first())
    }

    @Test
    fun searchRussian() = runRepoTest {
        val result = repo.searchWord(DbWordFilterRequest(searchString = "идт"))
        assertIs<DbWordsResponseOk>(result)
        val expected = initializedObjects[4]
        assertEquals(expected, result.data.first())
    }

    companion object: BaseInitWords("search") {

        val searchOwnerId = VcblUserId("owner-124")
        override val initObjects: List<VcblWord> = listOf(
            createInitTestModel("word1"),
            createInitTestModel("word2", ownerId = searchOwnerId),
            createInitTestModel("word3", english = "run", russian = "бежать", partOfSpeech = VcblPartOfSpeech.VERB),
            createInitTestModel("word4", ownerId = searchOwnerId),
            createInitTestModel("word5", english = "go", russian = "идти", partOfSpeech = VcblPartOfSpeech.VERB),
        )
    }
}