package com.spark.newbitrade.activity.bind_account;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定paypal/other
 */

public class BindPayPalActivity extends BaseActivity implements BindAliContract.View {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;

    private BindAliPresenterImpl presenter;
    private boolean isPayPal;
    private PayWaySetting payWaySetting;
    private boolean isUpdate = false;//添加或者更新

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_paypal;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        if (MyApplication.getApp().getCurrentUser() != null)
            etName.setText(MyApplication.getApp().getCurrentUser().getRealName());
    }

    @OnClick({R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvConfirm:
                confirm();
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BindAliPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWaySetting = (PayWaySetting) bundle.getSerializable("data");
            if (payWaySetting != null) {
                isUpdate = true;
                tvConfirm.setText(getResources().getString(R.string.str_text_change));
                etAccount.setText(payWaySetting.getPayAddress());
                etName.setText(payWaySetting.getRealName());
            }
            isPayPal = bundle.getBoolean("isPayPal");
        }
        if (isPayPal) {
            if (isUpdate) {
                tvTitle.setText(getString(R.string.text_change) + getString(R.string.str_paypal));
            } else {
                tvTitle.setText(getString(R.string.binding) + getString(R.string.str_paypal));
            }
        } else {
            if (isUpdate) {
                tvTitle.setText(getString(R.string.text_change) + getString(R.string.str_other));
            } else {
                tvTitle.setText(getString(R.string.binding) + getString(R.string.str_other));
            }
        }
    }

    private void confirm() {
        String account = etAccount.getText().toString().trim();
        String name = etName.getText().toString();
        String pwd = etNewPwd.getText().toString();
        if (StringUtils.isEmpty(name, account, pwd)) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else {
            if (isUpdate) {
                if (isPayPal) {
                    presenter.getUpdateAliOrWechat(payWaySetting.getId(), GlobalConstant.PAYPAL, account, getString(R.string.str_paypal), "", pwd, "", name);
                } else {
                    presenter.getUpdateAliOrWechat(payWaySetting.getId(), GlobalConstant.other, account, getString(R.string.str_other), "", pwd, "", name);
                }
            } else {
                if (isPayPal) {
                    presenter.getBindAliOrWechat(GlobalConstant.PAYPAL, account, getString(R.string.str_paypal), "", pwd, "", name);
                } else {
                    presenter.getBindAliOrWechat(GlobalConstant.other, account, getString(R.string.str_other), "", pwd, "", name);
                }
            }
        }
    }

    @Override
    public void uploadBase64PicSuccess(String obj) {

    }

    @Override
    public void doBindAliOrWechatSuccess(MessageResult obj) {
        if (StringUtils.isNotEmpty(obj.getMessage())) {
            if (obj.getMessage().equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj.getMessage());
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUpdateAliOrWechatSuccess(MessageResult obj) {
        if (StringUtils.isNotEmpty(obj.getMessage())) {
            if (obj.getMessage().equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj.getMessage());
        }
        setResult(RESULT_OK);
        finish();
    }


}
