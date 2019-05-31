package com.spark.newbitrade.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.CommonUtils;


public class SkipExtractTipDialog extends Dialog {
    private Context context;
    private TextView tvSure;
    private TextView tvCancel;

    public SkipExtractTipDialog(Context context) {
        super(context, R.style.myDialog);
        this.context = context;
        initView();
        setListener();
    }

    private void initView() {
        setContentView(R.layout.dialog_extract_tip);
        tvSure = findViewById(R.id.tvConfirm);
        tvCancel = findViewById(R.id.tvCancel);
    }

    private void setListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = CommonUtils.getScreenWidth();
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    public void onPositiveClickLisenter(View.OnClickListener onClickListener) {
        tvSure.setOnClickListener(onClickListener);
    }

}
