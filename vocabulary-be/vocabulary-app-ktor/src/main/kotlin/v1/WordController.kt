package ru.gorbunov.vocabulary.app.ktor.v1

import io.ktor.server.application.*
import ru.gorbunov.vocabulary.api.v1.models.*
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createWord::class
suspend fun ApplicationCall.createWord(appSettings: VcblAppSettings) =
    processV1<WordCreateRequest, WordCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readWord::class
suspend fun ApplicationCall.readWord(appSettings: VcblAppSettings) =
    processV1<WordReadRequest, WordReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateWord::class
suspend fun ApplicationCall.updateWord(appSettings: VcblAppSettings) =
    processV1<WordUpdateRequest, WordUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteWord::class
suspend fun ApplicationCall.deleteWord(appSettings: VcblAppSettings) =
    processV1<WordDeleteRequest, WordDeleteResponse>(appSettings, clDelete, "delete")

val clSearch: KClass<*> = ApplicationCall::searchWord::class
suspend fun ApplicationCall.searchWord(appSettings: VcblAppSettings) =
    processV1<WordSearchRequest, WordSearchResponse>(appSettings, clSearch, "search")
