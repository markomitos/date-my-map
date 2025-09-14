package com.ftn.sbnz.model.models;

import java.io.Serializable;
import java.util.Objects;

public class NextQuestion implements Serializable {

    private String questionId;

    public NextQuestion() {
    }

    public NextQuestion(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NextQuestion that = (NextQuestion) o;
        return Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }

    @Override
    public String toString() {
        return "NextQuestion{" +
                "questionId='" + questionId + '\'' +
                '}';
    }
}