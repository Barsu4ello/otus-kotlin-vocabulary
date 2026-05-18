import ru.gorbunov.vocabulary.api.kmp.v1.apiV1Mapper
import ru.gorbunov.vocabulary.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {

    private val request = WordCreateRequest(
        debug = WordDebug(
            mode = WordRequestDebugMode.STUB,
            stub = WordRequestDebugStubs.BAD_ENGLISH
        ),
        word = WordCreateObject(
            english = "cat",
            russian = "кот",
            partOfSpeech = PartOfSpeech.NOUN,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.encodeToString(IRequest.serializer(), request)

        assertContains(json, Regex("\"english\":\\s*\"cat\""))
        assertContains(json, Regex("\"russian\":\\s*\"кот\""))
        assertContains(json, Regex("\"partOfSpeech\":\\s*\"noun\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badEnglish\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(request)
        val obj = apiV1Mapper.decodeFromString<WordCreateRequest>(json)

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"word": null}
        """.trimIndent()
        val obj = apiV1Mapper.decodeFromString<WordCreateRequest>(jsonString)

        assertEquals(null, obj.word)
    }
}