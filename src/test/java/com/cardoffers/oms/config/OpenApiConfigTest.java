package com.cardoffers.oms.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void shouldReturnOpenAPIInstance_whenOfferManagementOpenApiCalled() {
        ApplicationContext context = new AnnotationConfigApplicationContext(OpenApiConfig.class);
        OpenAPI openAPI = context.getBean(OpenAPI.class);

        assertNotNull(openAPI);
        assertEquals("Offer Management System API", openAPI.getInfo().getTitle());
        assertEquals("REST API for card offers, merchants, card networks, and eligibility", openAPI.getInfo().getDescription());
        assertEquals("v1", openAPI.getInfo().getVersion());
    }
}