package com.spark.newbitrade.ui;

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
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.entity.VisionEntity;


/**
 * APP版本更新
 */
public class AppVersionDialog extends Dialog {
    private Context context;
    private TextView leftView;
    private TextView rightView;
    private TextView tvVersionNo;
    private TextView tvVersionInfo;
    private VisionEntity visionEntity;

    public AppVersionDialog(@NonNull Context context, VisionEntity visionEntity) {
        super(context);
        this.context = context;
        this.visionEntity = visionEntity;
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
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        int heightMax = context.getResources().getDisplayMetrics().heightPixels;
//        int widthMax = context.getResources().getDisplayMetrics().widthPixels;
//        lp.width = (int) (widthMax * 1);//宽度比例0~1
//        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_version, null);
        setContentView(view);
        tvVersionNo = view.findViewById(R.id.tvVersionNo);
        tvVersionInfo = view.findViewById(R.id.tvVersionInfo);
        leftView = view.findViewById(R.id.tvLeft);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rightView = view.findViewById(R.id.tvRight);

        tvVersionNo.setText(context.getString(R.string.str_my_version_no) + visionEntity.getData().getVersion());
        tvVersionInfo.setText(visionEntity.getData().getUpdateRemark());
    }

    public void setPositiveOnclickListener(View.OnClickListener onclickListener) {
        rightView.setOnClickListener(onclickListener);
    }

}
