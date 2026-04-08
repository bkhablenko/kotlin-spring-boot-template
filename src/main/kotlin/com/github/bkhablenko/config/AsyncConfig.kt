package com.github.bkhablenko.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.support.ContextPropagatingTaskDecorator
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableAsync
class AsyncConfig {

    @Bean
    fun contextPropagatingTaskDecorator() = ContextPropagatingTaskDecorator()
}
