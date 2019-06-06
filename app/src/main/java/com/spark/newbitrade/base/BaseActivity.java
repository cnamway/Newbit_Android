package com.spark.newbitrade.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gyf.barlibrary.ImmersionBar;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.lock.LockActivity;
import com.spark.newbitrade.activity.mychart.LoadDialog;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.LoadExceptionEvent;
import com.spark.newbitrade.event.CheckLoginEvent;
import com.spark.newbitrade.event.EmptyEvent;
import com.spark.newbitrade.event.LoginoutWithoutApiEvent;
import com.spark.newbitrade.utils.CheckVolleyErrorUtil;
import com.spark.newbitrade.utils.CommonUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.KeyboardUtils;
import com.spark.newbitrade.utils.LanguageUtil;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.spark.newbitrade.factory.HttpUrls.TYPE_AC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC_SYSTEM;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_UC;

public abstract class BaseActivity extends AppCompatActivity implements BaseContract.BaseView {
    protected ImageView ivBack;
    protected TextView tvTitle;
    protected TextView tvGoto;
    protected LinearLayout llTitle;
    protected Activity activity;
    protected LoadDialog loadDialog;
    protected View notLoginView;
    private Unbinder unbinder;
    protected ImmersionBar immersionBar;
    protected boolean isSetTitle = false;
    private ViewGroup emptyView;
    boolean lockOpen = false;
    protected boolean isNeedChecke = true;// 解锁界面 是不需要判断的 用此变量控制
    protected boolean isNeedhide = true;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLanguage();
        setContentView(getActivityLayoutId());
        unbinder = ButterKnife.bind(this);
        activity = this;
        initBaseView();
        if (isImmersionBarEnabled()) initImmersionBar();
        initView();
        initData();
        ActivityManage.addActivity(this);
        emptyView = getEmptyView();
        setListener();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });
    }

    /**
     * 设置基础化控件
     */
    private void initBaseView() {
        loadDialog = new LoadDialog(activity);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        llTitle = findViewById(R.id.llTitle);
        tvGoto = findViewById(R.id.tvGoto);
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    protected void setTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    private void setShowBackBtn(boolean showBackBtn) {
        if (ivBack != null && showBackBtn) {
            if (showBackBtn) {
                ivBack.setVisibility(View.VISIBLE);
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
            } else {
                ivBack.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化所有的控件
     */
    protected void initView() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 各控件的点击事件
     *
     * @param v
     */
    protected void setOnClickListener(View v) {

    }


    /**
     * 各控件的点击
     */
    protected void setListener() {

    }

    /**
     * 跳转activity,不关闭当前界面
     *
     * @param cls
     * @param bundle
     */
    protected void showActivity(Class<?> cls, Bundle bundle) {
        showActivity(cls, bundle, -1);
    }

    /**
     * 跳转activity,不关闭当前界面，含跳转回来的的回调
     *
     * @param cls
     * @param bundle
     */
    public void showActivity(Class<?> cls, Bundle bundle, int requesCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        if (requesCode >= 0)
            startActivityForResult(intent, requesCode);
        else
            startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isNeedShowLockActivity()) LockActivity.actionStart(this);
    }

    /**
     * 手势解锁
     *
     * @return
     */
    protected boolean isNeedShowLockActivity() {
        boolean isLogin = MyApplication.getApp().isLogin();//登录否？
        lockOpen = !StringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd());//开启否？
        boolean b = SharedPreferenceInstance.getInstance().getIsNeedShowLock();//是否处于后台过？
        return isLogin && isNeedChecke && lockOpen && b;
    }

    /**
     * 初始化语言设置
     */
    private void initLanguage() {
        LogUtils.i("initLanguage");
        Locale l = null;
        int code = SharedPreferenceInstance.getInstance().getLanguageCode();
        if (code == 1) {
            l = Locale.CHINESE;
        } else if (code == 2) {
            l = Locale.ENGLISH;
        } else if (code == 3) {
            l = Locale.JAPANESE;
        }
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.setLocale(l);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(l);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            getApplicationContext().createConfigurationContext(config);
        }
        Locale.setDefault(l);
        resources.updateConfiguration(config, dm);
    }

    public void setSetTitleAndBack(boolean setTitle, boolean isShowBackBtn) {
        setShowBackBtn(isShowBackBtn);
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this);
            immersionBar.statusBarDarkFont(true, 0.2f);
            immersionBar.statusBarColorTransform(R.color.black);
            immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
        }
        if (!setTitle) {
            isSetTitle = true;
            immersionBar.setTitleBar(this, llTitle);
        }
    }

    /**
     * 子类重写实现扩展设置
     */
    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true, 0.2f);
        immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
    }

    /**
     * 获取布局ID
     */
    protected abstract int getActivityLayoutId();

    /**
     * 获取空布局的父布局
     */
    protected ViewGroup getEmptyView() {
        return null;
    }

    /**
     * 是否启用沉浸式
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始数据加载
     */
    protected void loadData() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        lockOpen = !StringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd());
        if (lockOpen) {
            if (!CommonUtils.isAppOnForeground(getApplicationContext()))
                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(true);
            else
                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        ActivityManage.removeActivity(this);
        hideLoadingPopup();
        if (immersionBar != null) immersionBar.destroy();
    }

    /**
     * 显示加载框
     */
    public void displayLoadingPopup() {
        if (!isLiving(activity)) {
            return;
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (loadDialog != null && !loadDialog.isShowing())
                    loadDialog.show();
            }
        });
    }

    /**
     * 隐藏加载框
     */
    public void hideLoadingPopup() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (loadDialog != null && loadDialog.isShowing())
                    loadDialog.dismiss();
            }
        });
    }


    /**
     * 隐藏去登录的布局
     */
    public void hideToLoginView() {
        if (emptyView == null) return;
        emptyView.removeAllViews();
        emptyView.setVisibility(View.GONE);
    }

    /**
     * 获取用户token
     */
