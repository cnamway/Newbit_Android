package com.spark.newbitrade.data;

/**
 * Created by  on 2017/10/16 0016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * 另外需要权限 <uses-permission
 * android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
public class NetworkUtil {

    /**
     * 检查网络连接状态
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        boolean isNetStatus = false;
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
                if (wifiNetworkInfo.isAvailable() || dataNetworkInfo.isAvailable()) {
                    isNetStatus = true;
                }
            } else {
                isNetStatus = false;
            }
        } else {//API大于23时使用下面的方式进行网络监听
            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connectivityManager.getAllNetworks();
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networks[i]);
                if (networkInfo != null) {
                    String typeName = networkInfo.getTypeName();
                    if (typeName.equalsIgnoreCase("WIFI")) {
                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                            isNetStatus = true;
                            return isNetStatus;
                        }
                    } else if (typeName.equalsIgnoreCase("MOBILE")) {
                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                            isNetStatus = true;
                            return isNetStatus;
                        }
                    } else {
                        isNetStatus = false;
                    }
                }
            }
        }
        return isNetStatus;
    }
}
