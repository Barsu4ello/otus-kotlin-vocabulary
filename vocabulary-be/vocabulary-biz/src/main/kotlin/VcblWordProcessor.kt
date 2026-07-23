package ru.gorbunov.vocabulary.biz

import ru.gorbunov.vocabulary.biz.general.initStatus
import ru.gorbunov.vocabulary.biz.general.operation
import ru.gorbunov.vocabulary.biz.repo.checkLock
import ru.gorbunov.vocabulary.biz.repo.initRepo
import ru.gorbunov.vocabulary.biz.repo.prepareResult
import ru.gorbunov.vocabulary.biz.repo.repoCreate
import ru.gorbunov.vocabulary.biz.repo.repoDelete
import ru.gorbunov.vocabulary.biz.repo.repoPrepareCreate
import ru.gorbunov.vocabulary.biz.repo.repoPrepareDelete
import ru.gorbunov.vocabulary.biz.repo.repoPrepareUpdate
import ru.gorbunov.vocabulary.biz.repo.repoRead
import ru.gorbunov.vocabulary.biz.repo.repoSearch
import ru.gorbunov.vocabulary.biz.repo.repoUpdate
import ru.gorbunov.vocabulary.biz.stubs.*
import ru.gorbunov.vocabulary.biz.validation.*
import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.VcblCommand
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.common.models.VcblWordId
import ru.gorbunov.vocabulary.common.models.VcblWordLock
import ru.gorbunov.vocabulary.cor.chain
import ru.gorbunov.vocabulary.cor.rootChain
import ru.gorbunov.vocabulary.cor.worker


class VcblWordProcessor(
    private val corSettings: VcblCorSettings = VcblCorSettings.NONE
) {
    suspend fun exec(ctx: VcblContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<VcblContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

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
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объявления в БД")
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика чтения"
                repoRead("Чтение слова из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == VcblState.RUNNING }
                    handle { wordRepoDone = wordRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика обновления"
                repoRead("Чтение слова из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление слова в БД")
            }
            prepareResult("Подготовка ответа")
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
            chain {
                title = "Логика удаления"
                repoRead("Чтение слова из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление слова из БД")
            }
            prepareResult("Подготовка ответа")
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
            repoSearch("Поиск слов в БД по фильтру")
            prepareResult("Подготовка ответа")
        }
    }.build()
}