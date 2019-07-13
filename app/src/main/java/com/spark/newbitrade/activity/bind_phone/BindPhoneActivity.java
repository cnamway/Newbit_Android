package com.spark.newbitrade.activity.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.country.CountryActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.Injection;

/**
 * 绑定手机号和修改手机号界面
 */
public class BindPhoneActivity extends BaseActivity implements BindPhoneContract.View {
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.tvBind)
    TextView tvBind;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.llChgPhone)
    LinearLayout llChgPhone;
    @BindView(R.id.tvChgAreaCode)
    TextView tvChgAreaCode;
    @BindView(R.id.etChgPhone)
    EditText etChgPhone;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.etNewCode)
    EditText etNewCode;
    @BindView(R.id.tvNewGetCode)
    TextView tvNewGetCode;
    @BindView(R.id.llNewCode)
    LinearLayout llNewCode;
    private Country country;
    private BindPhoneContract.Presenter presenter;
    private TimeCount timeCount;
    private TimeCount newTimeCount;
    private String strAreaCode = "86";
    private boolean isChg;
    private String phone;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CountryActivity.RETURN_COUNTRY:
                if (data == null) return;
                country = (Country) data.getSerializableExtra("country");
                if (SharedPreferenceInstance.getInstance().getLanguageCode() == 1) {
                    tvCountry.setText(country.getZhName());
                } else if (SharedPreferenceInstance.getInstance().getLanguageCode() == 2) {
                    tvCountry.setText(country.getEnName());
                }
                strAreaCode = country.getAreaCode();
                if (isChg)
                    tvChgAreaCode.setText("+" + strAreaCode + ">");
                else
                    tvAreaCode.setText("+" + strAreaCode + ">");
                break;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.binding_phone));
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        newTimeCount = new TimeCount(60000, 1000, tvNewGetCode);
        new BindPhonePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            isChg = bundle.getBoolean("isChg");
            if (isChg) {
                if (StringUtils.isMobile(phone)) {
                    tvPhone.setText(getString(R.string.handset_tail_number) + phone.substring(7, 11) + getString(R.string.receive_message_code));
                } else {
                    tvPhone.setText(getString(R.string.handset_tail_number) + phone + getString(R.string.receive_message_code));
                }
                tvPhone.setVisibility(View.VISIBLE);
                llChgPhone.setVisibility(View.VISIBLE);
                llNewCode.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.GONE);
                tvBind.setText(getString(R.string.change_bind_phone_num));
                setTitle(getString(R.string.change_phone_num));
            }

        }
    }

    @OnClick({R.id.tvCountry, R.id.tvGetCode, R.id.tvBind, R.id.tvNewGetCode})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCountry:
                showActivity(CountryActivity.class, null, CountryActivity.RETURN_COUNTRY);
                break;
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.tvBind:
                bindOrChgPhone();
                break;
            case R.id.tvNewGetCode:
                getNewCode();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        if (isChg) {
            HashMap<String, String> map = new HashMap<>();
            map.put("type", "4");
            presenter.sendChangePhoneCode(map);
        } else {
            String phone = etPhone.getText().toString().trim();
            if (StringUtils.isEmpty(phone) || !StringUtils.isMobile(phone)) {
                ToastUtils.showToast(R.string.phone_not_correct);
            } else {
                HashMap<String, String> map = new HashMap<>();
                map.put("code", strAreaCode);
                map.put("phone", phone);
                map.put("type", "3");
                presenter.sendCode(map,1);
            }
        }
    }

    private void getNewCode(){
        String phone = "";
        phone = etChgPhone.getText().toString();
        if (StringUtils.isEmpty(phone) || !StringUtils.isMobile(phone)) {
            ToastUtils.showToast(R.string.phone_not_correct);
        }else {
            // 发送新的验证码
            HashMap<String, String> map = new HashMap<>();
            map.put("code", strAreaCode);
            map.put("phone", phone);
            map.put("type", "4");
            presenter.sendCode(map,2);
        }
    }

    /**
     * 绑定手机号/修改手机号
     */
    private void bindOrChgPhone() {
        String phone = "";
        if (isChg)
            phone = etChgPhone.getText().toString();
        else
            phone = etPhone.getText().toString();
        String password = etPwd.getText().toString();
        String code = etCode.getText().toString();
        String newCode = etNewCode.getText().toString();
        if (StringUtils.isEmpty(password, phone, code)) {
            ToastUtils.showToast(R.string.incomplete_information);
        } else if (!StringUtils.isMobile(phone)) {
            ToastUtils.showToast(R.string.phone_not_correct);
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", phone);
            map.put("password", password);
            if (isChg) {
                if (StringUtils.isEmpty(newCode)) {
                    ToastUtils.showToast(R.string.incomplete_information);
                } else {
                    map.put("oldCode", code);
                    map.put("newCode", newCode);
                    presenter.changePhone(map);
                }
            } else {
                map.put("code", code);
                presenter.bindPhone(map);
            }
        }
    }

    @Override
    public void setPresenter(BindPhoneContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void bindPhoneSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void sendChangePhoneCodeSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        timeCount.start();
        tvGetCode.setEnabled(false);
    }

    @Override
    public void changePhoneSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void sendCodeSuccess(String obj,int type) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        if(type == 2) { // 这个是发送新验证码的返回
            newTimeCount.start();
            tvNewGetCode.setEnabled(false);
        }else {
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

}
