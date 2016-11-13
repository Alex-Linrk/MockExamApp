package com.telecom.mockexamapp.model;

/**
 * @作者： Administrator
 * @创建时间： 2016/11/13.
 * @修改时间： 2016/11/13
 * @类说明：
 */

public interface IQuestionActivityView {
    void answerCallBacked(boolean isRight);

    void sendTestRecord();

    void emptyAnswerMessage();
}
