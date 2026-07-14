package ru.gorbunov.vocabulary.e2e.be.docker

import ru.gorbunov.vocabulary.e2e.be.base.AbstractDockerCompose

object KtorJvmCsDockerCompose : AbstractDockerCompose(
    "app-ktor",
    8080,
    "docker-compose-ktor-cs-jvm.yml",
)