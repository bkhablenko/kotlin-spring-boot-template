package com.github.bkhablenko.domain.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataJpaTest
abstract class AbstractDataJpaTest {

    @Autowired
    protected lateinit var entityManager: TestEntityManager

    protected fun TestEntityManager.flushAndClear() {
        flush()
        clear()
    }
}
