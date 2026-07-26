package ru.gorbunov.vocabulary.common.models

import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalRelations

data class VcblWord (
    var id: VcblWordId = VcblWordId.NONE,
    var english: String = "",
    var russian: String = "",
    var partOfSpeech: VcblPartOfSpeech = VcblPartOfSpeech.NONE,
    var ownerId: VcblUserId = VcblUserId.NONE,
    var lock: VcblWordLock = VcblWordLock.NONE,

    // Результат вычисления отношений текущего пользователя (который сделал запрос) к текущему слову
    var principalRelations: Set<VcblPrincipalRelations> = emptySet(),
    // Набор пермишинов, которые отдадим во фронтенд
    val permissionsClient: MutableSet<VcblWordPermissionClient> = mutableSetOf(),
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = VcblWord()
    }
}
