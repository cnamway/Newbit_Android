package com.spark.newbitrade.utils;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.Country;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Administrator on 2017/9/1.
 * 通用工具类
 */

public class CommonUtils {
    /**
     * 将指定内容粘贴到剪贴板
     *
     * @param content 剪切内容
     */
    public static void copyText(Context context, String content) {
        if (StringUtils.isNotEmpty(content)) {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newRawUri("copyLable", Uri.parse(content));
            cm.setPrimaryClip(mClipData);
            ToastUtils.showToast(context, context.getString(R.string.copy_success));
        } else {
            ToastUtils.showToast(context, context.getString(R.string.str_copy_fail));
        }

    }

    /**
     * 获取版本号
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机序列号
     *
     * @return
     */
    public static String getSerialNumber() {
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        return serialnum;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 程序是否在前台运行
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取md5key
     */
    public static String getMd5Key(String username, String pwd) {
        String key = CommonUtils.getSerialNumber() + username + pwd;
        return getMD5(key);
    }

    /**
     * 转化md5
     *
     * @param info
     * @return
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }


    public static String getCountryNameByLanguageCode(Country country) {
        if (country == null)
            return "China";
        if (SharedPreferenceInstance.getInstance().getLanguageCode() == 1) {
            return country.getZhName();
        } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 2) {
            return country.getEnName();
        }
        return "China";
    }

    /**
     * 返回版本号
     *
     * @return
     */
    public static String getAppVersionNum() {
        String versionName = "";
        try {
            PackageManager pm = MyApplication.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(MyApplication.getApp().getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 安装
     *
     * @param apkPath
     */
    public static void installAPk(File apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri apkUri = FileUtils.getUriForFile(MyApplication.getApp(), apkPath);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        MyApplication.getApp().startActivity(intent);
    }

    /**
     * 获取交易对单位
     *
     * @param strSymbol
     * @return
     */
    public static String getUnitBySymbol(String strSymbol) {
        if (StringUtils.isNotEmpty(strSymbol)) {
            String splitSymbol = strSymbol.substring(strSymbol.indexOf("/") + 1, strSymbol.length());
            if (StringUtils.isNotEmpty(splitSymbol))
                return splitSymbol;
        }
        return GlobalConstant.CNY;
    }

    /**
     * 获取包名
     *
     * @return
     */
    public static String getAppPackageName() {
        ActivityManager activityManager = (ActivityManager) MyApplication.getApp().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getPackageName();
    }

    /**
     * 比较版本大小
     * <p>
     * 说明：支n位基础版本号+1位子版本号
     * 示例：1.0.2>1.0.1 , 1.0.1.1>1.0.1
     *
     * @param version1 版本1
     * @param version2 版本2
     * @return 0:相同 1:version1大于version2 -1:version1小于version2
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0; //版本相同
        }
        String[] v1Array = version1.split("\\.");
        String[] v2Array = version2.split("\\.");
        int v1Len = v1Array.length;
        int v2Len = v2Array.length;
        int baseLen = 0; //基础版本号位数（取长度小的）
        if (v1Len > v2Len) {
            baseLen = v2Len;
        } else {
            baseLen = v1Len;
        }

        for (int i = 0; i < baseLen; i++) { //基础版本号比较
            if (v1Array[i].equals(v2Array[i])) { //同位版本号相同
                continue; //比较下一位
            } else {
                return Integer.parseInt(v1Array[i]) > Integer.parseInt(v2Array[i]) ? 1 : -1;
            }
        }
        //基础版本相同，再比较子版本号
        if (v1Len != v2Len) {
            return v1Len > v2Len ? 1 : -1;
        } else {
            //基础版本相同，无子版本号
            return 0;
        }
    }


    /**
     * 检查是否有网络
     */
    public static boolean isNetConnect() {
        boolean isConnect = false;
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        NetworkInfo.State mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if ((wifiState != null && wifiState == NetworkInfo.State.CONNECTED) || (mobileState != null && mobileState == NetworkInfo.State.CONNECTED)) {
            isConnect = true;
        }
        return isConnect;
    }

    /**
     * 根据国家返回相应值
     *
     * @param country
     * @return
     */
    public static String getNameByCode(Country country) {
        if (SharedPreferenceInstance.getInstance().getLanguageCode() == 1) {
            return country.getZhName();
        } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 2) {
            return country.getEnName();
        }
        return country.getZhName();
    }

    public static int getColor(Context context, @ColorRes int resId) {
        return ContextCompat.getColor(context, resId);
    }

    public static float getDimension(Context context, @DimenRes int resId) {
        return context.getResources().getDimension(resId);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return MyApplication.getApp().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        return MyApplication.getApp().getResources().getDisplayMetrics().heightPixels;
    }

}
