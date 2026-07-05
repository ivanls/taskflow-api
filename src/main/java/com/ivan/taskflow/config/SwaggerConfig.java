package com.ivan.taskflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI taskFlowOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("TaskFlow API")
                        .description("REST API for managing tasks")
                        .version("1.0"));
    }
}
