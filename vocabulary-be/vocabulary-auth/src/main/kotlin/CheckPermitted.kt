package ru.gorbunov.vocabulary.auth

import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalRelations
import ru.gorbunov.vocabulary.common.permissions.VcblUserPermissions

/**
 * Вычисляет доступность выполнения операции.
 * Здесь происходит сравнение доступных прав (пермишинов) и фактических отношений принципала к объекту, с которым работаем
 */
fun checkPermitted(
    command: VcblCommand,
    relations: Iterable<VcblPrincipalRelations>,
    permissions: Iterable<VcblUserPermissions>,
) =
    relations.asSequence().flatMap { relation ->
        permissions.map { permission ->
            AccessTableConditions(
                command = command,
                permission = permission,
                relation = relation,
            )
        }
    }.any {
        accessTable[it] != null
    }
// Дополнительно можно сделать проверку на отсутствие в результатах false

private data class AccessTableConditions(
    val command: VcblCommand,
    val permission: VcblUserPermissions,
    val relation: VcblPrincipalRelations
)

private val accessTable = mapOf(
    // Create
    AccessTableConditions(
        command = VcblCommand.CREATE,
        permission = VcblUserPermissions.CREATE_OWN,
        relation = VcblPrincipalRelations.NEW,
    ) to true,

    // Read
    AccessTableConditions(
        command = VcblCommand.READ,
        permission = VcblUserPermissions.READ_OWN,
        relation = VcblPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = VcblCommand.READ,
        permission = VcblUserPermissions.READ_ALL,
        relation = VcblPrincipalRelations.ALL,
    ) to true,

    // Update
    AccessTableConditions(
        command = VcblCommand.UPDATE,
        permission = VcblUserPermissions.UPDATE_OWN,
        relation = VcblPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = VcblCommand.UPDATE,
        permission = VcblUserPermissions.UPDATE_ALL,
        relation = VcblPrincipalRelations.ALL,
    ) to true,

    // Delete
    AccessTableConditions(
        command = VcblCommand.DELETE,
        permission = VcblUserPermissions.DELETE_OWN,
        relation = VcblPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = VcblCommand.DELETE,
        permission = VcblUserPermissions.DELETE_ALL,
        relation = VcblPrincipalRelations.ALL,
    ) to true,

    // Search
    AccessTableConditions(
        command = VcblCommand.SEARCH,
        permission = VcblUserPermissions.SEARCH_OWN,
        relation = VcblPrincipalRelations.OWN,
    ) to true,
    AccessTableConditions(
        command = VcblCommand.SEARCH,
        permission = VcblUserPermissions.SEARCH_ALL,
        relation = VcblPrincipalRelations.ALL,
    ) to true,
)