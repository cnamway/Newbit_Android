package com.spark.newbitrade.activity.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.country.CountryActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Captcha;
import com.spark.newbitrade.entity.CountryEntity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.FormatDataUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.StringFormatUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH;
import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH2;

/**
 * 注册
 */

public class SignUpActivity extends BaseActivity implements SignUpContract.SignView {
    public static String token = "";
    @BindView(R.id.tvArea)
    TextView tvArea;
    private TextView tvTag;
    private EditText etPhone;
    private EditText etEmail;
    //    private EditText etUsername;
    private EditText etCode;
    private EditText etPromoCode;
    private EditText etPassword;
    private TextView tvCountry;
    private TextView tvGetCode;
    private TextView tvSignUp;
    private EditText etComfirmPassword;
    private TextView tvLoginTag;
    private CountryEntity country;
    private TimeCount timeCount;
    private SignUpPresenterImpl presenter;
    boolean isEmail = false;
    private String strCountry = "China";
    private String strAreaCode = "86";
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CountryActivity.RETURN_COUNTRY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                country = (CountryEntity) data.getSerializableExtra("getCountry");
                if (country != null) {
                    tvCountry.setText(FormatDataUtils.getViewNameByCode(country, activity));
                    strCountry = country.getEnName();
                    strAreaCode = country.getAreaCode();
                    tvArea.setText("+" + country.getAreaCode() + ">");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initView() {
        super.initView();
        findView();
        tvTitle.setVisibility(View.INVISIBLE);
        setSetTitleAndBack(false, true);
        setTitle(getString(R.string.phone_sign_up));
//        ivBack.setVisibility(View.VISIBLE);
        tvGoto.setVisibility(View.GONE);
        tvGoto.setText(getString(R.string.str_email_sign_up));
    }


    /**
     * findview
     */
    private void findView() {
        tvTag = findViewById(R.id.tvTag);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
//        etUsername = findViewById(R.id.etUsername);
        etCode = findViewById(R.id.etCode);
        etPromoCode = findViewById(R.id.etPromoCode);
        etPassword = findViewById(R.id.etPassword);
        tvCountry = findViewById(R.id.tvCountry);
        tvGetCode = findViewById(R.id.tvGetCode);
        tvSignUp = findViewById(R.id.tvSignUp);
        etComfirmPassword = findViewById(R.id.etComfirmPassword);
        tvLoginTag = findViewById(R.id.tvLoginTag);
        String htm = "<font color=#6C6E8A>" + getString(R.string.str_please_login_have_account) + "</font>" + getString(R.string.str_please_login) + ">>";
        tvLoginTag.setText(Html.fromHtml(htm));
    }

    @Override
    protected void initData() {
        super.initData();
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        presenter = new SignUpPresenterImpl(this);
    }


    /**
     * 切换注册方式
     */
    private void doSwitchWay() {
        if (!isEmail) { // 当前为手机注册，切换为邮箱注册
            isEmail = true;
            tvTitle.setText(getString(R.string.str_email_sign_up));
            tvGoto.setText(getString(R.string.str_phone_sign_up));
            tvTag.setText(getString(R.string.str_email_sign_up));
            etEmail.setVisibility(View.VISIBLE);
            etPhone.setVisibility(View.GONE);
        } else {
            isEmail = false;
            tvTitle.setText(getString(R.string.str_phone_sign_up));
            tvGoto.setText(getString(R.string.str_email_sign_up));
            tvTag.setText(getString(R.string.str_phone_sign_up));
            etEmail.setVisibility(View.GONE);
            etPhone.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tvGetCode, R.id.tvSignUp, R.id.tvLoginTag, R.id.tvCountry})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvSignUp:
                checkInput();
                break;
            case R.id.tvLoginTag:
                finish();
                break;
            case R.id.tvCountry:
                showActivity(CountryActivity.class, null, 0);
                break;
        }

    }

    protected void checkInput() {
        String phone = StringUtils.getText(etPhone);
//        String username = StringUtils.getText(etUsername);
        String code = StringUtils.getText(etCode);
        String password = StringUtils.getText(etPassword);
        String confirmPassword = StringUtils.getText(etComfirmPassword);
        String email = StringUtils.getText(etEmail);
        if (StringUtils.isEmpty(phone) && !isEmail) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_phone_number));
        } else if (isEmail && StringUtils.isEmpty(email)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_email));
        } else if (isEmail && !StringFormatUtils.isEmail(email)) {
            ToastUtils.showToast(getString(R.string.str_please_input_correct) + getString(R.string.str_email));
        } else if (StringUtils.isEmpty(code)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_code));
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_pwd_login));
        } else if (StringUtils.isEmpty(confirmPassword)) {
            ToastUtils.showToast(getString(R.string.str_please) + getString(R.string.str_confirm_pwd));
        } else if (!password.equals(confirmPassword)) {
            ToastUtils.showToast(getString(R.string.str_pwd_diff));
        } else {
            doSignUp(phone, "", code, password, email);
        }
    }

    /**
     * 注册
     *
     * @param phone
     * @param username
     * @param code
     * @param password
     * @param email
     */
    private void doSignUp(String phone, String username, String code, String password, String email) {
        HashMap<String, String> map = new HashMap<>();
        if (!isEmail) {
            presenter.sighUpByPhone(username, password, strCountry, StringUtils.getText(etPromoCode), strAreaCode + phone, code);
        } else {
            presenter.sighUpByEmail(username, password, strCountry, StringUtils.getText(etPromoCode), email, code);
        }
    }


    /**
     * 获取验证码
     */
    private void getCode() {
        if (!isEmail) {
            String phone = StringUtils.getText(etPhone);
            if (StringUtils.isEmpty(phone)) {
                ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_phone_number));
            } else if (!StringFormatUtils.isMobile(phone)) {
                ToastUtils.showToast(getString(R.string.str_please_input_correct) + getString(R.string.str_phone_number));
            } else {
                presenter.getPhoneCode(strAreaCode + phone);
            }
        } else {
            String email = StringUtils.getText(etEmail);
            if (StringUtils.isEmpty(email)) {
                ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_email));
            } else if (!StringFormatUtils.isEmail(email)) {
                ToastUtils.showToast(getString(R.string.str_please_input_correct) + getString(R.string.str_email));
            } else {
                presenter.getEmailCode(email);
            }
        }
    }

    @Override
    public void codeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        if (!isEmail) {
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
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
            cid = httpErrorEntity.getCid();
            gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
            presenter.captch();
        } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCHA_HADBEEN_SEND) {
            ToastUtils.showToast(getResources().getString(R.string.str_no_repeat));
        } else {
            ToastUtils.showToast(httpErrorEntity.getMessage());
        }
    }

    @Override
    public void sighUpSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
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

}
