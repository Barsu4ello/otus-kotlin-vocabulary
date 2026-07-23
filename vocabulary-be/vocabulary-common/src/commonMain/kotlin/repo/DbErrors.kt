package ru.gorbunov.vocabulary.common.repo

import ru.gorbunov.vocabulary.common.helpers.errorSystem
import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.common.repo.exceptions.RepoConcurrencyException
import ru.gorbunov.vocabulary.common.repo.exceptions.RepoException

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

fun errorRepoConcurrency(
    oldWord: VcblWord,
    expectedLock: VcblWordLock,
    exception: Exception = RepoConcurrencyException(
        id = oldWord.id,
        expectedLock = expectedLock,
        actualLock = oldWord.lock,
    ),
) = DbWordResponseErrWithData(
    word = oldWord,
    err = VcblError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldWord.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: VcblWordId) = DbWordResponseErr(
    VcblError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Word ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbWordResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)