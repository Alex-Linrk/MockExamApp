package com.telecom.mockexamapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import android.widget.Toast;

import com.telecom.mockexamapp.R;
import com.telecom.mockexamapp.adapter.PopsUpPagerAdapter;
import com.telecom.mockexamapp.bean.MultipleChoice;
import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.TestRecord;
import com.telecom.mockexamapp.model.IQuestionActivityView;
import com.telecom.mockexamapp.view.MultipleChoiceQuestionContentVew;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/21.
 */

public class MultipleTestActivity extends BaseActivity implements IQuestionActivityView {
    @BindView(R.id.vp_m_content)
    ViewPager vp_Content;
    @BindView(R.id.tv_mulite_completed)
    TextView tv_muliteCompleted;
    @BindView(R.id.tv_mulite_right_count)
    TextView tv_muliteRight;
    private String type;
    private int recordPosition;
    private int countCompleted;
    private int countRight;
    private TestRecord record;
    private PopsUpPagerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulitechoice);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra(Question.TEST_TITLE);
        recordPosition = getIntent().getIntExtra(Question.TEST_RECORD_POSTION, 0);
        sendTestRecord();
        adapter = new PopsUpPagerAdapter(this,
                MultipleChoiceQuestionContentVew.class,
                MultipleChoice.class, record, DataSupport.count(MultipleChoice.class));
        vp_Content.setAdapter(adapter);
        vp_Content.setCurrentItem(recordPosition);
    }

    public void sendTestRecord() {
        List<TestRecord> records = new ArrayList<>();
        records = DataSupport.where("questiontype like ?", Question.TEST_QUESTION_TYPE_MULTI).find(TestRecord.class);
        if (records == null || records.size() == 0) {
            record = new TestRecord();
            record.setQuestionType(Question.TEST_QUESTION_TYPE_MULTI);
            record.save();
        } else {
            record = records.get(0);
        }
        readRightRecord();
    }
    private void readRightRecord() {
        countCompleted = DataSupport.where("useranswer is not NULL and useranswer not like '' ").count(MultipleChoice.class);
        countRight = DataSupport.where("rightselection = useranswer").count(MultipleChoice.class);
        tv_muliteCompleted.setText(String.valueOf(countCompleted));
        tv_muliteRight.setText(String.valueOf(countRight));
    }


    @Override
    public void answerCallBacked(boolean isRight) {
        tv_muliteCompleted.setText(String.valueOf(++countCompleted));
        if (isRight)
            tv_muliteRight.setText(String.valueOf(++countRight));
    }

    @Override
    public void emptyAnswerMessage() {
        Toast.makeText(this, "请选择一个答案", Toast.LENGTH_SHORT).show();
    }
}
