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
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.widget.TimeCount;


/**
 * 发送手机验证码
 */

public class PhoneVertifyDialog extends Dialog {

    private Context mContext;
    private float mWidthScale;
    private float mHeightScale;

    private ImageView mIvCancel;
    private EditText etCode;
    private TextView tvGetCode;
    private TextView mTvConfirm;

    private TimeCount timeCount;

    public PhoneVertifyDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        setDialogTheme();
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;
        if (mWidthScale > 0) {
            attributes.width = (int) (width * mWidthScale);
        }
        if (mHeightScale > 0) {
            attributes.height = (int) (height * mHeightScale);
        }
        window.setAttributes(attributes);
        window.setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_phone_vertify, null);
        setContentView(view);
        mIvCancel = view.findViewById(R.id.iv_cancel);
        etCode = view.findViewById(R.id.etCode);
        tvGetCode = view.findViewById(R.id.tvGetCode);
        mTvConfirm = view.findViewById(R.id.tv_confirm);
        timeCount = new TimeCount(60000, 1000, tvGetCode);
    }

    public PhoneVertifyDialog withWidthScale(float mWidthScale) {
        this.mWidthScale = mWidthScale;
        return this;
    }

    public PhoneVertifyDialog withHeightScale(float mHeightScale) {
        this.mHeightScale = mHeightScale;
        return this;
    }

    public String getVertifyCode() {
        if (etCode != null) {
            String vertifyCode = String.valueOf(etCode.getText());
            if (vertifyCode != null) {
                return vertifyCode.trim();
            }
        }
        return "";
    }

    /**
     * set dialog theme(设置对话框主题)
     */
    private void setDialogTheme() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
    }

    public interface ClickLister {
        void onCancel();

        void onSendVertifyCode();

        void onConfirm();
    }


    public void setClickListener(final ClickLister listener) {
        if (mIvCancel != null) {
            mIvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCancel();
                    }
                }
            });
        }

        if (tvGetCode != null) {
            tvGetCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onSendVertifyCode();
                    }
                }
            });
        }

        if (mTvConfirm != null) {
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onConfirm();
                    }
                }
            });
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (etCode != null) {
            etCode.setText("");
        }
    }

    public void setStart() {
        if (timeCount != null && tvGetCode != null) {
            timeCount.start();
            tvGetCode.setEnabled(false);
        }
    }



}
