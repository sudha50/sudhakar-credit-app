package com.cardoffers.oms.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import jakarta.validation.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import com.cardoffers.oms.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
void shouldSetAndGetAllFields() {
    // Given
    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp("2023-10-05T12:00:00Z")
        .status(404)
        .error("Not Found")
        .build();

    // When
    String timestamp = errorResponse.getTimestamp();
    int status = errorResponse.getStatus();
    String error = errorResponse.getError();

    // Then
    assertEquals("2023-10-05T12:00:00Z", timestamp);
    assertEquals(404, status);
    assertEquals("Not Found", error);
}

}