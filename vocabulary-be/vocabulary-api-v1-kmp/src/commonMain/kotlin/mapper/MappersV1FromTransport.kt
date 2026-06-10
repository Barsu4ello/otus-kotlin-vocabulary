package ru.gorbunov.vocabulary.api.kmp.v1.mapper

import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.stubs.VcblStubs

fun VcblContext.fromTransport(request: IRequest) = when (request) {
    is WordCreateRequest -> fromTransport(request)
    is WordReadRequest -> fromTransport(request)
    is WordUpdateRequest -> fromTransport(request)
    is WordDeleteRequest -> fromTransport(request)
    is WordSearchRequest -> fromTransport(request)
}

fun VcblContext.fromTransport(request: WordCreateRequest) {
    command = VcblCommand.CREATE
    wordRequest = request.word?.toInternal() ?: VcblWord()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun VcblContext.fromTransport(request: WordReadRequest) {
    command = VcblCommand.READ
    wordRequest = request.word.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun VcblContext.fromTransport(request: WordUpdateRequest) {
    command = VcblCommand.UPDATE
    wordRequest = request.word?.toInternal() ?: VcblWord()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun VcblContext.fromTransport(request: WordDeleteRequest) {
    command = VcblCommand.DELETE
    wordRequest = request.word.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun VcblContext.fromTransport(request: WordSearchRequest) {
    command = VcblCommand.SEARCH
    wordFilterRequest = request.wordFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun WordCreateObject.toInternal(): VcblWord = VcblWord(
    english = this.english ?: "",
    russian = this.russian ?: "",
    partOfSpeech = this.partOfSpeech.fromTransport(),
)

private fun WordUpdateObject.toInternal(): VcblWord = VcblWord(
    id = this.id.toWordId(),
    english = this.english ?: "",
    russian = this.russian ?: "",
    partOfSpeech = this.partOfSpeech.fromTransport(),
    lock = lock.toWordLock(),
)

private fun WordSearchFilter?.toInternal(): VcblWordFilter = VcblWordFilter(
    searchString = this?.searchString ?: "",
    partOfSpeech = this?.partOfSpeech.fromTransport()
)

private fun WordDebug?.transportToWorkMode(): VcblWorkMode = when (this?.mode) {
    WordRequestDebugMode.PROD -> VcblWorkMode.PROD
    WordRequestDebugMode.TEST -> VcblWorkMode.TEST
    WordRequestDebugMode.STUB -> VcblWorkMode.STUB
    null -> VcblWorkMode.PROD
}

private fun WordDebug?.transportToStubCase(): VcblStubs = when (this?.stub) {
    WordRequestDebugStubs.SUCCESS -> VcblStubs.SUCCESS
    WordRequestDebugStubs.NOT_FOUND -> VcblStubs.NOT_FOUND
    WordRequestDebugStubs.BAD_ID -> VcblStubs.BAD_ID
    WordRequestDebugStubs.BAD_ENGLISH -> VcblStubs.BAD_ENGLISH
    WordRequestDebugStubs.BAD_RUSSIAN -> VcblStubs.BAD_RUSSIAN
    WordRequestDebugStubs.BAD_PATH_OF_SPEECH -> VcblStubs.BAD_PATH_OF_SPEECH
    WordRequestDebugStubs.CANNOT_DELETE -> VcblStubs.CANNOT_DELETE
    WordRequestDebugStubs.BAD_SEARCH_STRING -> VcblStubs.BAD_SEARCH_STRING
    null -> VcblStubs.NONE
}

private fun WordReadObject?.toInternal(): VcblWord = if (this != null) {
    VcblWord(id = id.toWordId())
} else {
    VcblWord()
}

private fun WordDeleteObject?.toInternal(): VcblWord = if (this != null) {
    VcblWord(
        id = id.toWordId(),
        lock = lock.toWordLock(),
    )
} else {
    VcblWord()
}

private fun String?.toWordId() = this?.let { VcblWordId(it) } ?:  VcblWordId.NONE
private fun String?.toWordLock() = this?.let {  VcblWordLock(it) } ?:  VcblWordLock.NONE

private fun PartOfSpeech?.fromTransport(): VcblPartOfSpeech = when (this) {
    PartOfSpeech.NOUN -> VcblPartOfSpeech.NOUN
    PartOfSpeech.VERB -> VcblPartOfSpeech.VERB
    PartOfSpeech.ADJECTIVE -> VcblPartOfSpeech.ADJECTIVE
    PartOfSpeech.ADVERB -> VcblPartOfSpeech.ADVERB
    null -> VcblPartOfSpeech.NONE
}