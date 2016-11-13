package com.telecom.mockexamapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.TestRecord;
import com.telecom.mockexamapp.model.IQuestionActivityView;
import com.telecom.mockexamapp.model.LoadQuestionView;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者： Administrator
 * @创建时间： 2016/11/13.
 * @修改时间： 2016/11/13
 * @类说明：
 */

public class PopsUpPagerAdapter<T extends LoadQuestionView,V extends  Question> extends PagerAdapter {
    private Map<Integer, View> contents;
    private int count;
    private Context context;
    private Class<T> questionType;
    private Class<V> questionBean;
    private TestRecord testRecord;
    public PopsUpPagerAdapter(Context mContext, Class<T> questionClass, Class<V> questionBean, TestRecord record, int count) {
        contents = new HashMap<>();
        this.count = count;
        this.context = mContext;
        this.questionType = questionClass;
        this.testRecord = record;
        this.questionBean = questionBean;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (contents.get(position) != null)
            container.removeView(contents.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LoadQuestionView view = null;
        try {
            view = questionType.newInstance();
            view.init(LayoutInflater.from(context),null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(context instanceof IQuestionActivityView){
           view.setQuestionViewImpl((IQuestionActivityView)context);
        }
        contents.put(position,view.getView());
        view.setRecord(testRecord);
        container.addView(view.getView());
        List<V> choisce = DataSupport.where("question_id = ?", String.valueOf((position + 1))).find(questionBean);
        view.setQuestionContent(choisce.get(0), position + 1);
        return view.getView();
    }
}
