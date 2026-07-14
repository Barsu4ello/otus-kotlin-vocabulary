package ru.gorbunov.vocabulary.e2e.be.scenarios.v1.base

import apiV1RequestSerialize
import apiV1ResponseDeserialize
import ru.gorbunov.vocabulary.api.v1.models.IRequest
import ru.gorbunov.vocabulary.api.v1.models.IResponse
import ru.gorbunov.vocabulary.e2e.be.base.client.Client

private object SendReceiveLogger

private val log = org.slf4j.LoggerFactory.getLogger(SendReceiveLogger::class.java)

suspend fun Client.sendAndReceive(path: String, request: IRequest): IResponse {
    val requestBody = apiV1RequestSerialize(request)
    log.info("Send to v1/$path\n$requestBody")

    val responseBody = sendAndReceive("v1", path, requestBody)
    log.info("Received\n$responseBody")

    return apiV1ResponseDeserialize(responseBody)
}