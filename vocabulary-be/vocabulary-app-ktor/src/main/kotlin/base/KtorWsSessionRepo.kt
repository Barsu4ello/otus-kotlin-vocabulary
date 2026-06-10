package ru.gorbunov.vocabulary.app.ktor.base

import ru.gorbunov.vocabulary.common.ws.IVcblWsSession
import ru.gorbunov.vocabulary.common.ws.IVcblWsSessionRepo

class KtorWsSessionRepo: IVcblWsSessionRepo {
    private val sessions: MutableSet<IVcblWsSession> = mutableSetOf()
    override fun add(session: IVcblWsSession) {
        sessions.add(session)
    }

    override fun clearAll() {
        sessions.clear()
    }

    override fun remove(session: IVcblWsSession) {
        sessions.remove(session)
    }

    override suspend fun <T> sendAll(obj: T) {
        sessions.forEach { it.send(obj) }
    }
}