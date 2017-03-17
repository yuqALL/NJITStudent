package com.njit.student.yuqzy.njitstudent.model;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class BookShelfBean extends RealmObject {
    private int type;//0 ->书架    1->书目
    private String title;//书架名
    private BookShelfItem item;//书目信息

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookShelfItem getItem() {
        return item;
    }

    public void setItem(BookShelfItem item) {
        this.item = item;
    }
}
