package ru.gorbunov.vocabulary.biz

import ru.gorbunov.vocabulary.common.VcblContext
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech
import ru.gorbunov.vocabulary.common.models.VcblState

@Suppress("unused", "RedundantSuspendModifier")
class VcblWordProcessor(val corSettings: VcblCorSettings) {

    suspend fun exec(ctx: VcblContext) {
        ctx.wordResponse = VcblWordStub.get()
        ctx.wordsResponse = VcblWordStub.prepareSearchList("word search", VcblPartOfSpeech.NOUN).toMutableList()
        ctx.state = VcblState.RUNNING
    }
}