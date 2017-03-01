package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/9.
 */

public class Url  extends RealmObject {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
