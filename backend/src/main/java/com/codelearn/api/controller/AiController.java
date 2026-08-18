package com.codelearn.api.controller;

import com.codelearn.api.dto.*;
import com.codelearn.api.service.GeminiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private static final Logger log = LoggerFactory.getLogger(AiController.class);
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
                Réponds STRICTEMENT en JSON valide, sans texte avant ni après, sans balises markdown,
                avec exactement ce format :
                {"title": "...", "description": "...", "html": "...", "css": "...", "js": "..."}
                L'exercice doit être simple, testable, et cohérent (le HTML doit correspondre à la description).
                Les valeurs JSON doivent être des chaînes de texte simples sur une seule ligne,
                sans retours à la ligne littéraux à l'intérieur des valeurs.
                """;

        String userPrompt = "Sujet : %s. Niveau de difficulté : %s.".formatted(
                request.topic(), request.difficulty());

        String raw = geminiService.generateContent(systemPrompt, userPrompt);
        log.info("Réponse brute Gemini (generate-exercise): {}", raw);

        String cleaned = extractJson(raw);

        try {
            return objectMapper.readValue(cleaned, GenerateExerciseResponse.class);
        } catch (Exception e) {
            log.error("Échec du parsing JSON. Contenu nettoyé: {}", cleaned, e);
            throw new RuntimeException("Réponse IA invalide, impossible de générer l'exercice. Réessaie.", e);
        }
    }

    private String extractJson(String raw) {
        String trimmed = raw.trim();
        trimmed = trimmed.replaceAll("^```json\\s*", "").replaceAll("^```\\s*", "").replaceAll("```\\s*$", "");
        Pattern pattern = Pattern.compile("\\{.*\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(trimmed);
        if (matcher.find()) {
            return matcher.group();
        }
        return trimmed;
    }
}
