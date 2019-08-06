package com.spark.newbitrade.activity.edit_login_pwd;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.base.ActivityManage;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Captcha;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.json.JSONObject;


import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH;
import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH2;

/**
 * 修改登录密码
 */
public class EditLoginPwdActivity extends BaseActivity implements EditLoginPwdContract.View {
    @BindView(R.id.etOldPwd)
    EditText etOldPwd;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.etRepeatPwd)
    EditText etRepeatPwd;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.tvNumber)
    TextView tvNumber;

    private EditLoginPwdPresenter presenter;
    private String phone;
    private TimeCount timeCount;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_edit_login_pwd;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new EditLoginPwdPresenter(this);
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        setTitle(getString(R.string.change_login_pwd));
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        User user = MyApplication.getApp().getCurrentUser();
        if (user != null) {
            phone = user.getMobilePhone();
            if (StringUtils.isNotEmpty(phone)) {
                String phoneNumber = phone;
                if (phoneNumber.startsWith("86")) {
                    phoneNumber = phoneNumber.substring(2, phoneNumber.length());
                }
                phoneNumber = StringUtils.addStar(phoneNumber);
                tvNumber.setText(getString(R.string.change_login_pwd_bind_phone_tag) + phoneNumber);
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

    @OnClick({R.id.tvGetCode, R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvConfirm:
                editPwd();
                break;
            case R.id.tvGetCode:
                getCode();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        presenter.getPhoneCode(phone);
    }

    /**
     * 修改登录密码
     */
    private void editPwd() {
        String oldPassword = etOldPwd.getText().toString();
        String newPassword = etNewPwd.getText().toString();
        String repeatePwd = etRepeatPwd.getText().toString();
        String code = etCode.getText().toString();
        if (StringUtils.isEmpty(oldPassword, newPassword, repeatePwd, code)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (!newPassword.equals(repeatePwd)) {
            ToastUtils.showToast(getString(R.string.pwd_diff));
        } else {
            presenter.updateForget(phone, code, oldPassword, newPassword);
        }
    }

    @Override
    public void getPhoneCodeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(getString(R.string.str_code_success));
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
                    presenter.getPhoneCode(phone, checkData, cid);
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void updateForgetSuccess(String obj) {
        ToastUtils.showToast(getString(R.string.change_login_pwd_success_tag));
        presenter.loginOut();
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
        } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCHA_HADBEEN_SEND) {
            ToastUtils.showToast(getResources().getString(R.string.str_no_repeat));
        } else {
            ToastUtils.showToast(httpErrorEntity.getMessage());
        }
    }

    @Override
    public void codeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(getString(R.string.str_code_success));
        timeCount.start();
        tvGetCode.setEnabled(false);
    }

}
