package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/10.
 */

public class formKBdatabase extends RealmObject{
    private int colspan;
    private int rowspan;
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private String text;

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
