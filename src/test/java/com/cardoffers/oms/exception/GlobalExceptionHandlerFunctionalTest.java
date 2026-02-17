package com.cardoffers.oms.exception;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;

@WebMvcTest(GlobalExceptionHandler.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GlobalExceptionHandlerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HttpServletRequest request;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldHandleResourceNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Resource not found"))
            .when(request)
            .getRequestURI();
        
        mockMvc.perform(get("/non-existing-endpoint"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Resource not found"))
            .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    void shouldHandleValidationErrors() throws Exception {
        String invalidInputJson = "{}"; // assuming no input is invalid
        
        mockMvc.perform(post("/validate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidInputJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Validation failed"))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldHandleBusinessExceptions() throws Exception {
        doThrow(new InvalidOfferException("Invalid offer"))
            .when(request)
            .getRequestURI();
        
        mockMvc.perform(get("/invalid-offer"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid offer"))
            .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        doThrow(new RuntimeException("Unexpected error"))
            .when(request)
            .getRequestURI();

        mockMvc.perform(get("/any-endpoint"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("Unexpected error occurred"))
            .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}