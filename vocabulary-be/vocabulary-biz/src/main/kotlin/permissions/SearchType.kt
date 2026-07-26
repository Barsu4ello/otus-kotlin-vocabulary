package ru.gorbunov.vocabulary.biz.permissions

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblSearchPermissions
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.permissions.VcblUserPermissions
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.chain
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.searchTypes(title: String) = chain {
    this.title = title
    description = "Добавление ограничений в поисковый запрос согласно правам доступа и др. политикам"
    on { state == VcblState.RUNNING }
    worker("Определение типа поиска") {
        wordFilterValidated.searchPermissions = setOfNotNull(
            VcblSearchPermissions.OWN.takeIf { permissionsChain.contains(VcblUserPermissions.SEARCH_OWN) },
            VcblSearchPermissions.ALL.takeIf { permissionsChain.contains(VcblUserPermissions.SEARCH_ALL) },
        ).toMutableSet()
    }
}