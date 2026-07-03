package ru.gorbunov.vocabulary.common.repo.exceptions

import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock

class RepoConcurrencyException(id: VcblWordId, expectedLock: VcblWordLock, actualLock: VcblWordLock?) : RepoWordException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)