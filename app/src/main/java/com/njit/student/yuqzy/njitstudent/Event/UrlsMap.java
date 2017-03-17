package com.njit.student.yuqzy.njitstudent.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/9.
 */

public class UrlsMap {
    public UrlsMap(Map<String, String> queryUrl) {
        this.queryUrl = queryUrl;
    }

    Map<String, String> queryUrl = new HashMap<>();
    String Xh;

    public String getXh() {
        return Xh;
    }

    public void setXh(String xh) {
        Xh = xh;
    }

    String scoreUrl;
    String personInfo;
    String courseForm;
    String personCourse;

    public String getCourseForm() {
        return queryUrl.get("班级课表查询");
    }

    public void setCourseForm(String courseForm) {
        queryUrl.put("班级课表查询", courseForm);
    }

    public String getPersonCourse() {
        return queryUrl.get("学生个人课表");
    }

    public void setPersonCourse(String personCourse) {
        queryUrl.put("学生个人课表", personCourse);
    }

    public String getPersonInfo() {
        return queryUrl.get("个人信息");
    }

    public void setPersonInfo(String personInfo) {
        queryUrl.put("个人信息", personInfo);
    }

    public Map<String, String> getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(Map<String, String> queryUrl) {
        this.queryUrl = queryUrl;
    }

    public String getScoreUrl() {
        return queryUrl.get("平时成绩查询");
    }

    public void setScoreUrl(String scoreUrl) {
        queryUrl.put("平时成绩查询", scoreUrl);
    }
}
