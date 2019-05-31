package com.spark.newbitrade.activity.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.forgot_pwd.ForgotPwdActivity;
import com.spark.newbitrade.activity.main.MainActivity;
import com.spark.newbitrade.activity.signup.SignUpActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.PhoneVertifyDialog;
import com.spark.newbitrade.entity.Captcha;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.KeyboardUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import org.json.JSONObject;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH;
import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;


/**
 * 登录
 */

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {
    public static final int RETURN_LOGIN = 1;

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    //    @BindView(R.id.tvLogin)
//    TextView tvLogin;
    @BindView(R.id.tvForgetPas)
    TextView tvForgetPas;
    @BindView(R.id.ivEye)
    ImageView ivEye;
    @BindView(R.id.tvSign)
    TextView tvSign;

    private LoginPresenterImpl loginPresenter;
    private String gtc;
    private boolean isCasLogin;
    private PhoneVertifyDialog mPhoneVertifyDialog;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;
    private String strAreaCode = "86";
//    private LoginStatus loginStatus;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            String username = bundle.getString("account");
            etUsername.setText(username);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        ivEye.setTag(false);
        llTitle.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
//        loginStatus = new LoginStatus();
        loginPresenter = new LoginPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String username = bundle.getString("username");
            if (StringUtils.isNotEmpty(username)) {
                etUsername.setText(username);
                etPassword.requestFocus();
            }
        } else {
            //显示手机号码
            String username = SharedPreferenceInstance.getInstance().getStringParam(SharedPreferenceInstance.SP_KEY_LOGIN_ACCOUNT);
            if (StringUtils.isNotEmpty(username)) {
                etUsername.setText(username);
                etPassword.requestFocus();
            }
        }
        String htm = "<font color=#6C6E8A>" + getString(R.string.str_users_please_sigh_in_before) + "</font>" + getString(R.string.str_users_please_sigh_in_after) + ">>";
        tvSign.setText(Html.fromHtml(htm));
    }

    @OnClick({R.id.tvForgetPas, R.id.tvLogin, R.id.ivEye, R.id.tvSign})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvForgetPas:
                showActivity(ForgotPwdActivity.class, null);
                break;
            case R.id.tvLogin:
                checkInput();
                break;
            case R.id.ivEye:
                isShowVisible();
                break;
            case R.id.tvSign:
                showActivity(SignUpActivity.class, null, 0);
                break;
        }
    }

    /**
     * 是否可见
     */
    private void isShowVisible() {
        boolean isVisible = (boolean) ivEye.getTag();
        if (!isVisible) {
            isVisible = true;
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivEye.setImageResource(R.mipmap.icon_eye_open);
        } else {
            isVisible = false;
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivEye.setImageResource(R.mipmap.icon_eye_close);
        }
        ivEye.setTag(isVisible);
    }

    /**
     * 检查用户名和密码后进行登录
     */
    protected void checkInput() {
        MyApplication.getApp().deleteCurrentUser();
        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        SharedPreferenceInstance.getInstance().saveLockPwd("");
        MyApplication.getApp().getCookieManager().getCookieStore().removeAll();

        String username = StringUtils.getText(etUsername);
        String password = StringUtils.getText(etPassword);
        if (StringUtils.isEmpty(username)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_username));
        } else if (StringUtils.isEmpty(password)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_pwd));
        } else {
            KeyboardUtils.hideSoftInput(activity);
            loginPresenter.casLogn(strAreaCode + username, password, "true");
        }
    }

    /**
     * 极验证captcha成功回调
     *
     * @param obj
     */
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
                    loginPresenter.checkCaptcha(checkData, cid);
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void casLoginSuccess(Object o) {
        gtc = (String) o;
        loginPresenter.doUcLogin(gtc, HttpUrls.TYPE_UC);
        isCasLogin = true;
        //保存手机号码
        String username = StringUtils.getText(etUsername);
        SharedPreferenceInstance.getInstance().setParam(activity, SharedPreferenceInstance.SP_KEY_LOGIN_ACCOUNT, username);
    }

    /**
     * 极限验证验证成功回调
     *
     * @param o
     */
    @Override
    public void codeSuccess(Object o) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        if (mPhoneVertifyDialog != null && mPhoneVertifyDialog.isShowing()) {
            mPhoneVertifyDialog.setStart();
        }
    }

    @Override
    public void ucLoginSuccess(String response) {
        if (HttpUrls.TYPE_UC.equals(response)) {
//            loginStatus.setUcLogin(true);
            loginPresenter.doUcLogin(gtc, HttpUrls.TYPE_AC);
        } else if (HttpUrls.TYPE_AC.equals(response)) {
//            loginStatus.setAcLogin(true);
            loginPresenter.doUcLogin(gtc, HttpUrls.TYPE_OTC);
        } else if (HttpUrls.TYPE_OTC.equals(response)) {
            saveOtcSid();
            isCasLogin = false;
//            loginStatus.setOtcLogin(true);
            loginPresenter.getUserInfo();
//            MyApplication.getApp().setLoginStatus(loginStatus);
        }
    }

    private void saveOtcSid() {
        CookieManager cookieManager = MyApplication.getApp().getCookieManager();
        CookieStore cookieStore = cookieManager.getCookieStore();
        List<HttpCookie> cookies = cookieStore.getCookies();
        for (HttpCookie cookie : cookies) {
            if ("otcsid".equals(cookie.getName())) {
                SharedPreferenceInstance.getInstance().setOtcSid(MyApplication.getApp(), cookie.getValue());
                break;
            }
        }
    }

    /**
     * 0-未认证 1待审核 2-审核不通过 3-已认证
     *
     * @param user
     */
    @Override
    public void getUserInfoSuccess(User user) {
//        int status = user.getRealNameStatus();
//        if (status == 0) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("NoticeType", 0);
//            bundle.putString("Notice", "");
//            showActivity(CreditActivity.class, bundle);
//            ToastUtils.showToast(activity, getString(R.string.str_first_credit));
//            finish();
//        } else if (status == 1) {
//            ToastUtils.showToast(activity, getString(R.string.str_creditting));
//        } else if (status == 2) {
//            ToastUtils.showToast(activity, getString(R.string.str_creditfail));
//        } else if (status == 3) {
        user.setLogin(true);
        user.setGtc(gtc);
        MyApplication.getApp().setCurrentUser(user);
        setResult(RESULT_OK);
        ToastUtils.showToast(getString(R.string.str_login_success));
        showActivity(MainActivity.class, null);
        finish();
//        }
    }

    @Override
    public void checkPhoneCodeSuccess(String response) {
        if (isCasLogin) {
            loginPresenter.doUcLogin(gtc, HttpUrls.TYPE_UC);
        } else {
            checkInput();
        }
    }

    @Override
    public void checkCaptchaSuccess(String response) {
        if (isCasLogin) {
            loginPresenter.doUcLogin(gtc, HttpUrls.TYPE_UC);
        } else {
            checkInput();
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            int code = httpErrorEntity.getCode();
            if (code == GlobalConstant.LOGIN_ERROR && !isCasLogin) {
                ToastUtils.showToast(getString(R.string.str_login_error));
            } else if (code == JSON_ERROR) {
                ToastUtils.showToast(getString(R.string.str_json_error));
            } else if (code == CAPTCH) {
                vertify(httpErrorEntity);
            } else {
                String message = httpErrorEntity.getMessage();
                if (StringUtils.isNotEmpty(message)) {
                    ToastUtils.showToast(message);
                } else {
                    ToastUtils.showToast(R.string.socket_time);
                }
            }
        }
    }

    /**
     * 进行验证（根据data字段来判断，data=="phone"为短信验证，data=="gee"为极验证）
     *
     * @param httpErrorEntity
     */
    private void vertify(HttpErrorEntity httpErrorEntity) {
        String data = httpErrorEntity.getData();
        cid = httpErrorEntity.getCid();
        if ("phone".equals(data)) {
            showPhoneVertifyDialog();
        } else if ("gee".equals(data)) {
            geeVertify(httpErrorEntity);
        } else {
            ToastUtils.showToast(httpErrorEntity.getMessage());
        }
    }

    /**
     * 极验证
     *
     * @param httpErrorEntity
     */
    private void geeVertify(HttpErrorEntity httpErrorEntity) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestClose();
            gt3GeetestUtils = null;
        }
        cid = httpErrorEntity.getCid();
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        loginPresenter.captch();
    }

    /**
     * 展示发送短信验证码弹框
     */
    private void showPhoneVertifyDialog() {
        final String username = StringUtils.getText(etUsername);
        if (mPhoneVertifyDialog == null) {
            mPhoneVertifyDialog = new PhoneVertifyDialog(this);
            mPhoneVertifyDialog.withWidthScale(0.9f).withHeightScale(0.4f);
            mPhoneVertifyDialog.setClickListener(new PhoneVertifyDialog.ClickLister() {
                @Override
                public void onCancel() {
                    mPhoneVertifyDialog.dismiss();
                }

                @Override
                public void onSendVertifyCode() {
                    loginPresenter.getPhoneCode(strAreaCode + username);
                }

                @Override
                public void onConfirm() {
                    loginPresenter.checkPhoneCode(mPhoneVertifyDialog.getVertifyCode());
                    mPhoneVertifyDialog.dismiss();
                }
            });
        }
        mPhoneVertifyDialog.show();
    }

}
