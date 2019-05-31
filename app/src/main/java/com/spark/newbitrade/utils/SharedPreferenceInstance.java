package com.spark.newbitrade.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.spark.newbitrade.MyApplication;


/**
 * Created by Administrator on 2018/1/17.
 */

public class SharedPreferenceInstance {
    private SharedPreferences mPreferences;
    private static SharedPreferenceInstance mInstance = null;
    public static final String SP_KEY_ISFIRSTUSE = "SYSTEM_KEY_ISFIRSTUSE";
    public static final String SP_KEY_LANGUAGE = "SP_KEY_LANGUAGE";
    public static final String SP_KEY_MONEY_SHOW_TYPE = "SP_KEY_MONEY_SHOW_TYPE";
    public static final String SP_KEY_MONEY_SHOW_UNIT = "SP_KEY_MONEY_SHOW_UNIT";
    private static final String SP_KEY_LOCK_PASS = "SP_KEY_LOCK_PASS";
    private static final String SP_KEY_IS_NEED_SHOW_LOCK = "SP_KEY_IS_NEED_SHOW_LOCK";
    private static final String SP_KEY_TOKEN = "SP_KEY_TOKEN";
    private static final String HAS_NEW_MESSAGE = "HAS_NEW_MESSAGE";
    private static final String VERSION_CODE = "VERSION_CODE";
    private static final String GOOGLESTATUS = "GOOGLESTATUS"; // 谷歌验证
    private static final String COOKIE_NAME = "LOGIN_COOKIE";
    private static final String URL_NAME = "LOGIN_COOKIE_URL";
    public static final String SP_KEY_TGT = "SP_KEY_TGT";
    public static final String SP_KEY_OTC_SID = "SP_KEY_OTC_SID";
    public static final String SP_KEY_LOGIN_ACCOUNT = "SP_KEY_LOGIN_ACCOUNT";//登录账号：手机号码

