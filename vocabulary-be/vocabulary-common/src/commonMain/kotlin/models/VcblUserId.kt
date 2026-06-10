package ru.gorbunov.vocabulary.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class VcblUserId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = VcblUserId("")
    }
}