package com.spark.newbitrade.activity.account_pwd;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;

/**
 * 重置资金密码
 * Created by Administrator on 2018/8/16 0016.
 */

public class ResetAccountActivity extends BaseActivity implements AccountPwdContract.ResetView {
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.etRepeatPwd)
    EditText etRepeatPwd;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    private TimeCount timeCount;
    private AccountPwdContract.ResetPresenter presenter;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_reset_account_pwd;
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
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        setTitle(getString(R.string.change_money_pwd));
        new ResetAccountPwdPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @OnClick({R.id.tvGetCode, R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvConfirm:
                resetPwd();
                break;
            case R.id.tvGetCode:
                HashMap<String, String> map = new HashMap<>();
                map.put("type", "5");
                presenter.resetAccountPwdCode(map);
                break;
        }
    }

    /**
     * 重置密码
     */
    private void resetPwd() {
        String newPassword = etNewPwd.getText().toString();
        String repeatePwd = etRepeatPwd.getText().toString();
        String code = etCode.getText().toString();
        if (StringUtils.isEmpty(newPassword, repeatePwd, code)) {
            ToastUtils.showToast(getString(R.string.incomplete_information));
        } else if (!newPassword.equals(repeatePwd)) {
            ToastUtils.showToast(getString(R.string.pwd_diff));
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("newPassword", newPassword);
            map.put("code", code);
            presenter.resetAccountPwd(map);
        }
    }

    @Override
    public void setPresenter(AccountPwdContract.ResetPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void resetAccountPwdSuccess(String obj) {
        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {

    }

    @Override
    public void resetAccountPwdCodeSuccess(String obj) {
        ToastUtils.showToast(obj);
        timeCount.start();
        tvGetCode.setEnabled(false);
    }
}
