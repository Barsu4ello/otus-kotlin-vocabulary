package ru.gorbunov.vocabulary.backend.repo.cassandra

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.testcontainers.containers.ComposeContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import ru.gorbunov.vocabulary.backend.repo.tests.*
import ru.gorbunov.vocabulary.repo.common.WordRepoInitialized
import java.io.File
import java.time.Duration
import java.util.UUID

@Suppress("unused")
@RunWith(Enclosed::class)
class CassandraTest {

    class RepoWordCassandraCreateTest : RepoWordCreateTest() {
        override val repo = WordRepoInitialized(
            initObjects = initObjects,
            repo = repository(uuidNew.asString())
        )
    }

    class RepoWordCassandraReadTest : RepoWordReadTest() {
        override val repo = WordRepoInitialized(
            initObjects = initObjects,
            repo = repository()
        )
    }

    class RepoWordCassandraUpdateTest : RepoWordUpdateTest() {
        override val repo = WordRepoInitialized(
            initObjects = initObjects,
            repo = repository(lockNew.asString())
        )
    }

    class RepoWordCassandraDeleteTest : RepoWordDeleteTest() {
        override val repo = WordRepoInitialized(
            initObjects = initObjects,
            repo = repository()
        )
    }

    class RepoWordCassandraSearchTest : RepoWordSearchTest() {
        override val repo = WordRepoInitialized(
            initObjects = initObjects,
            repo = repository()
        )
    }

    @Ignore
    companion object {
        private const val CS_SERVICE = "cassandra"
        private const val CS_PORT = 9042
        private const val MG_SERVICE = "liquibase"

        val LOGGER = org.slf4j.LoggerFactory.getLogger(ComposeContainer::class.java)
        private val container: ComposeContainer by lazy {
            val resDc = this::class.java.classLoader.getResource("docker-compose-cs.yml")
                ?: throw Exception("No resource found")
            val fileDc = File(resDc.toURI())
            val logConsumer = Slf4jLogConsumer(LOGGER)
            ComposeContainer(
                fileDc,
            )
                .withExposedService(CS_SERVICE, CS_PORT)
                .withStartupTimeout(Duration.ofMinutes(10))
                .withLogConsumer(CS_SERVICE, logConsumer)
                .withLogConsumer(MG_SERVICE, logConsumer)
//                .withLogConsumer(PG_SERVICE, logConsumer)
                .waitingFor(
                    MG_SERVICE,
                    Wait.forLogMessage(".*Liquibase command 'update' was executed successfully.*", 1)
                    //Wait.defaultWaitStrategy().withStartupTimeout(Duration.ofSeconds(5000))
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
        fun start() {
            container.start()
        }

        @JvmStatic
        @AfterClass
        fun finish() {
            container.stop()
        }
    }
}