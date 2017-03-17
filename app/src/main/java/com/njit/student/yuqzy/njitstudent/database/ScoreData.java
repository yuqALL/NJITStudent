package com.njit.student.yuqzy.njitstudent.database;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * 每门功课成绩等数据
 */

public class ScoreData extends RealmObject {
    private String schoolYear = "";//学年
    private String schoolTerm = "";//学期
    private String courseCode = "";//课程代码
    private String courseName = "";//课程名称
    private String courseNature = "";//课程性质
    private String courseBelong = "";//课程归属
    private String credit = "";//学分
    private String usualScore = "";//平时成绩
    private String midtermScore = "";//期中成绩
    private String finalScore = "";//期末成绩
    private String experimentScore = "";//实验成绩
    private String score = "";//成绩
    private String make_upScore = "";//补考成绩
    private String rebuild = "";//是否重修
    private String collegeBelong = "";//开课学院
    private String comment = "";//备注
    private String make_upComment = "";//补考备注

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public String getSchoolTerm() {
        return schoolTerm;
    }

    public void setSchoolTerm(String schoolTerm) {
        this.schoolTerm = schoolTerm;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseNature() {
        return courseNature;
    }

    public void setCourseNature(String courseNature) {
        this.courseNature = courseNature;
    }

    public String getCourseBelong() {
        return courseBelong;
    }

    public void setCourseBelong(String courseBelong) {
        this.courseBelong = courseBelong;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(String usualScore) {
        this.usualScore = usualScore;
    }

    public String getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(String midtermScore) {
        this.midtermScore = midtermScore;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getExperimentScore() {
        return experimentScore;
    }

    public void setExperimentScore(String experimentScore) {
        this.experimentScore = experimentScore;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMake_upScore() {
        return make_upScore;
    }

    public void setMake_upScore(String make_upScore) {
        this.make_upScore = make_upScore;
    }

    public String getRebuild() {
        return rebuild;
    }

    public void setRebuild(String rebuild) {
        this.rebuild = rebuild;
    }

    public String getCollegeBelong() {
        return collegeBelong;
    }

    public void setCollegeBelong(String collegeBelong) {
        this.collegeBelong = collegeBelong;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMake_upComment() {
        return make_upComment;
    }

    public void setMake_upComment(String make_upComment) {
        this.make_upComment = make_upComment;
    }


}
