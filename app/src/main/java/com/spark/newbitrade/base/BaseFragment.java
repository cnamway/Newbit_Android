package com.spark.newbitrade.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.gyf.barlibrary.ImmersionBar;
import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.LoadExceptionEvent;
import com.spark.newbitrade.event.CheckLoginEvent;
import com.spark.newbitrade.event.EmptyEvent;
import com.spark.newbitrade.event.LoginoutWithoutApiEvent;
import com.spark.newbitrade.utils.CheckVolleyErrorUtil;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.spark.newbitrade.factory.HttpUrls.TYPE_AC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC_SYSTEM;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_UC;

/**
 * Created by Administrator on 2017/9/26.
 */

public abstract class BaseFragment extends Fragment implements BaseContract.BaseView {
    protected View rootView;
    Unbinder unbinder;
    protected ImmersionBar immersionBar;
    protected boolean isInit = false;
    protected boolean isNeedLoad = true;
    protected boolean isSetTitle = false;
    protected ImageView ivBack;
    protected TextView tvTitle;
    protected TextView tvGoto;
    protected LinearLayout llTitle;
    protected Activity activity;
    protected List<BaseFragment> fragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        tvTitle = rootView.findViewById(R.id.tvTitle);
        ivBack = rootView.findViewById(R.id.ivBack);
        llTitle = rootView.findViewById(R.id.llTitle);
        tvGoto = rootView.findViewById(R.id.tvGoto);
        activity = getmActivity();
        isInit = true;
        if (isImmersionBarEnabled()) initImmersionBar();
        initView();
        initData();
        setListener();
        EventBus.getDefault().register(this);
    }


    protected void setTitle(String title) {
        if (tvTitle != null)
            tvTitle.setText(title);
    }

    public void setShowBackBtn(boolean showBackBtn) {
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

    protected abstract int getLayoutId();

    /**
     * 初始化控件
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

    protected void fillWidget() {
    }

    ;

    protected void loadData() {
    }

    ;

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void initImmersionBar() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true, 0.2f);
        immersionBar.keyboardEnable(false, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN).init();
    }

    public static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void displayLoadingPopup() {
        if (getActivity() != null) ((BaseActivity) getActivity()).displayLoadingPopup();
    }

    public void hideLoadingPopup() {
        if (getActivity() != null) ((BaseActivity) getActivity()).hideLoadingPopup();
    }

    @Override
    public View getView() {
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (immersionBar != null) immersionBar.destroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroyView();
    }

    protected void finish() {
        getActivity().finish();
    }

    public BaseActivity getmActivity() {
        return (BaseActivity) super.getActivity();
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
    protected void showActivity(Class<?> cls, Bundle bundle, int requesCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        if (requesCode >= 0)
            startActivityForResult(intent, requesCode);
        else
            startActivity(intent);
    }

    @Override
    public void showLoading() {
        if (getActivity() != null) ((BaseActivity) getActivity()).displayLoadingPopup();
    }

    @Override
    public void hideLoading() {
        if (getActivity() != null) ((BaseActivity) getActivity()).hideLoadingPopup();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            if (httpErrorEntity.getCode() == GlobalConstant.LOGIN_ERROR) {
                if (StringUtils.isNotEmpty(httpErrorEntity.getUrl())) {
                    LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",httpErrorEntity.getUrl()==" + httpErrorEntity.getUrl());
                    if (httpErrorEntity.getUrl().contains("/" + TYPE_OTC_SYSTEM)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_OTC_SYSTEM));
                    } else if (httpErrorEntity.getUrl().contains("/" + TYPE_OTC)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_OTC));
                    } else if (httpErrorEntity.getUrl().contains("/" + TYPE_UC)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_UC));
                    } else if (httpErrorEntity.getUrl().contains("/" + TYPE_AC)) {
                        EventBus.getDefault().post(new CheckLoginEvent(TYPE_AC));
                    } else {
                        LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",new LoadExceptionEvent()==退出登录=======");
                        EventBus.getDefault().post(new LoadExceptionEvent());
                    }
                }
            } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCHA_HADBEEN_SEND) {
                Message message = new Message();
                message.what = 1;
                message.obj = getString(R.string.str_no_repeat);
                mToastHandler.sendMessage(message);
            } else if (httpErrorEntity.getCode() == GlobalConstant.SERVER_ERROR_CODE) {
                LogUtils.e("BaseFragment==dealError==HttpErrorEntity==" + httpErrorEntity.getCode());
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

    /**
     * Created by ccs
     * 因为 register event 时必须要在 activity 中声明 onEvent，所以定义了此类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEmptyEvent(EmptyEvent event) {
    }
}
