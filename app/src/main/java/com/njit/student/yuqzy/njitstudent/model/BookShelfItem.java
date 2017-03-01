package com.njit.student.yuqzy.njitstudent.model;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class BookShelfItem extends RealmObject{
    private String name;//题名
    private String authors;//责任者
    private String publishCompany;//出版社
    private String publishTime;//出版日期
    private String idSuoShu;//索书号
    private String bookUrl;//图书地址

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublishCompany() {
        return publishCompany;
    }

    public void setPublishCompany(String publishCompany) {
        this.publishCompany = publishCompany;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getIdSuoShu() {
        return idSuoShu;
    }

    public void setIdSuoShu(String idSuoShu) {
        this.idSuoShu = idSuoShu;
    }
}
