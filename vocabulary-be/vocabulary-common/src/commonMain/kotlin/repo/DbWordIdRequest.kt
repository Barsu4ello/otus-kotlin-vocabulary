package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock

data class DbWordIdRequest(
    val id: VcblWordId,
    val lock: VcblWordLock = VcblWordLock.NONE,
) {
    constructor(word: VcblWord): this(word.id, word.lock)
}
