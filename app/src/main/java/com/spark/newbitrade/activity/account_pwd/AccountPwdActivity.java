package com.spark.newbitrade.activity.account_pwd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置/修改资金密码
 */
public class AccountPwdActivity extends BaseActivity implements AccountPwdContract.View {
    @BindView(R.id.etOldPwd)
    TextView etOldPwd;
    @BindView(R.id.etAccountPwd)
    EditText etAccountPwd;
    @BindView(R.id.etRepeatPwd)
    EditText etRepeatPwd;
    @BindView(R.id.tvSet)
    TextView tvSet;
    @BindView(R.id.llOldPwd)
    LinearLayout llOldPwd;
    @BindView(R.id.tvPwdTag)
    TextView tvPwdTag;
    @BindView(R.id.tvRePwdTag)
    TextView tvRePwdTag;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvForgetPas)
    TextView tvForgetPas;

    private AccountPwdPresenterImpl presenter;
    private boolean isSet = false;

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
        return R.layout.activity_account_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.set_money_pwd));
        presenter = new AccountPwdPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isSet = bundle.getBoolean("isSet");
            if (isSet) {
                setTitle(getString(R.string.change_money_pwd));
                tvTag.setText(getString(R.string.edit_money_pwd_tag));
                llOldPwd.setVisibility(View.VISIBLE);
                tvTag.setText(getString(R.string.edit_money_pwd_tag));
                tvPwdTag.setText(getString(R.string.new_money_pwd));
                etAccountPwd.setHint(getString(R.string.new_money_pwd));
                tvRePwdTag.setText(getString(R.string.confirm_money_pwd));
                etRepeatPwd.setHint(getString(R.string.confirm_money_pwd));
                tvSet.setText(getString(R.string.text_change));
                tvForgetPas.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.tvSet)
    void accountPwd() {
        String jyPassword = etAccountPwd.getText().toString().trim();
        String repeatPWd = etRepeatPwd.getText().toString().trim();
        String oldPwd = etOldPwd.getText().toString().trim();
        if ((StringUtils.isEmpty(jyPassword, repeatPWd) && !isSet)) {
            ToastUtils.showToast(R.string.incomplete_information);
        } else if (StringUtils.isEmpty(jyPassword, repeatPWd, oldPwd) && isSet) {
            ToastUtils.showToast(R.string.incomplete_information);
        } else if (!jyPassword.equals(repeatPWd)) {
            ToastUtils.showToast(R.string.str_pwd_diff);
        } else {
            if (!isSet) {
                presenter.accountPwd(jyPassword);
            } else {
                presenter.editAccountPwd(jyPassword, oldPwd);
            }
        }
    }

    @OnClick(R.id.tvForgetPas)
    void skipResetActivity() {
        //显示手机号码
        String phone = SharedPreferenceInstance.getInstance().getStringParam(SharedPreferenceInstance.SP_KEY_LOGIN_ACCOUNT);
        if (StringUtils.isNotEmpty(phone)) {
            Bundle bundle = new Bundle();
            bundle.putString("phone", phone);
            //showActivity(AccountPwdResetActivity.class, bundle, 1);
        }
    }


    @Override
    public void accountPwdSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        MyApplication.getApp().getCurrentUser().setFundsVerified(1);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void editAccountPwdSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        ToastUtils.showToast(toastMessage);
    }
}
