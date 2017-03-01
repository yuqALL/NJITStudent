package com.njit.student.yuqzy.njitstudent.Event;

/**
 * Created by Administrator on 2017/2/6.
 */

public class InfoDoorResponseCode {
    public static final int LOGIN_OK=1;//登录成功
    public static final int LOGIN_PASSWORD_OR_PASSWORD_NULL=2;//用户名或密码空
    public static final int LOGIN_USERNAME_OR_PASSWORD_ERROR=3;//用户名或密码错误

    private int code=-1;

    public InfoDoorResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
