package com.huysg136.cirquo_server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI cirquoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cirquo E-commerce API")
                        .version("v1")
                        .description("REST API for the Cirquo e-commerce platform.")
                )
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Local development server")
                )
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
