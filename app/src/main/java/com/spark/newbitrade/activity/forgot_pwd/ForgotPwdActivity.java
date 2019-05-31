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
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.CommonUtils;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.Injection;

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
    private ForgotPwdContract.Presenter presenter;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    boolean isEmail = false;
    private String strAreaCode = "86";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gt3GeetestUtils.cancelUtils();
    }

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
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        new ForgotPwdPresenter(Injection.provideTasksRepository(activity), this);
        tvTitle.setVisibility(View.INVISIBLE);
        //setTitle(getString(R.string.phone_retrieve));
//        tvGoto.setVisibility(View.VISIBLE);
//        tvGoto.setText(getString(R.string.email_retrieve));

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
            HashMap<String, String> map = new HashMap<>();
            map.put("password", password);
            map.put("code", code);
            if (isEmail) {
                map.put("account", email);
                map.put("mode", "1");
            } else {
                map.put("account", phone);
                map.put("mode", "0");
            }
            presenter.doForget(map);
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        if (isEmail) {
            String email = etEmail.getText().toString().trim();
            if (StringUtils.isEmpty(email) || !StringUtils.isEmail(email)) {
                ToastUtils.showToast(R.string.email_diff);
            } else {
                presenter.captch();
            }
        } else {
            String phone = etPhone.getText().toString().trim();
            if (StringUtils.isEmpty(phone) || phone.length() < 11) {
                ToastUtils.showToast(R.string.phone_not_correct);
            } else {
                presenter.captch();
            }
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

    @Override
    public void setPresenter(ForgotPwdContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void forgotCodeSuccess(String obj) {
        try {
            gt3GeetestUtils.gt3TestFinish();
            timeCount.start();
            tvGetCode.setEnabled(false);
            ToastUtils.showToast(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forgotCodeFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void captchSuccess(JSONObject obj) {
        gt3GeetestUtils.gtSetApi1Json(obj);
        gt3GeetestUtils.getGeetest(activity, null, null, null, new GT3GeetestBindListener() {
            @Override
            public boolean gt3SetIsCustom() {
                return true;
            }

            @Override
            public void gt3GetDialogResult(boolean status, String result) {
                if (status) {
                    Captcha captcha = new Gson().fromJson(result, Captcha.class);
                    if (captcha == null) return;
                    String geetest_challenge = captcha.getGeetest_challenge();
                    String geetest_validate = captcha.getGeetest_validate();
                    String geetest_seccode = captcha.getGeetest_seccode();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("geetest_challenge", geetest_challenge);
                    map.put("geetest_validate", geetest_validate);
                    map.put("geetest_seccode", geetest_seccode);
                    if (isEmail) {
                        String email = etEmail.getText().toString().trim();
                        map.put("email", email);
                        presenter.forgotCode(UrlFactory.getEmailForgotPwdCodeUrl(), map);
                    } else {
                        String phone = etPhone.getText().toString().trim();
                        map.put("type", "2");
                        map.put("code", strAreaCode);
                        map.put("phone", phone);
                        presenter.forgotCode(UrlFactory.getCodeUrl(), map);
                    }
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void captchFail(Integer code, String toastMessage) {

    }

    @Override
    public void doForgetSuccess(String obj) {
        ToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void doForgetFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
}
