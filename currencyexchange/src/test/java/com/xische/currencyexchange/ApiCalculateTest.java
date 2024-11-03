package com.xische.currencyexchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiCalculateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jsonRequest;

    @BeforeEach
    public void setUp() {
        // Example request payload
        jsonRequest = "{\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"name\": \"item1\",\n" +
                "      \"category\": \"groceries\",\n" +
                "      \"amount\": 50\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"item2\",\n" +
                "      \"category\": \"electronics\",\n" +
                "      \"amount\": 150\n" +
                "    }\n" +
                "  ],\n" +
                "  \"totalAmount\": 200,\n" +
                "  \"userType\": \"user\",\n" +
                "  \"customerTenure\": 3,\n" +
                "  \"originalCurrency\": \"USD\",\n" +
                "  \"targetCurrency\": \"EUR\"\n" +
                "}";
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = {"USER"})
    public void testCalculateSuccess() throws Exception {
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk()) // Check for 200 OK response
                .andExpect(jsonPath("$.netPayableAmount").exists()); // Check for expected response field
    }

    @Test
    public void testCalculateUnauthorized() throws Exception {
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized()); // Check for 401 Unauthorized response
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    public void testCalculateWithAdminUser() throws Exception {
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden()); // Check for 403 Forbidden response for admin
    }
}
