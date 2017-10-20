package com.telecom.mockexamapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.telecom.mockexamapp.R;
import com.telecom.mockexamapp.bean.MultipleChoice;
import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.SingleChoice;
import com.telecom.mockexamapp.bean.TestRecord;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.start_mock)
    Button bv_LoadDb;
    private ProgressDialog loadProgress;
    private static final int START_LOAD = 0;
    private static final int END_LOAD = 1;
    private static final int AUTO_DISSM = 2;
    @BindView(R.id.rg_questiontype)
    RadioGroup rg_questionType;
    @BindView(R.id.rg_testoption)
    RadioGroup rg_testType;
    @BindView(R.id.rg_record)
    RadioGroup rg_testRecord;
    @BindView(R.id.rb_continue)
    RadioButton rb_continue;
    @BindView(R.id.bt_loadexcl)
    Button bt_loadExcl;
    private int recordPosition = 0;
    private boolean isRestart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bt_loadExcl.setOnClickListener(new LoadExcleOnclickListener());
        bv_LoadDb.setOnClickListener((view)-> {
                if (rg_questionType.getCheckedRadioButtonId() == R.id.rb_single) {
                    if (DataSupport.count(SingleChoice.class) == 0) {
                        Toast.makeText(MainActivity.this, "没有习题，请先导入题库", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, SignalChoiceActivity.class);
                    if (rg_testType.getCheckedRadioButtonId() == R.id.rb_all) {
                        intent.putExtra(Question.TEST_TITLE, Question.TEST_TYPE_ALL);
                    } else {
                        intent.putExtra(Question.TEST_TITLE, Question.TEST_TYPE_RANDOM);
                    }
                    if (isRestart) {
                        ContentValues values = new ContentValues();
                        values.put("useranswer", "");
                        SingleChoice.updateAll(SingleChoice.class, values);
                        ContentValues valuesTestRescord = new ContentValues();
                        valuesTestRescord.put("position", "0");
                        TestRecord.updateAll(TestRecord.class, valuesTestRescord, "questiontype = ?", "single");
                    }
                    intent.putExtra(Question.TEST_RECORD_POSTION, recordPosition);
                    startActivity(intent);
                } else if (rg_questionType.getCheckedRadioButtonId() == R.id.rb_mutile) {
                    if (DataSupport.count(MultipleChoice.class) == 0) {
                        Toast.makeText(MainActivity.this, "没有习题，请先导入题库", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(MainActivity.this, MultipleTestActivity.class);
                    if (rg_testType.getCheckedRadioButtonId() == R.id.rb_all) {
                        intent.putExtra(Question.TEST_TITLE, Question.TEST_TYPE_ALL);
                    } else {
                        intent.putExtra(Question.TEST_TITLE, Question.TEST_TYPE_RANDOM);
                    }
                    if (isRestart) {
                        ContentValues values = new ContentValues();
                        values.put("useranswer", "");
                        SingleChoice.updateAll(MultipleChoice.class, values);
                        ContentValues valuesTestRescord = new ContentValues();
                        valuesTestRescord.put("position", "0");
                        TestRecord.updateAll(TestRecord.class, valuesTestRescord, "questiontype = ?", "multi");
                    }
                    intent.putExtra(Question.TEST_RECORD_POSTION, recordPosition);
                    startActivity(intent);
                }

        });
        rg_questionType.setOnCheckedChangeListener((view,checked)->
                showRecord()
        );
        rg_testType.setOnCheckedChangeListener((group,checkedId)->{
                if (checkedId == R.id.rb_random_onehundred) {
                    rg_testRecord.setVisibility(View.GONE);
                } else if (checkedId == R.id.rb_all) {
                    recordPosition = getRecordPosition();
                    if (recordPosition > 1) {
                        rb_continue.setText("从第" + recordPosition + "继续");
                        rg_testRecord.setVisibility(View.VISIBLE);
                    }
                }

        });
        rg_testRecord.setOnCheckedChangeListener((RadioGroup group, int checkedId)-> {
                if (checkedId == R.id.rb_restart) {
                    recordPosition = 0;
                    isRestart = true;
                } else if (checkedId == R.id.rb_continue) {
                    isRestart = false;
                    recordPosition = getRecordPosition();
                }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDB();
    }

    private void loadDB() {
        Connector.getDatabase();
        showRecord();
    }

    private void showRecord() {
        recordPosition = getRecordPosition();
        if (recordPosition > 0) {
            rb_continue.setText("从第" + recordPosition + "继续");
            rg_testRecord.setVisibility(View.VISIBLE);
        } else {
            rg_testRecord.setVisibility(View.GONE);
        }
    }

    private int getRecordPosition() {
        if (rg_questionType.getCheckedRadioButtonId() == R.id.rb_single) {
            List<TestRecord> singleRecord = DataSupport.where("questionType like ?", Question.TEST_QUESTION_TYPE_SINGLE).find(TestRecord.class);
            if (singleRecord.size() > 0 && singleRecord.get(0).getPosition() > 1) {
                return singleRecord.get(0).getPosition();
            } else {
                return 0;
            }
        } else if (rg_questionType.getCheckedRadioButtonId() == R.id.rb_mutile) {
            List<TestRecord> mutileRecord = DataSupport.where("questionType like ?", Question.TEST_QUESTION_TYPE_MULTI).find(TestRecord.class);
            if (mutileRecord.size() > 0 && mutileRecord.get(0).getPosition() > 1) {
                return mutileRecord.get(0).getPosition();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    private class LoadExcleOnclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(MainActivity.this).setTitle("提示：")
                    .setMessage("此操作会清空原有习题数据库，是否继续？")
                    .setCancelable(false).setPositiveButton(android.R.string.yes, (DialogInterface dialog, int which)-> {
                    Intent intent = new Intent(MainActivity.this, LoadFileActivity.class);
                    startActivity(intent);
            }).setNegativeButton(android.R.string.cancel,
                    (DialogInterface dialog, int which)->
                    dialog.dismiss()
            ).create().show();
        }
    }

}
