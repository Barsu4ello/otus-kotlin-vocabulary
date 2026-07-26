package ru.gorbunov.vocabulary.auth

import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalModel
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalRelations
import ru.gorbunov.vocabulary.common.permissions.VcblUserGroups

fun VcblWord.resolveRelationsTo(principal: VcblPrincipalModel): Set<VcblPrincipalRelations> = setOfNotNull(
    VcblPrincipalRelations.NONE,
    // Используется при создании нового объявления
    VcblPrincipalRelations.NEW.takeIf { id == VcblWordId.NONE },
    VcblPrincipalRelations.OWN.takeIf { principal.id == ownerId },
    VcblPrincipalRelations.ALL.takeIf { principal.groups.contains(VcblUserGroups.ADMIN) },
)