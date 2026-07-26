package ru.gorbunov.vocabulary.stubs

import ru.gorbunov.vocabulary.common.models.*

object VcblWordStubCat {
    val WORD_CAT: VcblWord
        get() = VcblWord(
            id = VcblWordId("111"),
            english = "cat",
            russian = "кот",
            partOfSpeech = VcblPartOfSpeech.NOUN,
            ownerId = VcblUserId("user-1"),
            lock = VcblWordLock("123-234-abc-ABC"),
            permissionsClient = mutableSetOf(
                VcblWordPermissionClient.READ,
                VcblWordPermissionClient.UPDATE,
                VcblWordPermissionClient.DELETE,
            )
        )
}