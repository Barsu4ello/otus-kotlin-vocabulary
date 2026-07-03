package ru.gorbunov.vocabulary.biz.repo

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == VcblState.RUNNING }
    handle {
        wordRepoPrepare = wordRepoRead.copy().apply {
            english = wordValidated.english
            russian = wordValidated.russian
            partOfSpeech = wordValidated.partOfSpeech
        }
    }
}