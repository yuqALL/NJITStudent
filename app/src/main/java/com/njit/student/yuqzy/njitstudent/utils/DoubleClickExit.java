package com.njit.student.yuqzy.njitstudent.utils;
public class DoubleClickExit {
    public static long mLastClick = 0L;
    private static final int THRESHOLD = 2000;
    private static final int THRESHOLD2 = 500;

    public static boolean check() {
        long now = System.currentTimeMillis();
        boolean b = now - mLastClick < THRESHOLD;
        mLastClick = now;
        return b;
    }

    public static boolean first_check() {
        long now = System.currentTimeMillis();
        boolean b = now - mLastClick < THRESHOLD2;
        mLastClick = now;
        return b;
    }
}
