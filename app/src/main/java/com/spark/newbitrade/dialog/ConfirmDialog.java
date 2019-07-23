package com.spark.newbitrade.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.StringUtils;


/**
 * 确认、取消对话框
 */

public class ConfirmDialog extends Dialog {
    private Context context;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView leftView;
    private TextView rightView;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        setDialogTheme();
        initView();
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int heightMax = context.getResources().getDisplayMetrics().heightPixels;
        int widthMax = context.getResources().getDisplayMetrics().widthPixels;
        lp.width = (int) (widthMax * 0.8);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
        setContentView(view);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvContent = view.findViewById(R.id.tvContent);
        leftView = view.findViewById(R.id.tvLeft);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rightView = view.findViewById(R.id.tvRight);
    }

    public void setPositiveOnclickListener(View.OnClickListener onclickListener) {
        rightView.setOnClickListener(onclickListener);
    }

    public void setData(String title, String content, String left, String right) {
        if (StringUtils.isNotEmpty(title)) tvTitle.setText(title);
        if (StringUtils.isNotEmpty(content)) tvContent.setText(content);
        if (StringUtils.isNotEmpty(left)) leftView.setText(left);
        if (StringUtils.isNotEmpty(right)) rightView.setText(right);
    }
}
