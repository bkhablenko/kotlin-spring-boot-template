package com.github.bkhablenko.service

import com.github.bkhablenko.domain.repository.UserRepository
import com.github.bkhablenko.exception.UserNotFoundException
import com.github.bkhablenko.web.model.UserDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getUserByUsername(username: String): UserDto {
        val user = userRepository.findByUsername(username) ?: throw UserNotFoundException()
        return UserDto.from(user)
    }
}
