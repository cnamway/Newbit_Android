package com.spark.newbitrade.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;

/**
 * Created by Administrator on 2018/10/11 0011.
 */

public class AddAddressDialog extends Dialog {

    private Context context;
    private TextView tvPhone;
    private TextView tvGetCode;
    private TextView tvOption;
    private TextView tvCancle;
    private EditText etCode;
    private String phone;

    public AddAddressDialog(@NonNull Context context,String phone) {
        super(context, R.style.myDialog);
        this.context = context;
        this.phone = phone;
        initViews();
        setListener();
    }

    private void setListener() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initViews() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_address_view, null);
        setContentView(view);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvGetCode = view.findViewById(R.id.tvGetCode);
        tvOption = view.findViewById(R.id.tvOption);
        tvCancle = view.findViewById(R.id.tvCancle);
        etCode = view.findViewById(R.id.etCode);
        tvPhone.setText(phone.substring(0, 3) + "****" + phone.substring(7));
    }

    public TextView getTvGetCode() {
        return tvGetCode;
    }

    public TextView getTvOption() {
        return tvOption;
    }

    public EditText getEtCode() {
        return etCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (MyApplication.getApp().getmWidth());
        window.setAttributes(lp);
    }

    public interface AddListener{
        void getInfomation(String code);
    }

}
