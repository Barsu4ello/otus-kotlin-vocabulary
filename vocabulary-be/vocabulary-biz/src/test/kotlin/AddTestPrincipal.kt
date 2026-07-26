package ru.gorbunov.vocabulary.biz

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalModel
import ru.gorbunov.vocabulary.common.permissions.VcblUserGroups
import ru.gorbunov.vocabulary.stubs.VcblWordStubCat

fun VcblContext.addTestPrincipal(userId: VcblUserId = VcblWordStubCat.WORD_CAT.ownerId) {
    principal = VcblPrincipalModel(
        id = userId,
        groups = setOf(
            VcblUserGroups.USER,
            VcblUserGroups.TEST,
        )
    )
}