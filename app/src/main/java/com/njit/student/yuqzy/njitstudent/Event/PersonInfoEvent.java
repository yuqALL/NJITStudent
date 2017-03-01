package com.njit.student.yuqzy.njitstudent.Event;

import com.njit.student.yuqzy.njitstudent.database.PersonInfo;

/**
 * Created by Administrator on 2017/2/9.
 */

public class PersonInfoEvent {
    private PersonInfo info;

    public PersonInfoEvent() {
    }

    public PersonInfo getInfo() {
        return info;
    }

    public void setInfo(PersonInfo info) {
        this.info = info;
    }
}
