package com.github.bkhablenko.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration

/**
 * @see [CorsConfiguration.applyPermitDefaultValues]
 */
@ConfigurationProperties("cors")
data class CorsConfigProperties(

    val enabled: Boolean = true,
    val allowedOrigins: List<String> = listOf("*"),
    val allowedMethods: List<HttpMethod> = listOf(HttpMethod.GET, HttpMethod.HEAD, HttpMethod.POST),
    val allowedHeaders: List<String> = listOf("*"),
    val maxAge: Long = 1_800L,
)
