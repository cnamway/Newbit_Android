package com.spark.newbitrade.activity.skip;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.library.ac.model.MemberWalletVo;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.PasswordDialog;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 其它app跳转进来的支付页面
 */

public class SkipPayActivity extends BaseActivity implements SkipPayContract.View {
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvUse)
    TextView tvUse;

    private String orderNo;
    private String amount;
    private String address;
    private String coinName;
    private MemberWalletVo memberWalletVo;

    private SkipPayPresnetImpl presnet;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_skip_pay;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, false);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.activity_skip_pay_title));

        presnet = new SkipPayPresnetImpl(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderNo = bundle.getString("orderNo");
            amount = bundle.getString("amount");
            address = bundle.getString("address");
            coinName = bundle.getString("coinName");

            if (StringUtils.isNotEmpty(orderNo)) {
                tvId.setText(orderNo);
            }

            if (StringUtils.isNotEmpty(amount) && StringUtils.isNotEmpty(coinName)) {
                tvAmount.setText(amount + " " + coinName);
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();

        if (MyApplication.getApp().isLogin()) {
            if (StringUtils.isNotEmpty(coinName)) {
                presnet.getCoinMessage(coinName);
            }
        } else {
            ToastUtils.showToast(getString(R.string.text_login_first));
            showActivity(LoginActivity.class, null);
        }

    }

    @OnClick({R.id.tvPay})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvPay:
                if (MyApplication.getApp().isLogin()) {
                    if (memberWalletVo != null && StringUtils.isNotEmpty(amount, address, coinName)) {
                        if (Double.valueOf(memberWalletVo.getBalance().toString()) >= Double.valueOf(amount)) {
                            showPasswordDialog();
                        } else {
                            ToastUtils.showToast(R.string.str_no_enough_balance);
                        }
                    } else {
                        if (StringUtils.isNotEmpty(coinName)) {
                            presnet.getCoinMessage(coinName);
                        }
                    }
                } else {
                    ToastUtils.showToast(getString(R.string.text_login_first));
                    showActivity(LoginActivity.class, null);
                }
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();

    }

    private void showPasswordDialog() {
        final PasswordDialog passwordDialog = new PasswordDialog(this);
        passwordDialog.show();
        passwordDialog.setClicklistener(new PasswordDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(String password) {
                passwordDialog.dismiss();
                presnet.walletWithdraw(address, new BigDecimal(amount), coinName, password);
            }

            @Override
            public void doCancel() {
                passwordDialog.dismiss();
            }
        });
    }

    @Override
    public void walletWithdrawSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            ToastUtils.showToast("支付成功");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", "success");
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void getCoinMessageSuccess(MemberWalletVo obj) {
        if (obj == null) return;
        memberWalletVo = obj;
        tvUse.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(obj.getBalance().toString()), 8, null)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presnet.destory();
    }


}
