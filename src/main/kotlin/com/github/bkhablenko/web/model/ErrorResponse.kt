package com.github.bkhablenko.web.model

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import java.net.URI
import java.time.Instant

@Schema(
    title = "ErrorResponse",
    description = "Generic error format."
)
data class ErrorResponse(

    @field:Schema(example = "2c1d1d66b3b105e6780de552618735f2")
    val traceId: String,

    @field:Schema(examples = ["2026-04-01T12:30:45.125Z"])
    val timestamp: Instant,

    @field:Schema(
        description = "HTTP status code",
        examples = ["500"]
    )
    val status: Int,

    @field:Schema(example = "Internal Server Error")
    val error: String,

    @field:Schema(
        description = "Additional error context, if any.",
        examples = ["Something went awfully wrong"]
    )
    val message: String,

    @field:Schema(
        description = "Request URI.",
        examples = ["/api/v1/users/@me"]
    )
    val path: URI
) {

    constructor(traceId: String?, timestamp: Instant, status: HttpStatus, message: String?, path: URI) : this(
        traceId ?: "",
        timestamp,
        status.value(),
        status.reasonPhrase,
        message ?: "",
        path
    )
}
