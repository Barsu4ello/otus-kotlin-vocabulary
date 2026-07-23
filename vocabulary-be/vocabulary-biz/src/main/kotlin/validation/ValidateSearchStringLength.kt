package ru.gorbunov.vocabulary.biz.validation

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.helpers.errorValidation
import ru.gorbunov.vocabulary.common.helpers.fail
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.cor.ICorChainDsl
import ru.gorbunov.vocabulary.cor.chain
import ru.gorbunov.vocabulary.cor.worker

fun ICorChainDsl<VcblContext>.validateSearchStringLength(title: String) = chain {
    this.title = title
    this.description = """
        Валидация длины строки поиска в поисковых фильтрах. Допустимые значения:
        - null или пустая строка - не выполняем поиск по строке, 
        - 2-100 - допустимая длина
        - больше 100 - слишком длинная строка
    """.trimIndent()
    on { state == VcblState.RUNNING }
    worker("Обрезка пустых символов") { wordFilterValidating.searchString = wordFilterValidating.searchString.trim() }
    worker {
        this.title = "Проверка кейса длины на 1 символ"
        this.description = this.title
        on { state == VcblState.RUNNING && wordFilterValidating.searchString.length == 1 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooShort",
                    description = "Search string must contain at least 2 symbols"
                )
            )
        }
    }
    worker {
        this.title = "Проверка кейса длины на более 100 символов"
        this.description = this.title
        on { state == VcblState.RUNNING && wordFilterValidating.searchString.length > 100 }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "tooLong",
                    description = "Search string must be no more than 100 symbols long"
                )
            )
        }
    }
    worker {
        this.title = "Проверка того что в searchString или русские или английские буквы"
        this.description = this.title
        on {
            state == VcblState.RUNNING && wordFilterValidating.searchString != "" &&
                    !(wordFilterValidating.searchString.matches(Regex("^[а-яА-ЯёЁ]+$"))
                            || wordFilterValidating.searchString.matches(Regex("^[a-zA-Z]+$")))
        }
        handle {
            fail(
                errorValidation(
                    field = "searchString",
                    violationCode = "combineDifferentLanguages",
                    description = "Search string must contains only russian or english symbols"
                )
            )
        }
    }
}