    private SharedPreferenceInstance() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getApp().getApplicationContext());
    }

    public synchronized static SharedPreferenceInstance getInstance() {
        return mInstance == null ? new SharedPreferenceInstance() : mInstance;
    }

    /**
     * 获取是否是第一次使用APP
     **/
    public boolean getIsFirstUse() {
        return mPreferences == null ? true : mPreferences.getBoolean(SP_KEY_ISFIRSTUSE, true);
    }

    /**
     * 保存是否是第一次使用APP
     */
    public void saveIsFirstUse(boolean isFirstUse) {
        if (mPreferences == null) return;
        mPreferences.edit().putBoolean(SP_KEY_ISFIRSTUSE, isFirstUse).apply();
    }

    /**
     * 保存语言偏好  // 1 中文  2 英文  3日文
     *
     * @param languageCode
     */
    public void saveLanguageCode(int languageCode) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_KEY_LANGUAGE, languageCode).apply();
    }

    /**
     * 获取语言偏好
     *
     * @return
     */
    public int getLanguageCode() {
        //if (mPreferences == null) return 1;
        return mPreferences.getInt(SP_KEY_LANGUAGE, 1);
    }


    /**
     * 保存手势密码
     */
    public synchronized void saveLockPwd(String encryPas) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(SP_KEY_LOCK_PASS, encryPas).apply();
    }

    /**
     * 获取语言偏好
     *
     * @return
     */
    public synchronized void saveGoogleSatus(int status) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(GOOGLESTATUS, status).apply();
    }

    /**
     * 获取手势密码
     */
    public synchronized String getLockPwd() {
        return mPreferences == null ? null : mPreferences.getString(SP_KEY_LOCK_PASS, null);
    }


    /**
     * 保存账户余额显示偏好 // 1 明文显示  2 密文显示
     */
    public void saveMoneyShowtype(int type) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_KEY_MONEY_SHOW_TYPE, type).apply();
    }

    /**
     * 获取账户余额显示偏好
     */
    public int getMoneyShowType() {
        return mPreferences == null ? 2 : mPreferences.getInt(SP_KEY_MONEY_SHOW_TYPE, 1);
    }

    /**
     * 保存账户余额显示偏好 // 1 CNY  2  JPY
     */
    public void saveMoneyShowUnit(int type) {
        if (mPreferences == null) return;
        mPreferences.edit().putInt(SP_KEY_MONEY_SHOW_UNIT, type).apply();
    }

    /**
     * 获取账户余额显示单位偏好
     */
    public int getMoneyShowUnitType() {
        return mPreferences == null ? 1 : mPreferences.getInt(SP_KEY_MONEY_SHOW_UNIT, 1);
    }

    /**
     * 保存再进入 是否需要显示手势锁
     */
    public void saveIsNeedShowLock(boolean b) {
        if (mPreferences == null) return;
        mPreferences.edit().putBoolean(SP_KEY_IS_NEED_SHOW_LOCK, b).apply();
    }

    /**
     * 获取再进入 是否需要显示手势锁
     */
    public boolean getIsNeedShowLock() {
        return mPreferences == null ? false : mPreferences.getBoolean(SP_KEY_IS_NEED_SHOW_LOCK, false);
    }

    /**
     * 保存验证token
     */
    public void saveToken(String tokenKey) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(SP_KEY_TOKEN, tokenKey).apply();
    }

    /**
     * 获取验证token
     */
    public String getToken() {
        return mPreferences == null ? "" : mPreferences.getString(SP_KEY_TOKEN, "");
    }

    /**
     * 保存新消息提示
     */
    public void saveHasNew(boolean b) {
        if (mPreferences == null) return;
        mPreferences.edit().putBoolean(HAS_NEW_MESSAGE, b).apply();
    }

    /**
     * 获取新消息提示
     */
    public boolean getHasNew() {
        return mPreferences == null ? false : mPreferences.getBoolean(HAS_NEW_MESSAGE, false);
    }

    /**
     * 保存版本号
     */
    public void saveVersion(String code) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(VERSION_CODE, code).apply();
    }

    /**
     * 获取版本号
     */
    public String getVersion() {
        return mPreferences == null ? "" : mPreferences.getString(VERSION_CODE, "V1.0.0");
    }

    /**
     * 获取版本号
     */
    public int getGooglestatus() {
        return mPreferences == null ? 0 : mPreferences.getInt(GOOGLESTATUS, 0);
    }

    /**
     * 保存验证cookie
     */
    public void saveCookie(String strCookie, String url) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(COOKIE_NAME, strCookie).apply();
        mPreferences.edit().putString(URL_NAME, url).apply();
    }

    /**
     * 获取验证cookie
     */
    public String getCookie() {
        return mPreferences == null ? "" : mPreferences.getString(COOKIE_NAME, "");
    }

    /**
     * 获取验证cookie对应的链接
     */
    public String getCookieUrl() {
        return mPreferences == null ? "" : mPreferences.getString(URL_NAME, "");
    }

//    public void saveSID(String simulate) {
//        if (mPreferences == null) return;
//        mPreferences.edit().putString("spotsid", simulate).apply();
//    }
//
//    public String getSID() {
//        return mPreferences == null ? "" : mPreferences.getString("spotsid", "");
//    }

    /**
     * 保存tgt
     *
     * @return
     */
    public void setTgt(String tgt) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(SP_KEY_TGT, tgt).apply();
    }

    /**
     * 获取tgt
     *
     * @return
     */
    public String getTgt() {
        return mPreferences == null ? "" : mPreferences.getString(SP_KEY_TGT, "");
    }

    public void setOtcSid(Context context, String otcSid) {
        if (mPreferences == null) return;
        mPreferences.edit().putString(SP_KEY_OTC_SID, otcSid).apply();
    }

    public String getOtcSid() {
        return mPreferences == null ? "" : mPreferences.getString(SP_KEY_OTC_SID, "");
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public void setParam(Context context, String key, Object object) {
        if (mPreferences == null) return;
        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = mPreferences.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    /**
     * string类型返回值
     *
     * @param context
     * @param key
     * @return
     */
    public String getStringParam(String key) {
        return mPreferences == null ? "" : mPreferences.getString(key, "");
    }

    /**
     * int类型返回值
     *
     * @param context
     * @param key
     * @return
     */
    public int getIntParam(String key) {
        if (StringUtils.isNotEmpty(key) && key.equals(SP_KEY_LANGUAGE))
            return mPreferences == null ? 0 : mPreferences.getInt(key, 0);
        return mPreferences == null ? 0 : mPreferences.getInt(key, 0);
    }

    /**
     * boolean类型返回值
     *
     * @param context
     * @param key
     * @return
     */
    public boolean getBooleanParam(String key) {
        return mPreferences == null ? false : mPreferences.getBoolean(key, false);
    }


}

