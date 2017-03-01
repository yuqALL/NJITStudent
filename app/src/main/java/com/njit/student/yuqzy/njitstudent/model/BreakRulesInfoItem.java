package com.njit.student.yuqzy.njitstudent.model;

/**
 * Created by Administrator on 2017/2/25.
 */

//欠款信息
public class BreakRulesInfoItem {
    private String id_tiaoma;//条码号
    private String id_suoshu;//索书号
    private String name;//题名
    private String authors;//责任者
    private String readTime;//借阅日
    private String returnTime;//应还日
    private String place;//馆藏地
    private String should_pay;//应缴
    private String actual_pay;//实缴
    private String state;//状态

    public String getId_tiaoma() {
        return id_tiaoma;
    }

    public void setId_tiaoma(String id_tiaoma) {
        this.id_tiaoma = id_tiaoma;
    }

    public String getId_suoshu() {
        return id_suoshu;
    }

    public void setId_suoshu(String id_suoshu) {
        this.id_suoshu = id_suoshu;
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

    public String getShould_pay() {
        return should_pay;
    }

    public void setShould_pay(String should_pay) {
        this.should_pay = should_pay;
    }

    public String getActual_pay() {
        return actual_pay;
    }

    public void setActual_pay(String actual_pay) {
        this.actual_pay = actual_pay;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
