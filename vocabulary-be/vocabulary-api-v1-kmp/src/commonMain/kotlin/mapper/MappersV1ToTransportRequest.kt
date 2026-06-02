package ru.gorbunov.vocabulary.api.kmp.v1.mapper

import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock

fun VcblWord.toTransportCreateWord() = WordCreateObject(
    english = english.takeIf { it.isNotBlank() },
    russian = russian.takeIf { it.isNotBlank() },
    partOfSpeech = partOfSpeech.toTransportPathOfSpeech(),
)

fun VcblWord.toTransportReadWord() = WordReadObject(
    id = id.toTransportWord(),
)

fun VcblWord.toTransportUpdateWord() = WordUpdateObject(
    id = id.toTransportWord(),
    english = english.takeIf { it.isNotBlank() },
    russian = russian.takeIf { it.isNotBlank() },
    partOfSpeech = partOfSpeech.toTransportPathOfSpeech(),
    lock = lock.toTransportWord(),
)

fun VcblWord.toTransportDeleteWord() = WordDeleteObject(
    id = id.toTransportWord(),
    lock = lock.toTransportWord(),
)

internal fun VcblWordLock.toTransportWord() = takeIf { it != VcblWordLock.NONE }?.asString()
internal fun VcblWordId.toTransportWord() = takeIf { it != VcblWordId.NONE }?.asString()

