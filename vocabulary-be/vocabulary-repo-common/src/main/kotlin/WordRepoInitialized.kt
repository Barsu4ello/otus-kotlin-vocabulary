package ru.gorbunov.vocabulary.repo.common

import ru.gorbunov.vocabulary.common.models.VcblWord

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class WordRepoInitialized(
    private val repo: IRepoWordInitializable,
    initObjects: Collection<VcblWord> = emptyList(),
) : IRepoWordInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<VcblWord> = save(initObjects).toList()
}