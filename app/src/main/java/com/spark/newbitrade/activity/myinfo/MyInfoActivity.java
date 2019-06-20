package com.spark.newbitrade.activity.myinfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.account_pwd.AccountPwdActivity;
import com.spark.newbitrade.activity.credit.CreditActivity;
import com.spark.newbitrade.activity.forgot_pwd.ForgotPwdActivity;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.activity.my_account.MyAccountActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.ui.CircleImageView;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyInfoActivity extends BaseActivity implements MyInfoContract.MyInfoView {
    @BindView(R.id.llAccountPwd)
    LinearLayout llAccountPwd;
    @BindView(R.id.ivHeader)
    CircleImageView ivHeader;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    //    @BindView(R.id.tvEmail)
//    TextView tvEmail;
    @BindView(R.id.tvLoginPwd)
    TextView tvLoginPwd;
    @BindView(R.id.tvAcountPwd)
    TextView tvAcountPwd;
    @BindView(R.id.tvIdCard)
    TextView tvIdCard;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    //    @BindView(R.id.llEmail)
//    LinearLayout llEmail;
    @BindView(R.id.llLoginPwd)
    LinearLayout llLoginPwd;
    @BindView(R.id.llIdCard)
    LinearLayout llIdCard;
    //    @BindView(R.id.etAccount)
//    TextView tvAccount;
    //    @BindView(R.id.llAccount)
//    LinearLayout llAccount;
    private File imageFile;
    private String filename = "header.jpg";
    private Uri imageUri;
    private String url;
    private MyInfoPresenterImpl myInfoPresenter;
    private User curUser;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.TAKE_PHOTO:
                    takePhotoReturn(resultCode, data);
                    break;
                case GlobalConstant.CHOOSE_ALBUM:
                    choseAlbumReturn(resultCode, data);
                    break;
                case 0: // 重新请求设置参数
                    myInfoPresenter.getUserInfo();
                    break;
                case 1: // 修改了登录密码
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    /**
     * 相机
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

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.account_settings));
        myInfoPresenter = new MyInfoPresenterImpl(this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
    }

    @Override
    protected void loadData() {
        myInfoPresenter.getUserInfo();
    }

    @OnClick({R.id.ivHeader, R.id.llPhone, R.id.llLoginPwd, R.id.llIdCard, R.id.llAccountPwd, R.id.llAccount})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);

        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast(getString(R.string.text_login_first));
            showActivity(LoginActivity.class, null);
            return;
        }

        Bundle bundle;
        switch (v.getId()) {
            case R.id.ivHeader:
                actionSheetDialogNoTitle();
                break;
            case R.id.llPhone:
//                if (!safeSetting.isPhoneVerified()) {
//                    showActivity(BindPhoneActivity.class, null, 0);
//                } else {
//                    bundle.putString("phone", safeSetting.getMobilePhone());
//                    showActivity(PhoneViewActivity.class, bundle, 0);
//                }
                break;
            case R.id.llAccount:
                //收款账户
                if (curUser != null && curUser.getFundsVerified() == 1 && curUser.getRealNameStatus() == 3) {
                    showActivity(MyAccountActivity.class, null);
                } else {
                    ToastUtils.showToast(getString(R.string.password_realname));
                }
                break;
            case R.id.llLoginPwd:
                //登录密码
                showActivity(ForgotPwdActivity.class, null);
                break;
            case R.id.llIdCard:
                //身份验证
                if (curUser != null) {
                    bundle = new Bundle();
                    bundle.putInt("NoticeType", curUser.getRealNameStatus());
                    showActivity(CreditActivity.class, bundle, 0);
                }
                break;
            case R.id.llAccountPwd:
                // 设置资金密码
                bundle = new Bundle();
                int fundsVerified = MyApplication.getApp().getCurrentUser().getFundsVerified();
                bundle.putBoolean("isSet", fundsVerified == 1 ? true : false);
                showActivity(AccountPwdActivity.class, bundle, 0);
                break;
        }
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
     * 相册选取返回
     *
     * @param resultCode
     * @param data
     */
    private void choseAlbumReturn(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        imageUri = data.getData();
        if (Build.VERSION.SDK_INT >= 19)
            imageFile = UriUtils.getUriFromKitKat(this, imageUri);
        else
            imageFile = UriUtils.getUriBeforeKitKat(this, imageUri);
        if (imageFile == null) {
            ToastUtils.showToast(getString(R.string.library_file_exception));
            return;
        }
        Bitmap bm = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivHeader.getWidth(), ivHeader.getHeight());
//        if (GlobalConstant.isUpLoadFile) {
//            upLoadImageFile(bm);
//        } else {
        upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bm));
//        }
    }

    /**
     * 拍照返回
     *
     * @param resultCode
     * @param data
     */
    private void takePhotoReturn(int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), ivHeader.getWidth(), ivHeader.getHeight());
        upLoadBase64("data:image/jpeg;base64," + BitmapUtils.imgToBase64(bitmap));
    }

    /**
     * 上传base64位头像
     *
     * @param s
     */
    private void upLoadBase64(String s) {
        myInfoPresenter.uploadBase64Pic(s);
    }


    @Override
    public void uploadBase64PicSuccess(String obj) {
        url = obj;
        myInfoPresenter.avatar(obj);
    }

    @Override
    public void avatarSuccess(String obj) {
        MyApplication.getApp().getCurrentUser().setAvatar(url);
        MyApplication.getApp().saveCurrentUser();
        Glide.with(this).load(url).into(ivHeader);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void getUserInfoSuccess(User user) {
        if (user != null) {
            curUser = user;
            fillViews();
        }
    }

    /**
     * 根据安全参数显示
     */
    private void fillViews() {
        if (curUser != null) {
            Glide.with(getApplicationContext()).load(StringUtils.isEmpty(curUser.getAvatar()) ? R.mipmap.icon_avatar : curUser.getAvatar()).into(ivHeader);

            if (StringUtils.isNotEmpty(curUser.getMobilePhone())) {
                tvPhone.setText(curUser.getMobilePhone());
            }

            tvAcountPwd.setText(curUser.getFundsVerified() == 1 ? R.string.had_set : R.string.not_set);
            tvLoginPwd.setText(R.string.had_set);

            if (curUser.getRealNameStatus() == 0) {  // 未审核
                tvIdCard.setText(R.string.unverified);
            } else if (curUser.getRealNameStatus() == 1) { // 审核中
                tvIdCard.setText(R.string.creditting);
            } else if (curUser.getRealNameStatus() == 2) { // 审核失败
                tvIdCard.setText(R.string.creditfail);
            } else { // 已认证
                tvIdCard.setText(R.string.verification);
            }
        }
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        if (MyApplication.getApp().isLogin()) {
            myInfoPresenter.getUserInfo();
        }
    }
}
