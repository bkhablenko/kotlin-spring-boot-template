package com.github.bkhablenko.web.controller

import com.github.bkhablenko.service.UserService
import com.github.bkhablenko.support.security.Authenticated
import com.github.bkhablenko.web.model.GetUserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
@Tag(name = "Users")
class UserController(private val userService: UserService) {

    @GetMapping("/@me")
    @Operation(
        summary = "Get own user",
        security = [SecurityRequirement(name = "basicAuth")],
    )
    @ApiResponse(responseCode = "401", content = [Content()])
    @ApiResponse(responseCode = "404", content = [Content()], description = "User not found")
    @ResponseStatus(HttpStatus.OK)
    @Authenticated
    fun getOwnUser(authentication: Authentication): GetUserResponse {
        val username = authentication.name
        return GetUserResponse(
            user = userService.getUserByUsername(username),
        )
    }
}
