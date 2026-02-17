package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GlobalExceptionHandlerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testHandleResourceNotFound() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/api/non-existent-resource", ErrorResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    public void testHandleValidationErrors() {
        CreateOfferRequest request = new CreateOfferRequest(); // assuming a DTO
        request.setDescription(""); // assuming this field cannot be blank
        
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/offers", 
                POST, 
                new HttpEntity<>(request, null), 
                ErrorResponse.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getDetails().containsKey("fieldErrors"));
    }

    @Test
    public void testHandleBusinessExceptions() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/offers", 
                new InvalidOfferRequest(), 
                ErrorResponse.class); // assuming InvalidOfferRequest triggers an InvalidOfferException
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid offer details", response.getBody().getMessage()); // assuming this is the error message
    }

    @Test
    public void testHandleGenericException() {
        // manually triggering a generic exception
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/api/error", ErrorResponse.class); 
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
    }

    // Inner classes for the sake of example, these would correspond to your actual implementations

    public static class CreateOfferRequest {
        @NotBlank
        private String description;

        public void setDescription(String description) {
            this.description = description;
        }
    }
    
    public static class InvalidOfferRequest { }

}