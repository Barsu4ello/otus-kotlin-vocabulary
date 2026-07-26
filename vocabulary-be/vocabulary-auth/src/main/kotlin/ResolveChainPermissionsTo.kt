package ru.gorbunov.vocabulary.auth

import ru.gorbunov.vocabulary.common.permissions.VcblUserGroups
import ru.gorbunov.vocabulary.common.permissions.VcblUserPermissions

/**
 * На вход подаем группы/роли из JWT, на выход получаем пермишины, соответствующие этим группам/ролям
 */
fun resolveChainPermissions(
    groups: Iterable<VcblUserGroups>,
) = mutableSetOf<VcblUserPermissions>()
    .apply {
        // Группы, добавляющие права (пермишины)
        addAll(groups.flatMap { groupPermissionsAdmits[it] ?: emptySet() })
        // Группы, запрещающие права (пермишины)
        removeAll(groups.flatMap { groupPermissionsDenys[it] ?: emptySet() })
    }
    .toSet()

private val groupPermissionsAdmits = mapOf(
    VcblUserGroups.USER to setOf(
        VcblUserPermissions.READ_OWN,
        VcblUserPermissions.CREATE_OWN,
        VcblUserPermissions.UPDATE_OWN,
        VcblUserPermissions.DELETE_OWN,
        VcblUserPermissions.SEARCH_OWN,
    ),
    VcblUserGroups.ADMIN to setOf(
        VcblUserPermissions.READ_ALL,
        VcblUserPermissions.UPDATE_ALL,
        VcblUserPermissions.DELETE_ALL,
        VcblUserPermissions.SEARCH_ALL,
    ),
    VcblUserGroups.TEST to setOf(),
)

private val groupPermissionsDenys = mapOf(
    VcblUserGroups.USER to setOf(),
    VcblUserGroups.ADMIN to setOf(),
    VcblUserGroups.TEST to setOf(),
    VcblUserGroups.BAN_USER to setOf(
        VcblUserPermissions.CREATE_OWN,
        VcblUserPermissions.UPDATE_OWN,
        VcblUserPermissions.DELETE_OWN,
    ),
)
