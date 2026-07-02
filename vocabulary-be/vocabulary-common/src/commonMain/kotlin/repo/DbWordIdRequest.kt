package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId

data class DbWordIdRequest(
    val id: VcblWordId,
) {
    constructor(word: VcblWord): this(word.id)
}
