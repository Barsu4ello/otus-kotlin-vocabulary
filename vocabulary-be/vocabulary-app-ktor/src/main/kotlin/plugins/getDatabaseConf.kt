package ru.gorbunov.vocabulary.app.ktor.plugins

import io.ktor.server.application.*
import ru.gorbunov.vocabulary.app.ktor.configs.CassandraConfig
import ru.gorbunov.vocabulary.app.ktor.configs.ConfigPaths
import ru.gorbunov.vocabulary.backend.repo.cassandra.RepoWordCassandra
import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.repo.inmemory.WordRepoInMemory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.getDatabaseConf(type: WordDbType): IRepoWord {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "cassandra", "nosql", "cass" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath must be set in application.yml to one of: " +
                    "'inmemory', 'cassandra'"
        )
    }
}

enum class WordDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

private fun Application.initCassandra(): IRepoWord {
    val config = CassandraConfig(environment.config)
    return RepoWordCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}

fun Application.initInMemory(): IRepoWord {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return WordRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}