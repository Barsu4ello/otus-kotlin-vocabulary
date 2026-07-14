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
import ru.gorbunov.vocabulary.api.v1.models.WordUpdateObject
import ru.gorbunov.vocabulary.api.v1.models.WordUpdateRequest
import ru.gorbunov.vocabulary.api.v1.models.WordUpdateResponse
import ru.gorbunov.vocabulary.e2e.be.base.client.Client
import ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base.sendAndReceive
import ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base.someCreateWord

import kotlin.test.assertEquals
import kotlin.test.fail

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val debug: WordDebug? = null
) {
    @Test
    fun update() = runBlocking {
        val obj = someCreateWord
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

        val uObj = WordUpdateObject(
            id = cObj.id,
            lock = cObj.lock,
            english = cObj.english,
            russian = "собачка",
            partOfSpeech = cObj.partOfSpeech,
        )
        val resUpdate = client.sendAndReceive(
            "word/update",
            WordUpdateRequest(
                requestType = "update",
                debug = debug,
                word = uObj,
            )
        ) as WordUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: WordResponseObject = resUpdate.word ?: fail("No word in Update response")
        assertEquals(uObj.english, ruObj.english)
        assertEquals(uObj.russian, ruObj.russian)
        assertEquals(uObj.partOfSpeech, ruObj.partOfSpeech)


        val resDelete = client.sendAndReceive(
            "word/delete", WordDeleteRequest(
                requestType = "delete",
                debug = debug,
                word = WordDeleteObject(cObj.id, resUpdate.word?.lock),
            )
        ) as WordDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)
    }
}