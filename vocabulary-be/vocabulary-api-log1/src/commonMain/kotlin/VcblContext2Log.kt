package ru.gorbunov.vocabulary.api.log1.mapper

import ru.gorbunov.vocabulary.api.v1.models.CommonLogModel
import ru.gorbunov.vocabulary.api.v1.models.ErrorLogModel
import ru.gorbunov.vocabulary.api.v1.models.VcblWordLogModel
import ru.gorbunov.vocabulary.api.v1.models.WordFilterLog
import ru.gorbunov.vocabulary.api.v1.models.WordLog
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.*
import kotlin.time.Clock

fun VcblContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "vocabulary",
    word = toVcblLog(),
    errors = errors.map { it.toLog() },
)

private fun VcblContext.toVcblLog(): VcblWordLogModel? {
    val wordNone = VcblWord()
    return VcblWordLogModel(
        requestId = requestId.takeIf { it != VcblRequestId.NONE }?.asString(),
        requestWord = wordRequest.takeIf { it != wordNone }?.toLog(),
        responseWord = wordResponse.takeIf { it != wordNone }?.toLog(),
        responseWords = wordsResponse.takeIf { it.isNotEmpty() }?.filter { it != wordNone }?.map { it.toLog() },
        requestFilter = wordFilterRequest.takeIf { it != VcblWordFilter() }?.toLog(),
    ).takeIf { it != VcblWordLogModel() }
}

private fun VcblWordFilter.toLog() = WordFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != VcblUserId.NONE }?.asString(),
)

private fun VcblError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun VcblWord.toLog() = WordLog(
    id = id.takeIf { it != VcblWordId.NONE }?.asString(),
    english = english.takeIf { it.isNotBlank() },
    russian = russian.takeIf { it.isNotBlank() },
    partOfSpeech = partOfSpeech.takeIf { it != VcblPartOfSpeech.NONE }?.name,
    ownerId = ownerId.takeIf { it != VcblUserId.NONE }?.asString(),
)