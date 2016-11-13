package com.telecom.mockexamapp.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.TestRecord;

/**
 * @作者： Administrator
 * @创建时间： 2016/11/13.
 * @修改时间： 2016/11/13
 * @类说明：
 */
//presenter?
public interface LoadQuestionView {
    void init(LayoutInflater inflater, ViewGroup viewGroup);
    View getView();
    void setRecord(TestRecord record);
    void setQuestionContent(Question questionContent,int position);
    void setQuestionViewImpl(IQuestionActivityView questionViewImpl);
    boolean isRightAnswer(String answer);
}
