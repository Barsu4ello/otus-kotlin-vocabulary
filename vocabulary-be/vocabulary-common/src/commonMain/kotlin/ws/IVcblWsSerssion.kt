package ru.gorbunov.vocabulary.common.ws

interface IVcblWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IVcblWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}