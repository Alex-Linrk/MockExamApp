package com.telecom.mockexamapp.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/8/28.
 */

public class TestRecord extends DataSupport {
    private String questionType;
    private int position;
    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
