package ru.gorbunov.vocabulary.common

import kotlin.time.Instant
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.stubs.VcblStubs
import ru.gorbunov.vocabulary.common.ws.IVcblWsSession

data class VcblContext(
    var command: VcblCommand = VcblCommand.NONE,
    var state: VcblState = VcblState.NONE,
    val errors: MutableList<VcblError> = mutableListOf(),

    var workMode: VcblWorkMode = VcblWorkMode.PROD,
    var stubCase: VcblStubs = VcblStubs.NONE,
    var wsSession: IVcblWsSession = IVcblWsSession.NONE,

    var requestId: VcblRequestId = VcblRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var wordRequest: VcblWord = VcblWord(),
    var wordFilterRequest: VcblWordFilter = VcblWordFilter(),

    var wordResponse: VcblWord = VcblWord(),
    var wordsResponse: MutableList<VcblWord> = mutableListOf(),
)
