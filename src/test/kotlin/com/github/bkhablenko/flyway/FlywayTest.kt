package com.github.bkhablenko.flyway

import com.github.bkhablenko.flyway.FlywayTest.CommonAndDev.Companion.SPRING_FLYWAY_LOCATIONS
import com.github.bkhablenko.flyway.FlywayTest.CommonAndProd.Companion.SPRING_FLYWAY_LOCATIONS
import com.github.bkhablenko.flyway.FlywayTest.Invalid.Companion.SPRING_FLYWAY_LOCATIONS
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureJdbc
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.postgresql.PostgreSQLContainer

/**
 * Validates environment-specific Flyway migrations that run only during deployment.
 */
@SpringJUnitConfig
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureJdbc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
abstract class FlywayTest {

    @Autowired
    protected lateinit var flyway: TestFlywayMigrationStrategy

    @DisplayName("SPRING_FLYWAY_LOCATIONS=$SPRING_FLYWAY_LOCATIONS")
    @TestPropertySource(properties = ["spring.flyway.locations=$SPRING_FLYWAY_LOCATIONS"])
    class CommonAndDev : FlywayTest() {

        @Test
        fun `should migrate successfully`() {
            assertThat(flyway.success).isTrue()
        }

        @Test
        fun `should migrate successfully when repeated`() {
            with(flyway) {
                assertThat(success).isTrue()
                migrateLatestAgain()
                assertThat(success).isTrue()
            }
        }

        companion object {
            private const val SPRING_FLYWAY_LOCATIONS = "classpath:db/migration/common,classpath:db/migration/dev"
        }
    }

    @DisplayName("SPRING_FLYWAY_LOCATIONS=$SPRING_FLYWAY_LOCATIONS")
    @TestPropertySource(properties = ["spring.flyway.locations=$SPRING_FLYWAY_LOCATIONS"])
    class CommonAndProd : FlywayTest() {

        @Test
        fun `should migrate successfully`() {
            assertThat(flyway.success).isTrue()
        }

        @Test
        fun `should migrate successfully when repeated`() {
            with(flyway) {
                assertThat(success).isTrue()
                migrateLatestAgain()
                assertThat(success).isTrue()
            }
        }

        companion object {
            private const val SPRING_FLYWAY_LOCATIONS = "classpath:db/migration/common,classpath:db/migration/prod"
        }
    }

    @DisplayName("SPRING_FLYWAY_LOCATIONS=$SPRING_FLYWAY_LOCATIONS")
    @TestPropertySource(properties = ["spring.flyway.locations=$SPRING_FLYWAY_LOCATIONS"])
    class Invalid : FlywayTest() {

        @Test
        fun `should not migrate successfully`() {
            assertThat(flyway.success).isFalse()
        }

        companion object {
            private const val SPRING_FLYWAY_LOCATIONS = "classpath:db/migration/invalid"
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    @ImportAutoConfiguration(FlywayAutoConfiguration::class)
    class Config {

        @Bean
        fun migrationStrategy(@Lazy jdbcTemplate: JdbcTemplate): FlywayMigrationStrategy {
            return TestFlywayMigrationStrategy(jdbcTemplate)
        }

        @Bean
        @ServiceConnection
        fun postgres() = PostgreSQLContainer("postgres:18.1")
    }
}
