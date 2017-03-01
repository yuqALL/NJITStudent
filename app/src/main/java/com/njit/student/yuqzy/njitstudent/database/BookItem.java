package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/28.
 */

public class BookItem extends RealmObject {
    String item;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
