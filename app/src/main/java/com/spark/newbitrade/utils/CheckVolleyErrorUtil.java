package com.spark.newbitrade.utils;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.LoadExceptionEvent;
import com.spark.newbitrade.event.CheckLoginEvent;

import org.greenrobot.eventbus.EventBus;

import static com.spark.newbitrade.factory.HttpUrls.TYPE_AC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_UC;


/**
 * Created by wonderful on 2017/5/23.
 * 网络请求错误
 */

public class CheckVolleyErrorUtil {

    /**
     * VolleyError
     *
     * @param error
     */
    public static void checkError(VolleyError error) {
        if (error != null) {
            Context context = MyApplication.getApp();
            if (error instanceof TimeoutError) {
                Toast.makeText(context, context.getString(R.string.str_connection_out), Toast.LENGTH_SHORT).show();
                return;
            }
            if (error instanceof ServerError) {
                Toast.makeText(context, context.getString(R.string.str_server_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if (error instanceof NetworkError) {
                Toast.makeText(context, context.getString(R.string.str_please_check_net), Toast.LENGTH_SHORT).show();
                return;
            }
            if (error instanceof ParseError) {
                Toast.makeText(context, context.getString(R.string.str_json_error), Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNotEmpty(error.getMessage()) && error.getMessage().contains("BEGIN_")) {
                if (error.getMessage().contains(TYPE_OTC)) {
                    EventBus.getDefault().post(new CheckLoginEvent(TYPE_OTC));
                } else if (error.getMessage().contains(TYPE_UC)) {
                    EventBus.getDefault().post(new CheckLoginEvent(TYPE_UC));
                } else if (error.getMessage().contains(TYPE_AC)) {
                    EventBus.getDefault().post(new CheckLoginEvent(TYPE_AC));
                } else {
                    LogUtils.e("CheckVolleyErrorUtil.checkError(VolleyError error)===error.getMessage()==" + error.getMessage() + ",new LoadExceptionEvent()==退出登录=======");
                    EventBus.getDefault().post(new LoadExceptionEvent());
                }
                return;
            }
            Toast.makeText(MyApplication.getApp(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
