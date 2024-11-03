package com.xische.currencyexchange.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class CurrencyService {
    @Value("${exchange-api.api-key}")
    private  String API_KEY;
    @Value("${exchange-api.api-url}")
    private  String BASE_URL;

    public double getConversionRate(String originalCurrency, String targetCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + API_KEY + "/latest/" + originalCurrency).toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.get("conversion_rates") != null) {
            Map<String, Double> conversionRates = (Map<String, Double>) response.get("conversion_rates");
            return Double.valueOf(conversionRates.getOrDefault(targetCurrency, 1.0)); // Default to 1.0 if not found
        }

        throw new RuntimeException("Could not retrieve conversion rate");
    }
}
