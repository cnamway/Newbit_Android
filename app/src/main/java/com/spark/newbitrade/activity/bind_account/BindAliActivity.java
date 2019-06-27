package com.spark.newbitrade.activity.bind_account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.utils.BitmapUtils;
import com.spark.newbitrade.utils.FileUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.utils.UriUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 绑定支付宝/绑定微信
 * Created by Administrator on 2018/5/2 0002.
 */

public class BindAliActivity extends BaseActivity implements BindAliContract.View {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.llQRCode)
    LinearLayout llQRCode;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.ivIdFace)
    ImageView ivIdFace;
    @BindView(R.id.llAli)
    LinearLayout llAli;
    @BindView(R.id.llWeChat)
    LinearLayout llWeChat;
    @BindView(R.id.etWechatAccount)
    EditText etWechatAccount;

    private BindAliPresenterImpl presenter;
    private File imageFile;
    private Uri imageUri;
    private String filename = "Qrcode.jpg";
    private String url = "";
    private PayWaySetting payWaySetting;
    private boolean isUpdate = false;//添加或者更新
    private String payWay;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.TAKE_PHOTO:
                    Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivIdFace.getWidth(), ivIdFace.getHeight());
                    upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bitmap));
                    break;
                case GlobalConstant.CHOOSE_ALBUM:
                    imageUri = data.getData();
                    if (Build.VERSION.SDK_INT >= 19)
                        imageFile = UriUtils.getUriFromKitKat(this, imageUri);
                    else
                        imageFile = UriUtils.getUriBeforeKitKat(this, imageUri);
                    if (imageFile == null) {
                        ToastUtils.showToast(getString(R.string.library_file_exception));
                        return;
                    }
                    Bitmap bm = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivIdFace.getWidth(), ivIdFace.getHeight());
                    upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bm));
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_ali;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        if (MyApplication.getApp().getCurrentUser() != null)
            etName.setText(MyApplication.getApp().getCurrentUser().getRealName());
    }

    @OnClick({R.id.llQRCode, R.id.tvConfirm})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llQRCode:
                actionSheetDialogNoTitle();
                break;
            case R.id.tvConfirm:
                confirm();
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BindAliPresenterImpl(this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWay = bundle.getString("payWay");
            payWaySetting = (PayWaySetting) bundle.getSerializable("data");
            if (payWaySetting != null) {
                isUpdate = true;
                tvConfirm.setText(getResources().getString(R.string.str_text_change));
                if (GlobalConstant.alipay.equals(payWay)) {
                    etAccount.setText(payWaySetting.getPayAddress());
                } else if (GlobalConstant.wechat.equals(payWay)) {
                    etWechatAccount.setText(payWaySetting.getPayAddress());
                }
                url = payWaySetting.getQrCodeUrl().toString();
                Glide.with(this).load(url).into(ivIdFace);
                etName.setText(payWaySetting.getRealName());
            }
        }
        if (isUpdate) {
            if (GlobalConstant.alipay.equals(payWay)) {
                tvTitle.setText(getString(R.string.text_change) + getString(R.string.ali_account));
                llAli.setVisibility(View.VISIBLE);
                llWeChat.setVisibility(View.GONE);
            } else if (GlobalConstant.wechat.equals(payWay)) {
                tvTitle.setText(getString(R.string.text_change) + getString(R.string.str_payway_wechat));
                llAli.setVisibility(View.GONE);
                llWeChat.setVisibility(View.VISIBLE);
            }
        } else {
            if (GlobalConstant.alipay.equals(payWay)) {
                tvTitle.setText(getString(R.string.binding) + getString(R.string.ali_account));
                llAli.setVisibility(View.VISIBLE);
                llWeChat.setVisibility(View.GONE);
            } else if (GlobalConstant.wechat.equals(payWay)) {
                tvTitle.setText(getString(R.string.binding) + getString(R.string.str_payway_wechat));
                llAli.setVisibility(View.GONE);
                llWeChat.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 上传base64位头像
     *
     * @param s
     */
    private void upLoadBase64(String s) {
        presenter.uploadBase64Pic(s);
    }

    private void confirm() {
        String account = "";
        if (GlobalConstant.alipay.equals(payWay)) {
            account = etAccount.getText().toString().trim();
        } else {
            account = etWechatAccount.getText().toString().trim();
        }
        String name = etName.getText().toString();
        String pwd = etNewPwd.getText().toString();
        if (StringUtils.isEmpty(name, account, pwd, url)) {
            ToastUtils.showToast(this, R.string.incomplete_information);
        } else {
            if (isUpdate) {
                if (GlobalConstant.alipay.equals(payWay)) {
                    presenter.getUpdateAliOrWechat(payWaySetting.getId(), GlobalConstant.alipay, account, getString(R.string.str_payway_ali), "", pwd, url, name);
                } else {
                    presenter.getUpdateAliOrWechat(payWaySetting.getId(), GlobalConstant.wechat, account, getString(R.string.str_payway_wechat), "", pwd, url, name);
                }
            } else {
                if (GlobalConstant.alipay.equals(payWay)) {
                    presenter.getBindAliOrWechat(GlobalConstant.alipay, account, getString(R.string.str_payway_ali), "", pwd, url, name);
                } else {
                    presenter.getBindAliOrWechat(GlobalConstant.wechat, account, getString(R.string.str_payway_wechat), "", pwd, url, name);
                }
            }
        }
    }

    @Override
    public void uploadBase64PicSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            url = obj;
            Glide.with(this).load(obj).into(ivIdFace);
            ToastUtils.showToast(getString(R.string.upload_success));
        } else {
            ToastUtils.showToast(getString(R.string.empty_address));
        }
    }

    @Override
    public void doBindAliOrWechatSuccess(MessageResult obj) {
        ToastUtils.showToast(this, obj.getMessage());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void doUpdateAliOrWechatSuccess(MessageResult obj) {
        ToastUtils.showToast(this, obj.getMessage());
        setResult(RESULT_OK);
        finish();
    }

    private void actionSheetDialogNoTitle() {
        final String[] stringItems = {getString(R.string.photograph), getString(R.string.album)};
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.btn_normal))
                .cancelText(getResources().getString(R.string.dialog_one_cancel)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkPermission(position);
                dialog.dismiss();
            }
        });
    }

    private void checkPermission(final int position) {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission" + data.get(0));
                        if (position == 0) {
                            startCamera();
                        } else {
                            chooseFromAlbum();
                        }
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
     * 拍照
     */
    private void startCamera() {
        if (imageFile == null) {
            ToastUtils.showToast(getString(R.string.unknown_error));
            return;
        }
        imageUri = FileUtils.getUriForFile(this, imageFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, GlobalConstant.TAKE_PHOTO);
    }

    /**
     * 相册
     */
    private void chooseFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GlobalConstant.CHOOSE_ALBUM);
    }


}
