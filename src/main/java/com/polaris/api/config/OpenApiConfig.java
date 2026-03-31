package com.polaris.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI polarisOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Polaris TPA API")
                        .description("REST API for the Polaris TPA Analytics Platform - Maritime Insurance Management")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Apostolos Kagelaris")
                                .email("akagelaris@outlook.com")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
