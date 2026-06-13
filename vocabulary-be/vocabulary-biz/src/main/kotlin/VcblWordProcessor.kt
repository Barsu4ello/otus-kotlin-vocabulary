package ru.gorbunov.vocabulary.biz

import ru.gorbunov.vocabulary.biz.general.initStatus
import ru.gorbunov.vocabulary.biz.general.operation
import ru.gorbunov.vocabulary.biz.stubs.*
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.cor.rootChain


class VcblWordProcessor(
    private val corSettings: VcblCorSettings = VcblCorSettings.NONE
) {
    suspend fun exec(ctx: VcblContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<VcblContext> {
        initStatus("Инициализация статуса")

        operation("Создание слова", VcblCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadEnglish("Имитация ошибки валидации английского значения слова")
                stubValidationBadRussian("Имитация ошибки валидации русского значения слова")
                stubValidationBadPartOfSpeech("Имитация ошибки валидации части речи")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Получить слово", VcblCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Изменить слово", VcblCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadEnglish("Имитация ошибки валидации английского значения слова")
                stubValidationBadRussian("Имитация ошибки валидации русского значения слова")
                stubValidationBadPartOfSpeech("Имитация ошибки валидации части речи")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Удалить слово", VcblCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
        operation("Поиск слова", VcblCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}