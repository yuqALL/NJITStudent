package com.njit.student.yuqzy.njitstudent.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */

public class LikeNews implements Serializable {
    List<NormalItem> list;

    public List<NormalItem> getList() {
        return list;
    }

    public void setList(List<NormalItem> list) {
        this.list = list;
    }
}
