package ru.gorbunov.vocabulary.e2e.be.scenarios.v1

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import ru.gorbunov.vocabulary.api.v1.models.ResponseResult
import ru.gorbunov.vocabulary.api.v1.models.WordCreateRequest
import ru.gorbunov.vocabulary.api.v1.models.WordCreateResponse
import ru.gorbunov.vocabulary.api.v1.models.WordDebug
import ru.gorbunov.vocabulary.api.v1.models.WordDeleteObject
import ru.gorbunov.vocabulary.api.v1.models.WordDeleteRequest
import ru.gorbunov.vocabulary.api.v1.models.WordDeleteResponse
import ru.gorbunov.vocabulary.api.v1.models.WordResponseObject
import ru.gorbunov.vocabulary.api.v1.models.WordSearchFilter
import ru.gorbunov.vocabulary.api.v1.models.WordSearchRequest
import ru.gorbunov.vocabulary.api.v1.models.WordSearchResponse
import ru.gorbunov.vocabulary.e2e.be.base.client.Client
import ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base.sendAndReceive
import ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base.someCreateWord

import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioSearchV1(
    private val client: Client,
    private val debug: WordDebug? = null
) {
    @Test
    fun search() = runBlocking {
        val objs = listOf(
            someCreateWord,
            someCreateWord.copy(english = "cat", russian = "кот"),
            someCreateWord.copy(english = "cat", russian = "котик"),
            someCreateWord.copy(english = "cow", russian = "корова"),
        ).map { obj ->
            val resCreate = client.sendAndReceive(
                "word/create", WordCreateRequest(
                    requestType = "create",
                    debug = debug,
                    word = obj,
                )
            ) as WordCreateResponse

            assertEquals(ResponseResult.SUCCESS, resCreate.result)

            val cObj: WordResponseObject = resCreate.word ?: fail("No word in Create response")
            assertEquals(obj.english, cObj.english)
            assertEquals(obj.russian, cObj.russian)
            assertEquals(obj.partOfSpeech, cObj.partOfSpeech)
            cObj
        }

        val sObj = WordSearchFilter(searchString = "кот")
        val resSearch = client.sendAndReceive(
            "word/search",
            WordSearchRequest(
                requestType = "search",
                debug = debug,
                wordFilter = sObj,
            )
        ) as WordSearchResponse

        assertEquals(ResponseResult.SUCCESS, resSearch.result)

        val rsObj: List<WordResponseObject> = resSearch.words ?: fail("No words in Search response")
        val titles = rsObj.map { it.russian }
        assertContains(titles, "кот")
        assertContains(titles, "котик")

        objs.forEach { obj ->
            val resDelete = client.sendAndReceive(
                "word/delete", WordDeleteRequest(
                    requestType = "delete",
                    debug = debug,
                    word = WordDeleteObject(obj.id, obj.lock),
                )
            ) as WordDeleteResponse

            assertEquals(ResponseResult.SUCCESS, resDelete.result)
        }
    }
}