package ru.gorbunov.vocabulary.e2e.be.base

import io.ktor.http.*
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration

private val log = org.slf4j.LoggerFactory.getLogger(AbstractDockerCompose::class.java)

/**
 * apps - список приложений в docker-compose. Первое приложение - "главное", его url возвращается как inputUrl
 * (например ваш сервис при работе по rest или брокер сообщений при работе с брокером)
 * dockerComposeNames - имена docker-compose файлов (относительно vocabulary-acceptance/docker-compose)
 */
abstract class AbstractDockerCompose(
    private val apps: List<AppInfo>,
    private val dockerComposeNames: List<String>,
) : DockerCompose {
    constructor(
        service: String,
        port: Int,
        vararg dockerComposeName: String
    ): this(
        listOf(AppInfo(service, port)),
        dockerComposeName.toList()
    )
    private val LOGGER = org.slf4j.LoggerFactory.getLogger(ComposeContainer::class.java)
    private val logConsumer = Slf4jLogConsumer(LOGGER)
    private fun getComposeFiles(): List<File> = dockerComposeNames.map {
//        val file = File("build/dcompose/$it")
        val file = File("docker-compose/$it")
        if (!file.exists()) throw IllegalArgumentException("file $it not found!")
        file
    }

    private val compose by lazy {
        ComposeContainer(getComposeFiles()).apply {
            apps.forEach { (service, port) ->
                withExposedService(service, port)
                withLogConsumer(service, logConsumer)
                withStartupTimeout(Duration.ofSeconds(600))
                waitingFor(service, Wait.forHealthcheck())
            }
        }
    }

    override fun start() {
        kotlin.runCatching { compose.start() }.onFailure {
            log.error("Failed to start $dockerComposeNames")
            throw it
        }

        log.warn("\n=========== $dockerComposeNames started =========== \n")
        apps.forEachIndexed { index, _ ->
            log.info("Started docker-compose with App at: ${getUrl(index)}")
        }
    }

    override fun stop() {
        compose.close()
        log.warn("\n=========== $dockerComposeNames complete =========== \n")
    }

    override fun clearDb() {
        log.warn("===== clearDb =====")
        // TODO сделать очистку БД, когда до этого дойдет
    }

    override val inputUrl: URLBuilder
        get() = getUrl(0)

    fun getUrl(no: Int) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = apps[no].let { compose.getServiceHost(it.service, it.port) },
        port = apps[no].let { compose.getServicePort(it.service, it.port) },
    )
    data class AppInfo(
        val service: String,
        val port: Int,
    )

}