package com.github.bkhablenko.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Configuration

@OpenAPIDefinition(
    info = Info(
        title = "kotlin-spring-boot-template",
        version = "0.1.0",
        contact = Contact(
            name = "Bohdan Khablenko",
            url = "https://github.com/bkhablenko",
            email = "8275828+bkhablenko@users.noreply.github.com",
        ),
        license = License(name = "The MIT License", identifier = "MIT"),
    ),
    tags = [
        Tag(name = "Users"),
    ],
)
@SecurityScheme(
    name = "basicAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "basic",
    description = "To authenticate, provide any username with a matching password.",
)
@Configuration
class OpenAPIConfig
