package com.telecom.mockexamapp.view;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.telecom.mockexamapp.R;
import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.SingleChoice;
import com.telecom.mockexamapp.bean.TestRecord;
import com.telecom.mockexamapp.model.IQuestionActivityView;
import com.telecom.mockexamapp.model.LoadQuestionView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/4.
 */

public class SignalChoiceQuestionContentView implements LoadQuestionView {
    private View contentView;
    @BindView(R.id.tv_questionContent)
    TextView tv_content;
    @BindView(R.id.rg_selection_group)
    RadioGroup rg_answerList;
    @BindView(R.id.rb_selection_a)
    RadioButton rb_selecA;
    @BindView(R.id.rb_selection_b)
    RadioButton rb_selecB;
    @BindView(R.id.rb_selection_c)
    RadioButton rb_selecC;
    @BindView(R.id.rb_selection_d)
    RadioButton rb_selecD;
    @BindView(R.id.rb_selection_e)
    RadioButton rb_selecE;
    @BindView(R.id.rb_selection_f)
    RadioButton rb_selecF;
    @BindView(R.id.tv_back_answer)
    TextView tv_answerBack;
    @BindView(R.id.tv_right_answer)
    TextView tv_rightAnswer;
    @BindView(R.id.bt_post)
    Button bt_post;
    @BindView(R.id.bt_next)
    Button bt_next;
    @BindView(R.id.rl_rightAnswer)
    LinearLayout rl_rightAnswer;
    private SingleChoice choice;
    private boolean isRight = false;
    private TestRecord record;
    private int position = 0;
    private IQuestionActivityView questionActivityView;
    @Override
    public void init(LayoutInflater inflater, ViewGroup viewGroup) {
        contentView = inflater.inflate(R.layout.layout_signal,null);
        ButterKnife.bind(this,contentView);
    }

    @Override
    public void setQuestionViewImpl(IQuestionActivityView questionViewImpl) {
        this.questionActivityView = questionViewImpl;
    }

    public View getView(){
        return contentView;
    }
    public void setQuestionContent(Question questionContent, int position){
        this.choice = (SingleChoice) questionContent;
        this.position = position;
        initLister();
        tv_content.setText((position) + ":\t" + choice.getQuestionContent());
        tv_rightAnswer.setText("正确答案：" + choice.getRightSelection());
        rb_selecA.setText("A:\t" + choice.getSelectionA());
        rb_selecB.setText("B:\t" + choice.getSelectionB());
        if (choice.getSelectionC().isEmpty()) {
            rb_selecC.setVisibility(View.INVISIBLE);
        } else {
            rb_selecC.setVisibility(View.VISIBLE);
            rb_selecC.setText("C:\t" + choice.getSelectionC());
        }
        if (choice.getSelectionD().isEmpty()) {
            rb_selecD.setVisibility(View.INVISIBLE);
        } else {
            rb_selecD.setVisibility(View.VISIBLE);
            rb_selecD.setText("D:\t" + choice.getSelectionD());
        }
        if (choice.getSelectionE().isEmpty()) {
            rb_selecE.setVisibility(View.INVISIBLE);
        } else {
            rb_selecE.setVisibility(View.VISIBLE);
            rb_selecE.setText("E:\t" + choice.getSelectionE());
        }
        if (choice.getSelectionF().isEmpty()) {
            rb_selecF.setVisibility(View.INVISIBLE);
        } else {
            rb_selecF.setVisibility(View.VISIBLE);
            rb_selecF.setText("F:\t" + choice.getSelectionF());
        }

        if(!TextUtils.isEmpty(choice.getUserAnswer())){
                rl_rightAnswer.setVisibility(View.VISIBLE);
                rg_answerList.setClickable(false);
                for (View child:rg_answerList.getTouchables()){
                    RadioButton rButton = (RadioButton) child;
                    if(String.valueOf(rButton.getTag()).equals(choice.getUserAnswer())){
                        rButton.setChecked(true);
                    }
                    rButton.setEnabled(false);
                }
            bt_post.setEnabled(false);
        }
    }
    public boolean isRightAnswer(String answer){
        choice.setUserAnswer(answer);
        if(choice.getRightSelection().equals(answer)){
            isRight = true;
            tv_answerBack.setText("正确");
            tv_answerBack.setTextColor(Color.GREEN);
            tv_rightAnswer.setTextColor(Color.GREEN);
        }else{
            isRight = false;
            tv_answerBack.setText("答错了");
            tv_answerBack.setTextColor(Color.RED);
            tv_rightAnswer.setTextColor(Color.RED);
        }
        return isRight;
    }
    @OnClick(R.id.bt_post)
    void postAnswer(){
        if(rg_answerList.getCheckedRadioButtonId() == -1){
            if(questionActivityView != null){
                questionActivityView.emptyAnswerMessage();
            }
        }else {
            rl_rightAnswer.setVisibility(View.VISIBLE);
            bt_post.setEnabled(false);
            for (View child:
                 rg_answerList.getTouchables()) {
                child.setEnabled(false);
            }
            choice.setCount(choice.getCount()+1);
            if(questionActivityView != null){
                questionActivityView.answerCallBacked(isRight);
            }
            if(isRight) {
                choice.setRight(choice.getRight() + 1);
            }
            choice.save();
            if(record != null){
                record.setPosition(position);
                record.save();
            }

        }

    }

    private void initLister() {
        rg_answerList.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isRightAnswer(String.valueOf(group.findViewById(checkedId).getTag()));
            }
        });
    }
    public void setRecord(TestRecord testRecord){
        this.record = testRecord;
    }

}
