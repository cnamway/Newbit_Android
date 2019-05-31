package com.spark.newbitrade.activity.wallet_coin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private String unit;
    //    private String phone = "";
//    private AddAddressDialog dialog;
    private AddAddresssPresenterImpl presenter;

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        setTitle(getString(R.string.str_add_address));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new AddAddresssPresenterImpl(this);
        unit = getIntent().getStringExtra("unit");
        tvCoinName.setText(unit);
    }

    @Override
    protected void loadData() {
        super.loadData();

    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_add_address;
    }

    @OnClick(R.id.tvSubmit)
    public void onViewClicked() {
        if (!StringUtils.isEmpty(unit)) {
//            dialog.show();

            String remark = etRemark.getText().toString();
            String address = etAddress.getText().toString();
            if (!StringUtils.isEmpty(address)) {
                presenter.addWalletWithdrawAddressUsingPOST(address, unit, remark);
            } else {
                ToastUtils.showToast(R.string.incomplete_information);
            }
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


//    @Override
//    public void safeSettingSuccess(SafeSetting obj) {
//        if (obj.isPhoneVerified()) {
//            phone = obj.getMobilePhone();
//            dialog = new AddAddressDialog(AddAddressActivity.this, phone);
//            tvGetCode = dialog.getTvGetCode();
//            tvOption = dialog.getTvOption();
//            etCode = dialog.getEtCode();
//            timeCount = new TimeCount(60000, 1000, tvGetCode);
//            setDialogListener();
//        } else {
//            ToastUtils.showLong(R.string.binding_phone_first);
//        }
//    }

//    private void setDialogListener() {
//        tvGetCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String, String> map = new HashMap<>();
//                map.put("type", "6");
////                presenter.sendEditLoginPwdCode(map);
//            }
//        });
//        tvOption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                String remark = etRemark.getText().toString();
//                String address = etAddress.getText().toString();
//                String code = etCode.getText().toString();
//                if (!StringUtils.isEmpty(address, code)) {
//                    HashMap<String, String> map = new HashMap();
//                    map.put("address", address);
//                    map.put("coinId", unit);
//                    map.put("code", code);
//                    map.put("verifyType", "0");//0 手机   1邮箱
//                    map.put("remark", remark);
////                    presenter.addAddress(map);
//                }
//            }
//        });
//    }

//    @Override
//    public void addAddressSuccess(String obj) {
//        ToastUtils.showLong(obj);
//        Intent intent = new Intent();
//        intent.putExtra("unit", unit);
//        AddAddressActivity.this.setResult(RESULT_OK, intent);
//        finish();
//    }
//
//    @Override
//    public void sendEditLoginPwdCodeSuccess(String obj) {
//        ToastUtils.showToast(obj);
//        timeCount.start();
//        tvGetCode.setEnabled(false);
//    }


}
