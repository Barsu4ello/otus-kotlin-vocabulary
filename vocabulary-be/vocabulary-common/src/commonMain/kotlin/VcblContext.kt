import kotlinx.datetime.Instant
import models.*
import stubs.VcblStubs

data class VcblContext(
    var command: VcblCommand = VcblCommand.NONE,
    var state: VcblState = VcblState.NONE,
    val errors: MutableList<VcblError> = mutableListOf(),

    var workMode: VcblWorkMode = VcblWorkMode.PROD,
    var stubCase: VcblStubs = VcblStubs.NONE,

    var requestId: VcblRequestId = VcblRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var wordRequest: VcblWord = VcblWord(),
    var wordFilterRequest: VcblWordFilter = VcblWordFilter(),

    var wordResponse: VcblWord = VcblWord(),
    var wordsResponse: MutableList<VcblWord> = mutableListOf(),
)
