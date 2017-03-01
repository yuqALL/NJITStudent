package com.njit.student.yuqzy.njitstudent.model;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

//预约
public class OrderBookItem extends RealmObject{
    private String id;//索书号
    private String name;//题名/责任者
    private String bookUrl;//书籍信息地址
    private String place;//馆藏地
    private String orderTime;//预约日期
    private String abortTime;//截止日期
    private String getBookPlace;//取书地
    private String state;//状态

    public String getGetBookPlace() {
        return getBookPlace;
    }

    public void setGetBookPlace(String getBookPlace) {
        this.getBookPlace = getBookPlace;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

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

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getAbortTime() {
        return abortTime;
    }

    public void setAbortTime(String abortTime) {
        this.abortTime = abortTime;
    }
}
