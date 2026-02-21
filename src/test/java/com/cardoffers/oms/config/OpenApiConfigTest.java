package com.cardoffers.oms.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OpenApiConfigTest {

    @Autowired
    private OpenApiConfig openApiConfig;

    @Test
    void shouldReturnOpenAPIConfiguration_whenOfferManagementOpenApiIsCalled() {
        OpenAPI openAPI = openApiConfig.offerManagementOpenApi();
        assertNotNull(openAPI);
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("Offer Management System API", info.getTitle());
        assertEquals("REST API for card offers, merchants, card networks, and eligibility", info.getDescription());
        assertEquals("v1", info.getVersion());
    }
}