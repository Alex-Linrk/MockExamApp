package com.telecom.mockexamapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.telecom.mockexamapp.R;
import com.telecom.mockexamapp.adapter.PopsUpPagerAdapter;
import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.SingleChoice;
import com.telecom.mockexamapp.bean.TestRecord;
import com.telecom.mockexamapp.model.IQuestionActivityView;
import com.telecom.mockexamapp.view.SignalChoiceQuestionContentView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.litepal.crud.DataSupport.where;

/**
 * Created by Administrator on 2016/9/4.
 */

public class SignalChoiceActivity extends AppCompatActivity implements IQuestionActivityView {
    @BindView(R.id.vp_content)
    ViewPager vp_Content;
    private int countCompleted;
    private int countRight;
    private String type;
    private int recordPosition = 0;
    private PopsUpPagerAdapter choiceAdapater;
    private TestRecord record;
    @BindView(R.id.tv_completed)
    TextView tv_countCompleted;
    @BindView(R.id.tv_right_count)
    TextView tv_rightCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signalchoice);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra(Question.TEST_TITLE);
        recordPosition = getIntent().getIntExtra(Question.TEST_RECORD_POSTION, 0);
        sendTestRecord();
        choiceAdapater = new PopsUpPagerAdapter(this,
                SignalChoiceQuestionContentView.class,
                SingleChoice.class, record, DataSupport.count(SingleChoice.class));
        vp_Content.setAdapter(choiceAdapater);
        vp_Content.setCurrentItem(recordPosition);

    }

    public void sendTestRecord() {
        List<TestRecord> records = new ArrayList<>();
        records = where("questiontype like ?", Question.TEST_QUESTION_TYPE_SINGLE).find(TestRecord.class);
        if (records == null || records.size() == 0) {
            record = new TestRecord();
            record.setQuestionType(Question.TEST_QUESTION_TYPE_SINGLE);
            record.save();
        } else {
            record = records.get(0);
        }
        readRightRecord();
    }

    private void readRightRecord() {
        countCompleted = DataSupport.where("useranswer is not NULL and useranswer not like '' ").count(SingleChoice.class);
        countRight = DataSupport.where("rightselection = useranswer").count(SingleChoice.class);
        tv_countCompleted.setText(String.valueOf(countCompleted));
        tv_rightCount.setText(String.valueOf(countRight));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void answerCallBacked(boolean isRight) {
        tv_countCompleted.setText(String.valueOf(++countCompleted));
        if (isRight)
            tv_rightCount.setText(String.valueOf(++countRight));
    }

    @Override
    public void emptyAnswerMessage() {
        Toast.makeText(this, "请选择一个答案", Toast.LENGTH_SHORT).show();
    }
}
