package com.njit.student.yuqzy.njitstudent.utils;

import android.app.Dialog;
import android.content.Context;


import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Administrator on 2017/3/3.
 */

public class ShowLoadDialog {

    public static Dialog dialog = null;

    public static void show(Context context) {
        dialog = new MaterialDialog.Builder(context)
                .content("加载中...")
                .progress(true, 0)
                .show();
    }

    public static void dismiss() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
