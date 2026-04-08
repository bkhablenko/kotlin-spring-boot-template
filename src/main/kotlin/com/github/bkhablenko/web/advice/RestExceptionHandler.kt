package com.github.bkhablenko.web.advice

import com.github.bkhablenko.exception.UserNotFoundException
import io.micrometer.tracing.Tracer
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler(tracer: Tracer) : AbstractRestExceptionHandler(tracer) {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(request: HttpServletRequest, exception: UserNotFoundException): ErrorResponseEntity {
        return errorResponseOf(request, NOT_FOUND, exception.message)
    }
}
