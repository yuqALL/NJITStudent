package com.njit.student.yuqzy.njitstudent.utils;


import com.njit.student.yuqzy.njitstudent.App;
import com.njit.student.yuqzy.njitstudent.MainActivity;

public class SettingsUtil {

    public static final String NJ="nj";//年级
    public static final String THEME = "theme_color";//主题
    public static final String CLEAR_CACHE = "clean_cache";//清空缓存
    public static final String VIEW_MAIN = "view_main";//主页
    public static final String PERSON_FAVOR = "person_favor";//个人头像
    public static final String NAV_BAC = "nav_bac";//个人头像
    public static final String CLEAR_INFO = "clear_info";//清理个人信息
    public static final String FAST_GET_ALL_INFO = "fast_get_all_info";//得到所有信息
    public static final String ADD_URLS = "add_url";//添加网址
    public static final String SET_COURSE_CURRENT_WEEK = "course_current_week";//设置当前周
    public static final String SET_COURSE_START_WEEK = "course_start_week";//设置周
    public static final String SET_COURSE_CURRENT_WEEK_BIND_TIME = "course_current_week_bind_time";//设置当前周绑定时间
    public static final String USER_XH = "user_xh";//添加用户学号
    public static final String USER_SCORE_TERM = "user_score_term";//添加用户查询成绩学期
    public static final String USER_COURSE_TERM = "user_course_term";//添加用户查询学期信息
    public static final String USER_COURSE_TYPE = "user_course_type";//添加用户查询学期类型
    public static final String USER_ZF_MM = "user_zf_mm";//添加用户正方系统密码
    public static final String USER_LIBRARY_MM = "user_library_mm";//添加用户图书馆系统密码

    public static final String COURSE_BAC_GROUND = "course_bac_ground";//课程表背景
    public static void setNj(String nj) {
        SPUtil.put(App.getContext(), NJ, nj);
    }

    public static String getNj() {
        return (String) SPUtil.get(App.getContext(), NJ, "");
    }


    public static void setTheme(int themeIndex) {
        SPUtil.put(App.getContext(), THEME, themeIndex);
    }

    public static int getTheme() {
        return (int) SPUtil.get(App.getContext(), THEME, 0);
    }


    public static String getViewMain() {
        return (String) SPUtil.get(App.getContext(), VIEW_MAIN, MainActivity.FRAGMENT_TAG_EDUNOTIFICATION);
    }

    public static void setViewMain(String name) {
        SPUtil.put(App.getContext(), VIEW_MAIN, name);
    }

    public static void setXueHao(String xuehao) {
        SPUtil.put(App.getContext(), USER_XH, xuehao);
    }

    public static String getXueHao() {
        return (String) SPUtil.get(App.getContext(), USER_XH, "");
    }

    public static void setZFMM(String xuehao) {
        SPUtil.put(App.getContext(), USER_ZF_MM, xuehao);
    }

    public static String getZFMM() {
        return (String) SPUtil.get(App.getContext(), USER_ZF_MM, "");
    }

    public static void setUserLibraryMm(String mima) {
        SPUtil.put(App.getContext(), USER_LIBRARY_MM, mima);
    }

    public static String getUserLibraryMm() {
        return (String) SPUtil.get(App.getContext(), USER_LIBRARY_MM, "");
    }

    public static String getPersonFavor() {
        return (String) SPUtil.get(App.getContext(), PERSON_FAVOR, "");
    }

    public static void setPersonFavor(String yearAterm) {
        SPUtil.put(App.getContext(), PERSON_FAVOR, yearAterm);
    }

    public static void setUserCourseTerm(String path) {
        SPUtil.put(App.getContext(), USER_COURSE_TERM, path);
    }


    public static String getUserCourseTerm() {
        return (String) SPUtil.get(App.getContext(), USER_COURSE_TERM, "");
    }

    public static void setUserScoreTerm(String yearAterm) {
        SPUtil.put(App.getContext(), USER_SCORE_TERM, yearAterm);
    }

    public static String getUserScoreTerm() {
        return (String) SPUtil.get(App.getContext(), USER_SCORE_TERM, "");
    }

    public static void setCurrentWeek(String week) {
        SPUtil.put(App.getContext(), SET_COURSE_CURRENT_WEEK, week);
    }

    public static String getCurrentWeek() {
        return (String) SPUtil.get(App.getContext(), SET_COURSE_CURRENT_WEEK, "1");
    }

    public static void setCourseStartWeek(String week) {
        SPUtil.put(App.getContext(), SET_COURSE_START_WEEK, week);
    }

    public static String getCourseStartWeek() {
        return (String) SPUtil.get(App.getContext(), SET_COURSE_START_WEEK, "1");
    }

    public static void setCourseCurrentWeekBindTime(String time) {//2017-2-28
        SPUtil.put(App.getContext(), SET_COURSE_CURRENT_WEEK_BIND_TIME, time);
    }

    public static String getCourseCurrentWeekBindTime() {
        return (String) SPUtil.get(App.getContext(), SET_COURSE_CURRENT_WEEK_BIND_TIME, "");
    }

    public static void setUserCourseType(String type) {
        SPUtil.put(App.getContext(), USER_COURSE_TYPE, type);
    }

    public static String getUserCourseType() {
        return (String) SPUtil.get(App.getContext(), USER_COURSE_TYPE, "class");
    }

    public static void setCourseBacGround(String path) {
        SPUtil.put(App.getContext(), COURSE_BAC_GROUND, path);
    }

    public static String getCourseBacGround() {
        return (String) SPUtil.get(App.getContext(), COURSE_BAC_GROUND, "");
    }


    public static void setNavBac(String path) {
        SPUtil.put(App.getContext(), NAV_BAC, path);
    }

    public static String getNavBac() {
        return (String) SPUtil.get(App.getContext(), NAV_BAC, "");
    }
}
