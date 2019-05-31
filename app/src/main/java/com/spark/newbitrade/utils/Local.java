package com.spark.newbitrade.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.spark.newbitrade.MyApplication;

import java.util.List;

import okhttp3.Cookie;

/**
 * 保存cookie
 */

public class Local {
    private static SharedPreferences shared;

    public static void setShared(SharedPreferences shared) {
        Local.shared = shared;
    }

    public static void saveStrCookie(String cookie) {
        if (shared == null)
            shared = PreferenceManager.getDefaultSharedPreferences(MyApplication.getApp().getApplicationContext());
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(GlobalConstant.COOKIE_NAME, cookie);
        editor.commit();
    }

    public static boolean hasCookie() {
        String _session_id = shared.getString(GlobalConstant.COOKIE_NAME, null);
        if (_session_id == null || _session_id.equals("")) {
            LogUtil.i("hasCookie return false");
            return false;
        }
        LogUtil.i("hasCookie return true");
        return true;
    }

    public static String getCookieName() {
        String _session_id = shared.getString(GlobalConstant.COOKIE_NAME, null);
        LogUtil.i("====>getCookieName =" + _session_id);
        return _session_id;
    }

    public static void saveCookie(List<Cookie> cookies) {
        String strCookie = "";
        for (Cookie cookie : cookies) {
            if (cookie != null && cookie.toString().contains("access-auth-token")) {
                strCookie = cookie.toString();
                LogUtil.i("====>saveCookie = " + strCookie);
                saveStrCookie(strCookie);
            }
        }
    }

    public static Cookie getCookie() {
        String cookie = Local.getCookieName();
        if (StringUtils.isNotEmpty(cookie)) {
        }
        return null;
    }
}
