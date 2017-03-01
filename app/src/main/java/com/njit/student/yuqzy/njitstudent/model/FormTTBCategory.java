package com.njit.student.yuqzy.njitstudent.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */

public class FormTTBCategory implements Serializable {
    List<FormTTBinfo> ttbList=null;

    public List<FormTTBinfo> getTtbList() {
        return ttbList;
    }

    public void setTtbList(List<FormTTBinfo> ttbList) {
        this.ttbList = ttbList;
    }
}
