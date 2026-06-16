package ru.gorbunov.vocabulary.biz.validation

import kotlin.test.Test
import ru.gorbunov.vocabulary.common.models.VcblCommand

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = VcblCommand.UPDATE

    @Test fun correctEnglish() = validationEnglishCorrect(command, processor)
    @Test fun trimEnglish() = validationEnglishTrim(command, processor)
    @Test fun emptyEnglish() = validationEnglishEmpty(command, processor)
    @Test fun badSymbolsEnglish() = validationEnglishSymbols(command, processor)

    @Test fun correctRussian() = validationRussianCorrect(command, processor)
    @Test fun trimRussian() = validationRussianTrim(command, processor)
    @Test fun emptyRussian() = validationRussianEmpty(command, processor)
    @Test fun badSymbolsRussian() = validationRussianSymbols(command, processor)

    @Test fun correctPartOfSpeech() = validationPartOfSpeechCorrect(command, processor)
    @Test fun partOfSpeechIsNone() = validationPartOfSpeechIsNone(command, processor)

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}