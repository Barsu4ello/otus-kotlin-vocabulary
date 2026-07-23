package ru.gorbunov.vocabulary.app.ktor.repo

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.app.ktor.moduleJvm
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.mappers.v1.toTransportCreate
import ru.gorbunov.vocabulary.mappers.v1.toTransportDelete
import ru.gorbunov.vocabulary.mappers.v1.toTransportRead
import ru.gorbunov.vocabulary.mappers.v1.toTransportUpdate
import ru.gorbunov.vocabulary.stubs.VcblWordStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

abstract class V1WordRepoBaseTest {

    abstract val workMode: WordRequestDebugMode
    abstract val appSettingsCreate: VcblAppSettings
    abstract val appSettingsRead:   VcblAppSettings
    abstract val appSettingsUpdate: VcblAppSettings
    abstract val appSettingsDelete: VcblAppSettings
    abstract val appSettingsSearch: VcblAppSettings

    protected val uuidOld = "10000000-0000-0000-0000-000000000001"
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"
    protected val initWord = VcblWordStub.prepareResult {
        id = VcblWordId(uuidOld)
        lock = VcblWordLock(uuidOld)
    }

    @Test
    fun create() {
        val word = initWord.toTransportCreate()
        v1TestApplication(
            conf = appSettingsCreate,
            func = "create",
            request = WordCreateRequest(
                word = word,
                debug = WordDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<WordCreateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidNew, responseObj.word?.id)
            assertEquals(word.english, responseObj.word?.english)
            assertEquals(word.russian, responseObj.word?.russian)
            assertEquals(word.partOfSpeech, responseObj.word?.partOfSpeech)
        }
    }

    @Test
    fun read() {
        val word = initWord.toTransportRead()
        v1TestApplication(
            conf = appSettingsRead,
            func = "read",
            request = WordReadRequest(
                word = word,
                debug = WordDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<IResponse>() as WordReadResponse
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.word?.id)
        }
    }

    @Test
    fun update() {
        val word = initWord.toTransportUpdate()
        v1TestApplication(
            conf = appSettingsUpdate,
            func = "update",
            request = WordUpdateRequest(
                word = word,
                debug = WordDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<WordUpdateResponse>()
            assertEquals(200, response.status.value)
            assertEquals(word.id, responseObj.word?.id)
            assertEquals(word.english, responseObj.word?.english)
            assertEquals(word.russian, responseObj.word?.russian)
            assertEquals(word.partOfSpeech, responseObj.word?.partOfSpeech)
        }
    }
    @Test
    fun delete() {
        val word = initWord.toTransportDelete()
        v1TestApplication(
            conf = appSettingsDelete,
            func = "delete",
            request = WordDeleteRequest(
                word = word,
                debug = WordDebug(mode = workMode),
            ),
        ) { response ->
            val responseObj = response.body<WordDeleteResponse>()
            assertEquals(200, response.status.value)
            assertEquals(uuidOld, responseObj.word?.id)
        }
    }
    @Test
    fun search() = v1TestApplication(
        conf = appSettingsSearch,
        func = "search",
        request = WordSearchRequest(
            wordFilter = WordSearchFilter(),
            debug = WordDebug(mode = workMode),
        ),
    ) { response ->
        val responseObj = response.body<WordSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.words?.size)
        assertEquals(uuidOld, responseObj.words?.first()?.id)
    }

    private inline fun <reified T: IRequest> v1TestApplication(
        conf: VcblAppSettings,
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { moduleJvm(appSettings = conf) }
        val client = createClient {
            install(ContentNegotiation) {
                jackson()
            }
        }
        val response = client.post("/v1/word/$func") {
            contentType(ContentType.Application.Json)
            header("X-Trace-Id", "12345")
            setBody(request)
        }
        function(response)
    }
}