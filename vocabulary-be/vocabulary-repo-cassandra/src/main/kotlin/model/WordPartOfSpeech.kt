package ru.gorbunov.vocabulary.backend.repo.cassandra.model

import ru.gorbunov.vocabulary.common.models.VcblPartOfSpeech

enum class WordPartOfSpeech {
    NOUN,
    VERB,
    ADJECTIVE,
    ADVERB,
}

fun WordPartOfSpeech?.fromTransport() = when(this) {
    null -> VcblPartOfSpeech.NONE
    WordPartOfSpeech.NOUN -> VcblPartOfSpeech.NOUN
    WordPartOfSpeech.VERB -> VcblPartOfSpeech.VERB
    WordPartOfSpeech.ADJECTIVE -> VcblPartOfSpeech.ADJECTIVE
    WordPartOfSpeech.ADVERB -> VcblPartOfSpeech.ADVERB
}

fun VcblPartOfSpeech.toTransport() = when(this) {
    VcblPartOfSpeech.NONE -> null
    VcblPartOfSpeech.NOUN -> WordPartOfSpeech.NOUN
    VcblPartOfSpeech.VERB -> WordPartOfSpeech.VERB
    VcblPartOfSpeech.ADJECTIVE -> WordPartOfSpeech.ADJECTIVE
    VcblPartOfSpeech.ADVERB -> WordPartOfSpeech.ADVERB
}