//    public String getToken() {
//        return MyApplication.getApp().getCurrentUser().getToken();
//    }
    public long getId() {
        return MyApplication.getApp().getCurrentUser().getId();
    }

    /**
     * 处理软件盘智能弹出和隐藏
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                if (isNeedhide) {
                    KeyboardUtils.editKeyboard(ev, view, this);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = newBase.getSharedPreferences("language", Context.MODE_PRIVATE);
        String selectedLanguage = preferences.getString("language", "");
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, selectedLanguage));
    }

    @Override
    public void showLoading() {
        if (!isLiving(activity)) {
            return;
        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (loadDialog != null && !loadDialog.isShowing())
                    loadDialog.show();
            }
        });
    }

    @Override
    public void hideLoading() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (loadDialog != null && loadDialog.isShowing())
                    loadDialog.dismiss();
            }
        });
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            if (httpErrorEntity.getCode() == GlobalConstant.LOGIN_ERROR) {
                if (StringUtils.isNotEmpty(httpErrorEntity.getUrl())) {
                    LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",httpErrorEntity.getUrl()==" + httpErrorEntity.getUrl());
                    if (httpErrorEntity.getUrl().contains(TYPE_OTC_SYSTEM)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_OTC_SYSTEM));
                    } else if (httpErrorEntity.getUrl().contains(TYPE_OTC)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_OTC));
                    } else if (httpErrorEntity.getUrl().contains(TYPE_UC)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_UC));
                    } else if (httpErrorEntity.getUrl().contains(TYPE_AC)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_AC));
                    } else {
                        LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",new LoadExceptionEvent()==退出登录=======");
                        EventBus.getDefault().post(new LoadExceptionEvent());
                    }
                }
            } else if (StringUtils.isNotEmpty(httpErrorEntity.getMessage())) {
                Message message = new Message();
                message.what = 1;
                message.obj = httpErrorEntity.getMessage();
                mToastHandler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = 1;
                message.obj = "" + httpErrorEntity.getCode();
                mToastHandler.sendMessage(message);
            }
        } else {
            EventBus.getDefault().post(new LoginoutWithoutApiEvent());
        }
    }

    @Override
    public void dealError(VolleyError volleyError) {
        if (volleyError != null) {
            CheckVolleyErrorUtil.checkError(volleyError);
        }
    }

    private static Handler mToastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtils.showToast(msg.obj.toString());
                    break;
            }
        }
    };

    private static boolean isLiving(Activity activity) {
        if (activity == null) {
            Log.e("wisely", "activity == null");
            return false;
        }

        if (activity.isFinishing()) {
            Log.e("wisely", "activity is finishing");
            return false;
        }
        return true;
    }

    /**
     * Created by ccs
     * 因为 register event 时必须要在 activity 中声明 onEvent，所以定义了此类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEmptyEvent(EmptyEvent event) {
    }

}
