package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock

abstract class BaseInitWords(private val op: String): IInitObjects<VcblWord> {
    open val lockOld: VcblWordLock = VcblWordLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: VcblWordLock = VcblWordLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        english: String = "cat",
        russian: String = "кот",
        ownerId: VcblUserId = VcblUserId("owner-123"),
        partOfSpeech: VcblPartOfSpeech = VcblPartOfSpeech.NOUN,
        lock: VcblWordLock = lockOld,
    ) = VcblWord(
        id = VcblWordId("word-repo-$op-$suf"),
        english = english,
        russian = russian,
        partOfSpeech = partOfSpeech,
        ownerId = ownerId,
        lock = lock,
    )
}