package ru.gorbunov.vocabulary.biz.exceptions

import ru.gorbunov.vocabulary.common.models.VcblWorkMode

class VcblWordDbNotConfiguredException(val workMode: VcblWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)