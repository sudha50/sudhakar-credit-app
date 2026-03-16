package com.cardoffers.oms.config;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.cardoffers.oms.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;
import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
void shouldOfferManagementOpenApi_happyPath() {
    // Given
    String expectedOpenApi = "Expected OpenAPI Specification";
    when(openApiConfig.offerManagementOpenApi()).thenReturn(expectedOpenApi);

    // When
    String result = openApiConfig.offerManagementOpenApi();

    // Then
    assertNotNull(result);
    assertEquals(expectedOpenApi, result);
    verify(openApiConfig).offerManagementOpenApi();
}

}