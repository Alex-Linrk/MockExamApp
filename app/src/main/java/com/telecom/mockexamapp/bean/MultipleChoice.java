package com.telecom.mockexamapp.bean;

import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;

/**
 * @author Administrator
 * Created by Administrator on 2016/8/19.
 */

public class MultipleChoice extends Question {
    private String questionContent;
    private String selectionA;
    private String selectionB;
    private String selectionC;
    private String selectionD;
    private String selectionE;
    private String selectionF;
    private String rightSelection;
    private String userAnswer;
    private double count;
    private double right;
    public MultipleChoice(){

    }
    public MultipleChoice(Sheet sheet,int row) {
        for (int column = 1;column < sheet.getColumns();column ++){
            Cell contentCell = sheet.getCell(column,row);
            String content = contentCell.getContents().replace(" ","");
            content = content.replace("\n","");
            setValue(content,column);
        }
    }
    public void setValue(Cell[] cells){
        for (int column = 1;column < cells.length;column ++){
            Cell contentCell = cells[column];
            String content = contentCell.getContents().replace(" ","");
            content = content.replace("\n","");
            setValue(content,column);
        }
    }
    private void  setValue(String value,int location){
        switch (location){
            case 1:
                questionContent = value;
                break;
            case 2:
                selectionA = value;
                break;
            case 3:
                selectionB = value;
                break;
            case 4:
                selectionC = value;
                break;
            case 5:
                selectionD = value;
                break;
            case 6:
                selectionE = value;
                break;
            case 7:
                selectionF = value;
                break;
            case 8:
                rightSelection = value.toUpperCase(Locale.CHINA).replace(" ","");
                break;
        }
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getRight() {
        return right;
    }

    public void setRight(double right) {
        this.right = right;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getSelectionA() {
        return selectionA;
    }

    public void setSelectionA(String selectionA) {
        this.selectionA = selectionA;
    }

    public String getSelectionB() {
        return selectionB;
    }

    public void setSelectionB(String selectionB) {
        this.selectionB = selectionB;
    }

    public String getSelectionC() {
        return selectionC;
    }

    public void setSelectionC(String selectionC) {
        this.selectionC = selectionC;
    }

    public String getSelectionD() {
        return selectionD;
    }

    public void setSelectionD(String selectionD) {
        this.selectionD = selectionD;
    }

    public String getSelectionE() {
        return selectionE;
    }

    public void setSelectionE(String selectionE) {
        this.selectionE = selectionE;
    }

    public String getSelectionF() {
        return selectionF;
    }

    public void setSelectionF(String selectionF) {
        this.selectionF = selectionF;
    }

    public String getRightSelection() {
        return rightSelection;
    }

    public void setRightSelection(String rightSelection) {
        this.rightSelection = rightSelection;
    }
}
