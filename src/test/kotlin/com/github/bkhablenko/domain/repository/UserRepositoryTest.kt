package com.github.bkhablenko.domain.repository

import com.github.bkhablenko.domain.model.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest : AbstractDataJpaTest() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Nested
    inner class FindByUsername {

        @Test
        fun `should return expected entity if found`() {
            val user = entityManager.persist(UserEntity(username = "john.smith"))
            entityManager.flushAndClear()

            val result = userRepository.findByUsername(user.username)

            assertThat(result).isNotNull()
            with(result!!) {
                assertThat(id).isEqualTo(user.id)
            }
        }

        @Test
        fun `should return null if not found`() {
            assertThat(userRepository.findByUsername("john.smith")).isNull()
        }
    }
}
