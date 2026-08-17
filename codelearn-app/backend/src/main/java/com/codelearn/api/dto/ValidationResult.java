package com.codelearn.api.dto;

import java.util.List;

public class ValidationResult {
    private boolean passed;
    private List<String> messages;
    private int score;

    public ValidationResult(boolean passed, List<String> messages, int score) {
        this.passed = passed;
        this.messages = messages;
        this.score = score;
    }

    public boolean isPassed() { return passed; }
    public List<String> getMessages() { return messages; }
    public int getScore() { return score; }
}
