package models

import kotlin.jvm.JvmInline

@JvmInline
value class VcblRequestId(private val id: String)  {
    fun asString() = id

    companion object {
        val NONE = VcblRequestId("")
    }
}