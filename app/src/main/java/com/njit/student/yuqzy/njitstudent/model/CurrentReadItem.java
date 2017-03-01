package com.njit.student.yuqzy.njitstudent.model;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class CurrentReadItem extends RealmObject{
    private String id;//条码号
    private String name;//书名
    private String bookUrl;

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    private String getTime;//借阅日期
    private String returnTime;//应还时间
    private String continueTimes;//续借量
    private String place;//馆藏地
    private String otherThing;//附件
    private String getMoreTime;//续借

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getContinueTimes() {
        return continueTimes;
    }

    public void setContinueTimes(String continueTimes) {
        this.continueTimes = continueTimes;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOtherThing() {
        return otherThing;
    }

    public void setOtherThing(String otherThing) {
        this.otherThing = otherThing;
    }

    public String getGetMoreTime() {
        return getMoreTime;
    }

    public void setGetMoreTime(String getMoreTime) {
        this.getMoreTime = getMoreTime;
    }
}
