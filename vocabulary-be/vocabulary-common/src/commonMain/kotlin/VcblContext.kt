package ru.gorbunov.vocabulary.common

import kotlin.time.Instant
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalModel
import ru.gorbunov.vocabulary.common.permissions.VcblUserPermissions
import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import ru.gorbunov.vocabulary.common.ws.IVcblWsSession

data class VcblContext(
    var command: VcblCommand = VcblCommand.NONE,
    var state: VcblState = VcblState.NONE,
    val errors: MutableList<VcblError> = mutableListOf(),

    var corSettings: VcblCorSettings = VcblCorSettings(),
    var workMode: VcblWorkMode = VcblWorkMode.PROD,
    var stubCase: VcblStubs = VcblStubs.NONE,
    var wsSession: IVcblWsSession = IVcblWsSession.NONE,

    var principal: VcblPrincipalModel = VcblPrincipalModel.NONE,
    val permissionsChain: MutableSet<VcblUserPermissions> = mutableSetOf(),
    var permitted: Boolean = false,

    var requestId: VcblRequestId = VcblRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var wordRequest: VcblWord = VcblWord(),
    var wordFilterRequest: VcblWordFilter = VcblWordFilter(),

    var wordValidating: VcblWord = VcblWord(),
    var wordFilterValidating: VcblWordFilter = VcblWordFilter(),

    var wordValidated: VcblWord = VcblWord(),
    var wordFilterValidated: VcblWordFilter = VcblWordFilter(),

    var wordRepo: IRepoWord = IRepoWord.NONE,
    var wordRepoRead: VcblWord = VcblWord(), // То, что прочитали из репозитория
    var wordRepoPrepare: VcblWord = VcblWord(), // То, что готовим для сохранения в БД
    var wordRepoDone: VcblWord = VcblWord(),  // Результат, полученный из БД
    var wordsRepoDone: MutableList<VcblWord> = mutableListOf(),

    var wordResponse: VcblWord = VcblWord(),
    var wordsResponse: MutableList<VcblWord> = mutableListOf(),
)
