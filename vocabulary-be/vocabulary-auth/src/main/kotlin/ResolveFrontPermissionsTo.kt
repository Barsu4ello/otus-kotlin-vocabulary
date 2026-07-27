package ru.gorbunov.vocabulary.auth

import ru.gorbunov.vocabulary.common.models.VcblWordPermissionClient
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalRelations
import ru.gorbunov.vocabulary.common.permissions.VcblUserPermissions

fun resolveFrontPermissions(
    permissions: Iterable<VcblUserPermissions>,
    relations: Iterable<VcblPrincipalRelations>,
) = mutableSetOf<VcblWordPermissionClient>()
    .apply {
        for (permission in permissions) {
            for (relation in relations) {
                accessTable[permission]?.get(relation)?.let { this@apply.add(it) }
            }
        }
    }
    .toSet()

/**
 * Это трехмерная таблица пермишин в бэкенде->отношение к слову->пермишин на фронте
 */
private val accessTable = mapOf(
    // READ
    VcblUserPermissions.READ_OWN to mapOf(
        VcblPrincipalRelations.OWN to VcblWordPermissionClient.READ
    ),
    VcblUserPermissions.READ_ALL to mapOf(
        VcblPrincipalRelations.ALL to VcblWordPermissionClient.READ
    ),

    // UPDATE
    VcblUserPermissions.UPDATE_OWN to mapOf(
        VcblPrincipalRelations.OWN to VcblWordPermissionClient.UPDATE
    ),
    VcblUserPermissions.UPDATE_ALL to mapOf(
        VcblPrincipalRelations.ALL to VcblWordPermissionClient.UPDATE
    ),

    // DELETE
    VcblUserPermissions.DELETE_OWN to mapOf(
        VcblPrincipalRelations.OWN to VcblWordPermissionClient.DELETE
    ),
    VcblUserPermissions.DELETE_ALL to mapOf(
        VcblPrincipalRelations.ALL to VcblWordPermissionClient.DELETE
    ),
)