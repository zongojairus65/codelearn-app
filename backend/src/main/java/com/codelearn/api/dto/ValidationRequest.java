package com.codelearn.api.dto;

public class ValidationRequest {
    private Long exerciseId;
    private String html;
    private String css;
    private String js;

    public Long getExerciseId() { return exerciseId; }
    public void setExerciseId(Long exerciseId) { this.exerciseId = exerciseId; }

    public String getHtml() { return html; }
    public void setHtml(String html) { this.html = html; }

    public String getCss() { return css; }
    public void setCss(String css) { this.css = css; }

    public String getJs() { return js; }
    public void setJs(String js) { this.js = js; }
}
