package com.github.bkhablenko.config

import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableMethodSecurity
class WebSecurityConfig : WebMvcConfigurer {

    companion object {

        /**
         * @see [UserDetailsServiceAutoConfiguration.NOOP_PASSWORD_PREFIX]
         */
        private const val NOOP_PASSWORD_PREFIX = "{noop}"
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity {
            csrf { disable() }
            authorizeRequests {
                authorize(anyRequest, permitAll)
            }
            httpBasic {}
        }
        return httpSecurity.build()
    }

    /**
     * Allow any username to authenticate as long as the password is empty.
     */
    @Bean
    fun userDetailsService() = UserDetailsService { username -> User(username, NOOP_PASSWORD_PREFIX, emptyList()) }
}
