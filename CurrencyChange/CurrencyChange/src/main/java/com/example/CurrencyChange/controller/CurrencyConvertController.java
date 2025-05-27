package com.example.CurrencyChange.controller;

import com.example.CurrencyChange.service.CurrencyConvertService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // Marks this class as a REST API controller.
@RequestMapping("/currency") // Sets the base URL path for all endpoints.
public class CurrencyConvertController {

    private final CurrencyConvertService exchangeService;

    // Constructor to inject the CurrencyConvertService dependency.
    public CurrencyConvertController(CurrencyConvertService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("/convert") // POST endpoint for currency conversion.
    public ResponseEntity<?> convertCurrency(
            @Parameter(description = "Code ISO à 3 lettres de la devise source (ex: USD, EUR, GBP)", example = "USD")
            @RequestParam String fromCurrency,
            @Parameter(description = "Code ISO à 3 lettres de la devise cible (ex: EUR, JPY, CAD)", example = "EUR")
            @RequestParam String toCurrency,
            @Parameter(description = "Montant à convertir (doit être un nombre positif)", example = "100.0")
            @RequestParam Double amount
    ) {
        try {
            // Call the service to perform the conversion.
            Double convertedAmount = exchangeService.convertCurrency(fromCurrency, toCurrency, amount);

            // Prepare the response
            Map<String, Object> response = new HashMap<>();
            response.put("fromCurrency", fromCurrency);
            response.put("toCurrency", toCurrency);
            response.put("amount", amount);
            response.put("convertedAmount", convertedAmount);

            return ResponseEntity.ok(response); // Return the converted result
        } catch (Exception e) {
            // Return error details in case of an exception
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
