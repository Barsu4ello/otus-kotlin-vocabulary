package ru.gorbunov.vocabulary.mappers.v1

import ru.gorbunov.vocabulary.api.v1.models.WordCreateObject
import ru.gorbunov.vocabulary.api.v1.models.WordDeleteObject
import ru.gorbunov.vocabulary.api.v1.models.WordReadObject
import ru.gorbunov.vocabulary.api.v1.models.WordUpdateObject
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock

fun VcblWord.toTransportCreate() = WordCreateObject(
    english = english.takeIf { it.isNotBlank() },
    russian = russian.takeIf { it.isNotBlank() },
    partOfSpeech = partOfSpeech.toTransportPathOfSpeech(),
)

fun VcblWord.toTransportRead() = WordReadObject(
    id = id.takeIf { it != VcblWordId.NONE }?.asString(),
)

fun VcblWord.toTransportUpdate() = WordUpdateObject(
    id = id.takeIf { it != VcblWordId.NONE }?.asString(),
    english = english.takeIf { it.isNotBlank() },
    russian = russian.takeIf { it.isNotBlank() },
    partOfSpeech = partOfSpeech.toTransportPathOfSpeech(),
    lock = lock.takeIf { it != VcblWordLock.NONE }?.asString(),
)

fun VcblWord.toTransportDelete() = WordDeleteObject(
    id = id.takeIf { it != VcblWordId.NONE }?.asString(),
    lock = lock.takeIf { it != VcblWordLock.NONE }?.asString(),
)