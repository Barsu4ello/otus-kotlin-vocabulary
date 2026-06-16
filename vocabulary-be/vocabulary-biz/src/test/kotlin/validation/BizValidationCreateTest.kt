package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.models.VcblCommand
import kotlin.test.Test

class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: VcblCommand = VcblCommand.CREATE

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

}