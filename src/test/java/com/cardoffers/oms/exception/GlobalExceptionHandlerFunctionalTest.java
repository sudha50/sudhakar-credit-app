package com.cardoffers.oms.exception;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class GlobalExceptionHandlerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HttpServletRequest request;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldHandleResourceNotFoundException() throws Exception {
        when(request.getRequestURI()).thenReturn("/offers/1");
        throw new ResourceNotFoundException("Offer not found");
    }

    @Test
    void shouldHandleValidationErrors() throws Exception {
        // Here, we can simulate a MethodArgumentNotValidException manually
        mockMvc.perform(post("/some-endpoint") // Adjust the URL as necessary
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"invalidField\":\"\"}")) // Invalid data
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.invalidField").value("must not be blank"));
    }

    @Test
    void shouldHandleBusinessException() throws Exception {
        when(request.getRequestURI()).thenReturn("/offers");
        throw new InvalidOfferException("Invalid offer details");
    }

    @Test
    void shouldHandleGenericException() throws Exception {
        when(request.getRequestURI()).thenReturn("/unknown-endpoint");
        throw new Exception("Some unexpected error");
    }
}