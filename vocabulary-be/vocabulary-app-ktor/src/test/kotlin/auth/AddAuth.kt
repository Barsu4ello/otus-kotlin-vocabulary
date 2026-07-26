package ru.gorbunov.vocabulary.app.ktor.auth

import io.ktor.client.request.*
import ru.gorbunov.vocabulary.app.common.AUTH_HEADER
import ru.gorbunov.vocabulary.app.common.createJwtTestHeader
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalModel
import ru.gorbunov.vocabulary.common.permissions.VcblUserGroups
import ru.gorbunov.vocabulary.stubs.VcblWordStubCat.WORD_CAT

fun HttpRequestBuilder.addAuth(principal: VcblPrincipalModel) {
    header(AUTH_HEADER, principal.createJwtTestHeader())
}

fun HttpRequestBuilder.addAuth(
    id: VcblUserId = WORD_CAT.ownerId,
    groups: Collection<VcblUserGroups> = listOf(VcblUserGroups.TEST, VcblUserGroups.USER),
) {
    addAuth(VcblPrincipalModel(id, groups = groups.toSet()))
}