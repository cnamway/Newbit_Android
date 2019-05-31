package com.spark.newbitrade.activity.bind_account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.my_account.MyAccountContract;
import com.spark.newbitrade.activity.my_account.MyAccountPresenterImpl;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.utils.GlobalConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定账号
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindAccountActivity extends BaseActivity implements MyAccountContract.View {
    @BindView(R.id.llAliAccount)
    LinearLayout llAliAccount;
    @BindView(R.id.llWeiChatAccount)
    LinearLayout llWeiChatAccount;
    @BindView(R.id.llUnionPayAccount)
    LinearLayout llUnionPayAccount;
    @BindView(R.id.llPayPal)
    LinearLayout llPayPal;
    @BindView(R.id.llOther)
    LinearLayout llOther;

//    private MyAccountPresenterImpl presenter;
//    private List<PayWaySetting> payWaySettings;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_account;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
//        presenter = new MyAccountPresenterImpl(this);
        setTitle(getString(R.string.binding_account));
    }

    @OnClick({R.id.llAliAccount, R.id.llWeiChatAccount, R.id.llUnionPayAccount, R.id.llPayPal, R.id.llOther})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);

//        if (payWaySettings == null) {
//            refreshData();
//            return;
//        }

        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llUnionPayAccount:
                showActivity(BindBankActivity.class, bundle, 0);
                break;
            case R.id.llWeiChatAccount:
//                putBundelData(bundle, GlobalConstant.wechat);
                bundle.putString("payWay", GlobalConstant.wechat);
                showActivity(BindAliActivity.class, bundle, 0);
                break;
            case R.id.llAliAccount:
//                putBundelData(bundle, GlobalConstant.alipay);
                bundle.putString("payWay", GlobalConstant.alipay);
                showActivity(BindAliActivity.class, bundle, 0);
                break;
            case R.id.llPayPal:
//                putBundelData(bundle, GlobalConstant.PAYPAL);
                bundle.putBoolean("isPayPal", true);
                showActivity(BindPayPalActivity.class, bundle, 0);
                break;
            case R.id.llOther:
//                putBundelData(bundle, GlobalConstant.other);
                bundle.putBoolean("isPayPal", false);
                showActivity(BindPayPalActivity.class, bundle, 0);
                break;
        }
    }

//    private void putBundelData(Bundle bundle, String payType) {
//        for (PayWaySetting payWaySetting : payWaySettings) {
//            if (payWaySetting.getPayType().equals(payType)) {
//                bundle.putSerializable("data", payWaySetting);
//            }
//        }
//    }

    @Override
    protected void loadData() {
        refreshData();
    }

    private void refreshData() {
        if (MyApplication.getApp().isLogin()) {
//            presenter.queryPayWayList();
        }
    }

//    @Override
//    public void getAccountSettingSuccess(AccountSetting obj) {
//        this.accountSetting = obj;
//        tvAli.setText(accountSetting.isAlipayVerified() ? getString(R.string.to_edit) : getString(R.string.unbound));
//        tvAli.setEnabled(accountSetting.isAlipayVerified());
//
//        tvWeiChat.setText(accountSetting.isWechatVerified() ? getString(R.string.to_edit) : getString(R.string.unbound));
//        tvWeiChat.setEnabled(accountSetting.isWechatVerified());
//
//        tvUnionPay.setText(accountSetting.isCardVerified() ? getString(R.string.to_edit) : getString(R.string.unbound));
//        tvUnionPay.setEnabled(accountSetting.isCardVerified());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.destory();
    }

    @Override
    public void queryPayWayListSuccess(List<PayWaySetting> response) {
//        payWaySettings = new ArrayList<>();
//        if (response != null) {
//            payWaySettings.addAll(response);
//        }
    }

    @Override
    public void updateSuccess(MessageResult response) {

    }


}
