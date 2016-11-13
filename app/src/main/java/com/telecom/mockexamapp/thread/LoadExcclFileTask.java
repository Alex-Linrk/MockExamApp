package com.telecom.mockexamapp.thread;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.telecom.mockexamapp.bean.MultipleChoice;
import com.telecom.mockexamapp.bean.Question;
import com.telecom.mockexamapp.bean.SingleChoice;
import com.telecom.mockexamapp.bean.YesOrNoQuestion;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;

/**
 * @author 林荣昆
 * Created by Administrator on 2016/8/19.
 */

public class LoadExcclFileTask extends AsyncTask<String,String,String> {
    private String path;
    private Workbook workbook;
    private StringBuilder msg;
    private ProgressDialog progressDialog;
    public LoadExcclFileTask(String filePath, ProgressDialog dialog) {
        progressDialog = dialog;
        this.path = filePath;
    }
    public LoadExcclFileTask(String filePath){
        this.path = filePath;
    }

    @Override
    protected void onPreExecute() {
        File file = new File(path);
        msg = new StringBuilder();
        try {
            if(progressDialog != null){
                progressDialog.show();
            }
            workbook = Workbook.getWorkbook(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if(workbook != null){
            msg.append("getNumberOfSheets"+workbook.getNumberOfSheets()+"\n");
            for (String sheetName:
                 workbook.getSheetNames()) {
                msg.append("sheetName:"+sheetName+"\n");
            }
            for (String rang:
                 workbook.getRangeNames()) {
                msg.append("rang:"+rang+"\n");
            }
            for (Sheet sheet:
                 workbook.getSheets()) {
                msg.append("sheet:"+sheet.getName()+"\n");
                msg.append("sheetColumns:"+sheet.getColumns()+"\n");
                msg.append("sheetRows:"+sheet.getRows()+"\n");
            }
            for (Sheet sheet:workbook.getSheets()) {
                long signal_id = 1;
                long mulite_id = 1;
                long yesornot_id = 1;
                for (int row = 1;row<sheet.getRows();row++){
                    String questionType = sheet.getCell(0,row).getContents();
                    Question question = null;
                    if(questionType.equals("单选")){
                        question = new SingleChoice(sheet,row);
                        ((SingleChoice)question).setQuestion_id(signal_id);
                        question.save();
                        signal_id++;
                    }else if(questionType.equals("多选")){
                        question = new MultipleChoice(sheet,row);
                        ((MultipleChoice)question).setQuestion_id(mulite_id);
                        question.save();
                        mulite_id++;
                    }else{
                        question = new YesOrNoQuestion();
                        ((YesOrNoQuestion)question).setQuestion_id(yesornot_id);
                        question.save();
                        yesornot_id++;
                    }

                }
            }

        }
        if(workbook != null)
        workbook.close();
        return msg.toString();
    }

    @Override
    protected void onPostExecute(String s) {
      if(progressDialog != null && progressDialog.isShowing()){
          progressDialog.dismiss();
          progressDialog = null;
      }
    }
}
