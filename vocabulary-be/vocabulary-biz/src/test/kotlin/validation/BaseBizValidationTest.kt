package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.VcblCommand

abstract class BaseBizValidationTest {
    protected abstract val command: VcblCommand
    private val settings by lazy { VcblCorSettings() }
    protected val processor by lazy { VcblWordProcessor(settings) }
}