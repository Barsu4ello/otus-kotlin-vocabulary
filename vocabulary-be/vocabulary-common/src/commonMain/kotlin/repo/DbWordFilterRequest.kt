package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblUserId

data class DbWordFilterRequest(
    var searchString: String = "",
    var ownerId: VcblUserId = VcblUserId.NONE,
    var partOfSpeech: VcblPartOfSpeech = VcblPartOfSpeech.NONE,
)
