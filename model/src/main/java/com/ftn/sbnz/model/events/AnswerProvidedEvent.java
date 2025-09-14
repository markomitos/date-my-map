package com.ftn.sbnz.model.events;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;

//@Role(Role.Type.EVENT)
//@Timestamp("timestamp")
public class AnswerProvidedEvent implements Serializable {

    private String questionId;
    private String answer;
    //public Timestamp timestamp;

    public AnswerProvidedEvent() {
    }

    public AnswerProvidedEvent(String questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
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
