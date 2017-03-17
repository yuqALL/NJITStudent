package com.njit.student.yuqzy.njitstudent.database;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/14.
 */

public class NewsDetailItem extends RealmObject {

    int type = 0;

    //type=1 : 文本
    // type=2 : 图片
    // type=3 : 链接
    //type=4 : 标题
    //type=5 : 信息

    String value = "";
    String link = "";

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "NewsDetailItem{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
