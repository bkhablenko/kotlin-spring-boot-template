package com.github.bkhablenko.flyway

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.flywaydb.core.Flyway
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject

/**
 * Prevent exceptions during [ApplicationContext] initialization if a Flyway
 * migration fails.
 */
class TestFlywayMigrationStrategy(private val jdbcTemplate: JdbcTemplate) : FlywayMigrationStrategy {

    private lateinit var flyway: Flyway

    var success: Boolean = false
        private set

    override fun migrate(flyway: Flyway) {
        if (!this::flyway.isInitialized) this.flyway = flyway
        success = try {
            flyway.migrate().success
        } catch (cause: Exception) {
            logger.debug(cause) { "Flyway migrations failed" }
            false
        }
    }

    /**
     * Deletes all migrations of the latest major version from Flyway history and
     * re-runs them to verify idempotency.
     */
    fun migrateLatestAgain() {
        val table = flyway.configuration.table

        // E.g., "V42"
        val majorVersion = jdbcTemplate
            .queryForObject<String>("""SELECT "script" FROM "$table" ORDER BY "installed_rank" DESC LIMIT 1""")
            ?.substringBefore("_")
            ?: return

        logger.debug { "Removing Flyway migrations matching '${majorVersion}_*'" }
        jdbcTemplate.update("""DELETE FROM "$table" WHERE "script" LIKE ?""", "$majorVersion%")
        migrate(flyway)
    }

    companion object {
        private val logger = logger {}
    }
}
