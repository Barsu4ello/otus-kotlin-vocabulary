package ru.gorbunov.vocabulary.e2e.be

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import ru.gorbunov.vocabulary.e2e.be.base.BaseContainerTest
import ru.gorbunov.vocabulary.e2e.be.base.client.Client
import ru.gorbunov.vocabulary.e2e.be.base.client.RestAuthClient
import ru.gorbunov.vocabulary.e2e.be.docker.KtorJvmCsKeycloakDockerCompose
import ru.gorbunov.vocabulary.e2e.be.scenarios.v1.ScenariosV1
import ru.gorbunov.vocabulary.api.v1.models.WordDebug as WordDebugV1
import ru.gorbunov.vocabulary.api.v1.models.WordRequestDebugMode as WordRequestDebugModeV1

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestKeycloakCsJvm: BaseContainerTest(KtorJvmCsKeycloakDockerCompose) {
    private val client: Client = RestAuthClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1: ScenariosV1(client, WordDebugV1(mode = WordRequestDebugModeV1.PROD))
}