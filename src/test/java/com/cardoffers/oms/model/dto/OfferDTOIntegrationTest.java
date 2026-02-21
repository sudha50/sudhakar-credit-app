package com.cardoffers.oms.model.dto;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.containers.PostgreSQLContainer;
import org.springframework.boot.testcontainers.utility.PropertyMapper;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@SpringJUnitConfig
@ActiveProfiles("test")
public class OfferDTOIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    static {
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        PropertyMapper mapper = PropertyMapper.get();
        mapper.from(postgreSQLContainer::getJdbcUrl).to(registry::add);
        mapper.from(postgreSQLContainer::getUsername).to(registry::add);
        mapper.from(postgreSQLContainer::getPassword).to(registry::add);
    }

    @Test
    public void testCreateOfferSuccessfully() throws Exception {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Summer Sale");
        offerDTO.setDescription("Discounts on summer items");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(BigDecimal.valueOf(20));
        offerDTO.setCashbackAmount(BigDecimal.valueOf(5));
        offerDTO.setMinimumPurchaseAmount(BigDecimal.valueOf(50));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setTermsAndConditions("T&C apply");
        
        MerchantDTO merchant = new MerchantDTO(); // Assuming MerchantDTO has a no-arg constructor and setters
        merchant.setId(1L); // Set a valid ID
        offerDTO.setMerchant(merchant);

        CardNetworkDTO cardNetwork = new CardNetworkDTO(); // Assuming CardNetworkDTO has a no-arg constructor and setters
        cardNetwork.setId(1L); // Set a valid ID
        offerDTO.setCardNetwork(cardNetwork);

        offerDTO.setSource("Website");
        offerDTO.setMaxRedemptions(100);
        offerDTO.setCurrentRedemptions(0);
        offerDTO.setActive(true);

        mockMvc.perform(post("/api/offers")
                .contentType("application/json")
                .content(asJsonString(offerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Summer Sale")));
    }

    @Test
    public void testCreateOfferWithMissingTitle() throws Exception {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("Discounts on summer items");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(BigDecimal.valueOf(20));
        offerDTO.setCashbackAmount(BigDecimal.valueOf(5));
        offerDTO.setMinimumPurchaseAmount(BigDecimal.valueOf(50));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setTermsAndConditions("T&C apply");

        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L);
        offerDTO.setMerchant(merchant);

        CardNetworkDTO cardNetwork = new CardNetworkDTO();
        cardNetwork.setId(1L);
        offerDTO.setCardNetwork(cardNetwork);

        offerDTO.setSource("Website");
        offerDTO.setMaxRedemptions(100);
        offerDTO.setCurrentRedemptions(0);
        offerDTO.setActive(true);

        mockMvc.perform(post("/api/offers")
                .contentType("application/json")
                .content(asJsonString(offerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title", hasItem("must not be blank")));
    }

    private String asJsonString(final OfferDTO offer) {
        // Implement serialization logic to convert the offerDTO into a JSON string
        return ""; // Placeholder for JSON conversion
    }
}