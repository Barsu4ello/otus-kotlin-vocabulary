package ru.gorbunov.vocabulary.biz.permissions

import ru.gorbunov.vocabulary.auth.checkPermitted
import ru.gorbunov.vocabulary.auth.resolveRelationsTo
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.accessViolation
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.chain
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == VcblState.RUNNING }
    worker("Вычисление отношения слова к принципалу") {
        wordRepoRead.principalRelations = wordRepoRead.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к слову") {
        permitted = checkPermitted(command, wordRepoRead.principalRelations, permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(
                accessViolation(
                    principal = principal,
                    operation = command,
                    wordId = wordRepoRead.id,
                )
            )
        }
    }
}