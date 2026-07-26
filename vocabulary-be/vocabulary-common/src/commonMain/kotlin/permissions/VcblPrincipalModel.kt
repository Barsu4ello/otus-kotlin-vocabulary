package ru.gorbunov.vocabulary.common.permissions

import ru.gorbunov.vocabulary.common.models.VcblUserId

data class VcblPrincipalModel(
    val id: VcblUserId = VcblUserId.NONE,
    val fname: String = "",
    val mname: String = "",
    val lname: String = "",
    val groups: Set<VcblUserGroups> = emptySet()
) {
    fun genericName() = "$fname $mname $lname"
    companion object {
        val NONE = VcblPrincipalModel()
    }
}
