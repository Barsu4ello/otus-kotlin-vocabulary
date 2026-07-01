package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.models.VcblCommand
import kotlin.test.Test

class BizValidationReadTest: BaseBizValidationTest() {
    override val command = VcblCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}