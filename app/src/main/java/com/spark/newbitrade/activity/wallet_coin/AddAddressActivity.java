package com.spark.newbitrade.activity.wallet_coin;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Captcha;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH;
import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH2;

/**
 * Created by Administrator on 2018/10/11 0011.
 */

public class AddAddressActivity extends BaseActivity implements AddAddressContract.View {
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;

    private String unit;
    private AddAddresssPresenterImpl presenter;

    private TimeCount timeCount;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;
    private String phone;

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        setTitle(getString(R.string.str_add_address));
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
    protected void initData() {
        super.initData();
        presenter = new AddAddresssPresenterImpl(this);
        unit = getIntent().getStringExtra("unit");
        tvCoinName.setText(unit);

        timeCount = new TimeCount(60000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        User user = MyApplication.getApp().getCurrentUser();
        if (user != null) {
            phone = user.getMobilePhone();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();

    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_add_address;
    }

    @OnClick({R.id.tvSubmit, R.id.tvGetCode})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSubmit://确定
                if (!StringUtils.isEmpty(unit)) {
                    String remark = etRemark.getText().toString();
                    String address = etAddress.getText().toString().replaceAll(" ", "");
                    String code = StringUtils.getText(etCode);
                    if (StringUtils.isEmpty(address)) {
                        ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.mention_money_address));
                    } else if (StringUtils.isEmpty(code)) {
                        ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.code));
                    } else {
                        presenter.addWalletWithdrawAddressUsingPOST(address, unit, remark, code, phone);
                    }
                }
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
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showToast(R.string.phone_not_correct);
        } else {
            presenter.getPhoneCode(phone);
        }
    }


    @Override
    public void addWalletWithdrawAddressUsingPOSTSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj))
            ToastUtils.showToast(obj);

        Intent intent = new Intent();
        intent.putExtra("unit", unit);
        setResult(RESULT_OK, intent);
        finish();
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
        gt3GeetestUtils.getGeetest(activity, HttpUrls.getGeetest(), null, null, new GT3GeetestBindListener() {
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
        timeCount.start();
        tvGetCode.setEnabled(false);
    }
}
