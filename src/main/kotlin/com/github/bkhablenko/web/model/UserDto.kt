package com.github.bkhablenko.web.model

import com.github.bkhablenko.domain.model.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.UUID

@Schema(name = "User")
data class UserDto(

    @field:Schema(
        description = "ISO 8601 registration timestamp.",
        examples = ["2026-04-01T12:30:45.125Z"],
    )
    val createdAt: Instant,

    @field:Schema(
        description = "Internal user ID.",
        examples = ["019d67d0-1de8-7837-ac34-6f7c9e2644da"],
    )
    val id: UUID,

    @field:Schema(examples = ["john.smith"])
    val username: String,
) {
    companion object {
        fun from(source: UserEntity): UserDto = with(source) {
            UserDto(
                createdAt = createdAt,
                id = id,
                username = username,
            )
        }
    }
}
