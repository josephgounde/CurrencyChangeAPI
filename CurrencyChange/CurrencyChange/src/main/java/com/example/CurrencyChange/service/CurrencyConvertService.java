package com.example.CurrencyChange.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service // mark this class a service
public class CurrencyConvertService {

    private final WebClient webClient;

    // Constructor to initialize WebClient.
    public CurrencyConvertService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.exchangerate-api.com/v4/latest/").build();
    }

    // Converts currency based on the fetched exchange rates.
    public Double convertCurrency(String fromCurrency, String toCurrency, Double amount) {
        try {
            // Call the external API to get the exchange rates for the base currency
            String response = webClient.get()
                    .uri(fromCurrency)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Parse the response JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode ratesNode = rootNode.get("rates");

            if (ratesNode != null && ratesNode.has(toCurrency)) {
                // Extract the exchange rate for the target currency
                Double exchangeRate = ratesNode.get(toCurrency).asDouble();
                // Calculate the converted amount
                return amount * exchangeRate;
            } else {
                throw new IllegalArgumentException("Invalid target currency: " + toCurrency);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during currency conversion: " + e.getMessage());
        }
    }
}
