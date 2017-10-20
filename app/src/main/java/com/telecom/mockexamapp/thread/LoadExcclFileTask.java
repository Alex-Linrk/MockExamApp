package com.telecom.mockexamapp.thread;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.telecom.mockexamapp.bean.MultipleChoice;
import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.SingleChoice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Workbook;
import rx.Observable;

/**
 * @author 林荣昆
 *         Created by Administrator on 2016/8/19.
 */

public class LoadExcclFileTask extends AsyncTask<String, String, String> {
    private String path;
    private Workbook workbook;
    private StringBuilder msg;
    private ProgressDialog progressDialog;
    private long signal_id = 1;
    private long mulite_id = 1;
    private long yesornot_id = 1;

    public LoadExcclFileTask(String filePath, ProgressDialog dialog) {
        progressDialog = dialog;
        this.path = filePath;
    }

    public LoadExcclFileTask(String filePath) {
        this.path = filePath;
    }

    @Override
    protected void onPreExecute() {
        File file = new File(path);
        msg = new StringBuilder();
        try {
            if (progressDialog != null) {
                progressDialog.show();
            }
            workbook = Workbook.getWorkbook(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if (workbook != null) {
            Observable
                    .from(workbook.getSheets()).filter(sheet -> sheet.getRows() > 0)
                    .map(sheet -> {
                        List<Cell[]> cellList = new ArrayList<>();
                        for (int i = 1; i < sheet.getRows(); i++) {
                            cellList.add(sheet.getRow(i));
                        }
                        return cellList;
                    }).
                    flatMap(Observable::from)
                    .filter(cells -> "单选".equals(cells[0].getContents()) || "多选".equals(cells[0].getContents()) || "判断".equals(cells[0].getContents()))
                    .map(cells -> {
                        Question question = null;
                        if ("单选".equals(cells[0].getContents())) {
                            question = new SingleChoice();
                            ((SingleChoice) question).setValue(cells);
                            question.setQuestion_id(signal_id);
                            signal_id++;
                        } else if ("多选".equals(cells[0].getContents())) {
                            question = new MultipleChoice();
                            ((MultipleChoice) question).setValue(cells);
                            question.setQuestion_id(mulite_id);
                            mulite_id++;
                        }
                        return question;
                    }).subscribe(Question::save);

        }
        if (workbook != null) {
            workbook.close();
        }
        return msg.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
