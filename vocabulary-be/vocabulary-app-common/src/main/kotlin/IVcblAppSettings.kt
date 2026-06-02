package ru.gorbunov.vocabulary.app.common

import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblCorSettings

interface IVcblAppSettings {
    val processor: VcblWordProcessor
    val corSettings: VcblCorSettings
}