package com.njit.student.yuqzy.njitstudent.Event;

/**
 * Created by Administrator on 2017/2/6.
 */

public class LibraryResponseCode {
    public static final int LOGIN_OK=1;//登录成功
    public static final int LOGIN_ERROR=2;//用户名或密码空

    private int code=-1;

    public LibraryResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
