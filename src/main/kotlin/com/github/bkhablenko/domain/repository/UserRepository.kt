package com.github.bkhablenko.domain.repository

import com.github.bkhablenko.domain.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<UserEntity, UUID> {

    fun findByUsername(username: String): UserEntity?
}
