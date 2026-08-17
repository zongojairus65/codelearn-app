package com.codelearn.api.controller;

import com.codelearn.api.dto.ValidationRequest;
import com.codelearn.api.dto.ValidationResult;
import com.codelearn.api.service.HtmlCssValidator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final HtmlCssValidator validator;

    public ExerciseController(HtmlCssValidator validator) {
        this.validator = validator;
    }

    @PostMapping("/validate")
    public ValidationResult validate(@RequestBody ValidationRequest request) {
        return validator.validate(request);
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
