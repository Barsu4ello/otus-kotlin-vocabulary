package ru.gorbunov.vocabulary.app.ktor

import ru.gorbunov.vocabulary.app.common.IVcblAppSettings
import ru.gorbunov.vocabulary.biz.VcblWordProcessor
import ru.gorbunov.vocabulary.common.VcblCorSettings

data class VcblAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: VcblCorSettings = VcblCorSettings(),
    override val processor: VcblWordProcessor = VcblWordProcessor(corSettings),
) : IVcblAppSettings