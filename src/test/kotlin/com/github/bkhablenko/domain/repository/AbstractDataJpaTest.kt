package com.github.bkhablenko.domain.repository

import com.github.bkhablenko.domain.repository.AbstractDataJpaTest.Testcontainers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.testcontainers.postgresql.PostgreSQLContainer

@DataJpaTest
@Import(Testcontainers::class)
abstract class AbstractDataJpaTest {

    @Autowired
    protected lateinit var entityManager: TestEntityManager

    protected fun TestEntityManager.flushAndClear() {
        flush()
        clear()
    }

    @TestConfiguration(proxyBeanMethods = false)
    class Testcontainers {

        @Bean
        @ServiceConnection
        fun postgres() = PostgreSQLContainer("postgres:18.1")
    }
}
