package com.github.bkhablenko.domain.model

import com.github.bkhablenko.domain.repository.AbstractDataJpaTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class UserEntityJpaTest : AbstractDataJpaTest() {

    @Test
    fun persistFlushFind() {
        val user = UserEntity(username = "john.smith")

        with(entityManager.persistFlushFind(user)) {
            val now = Instant.now()

            assertThat(createdAt).isCloseTo(now, within(2L, ChronoUnit.SECONDS))
            assertThat(updatedAt).isCloseTo(now, within(2L, ChronoUnit.SECONDS))
        }
    }
}
