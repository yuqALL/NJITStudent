package com.njit.student.yuqzy.njitstudent.model;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2017/2/25.
 */

public class HotRecommendItem extends RealmObject {
    private String name;//题名

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    private String bookUrl;
    private String author;//责任者
    private String publishInfo;//出版信息
    private String idSuoShu;//索书号
    private String num;//馆藏
    private String readTimes;//借阅次数
    private String read;//借阅比

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

    public String getPublishInfo() {
        return publishInfo;
    }

    public void setPublishInfo(String publishInfo) {
        this.publishInfo = publishInfo;
    }

    public String getIdSuoShu() {
        return idSuoShu;
    }

    public void setIdSuoShu(String idSuoShu) {
        this.idSuoShu = idSuoShu;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(String readTimes) {
        this.readTimes = readTimes;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
