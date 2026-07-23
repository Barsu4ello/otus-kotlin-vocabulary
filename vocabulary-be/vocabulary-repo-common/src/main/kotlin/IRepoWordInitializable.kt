package ru.gorbunov.vocabulary.repo.common

import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.common.repo.IRepoWord

interface IRepoWordInitializable: IRepoWord {
    fun save(words: Collection<VcblWord>) : Collection<VcblWord>
}