package com.telecom.mockexamapp.view;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telecom.mockexamapp.R;
import com.telecom.mockexamapp.bean.MultipleChoice;
import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.TestRecord;
import com.telecom.mockexamapp.model.IQuestionActivityView;
import com.telecom.mockexamapp.model.LoadQuestionView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/10.
 */
public class MultipleChoiceQuestionContentVew implements LoadQuestionView{
    private View contentView;
    @BindView(R.id.tv_mulite_quetion_content)
    TextView tv_QuestionContent;
    @BindView(R.id.cb_m_selectionA)
    CheckBox cb_selectA;
    @BindView(R.id.cb_m_selectionB)
    CheckBox cb_selectB;
    @BindView(R.id.cb_m_selectionC)
    CheckBox cb_selectC;
    @BindView(R.id.cb_m_selectionD)
    CheckBox cb_selectD;
    @BindView(R.id.cb_m_selectionE)
    CheckBox cb_selectE;
    @BindView(R.id.cb_m_selectionF)
    CheckBox cb_selectF;
    @BindView(R.id.ll_answer_describe)
    LinearLayout ll_AnswerDescribe;
    @BindView(R.id.tv_m_rightanswer)
    TextView tv_MultipleRightAnswer;
    @BindView(R.id.tv_m_answerback)
    TextView tv_AnswerBack;
    @BindView(R.id.ll_answer_list)
    LinearLayout ll_AnserList;
    @BindView(R.id.bt_m_post)
    Button bt_post;
    private MultipleChoice choice;
    private int position;
    private TestRecord record;
    private StringBuilder answerBuilder;
    private AnswerCheckedChangedListener changedListener;
    private IQuestionActivityView iQuestionActivityView;
    @Override
    public void init(LayoutInflater inflater, ViewGroup viewGroup) {
        contentView = inflater.inflate(R.layout.mulitechoice_layout, null);
        ButterKnife.bind(this, contentView);
        answerBuilder = new StringBuilder();
        changedListener = new AnswerCheckedChangedListener();
    }

    public View getView() {
        return this.contentView;
    }

    public void setRecord(TestRecord record) {
        this.record = record;
    }
    public void setQuestionViewImpl(IQuestionActivityView questionViewImpl){
        this.iQuestionActivityView = questionViewImpl;
    }
    public void setQuestionContent(Question question, int position) {
        this.choice = (MultipleChoice) question;
        this.position = position;
        tv_QuestionContent.setText(position + ".:" + choice.getQuestionContent());
        tv_MultipleRightAnswer.setText(choice.getRightSelection());
        cb_selectA.setText("A:" + choice.getSelectionA());
        cb_selectB.setText("B:" + choice.getSelectionB());
        if (!TextUtils.isEmpty(choice.getSelectionC())) {
            cb_selectC.setText("C:" + choice.getSelectionC());
        } else {
            cb_selectC.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(choice.getSelectionD())) {
            cb_selectD.setVisibility(View.GONE);
        } else {
            cb_selectD.setText("D:" + choice.getSelectionD());
        }
        if (TextUtils.isEmpty(choice.getSelectionE())) {
            cb_selectE.setVisibility(View.GONE);
        } else {
            cb_selectE.setText("E:" + choice.getSelectionE());
        }
        if (TextUtils.isEmpty(choice.getSelectionF())) {
            cb_selectF.setVisibility(View.GONE);
        } else {
            cb_selectF.setText("F:" + choice.getSelectionF());
        }
        if (!TextUtils.isEmpty(choice.getUserAnswer())) {
            String answer = choice.getUserAnswer();
            char[] selects = answer.toCharArray();
            for (View view : ll_AnserList.getTouchables()) {
                if (view instanceof CheckBox) {
                    CheckBox selectView = (CheckBox) view;
                    selectView.setEnabled(false);
                    for (char userSelect : selects) {
                        if (String.valueOf(userSelect).equals(String.valueOf(selectView.getTag()))) {
                            selectView.setChecked(true);
                            break;
                        }
                    }
                }
            }
            isRightAnswer(answer);
            bt_post.setEnabled(false);
            ll_AnswerDescribe.setVisibility(View.VISIBLE);

        }
        initLister();
    }

    private void initLister() {
        cb_selectA.setOnCheckedChangeListener(changedListener);
        cb_selectB.setOnCheckedChangeListener(changedListener);
        cb_selectC.setOnCheckedChangeListener(changedListener);
        cb_selectD.setOnCheckedChangeListener(changedListener);
        cb_selectE.setOnCheckedChangeListener(changedListener);
        cb_selectF.setOnCheckedChangeListener(changedListener);
    }

    private class AnswerCheckedChangedListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                answerBuilder.append(String.valueOf(buttonView.getTag()));
            } else {
                answerBuilder.deleteCharAt(answerBuilder.toString().indexOf(String.valueOf(buttonView.getTag())));
            }
            if(answerBuilder.toString().length()>0) {
                char[] anserArray = answerBuilder.toString().toCharArray();
                Arrays.sort(anserArray);
                if(!answerBuilder.toString().equals(anserArray.toString())){
                    answerBuilder = new StringBuilder();
                    answerBuilder.append(anserArray);
                }
            }
        }
    }

    @OnClick(R.id.bt_m_post)
    void postAnswer() {
        if (!TextUtils.isEmpty(answerBuilder)) {
            String userAnswer = answerBuilder.toString();
            boolean isRight =  isRightAnswer(userAnswer);
            for (View anserVeiew :
                    ll_AnserList.getTouchables()) {
                if (anserVeiew instanceof CheckBox) {
                    anserVeiew.setEnabled(false);
                }
            }
            bt_post.setEnabled(false);
            if(isRight){
                choice.setRight(choice.getRight()+1);
            }
            choice.setCount(choice.getCount() + 1);
            choice.setUserAnswer(answerBuilder.toString());
            choice.save();
            if (record != null) {
                record.setPosition(position);
                record.save();
            }
            if(iQuestionActivityView!=null){
                iQuestionActivityView.answerCallBacked(isRight);
            }
            ll_AnswerDescribe.setVisibility(View.VISIBLE);
        } else {
            if(iQuestionActivityView != null){
                iQuestionActivityView.emptyAnswerMessage();
            }
        }
    }

    public boolean isRightAnswer(String answer) {
        boolean isRight = false;
        String[] rightAnswer = null;
        StringBuilder builder = new StringBuilder();
        if (choice.getRightSelection().contains(",")) {
            rightAnswer = choice.getRightSelection().split(",");
        } else if (choice.getRightSelection().contains("，")) {
            rightAnswer = choice.getRightSelection().split("，");
        } else {
            rightAnswer = new String[choice.getRightSelection().toCharArray().length];
            for (int i = 0; i < choice.getRightSelection().toCharArray().length; i++) {
                rightAnswer[i] = String.valueOf(choice.getRightSelection().toCharArray()[i]);
            }
        }
        for (String selection:
             rightAnswer) {
            builder.append(selection);
        }
        if (!answer.equals(builder.toString())) {
            tv_AnswerBack.setText("答错了");
            tv_AnswerBack.setTextColor(Color.RED);
            tv_MultipleRightAnswer.setTextColor(Color.RED);
        } else {
            isRight = true;
            choice.setRight(choice.getRight() + 1);
            tv_AnswerBack.setText("正确");
            tv_AnswerBack.setTextColor(Color.BLUE);
            tv_MultipleRightAnswer.setTextColor(Color.BLUE);
        }
        return isRight;
    }
}

