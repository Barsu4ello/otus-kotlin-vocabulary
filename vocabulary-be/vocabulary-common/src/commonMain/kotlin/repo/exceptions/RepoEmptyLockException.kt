package ru.gorbunov.vocabulary.common.repo.exceptions

import ru.gorbunov.vocabulary.common.models.VcblWordId

class RepoEmptyLockException(id: VcblWordId) : RepoWordException(
    id,
    "Lock is empty in DB"
)