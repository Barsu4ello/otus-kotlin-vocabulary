package ru.gorbunov.vocabulary.common.models

data class VcblWordFilter(
    var searchString: String = "",
    var ownerId: VcblUserId = VcblUserId.NONE,
    var partOfSpeech: VcblPartOfSpeech = VcblPartOfSpeech.NONE,
    var searchPermissions: MutableSet<VcblSearchPermissions> = mutableSetOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = VcblWordFilter()
    }
}