package com.codelearn.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String PRIMARY_MODEL = "gemini-flash-lite-latest";
    private static final String FALLBACK_MODEL = "gemini-flash-latest";

    public GeminiService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .build();
    }

    public String generateContent(String systemPrompt, String userPrompt) {
        try {
            return callModel(PRIMARY_MODEL, systemPrompt, userPrompt);
        } catch (Exception primaryError) {
            try {
                return callModel(FALLBACK_MODEL, systemPrompt, userPrompt);
            } catch (Exception fallbackError) {
                throw new RuntimeException("Gemini indisponible: " + fallbackError.getMessage(), fallbackError);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String callModel(String model, String systemPrompt, String userPrompt) {
        Map<String, Object> body = Map.of(
                "system_instruction", Map.of(
                        "parts", List.of(Map.of("text", systemPrompt))
                ),
                "contents", List.of(
                        Map.of("role", "user", "parts", List.of(Map.of("text", userPrompt)))
                )
        );

        Map<String, Object> response = restClient.post()
                .uri("/models/{model}:generateContent?key={key}", model, apiKey)
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        return (String) parts.get(0).get("text");
    }
}
