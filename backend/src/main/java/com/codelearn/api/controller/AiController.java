package com.codelearn.api.controller;

import com.codelearn.api.dto.*;
import com.codelearn.api.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String systemPrompt = """
                Tu es un assistant pédagogique pour un débutant qui apprend le HTML, CSS et JavaScript.
                Réponds en français, de manière brève et claire (5 lignes maximum),
                sans jamais donner directement la solution complète de l'exercice,
                mais en guidant avec des indices.
                Contexte de l'exercice actuel : %s
                """.formatted(request.exerciseContext());

        String reply = geminiService.generateContent(systemPrompt, request.userMessage());
        return new ChatResponse(reply);
    }

    @PostMapping("/generate-exercise")
    public GenerateExerciseResponse generateExercise(@RequestBody GenerateExerciseRequest request) {
        String systemPrompt = """
                Tu génères des exercices HTML/CSS/JS pour débutants.
                Réponds STRICTEMENT en JSON valide, sans texte autour, avec ce format exact :
                {"title": "...", "description": "...", "html": "...", "css": "...", "js": "..."}
                L'exercice doit être simple, testable, et cohérent (le HTML doit correspondre à la description).
                """;

        String userPrompt = "Sujet : %s. Niveau de difficulté : %s.".formatted(
                request.topic(), request.difficulty());

        String raw = geminiService.generateContent(systemPrompt, userPrompt);
        String cleaned = raw.trim()
                .replaceAll("^```json\\s*", "")
                .replaceAll("^```\\s*", "")
                .replaceAll("```\\s*$", "");

        try {
            return objectMapper.readValue(cleaned, GenerateExerciseResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Réponse IA invalide: " + cleaned, e);
        }
    }
}
