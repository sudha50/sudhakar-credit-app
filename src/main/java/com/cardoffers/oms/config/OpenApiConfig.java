package com.cardoffers.oms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI offerManagementOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Offer Management System API")
                        .description("REST API for card offers, merchants, card networks, and eligibility")
                        .version("v1"));
    }
}
