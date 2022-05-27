package com.hp.gre3000.model;

import java.io.Serializable;

public class TodayLearnBean implements Serializable {
    private int ListNum;
    private boolean isLearn;

    public TodayLearnBean() {
    }

    public TodayLearnBean(int listNum, boolean isLearn) {
        ListNum = listNum;
        this.isLearn = isLearn;
    }

    public int getListNum() {
        return ListNum;
    }

    public void setListNum(int listNum) {
        ListNum = listNum;
    }

    public boolean isLearn() {
        return isLearn;
    }

    public void setLearn(boolean learn) {
        isLearn = learn;
    }
}
