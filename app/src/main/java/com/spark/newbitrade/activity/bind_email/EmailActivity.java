package com.spark.newbitrade.activity.bind_email;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 邮箱显示
 */
public class EmailActivity extends BaseActivity {
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
    private String email;

    public static void actionStart(Context context, String email) {
        Intent intent = new Intent(context, EmailActivity.class);
        intent.putExtra("email", email);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_email;
    }

    @OnClick(R.id.tvEdit)
    void edit() {
//        showActivity(EmailChgActivity.class, getIntent().getExtras(), 0);
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.binding_email));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
            tvEmail.setText(email);
        }
    }

}
