package com.spark.newbitrade.activity.setting;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.language.LanguageActivity;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.base.ActivityManage;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.SharedPreferenceInstance;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements SettingContact.View {
    //    @BindView(R.id.llSwitchUser)
//    LinearLayout llSwitchUser;
    @BindView(R.id.llSwitchLanguage)
    LinearLayout llSwitchLanguage;
    private SettingPresenterImpl presenter;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.setting));
        presenter = new SettingPresenterImpl(this);
    }

    @OnClick({R.id.tvLoginOut, R.id.llSwitchLanguage})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
//            case R.id.llSwitchUser:
//                showActivity(SwitchUserActivity.class, null);
//                break;
            case R.id.llSwitchLanguage:
                showActivity(LanguageActivity.class, null);
                break;
            case R.id.tvLoginOut:
                showCofirmDialog();
                break;
        }
    }

    /**
     * 确认是否退出登录
     */
    private void showCofirmDialog() {
        final NormalDialog dialog = new NormalDialog(activity);
        dialog.isTitleShow(false).bgColor(Color.parseColor("#ffffff"))
                .content(getString(R.string.logout_current_account))
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#6a6e8a"))
                .btnTextColor(Color.parseColor("#6a6e8a"), Color.parseColor("#6a6e8a"))
                //.btnPressColor(Color.parseColor("#2B2B2B"))
                .btnText(getString(R.string.str_cancel), getString(R.string.dialog_sure))
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
//                doDelete();
                presenter.loginOut();
                dialog.superDismiss();
            }
        });
    }

    @Override
    public void loginOutSuccess(String obj) {
        MyApplication.getApp().deleteCurrentUser();
        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        SharedPreferenceInstance.getInstance().saveLockPwd("");
        MyApplication.getApp().getCookieManager().getCookieStore().removeAll();
        ActivityManage.finishAll();
        showActivity(LoginActivity.class, null);
    }


}
