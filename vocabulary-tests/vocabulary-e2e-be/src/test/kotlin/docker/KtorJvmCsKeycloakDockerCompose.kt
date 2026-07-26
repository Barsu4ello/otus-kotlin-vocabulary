package ru.gorbunov.vocabulary.e2e.be.docker

import ru.gorbunov.vocabulary.e2e.be.base.AbstractDockerCompose

object KtorJvmCsKeycloakDockerCompose : AbstractDockerCompose(
    "envoy", 8080, "docker-compose-ktor-cs-keycloak-jvm.yml"
)