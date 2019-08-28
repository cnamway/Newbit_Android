package com.spark.newbitrade.activity.account_pwd_reset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Captcha;
import com.spark.newbitrade.entity.CountryEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.StringFormatUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH;
import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH2;

/**
 * 重置资金密码
 */
public class AccountPwdResetActivity extends BaseActivity implements AccountPwdResetContract.ForgotPwdView {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etAccountPwd)
    EditText etAccountPwd;
    @BindView(R.id.etRepeatPwd)
    EditText etRepeatPwd;
    @BindView(R.id.tvSet)
    TextView tvSet;

    private TimeCount timeCount;
    private AccountPwdResetPresenterImpl presenter;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String strAreaCode = "86";
    private boolean isCaptch;
    private String cid;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_account_pwd_reset;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                CountryEntity country = (CountryEntity) data.getSerializableExtra("getCountry");
                if (country != null) {
                    strAreaCode = country.getAreaCode();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.cancelUtils();
            gt3GeetestUtils = null;
        }
        presenter.destory();
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        setTitle(getString(R.string.reset_money_pwd));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String phone = bundle.getString("phone");
            if (StringUtils.isNotEmpty(phone)) {
                etPhone.setText(phone);
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        presenter = new AccountPwdResetPresenterImpl(this);
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
    }

    @Override
    protected void setListener() {
        super.setListener();
        etCode.addTextChangedListener(new MyTextWatcher());
        etAccountPwd.addTextChangedListener(new MyTextWatcher());
        etRepeatPwd.addTextChangedListener(new MyTextWatcher());
    }


    @OnClick({R.id.tvGetCode, R.id.tvSet})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvSet:
                checkInput();
                break;
        }
    }

    protected void checkInput() {
        String phone = StringUtils.getText(etPhone);
        String code = StringUtils.getText(etCode);
        String password = StringUtils.getText(etAccountPwd);
        String passwordRe = StringUtils.getText(etRepeatPwd);
        if (StringUtils.isEmpty(phone, code, password, passwordRe)) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else if (password.length() != 6) {
            ToastUtils.showToast(this, R.string.text_money_pwd_tag);
        } else if (!password.equals(passwordRe)) {
            ToastUtils.showToast(activity, getString(R.string.str_pwd_diff));
        } else {
            presenter.updateForget(strAreaCode + phone, password, code);
        }
    }

    @Override
    public void checkPhoneCodeSuccess(String response) {
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String phone = StringUtils.getText(etPhone);
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_phone_number));
        } else if (!StringFormatUtils.isMobile(phone)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input_correct) + getString(R.string.str_phone_number));
        } else {
            presenter.getPhoneCode(strAreaCode + phone);
        }

    }

    @Override
    public void getPhoneCodeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        timeCount.start();
        tvGetCode.setEnabled(false);
    }

    @Override
    public void captchSuccess(JSONObject obj) {
        gt3GeetestUtils.gtSetApi1Json(obj);
        gt3GeetestUtils.getGeetest(activity, HttpUrls.UC_HOST + "/captcha/mm/gee", null, null, new GT3GeetestBindListener() {
            @Override
            public boolean gt3SetIsCustom() {
                return true;
            }

            @Override
            public void gt3GetDialogResult(boolean status, String result) {
                if (status) {
                    Captcha captcha = new Gson().fromJson(result, Captcha.class);
                    String checkData = "gee::" + captcha.getGeetest_challenge() + "$" + captcha.getGeetest_validate() + "$" + captcha.getGeetest_seccode();
                    presenter.getPhoneCode(strAreaCode + StringUtils.getText(etPhone), checkData, cid);
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void updateForgetSuccess(String obj) {
        ToastUtils.showToast(activity, obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestClose();
            gt3GeetestUtils = null;
        }
        int code = httpErrorEntity.getCode();
        String msg = httpErrorEntity.getMessage();
        if (code == CAPTCH && StringUtils.isNotEmpty(msg) && msg.contains("captcha")) {
            cid = httpErrorEntity.getCid();
            gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
            presenter.captch();
        } else if (code == CAPTCH2 && StringUtils.isNotEmpty(msg) && msg.contains("Captcha")) {//解决验证码失效问题
            ToastUtils.showToast(getResources().getString(R.string.str_code_error));
        } else {
            ToastUtils.showToast(activity, httpErrorEntity.getMessage());
        }
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String password = etAccountPwd.getText().toString().trim();
            String rePwd = etRepeatPwd.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            if (StringUtils.isEmpty(password, rePwd, phone, code)) {
                tvSet.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                tvSet.setEnabled(false);
            } else {
                tvSet.setBackgroundResource(R.drawable.ripple_btn_global_option_corner_selector);
                tvSet.setEnabled(true);
            }
        }
    }

    @Override
    public void codeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        timeCount.start();
        tvGetCode.setEnabled(false);
    }
}