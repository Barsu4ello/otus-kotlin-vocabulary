package ru.gorbunov.vocabulary.common.repo.exceptions

import ru.gorbunov.vocabulary.common.models.VcblWordId

open class RepoWordException (
    @Suppress("unused")
    val wordId: VcblWordId,
    msg: String,
): RepoException(msg)