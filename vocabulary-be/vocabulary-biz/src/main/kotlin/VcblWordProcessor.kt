package ru.gorbunov.vocabulary.biz

import ru.gorbunov.vocabulary.biz.general.initStatus
import ru.gorbunov.vocabulary.biz.general.operation
import ru.gorbunov.vocabulary.biz.stubs.*
import ru.gorbunov.vocabulary.biz.validation.*
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.cor.rootChain
import ru.gorbunov.vocabulary.cor.worker


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
            validation {
                worker("Копируем поля в wordValidating") { wordValidating = wordRequest.copy() }
                worker("Очистка id") { wordValidating.id = VcblWordId.NONE }
                worker("Очистка английского значения слова") { wordValidating.english = wordValidating.english.trim() }
                worker("Очистка русского значения слова") { wordValidating.russian = wordValidating.russian.trim() }
                validateEnglishNotEmpty("Проверка, что английское значение не пустое")
                validateEnglishHasContent("Проверка символов")
                validateRussianNotEmpty("Проверка, что русское значение не пустое")
                validateRussianHasContent("Проверка символов")
                validatePartOfSpeech("Проверка части речи")

                finishWordValidation("Завершение проверок")
            }
        }
        operation("Получить слово", VcblCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в wordValidating") { wordValidating = wordRequest.copy() }
                worker("Очистка id") { wordValidating.id = VcblWordId(wordValidating.id.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")

                finishWordValidation("Успешное завершение процедуры валидации")
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
            validation {
                worker("Копируем поля в wordValidating") { wordValidating = wordRequest.copy() }
                worker("Очистка id") { wordValidating.id = VcblWordId(wordValidating.id.asString().trim()) }
                worker("Очистка lock") { wordValidating.lock = VcblWordLock(wordValidating.lock.asString().trim()) }
                worker("Очистка английского значения слова") { wordValidating.english = wordValidating.english.trim() }
                worker("Очистка русского значения слова") { wordValidating.russian = wordValidating.russian.trim() }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")
                validateEnglishNotEmpty("Проверка, что английское значение не пустое")
                validateEnglishHasContent("Проверка символов")
                validateRussianNotEmpty("Проверка, что русское значение не пустое")
                validateRussianHasContent("Проверка символов")
                validatePartOfSpeech("Проверка части речи")

                finishWordValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Удалить слово", VcblCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в wordValidating") { wordValidating = wordRequest.copy() }
                worker("Очистка id") { wordValidating.id = VcblWordId(wordValidating.id.asString().trim()) }
                worker("Очистка lock") { wordValidating.lock = VcblWordLock(wordValidating.lock.asString().trim()) }
                validateIdNotEmpty("Проверка на непустой id")
                validateIdProperFormat("Проверка формата id")
                validateLockNotEmpty("Проверка на непустой lock")
                validateLockProperFormat("Проверка формата lock")

                finishWordValidation("Успешное завершение процедуры валидации")
            }
        }
        operation("Поиск слова", VcblCommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
            validation {
                worker("Копируем поля в wordFilterValidating") { wordFilterValidating = wordFilterRequest.copy() }
                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishWordFilterValidation("Успешное завершение процедуры валидации")
            }
        }
    }.build()
}