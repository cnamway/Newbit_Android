package com.spark.newbitrade.activity.wallet_coin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.library.ac.model.MemberWallet;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.ExtractInfo;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.utils.BitmapUtils;
import com.spark.newbitrade.utils.CommonUtils;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充币
 */
public class RechargeActivity extends BaseActivity implements RechargeContract.WalletView {
    @BindView(R.id.ivAddress)
    ImageView ivAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    private Wallet coin;
    private Bitmap saveBitmap;
    private String address;

    private RechargePresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();

        presenter = new RechargePresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            coin = (Wallet) bundle.getSerializable("coin");
            if (coin != null) {
                tvTitle.setText(coin.getCoinId() + getString(R.string.charge_money));
                tvNotice.setText(getString(R.string.risk_warning) + coin.getCoinId() + getString(R.string.assets));
            } else {
                tvAddress.setText(getString(R.string.no_address));
            }
        }
    }

    @OnClick({R.id.tvCopy, R.id.tvAlbum})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCopy:
                if (StringUtils.isNotEmpty(address)) {
                    copy();
                } else {
                    ToastUtils.showToast(getString(R.string.no_address));
                }
                break;
            case R.id.tvAlbum:
                if (StringUtils.isNotEmpty(address)) {
                    checkPermission();
                } else {
                    ToastUtils.showToast(getString(R.string.no_address));
                }
                break;
        }
    }


    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        save();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(activity, data)) {
                            AndPermission.permissionSetting(activity).execute();
                            return;
                        }
                        ToastUtils.showToast(getString(R.string.str_no_permission));
                    }
                }).start();
    }

    /**
     * 保存到相册
     */
    private void save() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/digiccy/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (saveBitmap != null) try {
            BitmapUtils.saveBitmapToFile(saveBitmap, file, 100);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (file != null && file.exists()) {
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtils.showToast(getString(R.string.savesuccess));
        }
    }

    /**
     * 复制
     */
    private void copy() {
        CommonUtils.copyText(this, tvAddress.getText().toString());
        ToastUtils.showToast(R.string.copy_success);
    }


    @Override
    protected void loadData() {
        if (coin != null) {
            presenter.getAddress(coin.getCoinId());
            presenter.getExtractInfo(coin.getCoinId());
        }
    }

    public static Bitmap createQRCode(String text, int size) {
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 2);   //设置白边大小 取值为 0- 4 越大白边越大
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints);
            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * size + x] = 0xff000000;
                    } else {
                        pixels[y * size + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void getAddressSuccess(final MemberWallet obj) {
        if (obj == null) return;
        if (StringUtils.isNotEmpty(obj.getAddress())) {
            address = obj.getAddress();
            tvAddress.setText(obj.getAddress());

            ivAddress.post(new Runnable() {
                @Override
                public void run() {
                    saveBitmap = createQRCode(obj.getAddress(), Math.min(ivAddress.getWidth(), ivAddress.getHeight()));
                    ivAddress.setImageBitmap(saveBitmap);
                }
            });
        } else {
            tvAddress.setText(getString(R.string.no_address));
        }
    }

    @Override
    public void getExtractInfoSuccess(List<ExtractInfo> list) {
        if (list != null && list.size() > 0) {
            HashMap<String, ExtractInfo> map = new HashMap<>();
            for (ExtractInfo extractInfo : list) {
                map.put(extractInfo.getCoinName(), extractInfo);
            }
            if (map != null) {
                ExtractInfo extractInfo = map.get(coin.getCoinId());
                if (extractInfo != null && extractInfo.getMinDepositAmount() != null) {
                    tvNotice.setText(getString(R.string.risk_warning) + coin.getCoinId() + getString(R.string.assets) + "，最小提币金额" + MathUtils.subZeroAndDot(extractInfo.getMinDepositAmount().toString()) + " " + coin.getCoinId());
                }
            }
        }
    }
}
