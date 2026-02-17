package com.cardoffers.oms.exception;

import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(GlobalExceptionHandler.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GlobalExceptionHandlerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldHandleResourceNotFound() throws Exception {
        mockMvc.perform(get("/non-existent-endpoint"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Resource not found"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void shouldHandleValidationErrors() throws Exception {
        mockMvc.perform(get("/some-endpoint-that-validates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"invalidField\": \"\"}")) // Assuming empty is invalid
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isNotEmpty());
    }

    @Test
    public void shouldHandleBusinessExceptions() throws Exception {
        mockMvc.perform(get("/some-endpoint-that-throws-business-exception"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Business exception occurred"));
    }

    @Test
    public void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/some-endpoint-that-throws-generic-exception"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"));
    }
}