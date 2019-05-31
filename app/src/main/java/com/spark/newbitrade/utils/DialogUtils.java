package com.spark.newbitrade.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2017/12/6.
 */

public class DialogUtils {
    /**
     * 显示默认布局的dialog
     */
    public static void showDefaultDialog(Activity context, String title, String message, String cancleText, String sureText, DialogInterface.OnClickListener cancleListeners, DialogInterface.OnClickListener sureListeners) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!StringUtils.isEmpty(title)) builder.setTitle(title);
        if (!StringUtils.isEmpty(cancleText) || cancleListeners != null)
            builder.setNegativeButton(cancleText, cancleListeners);
        if (!StringUtils.isEmpty(sureText) || sureListeners != null)
            builder.setPositiveButton(sureText, sureListeners);
        builder.setMessage(message).create().show();
    }

}
