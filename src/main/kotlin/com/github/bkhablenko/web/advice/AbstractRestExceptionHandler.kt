package com.github.bkhablenko.web.advice

import com.github.bkhablenko.web.model.ErrorResponse
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.micrometer.tracing.Tracer
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.validation.method.MethodValidationException
import org.springframework.web.ErrorResponseException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.async.AsyncRequestNotUsableException
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.net.URI
import java.time.Instant

abstract class AbstractRestExceptionHandler(private val tracer: Tracer) {

    /**
     * @see [ResponseEntityExceptionHandler.handleException]
     */
    @ExceptionHandler(
        HttpRequestMethodNotSupportedException::class,
        HttpMediaTypeNotSupportedException::class,
        HttpMediaTypeNotAcceptableException::class,
        MissingPathVariableException::class,
        MissingServletRequestParameterException::class,
        MissingServletRequestPartException::class,
        ServletRequestBindingException::class,
        MethodArgumentNotValidException::class,
        HandlerMethodValidationException::class,
        NoHandlerFoundException::class,
        NoResourceFoundException::class,
        ErrorResponseException::class,
        MaxUploadSizeExceededException::class,

        ConversionNotSupportedException::class,
        TypeMismatchException::class,
        HttpMessageNotReadableException::class,
        HttpMessageNotWritableException::class,
        MethodValidationException::class,
        AsyncRequestNotUsableException::class,

        // Everything else as well
        Exception::class,
    )
    fun handleException(request: HttpServletRequest, exception: Exception): ErrorResponseEntity? {
        return when (exception) {
            is org.springframework.web.ErrorResponse -> {
                val status = HttpStatus.valueOf(exception.statusCode.value())
                errorResponseOf(request, status)
            }

            is TypeMismatchException -> handleTypeMismatchException(request, exception) // Including ConversionNotSupportedException
            is HttpMessageNotReadableException -> errorResponseOf(request, BAD_REQUEST, "Failed to read request")
            is HttpMessageNotWritableException -> errorResponseOf(request, INTERNAL_SERVER_ERROR, "Failed to write request")
            is MethodValidationException -> errorResponseOf(request, INTERNAL_SERVER_ERROR, "Validation failed")
            is AsyncRequestNotUsableException -> null // The response is not usable

            else -> {
                logger.error(exception) { "Unhandled exception occurred" }
                errorResponseOf(request, INTERNAL_SERVER_ERROR, "Something went awfully wrong")
            }
        }
    }

    private fun handleTypeMismatchException(request: HttpServletRequest, exception: TypeMismatchException): ErrorResponseEntity {
        val message = with(exception) { "Failed to convert '$propertyName' with value: '$value'" }
        val status = if (exception is ConversionNotSupportedException) INTERNAL_SERVER_ERROR else BAD_REQUEST
        return errorResponseOf(request, status, message)
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException::class)
    fun handleAccessDeniedException(exception: org.springframework.security.access.AccessDeniedException) {
        // Let the filter chain deal with it
        throw exception
    }

    // region errorResponseOf
    protected fun errorResponseOf(request: HttpServletRequest, status: HttpStatus, message: String? = null): ErrorResponseEntity {
        return ResponseEntity.status(status).body(
            ErrorResponse(
                traceId = traceId(),
                timestamp = Instant.now(),
                status = status,
                message = message,
                path = URI(request.requestURI)
            )
        )
    }

    private fun traceId(): String? = tracer.currentTraceContext()?.context()?.traceId()
    // endregion

    companion object {
        private val logger = logger {}
    }
}

typealias ErrorResponseEntity = ResponseEntity<ErrorResponse>
