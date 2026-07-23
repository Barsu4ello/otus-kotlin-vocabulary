package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.biz.exceptions.VcblWordDbNotConfiguredException
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.errorSystem
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.models.VcblWorkMode
import ru.gorbunov.vocabulary.common.repo.IRepoWord

fun ICorChainDsl<VcblContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы        
    """.trimIndent()
    handle {
        wordRepo = when {
            workMode == VcblWorkMode.TEST -> corSettings.repoTest
            workMode == VcblWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != VcblWorkMode.STUB && wordRepo == IRepoWord.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = VcblWordDbNotConfiguredException(workMode)
            )
        )
    }
}