package com.codelearn.api.service;

import com.codelearn.api.dto.ValidationRequest;
import com.codelearn.api.dto.ValidationResult;
import com.codelearn.api.dto.ValidationRules;
import com.codelearn.api.model.Exercise;
import com.codelearn.api.repository.ExerciseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HtmlCssValidator {

    private final ExerciseRepository exerciseRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HtmlCssValidator(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public ValidationResult validate(ValidationRequest req) {
        List<String> messages = new ArrayList<>();
        Long exerciseId = req.getExerciseId();

        Optional<Exercise> exerciseOpt = exerciseId != null
                ? exerciseRepository.findById(exerciseId)
                : Optional.empty();

        if (exerciseOpt.isEmpty()) {
            messages.add("Exercice inconnu (id=" + exerciseId + ").");
            return new ValidationResult(false, messages, 0);
        }

        Exercise exercise = exerciseOpt.get();
        ValidationRules rules;
        try {
            rules = objectMapper.readValue(exercise.getValidationRules(), ValidationRules.class);
        } catch (Exception e) {
            messages.add("Erreur de configuration de l'exercice.");
            return new ValidationResult(false, messages, 0);
        }

        Document doc = Jsoup.parse(req.getHtml() == null ? "" : req.getHtml());

        List<String> requiredTags = rules.getRequiredTags() != null ? rules.getRequiredTags() : List.of();
        int minTextLength = rules.getMinTextLength() != null ? rules.getMinTextLength() : 0;

        int totalChecks = requiredTags.size() + (minTextLength > 0 ? 1 : 0);
        int score = 0;

        Elements lastFoundElements = null;
        for (String tag : requiredTags) {
            Elements found = doc.select(tag);
            lastFoundElements = found;
            if (!found.isEmpty()) {
                score++;
                messages.add("✓ Balise <" + tag + "> trouvée.");
            } else {
                messages.add("✗ Il manque une balise <" + tag + ">.");
            }
        }

        if (minTextLength > 0) {
            boolean hasEnoughText = lastFoundElements != null
                    && !lastFoundElements.isEmpty()
                    && lastFoundElements.text().trim().length() >= minTextLength;
            if (hasEnoughText) {
                score++;
                messages.add("✓ Le texte requis est présent.");
            } else {
                messages.add("✗ Il manque du texte (minimum " + minTextLength + " caractère(s)).");
            }
        }

        boolean passed = totalChecks > 0 && score == totalChecks;
        return new ValidationResult(passed, messages, score);
    }
}
