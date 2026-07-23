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
import ru.gorbunov.vocabulary.e2e.be.base.client.Client
import ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base.sendAndReceive
import ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base.someCreateWord
import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioCreateDeleteV1(
    private val client: Client,
    private val debug: WordDebug? = null
) {
    @Test
    fun createDelete() = runBlocking {
        val obj = someCreateWord
        val resCreate = client.sendAndReceive(
            "word/create",WordCreateRequest(
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

        val resDelete = client.sendAndReceive(
            "word/delete", WordDeleteRequest(
                requestType = "delete",
                debug = debug,
                word = WordDeleteObject(cObj.id, cObj.lock),
            )
        ) as WordDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: WordResponseObject = resDelete.word ?: fail("No word in Delete response")
        assertEquals(obj.english, dObj.english)
        assertEquals(obj.russian, dObj.russian)
        assertEquals(obj.partOfSpeech, dObj.partOfSpeech)
    }
}