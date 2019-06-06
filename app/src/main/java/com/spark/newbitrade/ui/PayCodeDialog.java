package com.spark.newbitrade.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.newbitrade.R;
import com.spark.newbitrade.utils.BitmapUtils;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 付款二维码
 */
public class PayCodeDialog extends Dialog {
    private Context context;
    private ImageView ivClose;
    private ImageView ivCode;
    private ImageView tvCode;
    private TextView tvCode2;
    private Bitmap bitmapCode;
    private String codeUrl;

    public PayCodeDialog(@NonNull Context context) {
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
        lp.width = (int) (widthMax * 0.9);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    /**
     * 初始化view
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_pay_code, null);
        setContentView(view);
        ivClose = view.findViewById(R.id.ivClose);
        ivCode = view.findViewById(R.id.ivCode);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ivCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkPermission();
                return false;
            }
        });
        tvCode = view.findViewById(R.id.tvCode);
        tvCode2 = view.findViewById(R.id.tvCode2);
    }

    public void setImg(String qrCodeUrl, long randomTime) {
        this.codeUrl = qrCodeUrl;
        Glide.with(context).load(codeUrl).into(ivCode);
        bitmapCode = returnBitmap(codeUrl);
        if (randomTime > 0) {
            ivCode.setVisibility(View.VISIBLE);
            tvCode.setVisibility(View.GONE);
            tvCode2.setVisibility(View.GONE);
        } else {
            ivCode.setVisibility(View.GONE);
            tvCode.setVisibility(View.VISIBLE);
            tvCode2.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 检查是否具有权限
     */
    private void checkPermission() {
        AndPermission.with(context)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        save();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(context, data)) {
                            AndPermission.permissionSetting(context).execute();
                            return;
                        }
                        ToastUtils.showToast(context, context.getString(R.string.str_no_permission));
                    }
                }).start();
    }


    /**
     * 保存到相册
     */
    private void save() {
        String time = DateUtils.getFormatTime(DateUtils.DATE_FORMAT_1, new Date());
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + "/" + time + ".jpg");
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            if (bitmapCode != null) {
                BitmapUtils.saveBitmapToFile2(bitmapCode, file, 100);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtils.showToast(context, context.getString(R.string.str_save_success));
        }
    }

    private Bitmap returnBitmap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmapCode = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmapCode;
    }

}
