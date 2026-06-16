package ru.gorbunov.vocabulary.common.helpers

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.models.VcblError
import ru.gorbunov.vocabulary.common.models.VcblState
import ru.gorbunov.vocabulary.logging.common.LogLevel

fun Throwable.asVcblError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = VcblError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun VcblContext.addError(vararg error: VcblError) = errors.addAll(error)

inline fun VcblContext.fail(error: VcblError) {
    addError(error)
    state = VcblState.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = VcblError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)