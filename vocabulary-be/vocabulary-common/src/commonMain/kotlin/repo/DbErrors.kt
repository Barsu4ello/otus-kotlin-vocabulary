package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblWordId

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: VcblWordId) = DbWordResponseErr(
    VcblError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbWordResponseErr(
    VcblError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)