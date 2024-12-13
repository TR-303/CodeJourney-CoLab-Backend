package com.tongji.codejourneycolab.codejourneycolabbackend.openapi;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Code Journey CoLab API")
                        .description("Swagger3")
                        .version("0.0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("项目API文档")
                        .url("/"));
    }
}
