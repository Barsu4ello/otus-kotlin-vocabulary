import ru.gorbunov.vocabulary.common.models.*

object VcblWordStubCat {
    val WORD_CAT: VcblWord
        get() = VcblWord(
            id = VcblWordId("111"),
            english = "cat",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            ownerId = VcblUserId("user-1"),
            lock = VcblWordLock("123"),
            )
}