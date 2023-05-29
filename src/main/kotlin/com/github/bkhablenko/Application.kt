package com.github.bkhablenko

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.TimeZone

@SpringBootApplication
class Application {

    companion object {
        private val DEFAULT_TIME_ZONE = TimeZone.getTimeZone("UTC")
    }

    @PostConstruct
    fun init() {
        TimeZone.setDefault(DEFAULT_TIME_ZONE)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
