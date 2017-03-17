package com.njit.student.yuqzy.njitstudent.model;


import java.io.Serializable;

public class BookItem implements Serializable {
    private String name;//题名
    private String author;//作者
    private String id_suoshu;//索书号
    private String bookType;//书目类型
    private String publishCompany;//出版社
    private String publishTime;//年卷期
    private String numTotal;//馆藏复本
    private String numCanBorrow;//可借复本
    private String bookUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_suoshu() {
        return id_suoshu;
    }

    public void setId_suoshu(String id_suoshu) {
        this.id_suoshu = id_suoshu;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getNumTotal() {
        return numTotal;
    }

    public void setNumTotal(String numTotal) {
        this.numTotal = numTotal;
    }

    public String getNumCanBorrow() {
        return numCanBorrow;
    }

    public void setNumCanBorrow(String numCanBorrow) {
        this.numCanBorrow = numCanBorrow;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }
}
