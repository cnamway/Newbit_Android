package com.spark.newbitrade.activity.safe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SafeActivity extends BaseActivity {
    @BindView(R.id.llLockSet)
    LinearLayout llLockSet;
    @BindView(R.id.switchButton)
    SwitchButton switchButton;
    @BindView(R.id.llGoogleView)
    LinearLayout llGoogleView;
    private int googleStatus;
    @BindView(R.id.tvGoogle)
    TextView tvGoogle;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SafeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SetLockActivity.RETURN_SET_LOCK:
                switchButton.setOnCheckedChangeListener(null);
                String password = SharedPreferenceInstance.getInstance().getLockPwd();
                switchButton.setChecked(!StringUtils.isEmpty(password));
                switchButton.setOnCheckedChangeListener(listener);
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    if (googleStatus == 0) {
                        googleStatus = 1;
                    } else {
                        googleStatus = 0;
                    }
                    SharedPreferenceInstance.getInstance().saveGoogleSatus(googleStatus);
                    tvGoogle.setText(googleStatus == 0 ? R.string.unbound : R.string.bound);
                    tvGoogle.setEnabled(googleStatus == 0);
                }
                break;
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_safe;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_safe));
        if (GlobalConstant.isOPenGoogle)
            llGoogleView.setVisibility(View.VISIBLE);
        googleStatus = SharedPreferenceInstance.getInstance().getLanguageCode();
        tvGoogle.setText(googleStatus == 0 ? R.string.unbound : R.string.bound);
        tvGoogle.setEnabled(googleStatus == 0);
        switchButton.setChecked(!StringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd()));
    }

    @Override
    protected void setListener() {
        super.setListener();
        switchButton.setOnCheckedChangeListener(listener);
    }

    @OnClick(R.id.llGoogle)
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llGoogle:
                if (googleStatus == 0) {
                    showActivity(GoogleAuthorActivity.class, null, 1);
                } else {
                    showActivity(GoogleUnbindActivity.class, null, 1);
                }
                break;
        }
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", isChecked ? 0 : 1);
            showActivity(SetLockActivity.class, bundle, 0);
        }
    };
}
