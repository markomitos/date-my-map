package com.ftn.sbnz.model.models;

public class QuestionNode {

    private String id;
    private String text;
    private String nextQuestionIdOnYes;
    private String nextQuestionIdOnNo;

    public QuestionNode(String id, String text, String nextQuestionIdOnYes, String nextQuestionIdOnNo) {
        this.id = id;
        this.text = text;
        this.nextQuestionIdOnYes = nextQuestionIdOnYes;
        this.nextQuestionIdOnNo = nextQuestionIdOnNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNextQuestionIdOnYes() {
        return nextQuestionIdOnYes;
    }

    public void setNextQuestionIdOnYes(String nextQuestionIdOnYes) {
        this.nextQuestionIdOnYes = nextQuestionIdOnYes;
    }

    public String getNextQuestionIdOnNo() {
        return nextQuestionIdOnNo;
    }

    public void setNextQuestionIdOnNo(String nextQuestionIdOnNo) {
        this.nextQuestionIdOnNo = nextQuestionIdOnNo;
    }
}
