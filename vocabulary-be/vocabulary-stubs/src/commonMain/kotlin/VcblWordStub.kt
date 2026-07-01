package ru.gorbunov.vocabulary.stubs

import ru.gorbunov.vocabulary.common.models.*
import ru.gorbunov.vocabulary.common.models.VcblWord
import ru.gorbunov.vocabulary.stubs.VcblWordStubCat.WORD_CAT

object VcblWordStub {
    fun get(): VcblWord = VcblWordStubCat.WORD_CAT.copy()

    fun prepareResult(block: VcblWord.() -> Unit): VcblWord = get().apply(block)

    fun prepareSearchList(filter: String, partOfSpeech: VcblPartOfSpeech) = listOf(
        vcblWord("cat-01", filter, partOfSpeech),
        vcblWord("cat-02", filter, partOfSpeech),
        vcblWord("cat-03", filter, partOfSpeech),
        vcblWord("cat-04", filter, partOfSpeech),
        vcblWord("cat-05", filter, partOfSpeech),
        vcblWord("cat-06", filter, partOfSpeech),
    )


    private fun vcblWord(id: String, filter: String, partOfSpeech: VcblPartOfSpeech) =
        vcblWord(base = WORD_CAT, id = id, filter = filter, partOfSpeech = partOfSpeech)


    private fun vcblWord(base: VcblWord, id: String, filter: String, partOfSpeech: VcblPartOfSpeech) = base.copy(
        id = VcblWordId(id),
        english = filter,
        partOfSpeech = partOfSpeech,
    )
}