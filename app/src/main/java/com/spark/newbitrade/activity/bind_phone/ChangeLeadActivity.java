package com.spark.newbitrade.activity.bind_phone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改手机号主界面
 */
public class ChangeLeadActivity extends BaseActivity {
    @BindView(R.id.llCanReceive)
    LinearLayout llCanReceive;
    @BindView(R.id.llCannotReceive)
    LinearLayout llCannotReceive;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_change_lead;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.change_bind_phone_num));
        Bundle bundle = getIntent().getExtras();
    }

    @OnClick({R.id.llCanReceive, R.id.llCannotReceive})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llCannotReceive:
                Toast.makeText(this, getString(R.string.unreceive_phone_code_tag), Toast.LENGTH_LONG).show();
                break;
            case R.id.llCanReceive:
                Bundle bundle = getIntent().getExtras();
                bundle.putBoolean("isChg", true);
                showActivity(BindPhoneActivity.class, bundle, 0);
                break;
        }

    }
}
