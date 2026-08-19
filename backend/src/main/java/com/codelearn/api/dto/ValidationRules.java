package com.codelearn.api.dto;

import java.util.List;

public class ValidationRules {
    private List<String> requiredTags;
    private Integer minTextLength;

    public List<String> getRequiredTags() { return requiredTags; }
    public void setRequiredTags(List<String> requiredTags) { this.requiredTags = requiredTags; }
    public Integer getMinTextLength() { return minTextLength; }
    public void setMinTextLength(Integer minTextLength) { this.minTextLength = minTextLength; }
}
