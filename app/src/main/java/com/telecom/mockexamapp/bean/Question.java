package com.telecom.mockexamapp.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/8/19.
 */

public class Question extends DataSupport {
    public static final String TEST_TITLE = "TEST_TYPE";
    public static final String TEST_TYPE_ALL = "all";
    public static final String TEST_TYPE_RANDOM = "random";
    public static final String TEST_RECORD_POSTION = "startposition";
    public static final String TEST_QUESTION_TYPE_SINGLE = "single";
    public static final String TEST_QUESTION_TYPE_MULTI = "multi";
    public static final String TEST_TYPY_RESTART = "restart";
    protected long question_id;
    protected String question_type;

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }
}
