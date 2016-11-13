package com.telecom.mockexamapp.bean;

/**
 * Created by Administrator on 2016/8/19.
 */

public class YesOrNoQuestion extends Question {
    private String questionContent;
    private boolean rightAnswer;

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public boolean isRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(boolean rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

}
