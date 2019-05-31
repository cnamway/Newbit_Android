package com.spark.newbitrade.activity.mychart;

import android.content.Context;
import android.content.SharedPreferences;

import com.spark.newbitrade.MyApplication;

/**
 * Created by wuzongjie on 2018/10/18
 * SharedPreferences工具类封装
 */
public class SPManagerUtils {

    private static SharedPreferences sp = null;
    private static SPManagerUtils mInstance = null;
    /**
     * 默认的文件名
     */
    private static String mPreferencesName = "share_k_preferences";

    private SPManagerUtils() {
        sp = MyApplication.getApp().getSharedPreferences(mPreferencesName, Context.MODE_PRIVATE);
    }

   public static SPManagerUtils getInstance() {
        if (mInstance == null || sp == null) {
            synchronized (SPManagerUtils.class) {
                if (mInstance == null || sp == null) {
                    mInstance = new SPManagerUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置preferencesName
     *
     * @param preferencesName preferencesName
     */
    private void setPreferencesName(String preferencesName) {
        mPreferencesName = preferencesName;
    }

    /**
     * 写入String变量到sp中
     *
     * @param key   存储节点名称
     * @param value 存储节点的值
     */
    public void putString(String key, String value) {
        sp.edit().putString(key, value).apply();
    }

    /**
     * 读取String标示从sp中
     *
     * @param key 存储节点名称
     */
    public String getString(String key) {
        return sp.getString(key, null);
    }

}
