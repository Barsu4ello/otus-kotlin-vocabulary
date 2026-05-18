package models

data class VcblWordFilter(
    var searchString: String = "",
    var ownerId: VcblUserId = VcblUserId.NONE,
    var partOfSpeech: VcblPartOfSpeech = VcblPartOfSpeech.NONE,
)