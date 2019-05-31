package com.spark.newbitrade;

import android.app.Activity;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.activity.main.MainActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.utils.FileUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.okhttp.store.PersistentCookieStore;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by pc on 2017/3/8.
 */
public class MyApplication extends MultiDexApplication {
    private boolean isReleased = false; // 是否发布了
    public static MyApplication app;
    private User currentUser = new User();
    private TextView tvToast; // Toast
    private int mWidth; // 当前手机屏幕的宽高
    private int mHeight;
    private Activity app_activity = null;
    private PersistentCookieStore persistentCookieStore;
    private CookieManager cookieManager;

    @Override
    public void onCreate() {
        super.onCreate();
        persistentCookieStore = new PersistentCookieStore(this);
        cookieManager = new CookieManager(persistentCookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        app = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initGlobeActivity();
                initView();
                getDisplayMetric();
                getCurrentUserFromFile();
                LitePal.initialize(MyApplication.getApp());
                ZXingLibrary.initDisplayOpinion(MyApplication.getApp());
                x.Ext.init(MyApplication.getApp());
            }
        }).start();
        closeAndroidPDialog();
    }

    private void initView() {
        tvToast = (TextView) View.inflate(app, R.layout.my_toast, null);
    }

    public boolean isLogin() {
        if (getCurrentUser() == null) return false;
        return getCurrentUser().isLogin();
    }

    /**
     * 获取屏幕的宽高
     */
    private void getDisplayMetric() {
        mWidth = getResources().getDisplayMetrics().widthPixels;
        mHeight = getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 获取程序的Application对象
     */
    public static MyApplication getApp() {
        return app;
    }

    /**
     * 重新登录
     */
    public void loginAgain(BaseActivity activity) {
        deleteCurrentUser();
        if (getCurrentActivity().getClass().toString().equals(LoginActivity.class.toString())) {
            return;
        }
        if (StringUtils.isNotEmpty(activity.getClass().toString()) && activity.getClass().toString().equals(MainActivity.class.toString())) {
            return;
        }
        activity.finish();
    }

    /**
     * 重新登录
     */
    public void loginAgain(Fragment fragment) {
        Activity activity = fragment.getActivity();
        deleteCurrentUser();
        if (getCurrentActivity().getClass().toString().equals(LoginActivity.class.toString())) {
            return;
        }
        if (StringUtils.isNotEmpty(activity.getClass().toString()) && activity.getClass().toString().equals(MainActivity.class.toString())) {
            return;
        }
        activity.finish();
    }


    public synchronized void saveCurrentUser() {
        try {
            File file = FileUtils.getLongSaveFile(this, "User", GlobalConstant.UserSaveFileName);
            if (file.exists()) {
                file.delete();
            }
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(currentUser);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地存储的文件
     */
    public void deleteCurrentUser() {
        this.currentUser = null;
        File file = FileUtils.getLongSaveFile(this, "User", GlobalConstant.UserSaveFileName);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public void getCurrentUserFromFile() {
        try {
            File file = new File(FileUtils.getLongSaveDir(this, "User"), GlobalConstant.UserSaveFileName);
            if (file != null && file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                this.currentUser = (User) ois.readObject();
                if (this.currentUser == null) {
                    this.currentUser = new User();
                }
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int getmWidth() {
        return mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public User getCurrentUser() {
        return currentUser == null ? currentUser = new User() : currentUser;
    }

    public synchronized void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        saveCurrentUser();
    }

    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                app_activity = activity;
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                app_activity = activity;
            }

            /** Unused implementation **/
            @Override
            public void onActivityStarted(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                app_activity = activity;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    /**
     * 公开方法，外部可通过 MyApplication.getInstance().getCurrentActivity() 获取到当前最上层的activity
     */
    public Activity getCurrentActivity() {
        return app_activity;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    /**
     * 解决在Android P上的提醒弹窗 'Detected problems with API'
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
