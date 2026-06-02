package ru.gorbunov.vocabulary.common.ws

interface IVcblWsSessionRepo {
    fun add(session: IVcblWsSession)
    fun clearAll()
    fun remove(session: IVcblWsSession)
    suspend fun <K> sendAll(obj: K)

    companion object {
        val NONE = object : IVcblWsSessionRepo {
            override fun add(session: IVcblWsSession) {}
            override fun clearAll() {}
            override fun remove(session: IVcblWsSession) {}
            override suspend fun <K> sendAll(obj: K) {}
        }
    }
}