package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.repo.common.WordRepoInitialized
import ru.gorbunov.vocabulary.repo.inmemory.WordRepoInMemory
import ru.gorbunov.vocabulary.stubs.VcblWordStub

abstract class BaseBizValidationTest {
    protected abstract val command: VcblCommand
    private val repo = WordRepoInitialized(
        repo = WordRepoInMemory(),
        initObjects = listOf(
            VcblWordStub.get(),
        ),
    )
    private val settings by lazy { VcblCorSettings(repoTest = repo) }
    protected val processor by lazy { VcblWordProcessor(settings) }
}