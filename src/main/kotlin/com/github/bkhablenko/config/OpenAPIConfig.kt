package com.github.bkhablenko.config

import com.github.bkhablenko.web.model.ErrorResponse
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.SpecVersion
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.HandlerMethod
import kotlin.reflect.KClass

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
class OpenAPIConfig : OpenApiCustomizer {

    @Bean
    fun addDefaultApiResponse() = OperationCustomizer { operation: Operation, _: HandlerMethod ->
        operation.apply {
            if (ApiResponses.DEFAULT !in responses) {
                responses.addApiResponse(ApiResponses.DEFAULT, ApiResponse().`$ref`(ERROR_RESPONSE_NAME))
            }
        }
    }

    override fun customise(openAPI: OpenAPI) {
        resolveSchema(ErrorResponse::class, openAPI.specVersion).let { schema ->

            val response = ApiResponse()
                .content(Content().addMediaType("application/json", MediaType().schema(schema)))
                .description("Unexpected error")

            openAPI.components.addResponses(ERROR_RESPONSE_NAME, response)
        }
    }

    private fun resolveSchema(type: KClass<*>, specVersion: SpecVersion): Schema<*> {
        return ModelConverters
            .getInstance(specVersion == SpecVersion.V31)
            .resolveAsResolvedSchema(AnnotatedType(type.java))
            .schema
    }

    companion object {
        private const val ERROR_RESPONSE_NAME = "errorResponse"
    }
}
