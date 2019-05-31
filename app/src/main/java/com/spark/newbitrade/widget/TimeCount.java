package com.spark.newbitrade.widget;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.MyApplication;


public class TimeCount extends CountDownTimer {
    private TextView codeView;

    public TimeCount(long millisInFuture, long countDownInterval, TextView codeView) {
        super(millisInFuture, countDownInterval);
        this.codeView = codeView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //codeView.setBackgroundResource(R.drawable.shape_bg_send_code_pressed);
        codeView.setEnabled(false);
        codeView.setText("(" + millisUntilFinished / 1000 + ") " + MyApplication.getApp().getResources().getString(R.string.resend_code));
        codeView.setTextColor(MyApplication.getApp().getResources().getColor(R.color.grey_a5a5a5));
    }

    @Override
    public void onFinish() {
        codeView.setText(MyApplication.getApp().getResources().getString(R.string.send_code));
        codeView.setEnabled(true);
        //codeView.setBackgroundResource(R.drawable.shape_bg_send_code_normal);
        codeView.setTextColor(MyApplication.getApp().getResources().getColor(R.color.btn_normal));
    }
}