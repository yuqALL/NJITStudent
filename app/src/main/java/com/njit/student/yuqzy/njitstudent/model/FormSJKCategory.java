package com.njit.student.yuqzy.njitstudent.model;

import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */

public class FormSJKCategory implements Serializable {
    List<FormSJK> sjkList=null;

    public List<FormSJK> getSjkList() {
        return sjkList;
    }

    public void setSjkList(List<FormSJK> sjkList) {
        this.sjkList = sjkList;
    }
}
