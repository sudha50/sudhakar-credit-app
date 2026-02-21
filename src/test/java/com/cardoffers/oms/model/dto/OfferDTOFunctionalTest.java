package com.cardoffers.oms.model.dto;

import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OfferDTOFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateOfferSuccessfully() throws Exception {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Special Offer");
        offerDTO.setDescription("A special offer for our customers.");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(new BigDecimal("10.0"));
        offerDTO.setCashbackAmount(new BigDecimal("5.0"));
        offerDTO.setMinimumPurchaseAmount(new BigDecimal("20.0"));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setTermsAndConditions("Terms apply.");
        MerchantDTO merchant = new MerchantDTO();
        merchant.setId(1L); // Assume valid merchant ID
        offerDTO.setMerchant(merchant);
        CardNetworkDTO cardNetwork = new CardNetworkDTO();
        cardNetwork.setId(1L); // Assume valid card network ID
        offerDTO.setCardNetwork(cardNetwork);
        offerDTO.setSource("Website");
        offerDTO.setMaxRedemptions(100);
        offerDTO.setCurrentRedemptions(0);
        offerDTO.setActive(true);

        mockMvc.perform(post("/offers") // Assuming the endpoint is /offers
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Special Offer"));
    }

    @Test
    public void shouldRejectOfferCreationWithoutTitle() throws Exception {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setDescription("A special offer for our customers.");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(new BigDecimal("10.0"));
        offerDTO.setCashbackAmount(new BigDecimal("5.0"));
        offerDTO.setMinimumPurchaseAmount(new BigDecimal("20.0"));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setTermsAndConditions("Terms apply.");
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

        mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectOfferCreationWithInvalidDiscountPercentage() throws Exception {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Special Offer");
        offerDTO.setDescription("A special offer for our customers.");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(new BigDecimal("-5.0")); // Invalid
        offerDTO.setCashbackAmount(new BigDecimal("5.0"));
        offerDTO.setMinimumPurchaseAmount(new BigDecimal("20.0"));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setTermsAndConditions("Terms apply.");
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

        mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectOfferCreationWithEndDateBeforeStartDate() throws Exception {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Special Offer");
        offerDTO.setDescription("A special offer for our customers.");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(new BigDecimal("10.0"));
        offerDTO.setCashbackAmount(new BigDecimal("5.0"));
        offerDTO.setMinimumPurchaseAmount(new BigDecimal("20.0"));
        offerDTO.setStartDate(LocalDate.now().plusDays(10)); // Start date is after End date
        offerDTO.setEndDate(LocalDate.now());
        offerDTO.setTermsAndConditions("Terms apply.");
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

        mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRejectOfferCreationWhenMerchantIsNull() throws Exception {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setTitle("Special Offer");
        offerDTO.setDescription("A special offer for our customers.");
        offerDTO.setOfferType("Discount");
        offerDTO.setDiscountPercentage(new BigDecimal("10.0"));
        offerDTO.setCashbackAmount(new BigDecimal("5.0"));
        offerDTO.setMinimumPurchaseAmount(new BigDecimal("20.0"));
        offerDTO.setStartDate(LocalDate.now());
        offerDTO.setEndDate(LocalDate.now().plusDays(10));
        offerDTO.setTermsAndConditions("Terms apply.");
        offerDTO.setMerchant(null); // Invalid
        CardNetworkDTO cardNetwork = new CardNetworkDTO();
        cardNetwork.setId(1L);
        offerDTO.setCardNetwork(cardNetwork);
        offerDTO.setSource("Website");
        offerDTO.setMaxRedemptions(100);
        offerDTO.setCurrentRedemptions(0);
        offerDTO.setActive(true);

        mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(offerDTO)))
                .andExpect(status().isBadRequest());
    }
}