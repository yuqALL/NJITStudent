package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/15.
 */

public class NewsDetail extends RealmObject {

    RealmList<NewsDetailItem> value;
    String host;
    String title;
    String updatetime;

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public RealmList<NewsDetailItem> getValue() {
        return value;
    }

    public void setValue(RealmList<NewsDetailItem> value) {
        this.value = value;
    }
}
