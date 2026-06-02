package ru.gorbunov.vocabulary.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class VcblWordId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = VcblWordId("")
    }
}