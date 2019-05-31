package com.spark.newbitrade.activity.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 显示手机号码
 */
public class PhoneViewActivity extends BaseActivity {
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    private String phone;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_chg_phone;
    }


    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @OnClick(R.id.tvEdit)
    void edit() {
        showActivity(ChangeLeadActivity.class, getIntent().getExtras(), 0);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.change_phone_num));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
            tvPhone.setText(phone);
        }
    }
}
