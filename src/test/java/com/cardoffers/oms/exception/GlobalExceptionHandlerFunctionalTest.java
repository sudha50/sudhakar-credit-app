package com.cardoffers.oms.exception;

import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestBody;

@WebMvcTest(GlobalExceptionHandler.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("shouldReturn404WhenResourceNotFound")
    void shouldReturn404WhenResourceNotFound() throws Exception {
        when(request.getRequestURI()).thenReturn("/some-uri");
        
        mockMvc.perform(post("/some-uri")
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @Test
    @DisplayName("shouldReturn400WhenValidationErrorOccurs")
    void shouldReturn400WhenValidationErrorOccurs() throws Exception {
        // Simulating method argument not valid exception with validation error
        when(request.getRequestURI()).thenReturn("/some-uri");
        
        mockMvc.perform(post("/some-uri")
                .contentType("application/json")
                .content("{\"invalidField\":\"\"}")) // sending invalid data
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details.fieldErrors.invalidField").value("must not be empty"));
    }

    @Test
    @DisplayName("shouldReturn400WhenBusinessExceptionOccurs")
    void shouldReturn400WhenBusinessExceptionOccurs() throws Exception {
        when(request.getRequestURI()).thenReturn("/some-uri");
        
        mockMvc.perform(post("/some-uri")
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Business error message")); // Assume you have this error message
    }

    @Test
    @DisplayName("shouldReturn500WhenGenericExceptionOccurs")
    void shouldReturn500WhenGenericExceptionOccurs() throws Exception {
        when(request.getRequestURI()).thenReturn("/some-uri");
        
        mockMvc.perform(post("/some-uri")
                .contentType("application/json"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"));
    }
}