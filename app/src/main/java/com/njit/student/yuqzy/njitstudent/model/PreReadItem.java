package com.njit.student.yuqzy.njitstudent.model;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class PreReadItem extends RealmObject{
    private String id;//条码号
    private String bookUrl;

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    private String name;//书名
    private String author;//责任者
    private String readTime;//借阅日期
    private String returnTime;//归还日期
    private String place;//馆藏地

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


}
