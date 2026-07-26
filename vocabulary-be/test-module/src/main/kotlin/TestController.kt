package ru.gorbunov.kotlin

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    private val log = LoggerFactory.getLogger(TestController::class.java)

    @GetMapping
    fun getTest(): String {
        log.info("getTest method invoke")
        return "Get test module"
    }
}