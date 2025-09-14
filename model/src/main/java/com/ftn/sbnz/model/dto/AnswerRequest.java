package com.ftn.sbnz.model.dto;

public class AnswerRequest {
    private String sessionId;
    private String questionId;
    private String answer;

    public AnswerRequest() {
    }

    public AnswerRequest(String sessionId, String questionId, String answer) {
        this.sessionId = sessionId;
        this.questionId = questionId;
        this.answer = answer;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

