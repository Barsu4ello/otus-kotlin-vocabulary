package ru.gorbunov.vocabulary.backend.repo.tests

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId

abstract class BaseInitWords(private val op: String): IInitObjects<VcblWord> {
    fun createInitTestModel(
        suf: String,
        english: String = "cat",
        russian: String = "кот",
        ownerId: VcblUserId = VcblUserId("owner-123"),
        partOfSpeech: VcblPartOfSpeech = VcblPartOfSpeech.NOUN,
    ) = VcblWord(
        id = VcblWordId("word-repo-$op-$suf"),
        english = english,
        russian = russian,
        partOfSpeech = partOfSpeech,
        ownerId = ownerId,
    )
}