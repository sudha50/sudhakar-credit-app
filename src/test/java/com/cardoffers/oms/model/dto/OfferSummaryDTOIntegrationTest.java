package com.cardoffers.oms.model.dto;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OfferSummaryDTOIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateOfferSummary() {
        OfferSummaryDTO offerSummary = new OfferSummaryDTO();
        offerSummary.setTitle("Summer Sale");
        offerSummary.setMerchantName("Best Store");
        offerSummary.setOfferType("Discount");
        offerSummary.setDiscountPercentage(new BigDecimal("20.0"));
        offerSummary.setStartDate(LocalDate.now());
        offerSummary.setEndDate(LocalDate.now().plusDays(30));
        offerSummary.setActive(true);
        
        ResponseEntity<OfferSummaryDTO> response = restTemplate.postForEntity("/offers", offerSummary, OfferSummaryDTO.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Summer Sale");
    }

    @Test
    public void testCreateOfferSummary_InvalidData() {
        OfferSummaryDTO offerSummary = new OfferSummaryDTO();
        offerSummary.setTitle(null);
        
        ResponseEntity<String> response = restTemplate.postForEntity("/offers", offerSummary, String.class);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void testGetOfferSummary() {
        OfferSummaryDTO offerSummary = new OfferSummaryDTO();
        offerSummary.setTitle("Winter Sale");
        offerSummary.setMerchantName("Cool Store");
        offerSummary.setOfferType("Discount");
        offerSummary.setDiscountPercentage(new BigDecimal("15.0"));
        offerSummary.setStartDate(LocalDate.now());
        offerSummary.setEndDate(LocalDate.now().plusDays(30));
        offerSummary.setActive(true);
        
        ResponseEntity<OfferSummaryDTO> createdResponse = restTemplate.postForEntity("/offers", offerSummary, OfferSummaryDTO.class);
        
        Long id = createdResponse.getBody().getId();
        ResponseEntity<OfferSummaryDTO> getResponse = restTemplate.getForEntity("/offers/" + id, OfferSummaryDTO.class);
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getTitle()).isEqualTo("Winter Sale");
    }
}