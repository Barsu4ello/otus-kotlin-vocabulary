import exceptions.UnknownVcblCommand
import models.*
import ru.gorbunov.vocabulary.api.v1.models.*

fun VcblContext.toTransportWord(): IResponse = when (val cmd = command) {
    VcblCommand.CREATE -> toTransportCreate()
    VcblCommand.READ -> toTransportRead()
    VcblCommand.UPDATE -> toTransportUpdate()
    VcblCommand.DELETE -> toTransportDelete()
    VcblCommand.SEARCH -> toTransportSearch()
    VcblCommand.NONE -> throw UnknownVcblCommand(cmd)
}

fun VcblContext.toTransportCreate() = WordCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    word = wordResponse.toTransportWord(),
)

fun VcblContext.toTransportRead() = WordReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    word = wordResponse.toTransportWord()
)

fun VcblContext.toTransportUpdate() = WordUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    word = wordResponse.toTransportWord()
)

fun VcblContext.toTransportDelete() = WordDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    word = wordResponse.toTransportWord()
)

fun VcblContext.toTransportSearch() = WordSearchResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    words = wordsResponse.toTransportWord()
)

private fun VcblState.toResult(): ResponseResult? = when (this) {
    VcblState.RUNNING -> ResponseResult.SUCCESS
    VcblState.FAILING -> ResponseResult.ERROR
    VcblState.FINISHING -> ResponseResult.SUCCESS
    VcblState.NONE -> null
}

private fun List<VcblError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportErrors() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun VcblError.toTransportErrors() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

fun VcblWord.toTransportWord(): WordResponseObject = WordResponseObject(
    id = id.takeIf { it != VcblWordId.NONE }?.asString(),
    english = english.takeIf { it.isNotBlank() },
    russian = russian.takeIf { it.isNotBlank() },
    partOfSpeech = partOfSpeech.toTransportPathOfSpeech(),
    ownerId = ownerId.takeIf { it != VcblUserId.NONE }?.asString(),
)

fun List<VcblWord>.toTransportWord(): List<WordResponseObject>? = this
    .map { it.toTransportWord() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun VcblPartOfSpeech.toTransportPathOfSpeech(): PartOfSpeech? = when (this) {
    VcblPartOfSpeech.NOUN -> PartOfSpeech.NOUN
    VcblPartOfSpeech.VERB -> PartOfSpeech.VERB
    VcblPartOfSpeech.ADJECTIVE -> PartOfSpeech.ADJECTIVE
    VcblPartOfSpeech.ADVERB -> PartOfSpeech.ADVERB
    VcblPartOfSpeech.NONE -> null
}


