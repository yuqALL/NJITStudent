package com.njit.student.yuqzy.njitstudent.Event;

import org.apache.http.client.CookieStore;

/**
 * Created by Administrator on 2017/2/6.
 */

public class LoginResponseCode {
    public static final int LOGIN_OK = 1;//登录成功
    public static final int LOGIN_VERFATION_ERROR = 2;//验证码错误
    public static final int LOGIN_USERNAME_ERROR = 3;//用户名空
    public static final int LOGIN_PASSWORD_ERROR = 4;//密码空
    public static final int LOGIN_USERNAME_OR_PASSWORD_ERROR = 5;//用户名或密码错误
    public static final int REALM_SCORE_STO_OK = 6;//成绩保存realm成功
    public static final int REALM_PERSON_STO_OK = 7;//个人信息保存realm成功
    public static final int REALM_COURSE_STO_OK = 8;//班级课表保存realm成功
    private int code = -1;

    public LoginResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
