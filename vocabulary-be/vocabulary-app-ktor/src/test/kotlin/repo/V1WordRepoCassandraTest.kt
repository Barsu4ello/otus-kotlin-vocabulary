package ru.gorbunov.vocabulary.app.ktor.repo

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import ru.gorbunov.vocabulary.api.v1.models.WordRequestDebugMode
import ru.gorbunov.vocabulary.app.ktor.VcblAppSettings
import ru.gorbunov.vocabulary.backend.repo.cassandra.RepoWordCassandra
import ru.gorbunov.vocabulary.common.VcblCorSettings
import ru.gorbunov.vocabulary.common.repo.IRepoWord
import ru.gorbunov.vocabulary.repo.common.WordRepoInitialized
import java.io.File
import java.time.Duration
import java.util.UUID

class V1WordRepoCassandraTest : V1WordRepoBaseTest() {
    override val workMode: WordRequestDebugMode = WordRequestDebugMode.TEST
    private fun mkAppSettings(repo: IRepoWord) = VcblAppSettings(
        corSettings = VcblCorSettings(
            repoTest = repo
        )
    )

    override val appSettingsCreate: VcblAppSettings = mkAppSettings(
        repo = WordRepoInitialized(repository(uuidNew))
    )
    override val appSettingsRead: VcblAppSettings = mkAppSettings(
        repo = WordRepoInitialized(
            repository(),
            initObjects = listOf(initWord),
        )
    )
    override val appSettingsUpdate: VcblAppSettings = mkAppSettings(
        repo = WordRepoInitialized(
            repository(uuidNew),
            initObjects = listOf(initWord),
        )
    )
    override val appSettingsDelete: VcblAppSettings = mkAppSettings(
        repo = WordRepoInitialized(
            repository(),
            initObjects = listOf(initWord),
        )
    )
    override val appSettingsSearch: VcblAppSettings = mkAppSettings(
        repo = WordRepoInitialized(
            repository(),
            initObjects = listOf(initWord),
        )
    )

    @Test
    fun cassandraV1Test() {
        println("Cassandra v1")
    }

    companion object {
        private const val CS_SERVICE = "cassandra"
        private const val CS_PORT = 9042
        private const val MG_SERVICE = "liquibase"

        // val LOGGER = org.slf4j.LoggerFactory.getLogger(ComposeContainer::class.java)
        private val container: ComposeContainer by lazy {
            val resDc = this::class.java.classLoader.getResource("docker-compose-cs.yml")
                ?: throw Exception("No resource found")
            val fileDc = File(resDc.toURI())
            //  val logConsumer = Slf4jLogConsumer(LOGGER)
            ComposeContainer(
                fileDc,
            )
                .withExposedService(CS_SERVICE, CS_PORT)
                .withStartupTimeout(Duration.ofSeconds(300))
//                .withLogConsumer(MG_SERVICE, logConsumer)
//                .withLogConsumer(PG_SERVICE, logConsumer)
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                )
        }

        fun repository(uuid: String? = null): RepoWordCassandra {
            return RepoWordCassandra(
                keyspaceName = "vocabulary",
                host = container.getServiceHost(CS_SERVICE, CS_PORT),
                port = container.getServicePort(CS_SERVICE, CS_PORT),
                randomUuid = uuid?.let { { uuid } } ?: { UUID.randomUUID().toString() },
                dc = "dc1",
            ).apply { clear() }
        }

        @JvmStatic
        @BeforeClass
        fun tearUp() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            container.stop()
        }
    }
}