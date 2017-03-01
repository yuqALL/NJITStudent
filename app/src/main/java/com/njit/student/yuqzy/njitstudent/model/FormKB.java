package com.njit.student.yuqzy.njitstudent.model;

/**
 * Created by Administrator on 2017/2/10.
 * 记录课表每个单元格所有参数
 */

public class FormKB {
    int colspan;
    int rowspan;
    int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    String text;

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

    public FormKB(int colspan, int rowspan, int length,String text) {
        this.colspan = colspan;
        this.rowspan = rowspan;
        this.text = text;
        this.length=length;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "FormKB{" +
                "colspan=" + colspan +
                ", rowspan=" + rowspan +
                ", text='" + text + '\'' +
                '}';
    }
}
