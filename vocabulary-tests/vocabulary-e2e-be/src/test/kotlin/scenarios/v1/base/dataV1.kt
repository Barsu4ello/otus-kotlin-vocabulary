package ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base

import ru.gorbunov.vocabulary.api.v1.models.PartOfSpeech
import ru.gorbunov.vocabulary.api.v1.models.WordCreateObject

val someCreateWord = WordCreateObject(
    english = "dog",
    russian = "собака",
    partOfSpeech = PartOfSpeech.NOUN,
)