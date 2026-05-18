import ru.gorbunov.vocabulary.api.v1.models.IResponse
import ru.gorbunov.vocabulary.api.v1.models.PartOfSpeech
import ru.gorbunov.vocabulary.api.v1.models.WordCreateResponse
import ru.gorbunov.vocabulary.api.v1.models.WordResponseObject
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {

    private val response = WordCreateResponse(
        word = WordResponseObject(
            english = "cat",
            russian = "кот",
            partOfSpeech = PartOfSpeech.NOUN,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, Regex("\"english\":\\s*\"cat\""))
        assertContains(json, Regex("\"russian\":\\s*\"кот\""))
        assertContains(json, Regex("\"partOfSpeech\":\\s*\"noun\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as WordCreateResponse

        assertEquals(response, obj)
    }
}