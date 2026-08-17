package com.codelearn.api.service;

import com.codelearn.api.dto.ValidationRequest;
import com.codelearn.api.dto.ValidationResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HtmlCssValidator {

    public ValidationResult validate(ValidationRequest req) {
        List<String> messages = new ArrayList<>();
        int score = 0;
        int totalChecks = 0;

        Document doc = Jsoup.parse(req.getHtml() == null ? "" : req.getHtml());
        long exerciseId = req.getExerciseId() != null ? req.getExerciseId() : -1;

        if (exerciseId == 1L) {
            totalChecks = 2;

            Elements h1 = doc.select("h1");
            if (!h1.isEmpty()) {
                score++;
                messages.add("✓ Balise <h1> trouvée.");
            } else {
                messages.add("✗ Il manque une balise <h1>.");
            }

            if (!h1.isEmpty() && !h1.text().trim().isEmpty()) {
                score++;
                messages.add("✓ Le <h1> contient du texte.");
            } else {
                messages.add("✗ Le <h1> doit contenir du texte.");
            }
        } else {
            messages.add("Exercice inconnu (id=" + exerciseId + ").");
        }

        boolean passed = totalChecks > 0 && score == totalChecks;
        return new ValidationResult(passed, messages, score);
    }
}
