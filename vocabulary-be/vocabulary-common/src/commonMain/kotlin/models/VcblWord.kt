package ru.gorbunov.vocabulary.common.models

data class VcblWord (
    var id: VcblWordId = VcblWordId.NONE,
    var english: String = "",
    var russian: String = "",
    var partOfSpeech: VcblPartOfSpeech = VcblPartOfSpeech.NONE,
    var ownerId: VcblUserId = VcblUserId.NONE,
    var lock: VcblWordLock = VcblWordLock.NONE,
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = VcblWord()
    }
}
