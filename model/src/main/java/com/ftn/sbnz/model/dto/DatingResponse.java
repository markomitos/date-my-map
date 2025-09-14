package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.models.Period;

import java.util.List;

public class DatingResponse {
    private String message;
    private String sessionId;
    private List<Period> periods;
    private String questionId;

    public DatingResponse() {
    }

    public DatingResponse(String sessionId, String message, List<Period> periods) {
        this.message = message;
        this.sessionId = sessionId;
        this.periods = periods;
        this.questionId = null;
    }

    public DatingResponse(String sessionId, String message, List<Period> periods, String questionId) {
        this.message = message;
        this.sessionId = sessionId;
        this.periods = periods;
        this.questionId = questionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}

