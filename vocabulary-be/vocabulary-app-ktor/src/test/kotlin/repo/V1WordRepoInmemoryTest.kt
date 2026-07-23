package ru.gorbunov.vocabulary.app.ktor.repo

import ru.gorbunov.vocabulary.api.v1.models.WordRequestDebugMode
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.repo.common.WordRepoInitialized
import ru.gorbunov.vocabulary.repo.inmemory.WordRepoInMemory

class V1WordRepoInmemoryTest : V1WordRepoBaseTest() {

    override val workMode: WordRequestDebugMode = WordRequestDebugMode.TEST

    private fun vcblAppSettings(repo: IRepoWord) = VcblAppSettings(
        corSettings = VcblCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: VcblAppSettings = vcblAppSettings(
        repo = WordRepoInitialized(WordRepoInMemory(randomUuid = { uuidNew }))
    )
    override val appSettingsRead: VcblAppSettings = vcblAppSettings(
        repo = WordRepoInitialized(
            WordRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initWord),
        )
    )
    override val appSettingsUpdate: VcblAppSettings = vcblAppSettings(
        repo = WordRepoInitialized(
            WordRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initWord),
        )
    )
    override val appSettingsDelete: VcblAppSettings = vcblAppSettings(
        repo = WordRepoInitialized(
            WordRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initWord),
        )
    )
    override val appSettingsSearch: VcblAppSettings = vcblAppSettings(
        repo = WordRepoInitialized(
            WordRepoInMemory(randomUuid = { uuidNew }),
            initObjects = listOf(initWord),
        )
    )
}