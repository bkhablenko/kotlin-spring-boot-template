package com.github.bkhablenko.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.HandlerTypePredicate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(CorsConfigProperties::class)
@EnableMethodSecurity
class WebSecurityConfig(private val cors: CorsConfigProperties) : WebMvcConfigurer {

    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController::class.java))
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        if (!cors.enabled) return

        val allowedMethods = cors.allowedMethods.map { method -> method.name() }.toTypedArray()

        registry
            .addMapping("/**")
            .allowedOriginPatterns(cors.allowedOrigins.joinToString())
            .allowedMethods(*allowedMethods)
            .allowedHeaders(cors.allowedHeaders.joinToString())
            .maxAge(cors.maxAge)
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(anyRequest, permitAll)
            }
            if (!cors.enabled) cors { disable() }
            csrf {
                disable()
            }
            httpBasic {}
        }
        return http.build()
    }

    @Bean
    fun userDetailsService() = UserDetailsService { username ->
        // Accept any username with a matching password
        User.withUsername(username)
            .password("$NOOP_PASSWORD_PREFIX$username")
            .build()
    }

    companion object {
        /**
         * @see [UserDetailsServiceAutoConfiguration.NOOP_PASSWORD_PREFIX]
         */
        private const val NOOP_PASSWORD_PREFIX = "{noop}"
    }
}
