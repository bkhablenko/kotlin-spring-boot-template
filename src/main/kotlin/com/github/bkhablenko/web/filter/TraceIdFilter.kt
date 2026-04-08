package com.github.bkhablenko.web.filter

import io.micrometer.tracing.Tracer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

// https://spring.io/blog/2025/11/18/opentelemetry-with-spring-boot#beware-the-context
@Component
class TraceIdFilter(private val tracer: Tracer) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        traceId()?.let { response.setHeader("X-Trace-Id", it) }

        filterChain.doFilter(request, response)
    }

    private fun traceId(): String? = tracer.currentTraceContext()?.context()?.traceId()
}
