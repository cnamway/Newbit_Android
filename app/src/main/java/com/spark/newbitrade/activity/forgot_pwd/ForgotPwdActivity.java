package com.spark.newbitrade.activity.forgot_pwd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.country.CountryActivity;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Captcha;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.CommonUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.json.JSONObject;


import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH;
import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH2;

/**
 * 忘记密码
 */
public class ForgotPwdActivity extends BaseActivity implements ForgotPwdContract.View {


    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.llGetCode)
    LinearLayout llGetCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRenewPassword)
    EditText etRenewPassword;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.llGoSign)
    LinearLayout llGoSign;

    private TimeCount timeCount;
    private ForgotPwdPresenter presenter;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    boolean isEmail = false;
    private String strAreaCode = "86";
    private String cid;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CountryActivity.RETURN_COUNTRY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Country country = (Country) data.getSerializableExtra("country");
                if (country != null) {
                    tvCountry.setText(CommonUtils.getCountryNameByLanguageCode(country));
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
    protected int getActivityLayoutId() {
        return R.layout.activity_forgot_pwd;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new ForgotPwdPresenter(this);
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        tvTitle.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void loadData() {

    }

    @OnClick({R.id.tvGetCode, R.id.tvGoto, R.id.tvConfirm, R.id.tvCountry, R.id.llGoSign})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvGoto:
                if (!isEmail) {
                    isEmail = true;
                    //setTitle(getString(R.string.email_retrieve));
                    tvGoto.setText(getString(R.string.phone_retrieve));
                    etCode.setHint(getString(R.string.email_code));
                    etEmail.setVisibility(View.VISIBLE);
                    etPhone.setVisibility(View.GONE);
                } else {
                    isEmail = false;
                    //setTitle(getString(R.string.phone_retrieve));
                    tvGoto.setText(getString(R.string.email_retrieve));
                    etCode.setHint(getString(R.string.phone_code));
                    etEmail.setVisibility(View.GONE);
                    etPhone.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tvConfirm:
                doSubmit();
                break;
            case R.id.tvCountry:
                showActivity(CountryActivity.class, null, 0);
                break;
            case R.id.llGoSign:
                showActivity(LoginActivity.class, null);
                break;
        }

    }

    /**
     * 提交数据
     */
    private void doSubmit() {
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String code = etCode.getText().toString();
        String password = etPassword.getText().toString();
        String passwordRe = etRenewPassword.getText().toString();
        if ((StringUtils.isEmpty(email, code, password, passwordRe) && isEmail) || (!isEmail && StringUtils.isEmpty(phone, code, password, passwordRe))) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (!password.equals(passwordRe)) {
            ToastUtils.showToast(R.string.pwd_diff);
        } else {
            presenter.updateForget(strAreaCode + phone, password, code);
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        String phone = etPhone.getText().toString().trim();
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showToast(R.string.phone_empty);
        } else {
            presenter.getPhoneCode(strAreaCode + phone);
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        etEmail.addTextChangedListener(new MyTextWatcher());
        etPhone.addTextChangedListener(new MyTextWatcher());
        etCode.addTextChangedListener(new MyTextWatcher());
        etPassword.addTextChangedListener(new MyTextWatcher());
        etRenewPassword.addTextChangedListener(new MyTextWatcher());
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
            String password = etPassword.getText().toString().trim();
            String rePwd = etRenewPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            if ((StringUtils.isEmpty(password, rePwd, phone, code) && !isEmail) || (StringUtils.isEmpty(password, rePwd, email, code) && isEmail)) {
                tvConfirm.setBackgroundResource(R.drawable.shape_bg_normal_corner_grey_enabled);
                tvConfirm.setEnabled(false);
            } else {
                tvConfirm.setBackgroundResource(R.drawable.ripple_btn_global_option_selector);
                tvConfirm.setEnabled(true);
            }
        }
    }

    @Override
    public void checkPhoneCodeSuccess(String response) {

    }

    @Override
    public void getPhoneCodeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        if (!isEmail) {
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
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
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (isEmail) {
            bundle.putString("account", StringUtils.getText(etEmail));
        } else {
            bundle.putString("account", StringUtils.getText(etPhone));
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
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

    @Override
    public void codeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        if (!isEmail) {
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
    }
}
