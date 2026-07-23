package ru.gorbunov.vocabulary.e2e.be.docker

import ru.gorbunov.vocabulary.e2e.be.base.AbstractDockerCompose

object KtorJvmCsWsDockerCompose : AbstractDockerCompose(
    "app-ktor",
    8080,
    "docker-compose-ktor-cs-jvm.yml",
)