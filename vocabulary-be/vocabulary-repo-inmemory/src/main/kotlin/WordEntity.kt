package ru.gorbunov.vocabulary.repo.inmemory

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock

data class WordEntity(
    val id: String? = null,
    val english: String? = null,
    val russian: String? = null,
    val partOfSpeech: String? = null,
    val ownerId: String? = null,
    val lock: String? = null,
) {
    constructor(model: VcblWord) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        english = model.english.takeIf { it.isNotBlank() },
        russian = model.russian.takeIf { it.isNotBlank() },
        partOfSpeech = model.partOfSpeech.takeIf { it != VcblPartOfSpeech.NONE }?.name,
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
        // Не нужно сохранять permissions, потому что он ВЫЧИСЛЯЕМЫЙ, а не хранимый
    )

    fun toInternal() = VcblWord(
        id = id?.let { VcblWordId(it) } ?: VcblWordId.NONE,
        english = english ?: "",
        russian = russian ?: "",
        partOfSpeech = partOfSpeech?.let { VcblPartOfSpeech.valueOf(it) } ?: VcblPartOfSpeech.NONE,
        ownerId = ownerId?.let { VcblUserId(it) } ?: VcblUserId.NONE,
        lock = lock?.let { VcblWordLock(it) } ?: VcblWordLock.NONE,
    )
}