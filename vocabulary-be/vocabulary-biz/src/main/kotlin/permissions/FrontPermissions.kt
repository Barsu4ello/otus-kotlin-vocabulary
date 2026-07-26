package ru.gorbunov.vocabulary.biz.permissions

import ru.gorbunov.vocabulary.auth.resolveFrontPermissions
import ru.gorbunov.vocabulary.auth.resolveRelationsTo
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == VcblState.RUNNING }

    handle {
        wordRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                wordRepoDone.resolveRelationsTo(principal)
            )
        )

        for (word in wordsRepoDone) {
            word.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    word.resolveRelationsTo(principal)
                )
            )
        }
    }
}