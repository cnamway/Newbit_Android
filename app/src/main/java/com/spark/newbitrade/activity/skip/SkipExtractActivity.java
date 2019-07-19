package com.spark.newbitrade.activity.skip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.library.ac.model.MemberWallet;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.ActivityManage;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.SkipExtractTipDialog;
import com.spark.newbitrade.entity.CasLoginEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.event.CheckLoginEvent;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.factory.HttpUrls.TYPE_AC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC_SYSTEM;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_UC;

/**
 * 其它app跳转进来的提币页面
 */

public class SkipExtractActivity extends BaseActivity implements SkipExtractContract.View {
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.tvAmount)
    TextView tvAmount;

    private String coinName;
    private String amount;
    private MemberWallet memberWallet;

    private SkipExtractPresnetImpl presnet;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_skip_extract;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    LogUtils.e("SkipExtractActivity==onActivityResult==loadData==");
                    loadData();
                    break;
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        LogUtils.e("SkipExtractActivity==onActivityResult==EventBus.getDefault().register==");
        EventBus.getDefault().register(this);
        setSetTitleAndBack(false, false);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.activity_skip_extract_title));

        presnet = new SkipExtractPresnetImpl(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            coinName = bundle.getString("coinName");
            amount = bundle.getString("amount");

            if (StringUtils.isNotEmpty(coinName)) {
                tvCoinName.setText(coinName);
            }

            if (StringUtils.isNotEmpty(amount)) {
                tvAmount.setText(amount);
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (MyApplication.getApp().isLogin()) {
            if (StringUtils.isNotEmpty(coinName)) {
                LogUtils.e("SkipExtractActivity==loadData()==开始加载数据==");
                presnet.getAddress(coinName);
            }
        } else {
            goLogin();
        }
    }

    @OnClick({R.id.tvPay})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvPay:
                showTipDialog();
                break;
        }
    }

    private void showTipDialog() {
        final SkipExtractTipDialog extractTipDialog = new SkipExtractTipDialog(this);
        extractTipDialog.onPositiveClickLisenter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractTipDialog.dismiss();
                LogUtils.e("SkipExtractActivity==setOnClickListener()==memberWallet==" + memberWallet);
                if (memberWallet != null && StringUtils.isNotEmpty(memberWallet.getAddress())) {
                    LogUtils.e("SkipExtractActivity==setOnClickListener()==memberWalletVo==" + memberWallet.toString());
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("address", memberWallet.getAddress());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    //ToastUtils.showToast("未获取到提币地址,请重试");
                    goLogin();
                }
            }
        });
        extractTipDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presnet.destory();
    }

    @Override
    public void getAddressSuccess(MemberWallet obj) {
        if (obj == null) return;
        memberWallet = obj;

        LogUtils.e("SkipExtractActivity==getAddressSuccess()==memberWallet==" + memberWallet.toString());
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        loadData();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            LogUtils.e("SkipExtractActivity===ccse==response==" + "dealError===HttpErrorEntity===" + httpErrorEntity.toString());
            if (httpErrorEntity.getCode() == GlobalConstant.LOGIN_ERROR) {
                if (StringUtils.isNotEmpty(httpErrorEntity.getUrl())) {
                    LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",httpErrorEntity.getUrl()==" + httpErrorEntity.getUrl());
                    if (httpErrorEntity.getUrl().contains("/" + TYPE_AC)) {
                        presnet.checkBusinessLogin(TYPE_AC);
                    } else {
                        LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",new LoadExceptionEvent()==退出登录=======");
                        logOut();
                    }
                }
            } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCH2) {
                Message message = new Message();
                message.what = 1;
                message.obj = getString(R.string.str_code_error);
                mToastHandler.sendMessage(message);
            } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCHA_HADBEEN_SEND) {
                Message message = new Message();
                message.what = 1;
                message.obj = getString(R.string.str_no_repeat);
                mToastHandler.sendMessage(message);
            } else if (httpErrorEntity.getCode() == GlobalConstant.SERVER_ERROR_CODE) {
                LogUtils.e("SkipExtractActivity==dealError==HttpErrorEntity==" + httpErrorEntity.getCode());
            } else if (StringUtils.isNotEmpty(httpErrorEntity.getMessage())) {
                Message message = new Message();
                message.what = 1;
                message.obj = httpErrorEntity.getMessage();
                mToastHandler.sendMessage(message);
            } else {
                if (GlobalConstant.isDebug) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = "" + httpErrorEntity.getCode();
                    mToastHandler.sendMessage(message);
                }
            }
        } else {
            logOut();
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

    private void logOut() {
        MyApplication.getApp().deleteCurrentUser();
        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        SharedPreferenceInstance.getInstance().saveLockPwd("");
        MyApplication.getApp().getCookieManager().getCookieStore().removeAll();
        ActivityManage.finishAll();
        goLogin();
    }

    @Override
    public void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity) {
        if (casLoginEntity != null) {
            String type = casLoginEntity.getType();
            if (!casLoginEntity.isLogin()) {
                String gtc = SharedPreferenceInstance.getInstance().getTgt();
                presnet.doLoginBusiness(gtc, type);
            }
        }
    }

    @Override
    public void doLoginBusinessSuccess(String type) {
        loadData();
    }

    private void goLogin() {
        ToastUtils.showToast(getString(R.string.text_login_first) + getString(R.string.app_name));
        Bundle bundle = new Bundle();
        bundle.putBoolean("isJumpApp", true);
        showActivity(LoginActivity.class, bundle, 1);
        finish();
    }

    /**
     * 检测登录状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginEvent(CheckLoginEvent event) {
        LogUtils.e("SkipExtractActivity==onCheckLoginEvent==event.type==" + event.type);
        if (event.type.contains(TYPE_OTC_SYSTEM)) {
            presnet.checkBusinessLogin(TYPE_OTC_SYSTEM);
        } else if (event.type.contains(TYPE_OTC)) {
            presnet.checkBusinessLogin(TYPE_OTC);
        } else if (event.type.contains(TYPE_UC)) {
            presnet.checkBusinessLogin(TYPE_UC);
        } else if (event.type.contains(TYPE_AC)) {
            presnet.checkBusinessLogin(TYPE_AC);
        }
    }
}
