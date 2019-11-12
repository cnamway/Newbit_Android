package com.spark.newbitrade.activity.appeal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.library.otc.model.AppealApplyInTransitDto;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.factory.HttpUrls;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申诉
 */
public class AppealActivity extends BaseActivity implements AppealContract.View {
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.ivImg1)
    ImageView ivImg1;
    @BindView(R.id.ivImg2)
    ImageView ivImg2;
    @BindView(R.id.ivImg3)
    ImageView ivImg3;
    private String orderSn;
    private AppealPresenterImpl presenter;
    private NormalListDialog normalDialog;
    private String[] appealTypes;
    public static final int FACE = 1;
    public static final int BACK = 2;
    public static final int HOLD = 3;
    private int type;
    private ImageView currentImg;
    private File imageFile;
    private String filename = "idCard.jpg";
    private String idCardFront = "";
    private String idCardBack = "";
    private String handHeldIdCard = "";
    private Uri imageUri;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_appeal;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.order_appeal));
        presenter = new AppealPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderSn = bundle.getString("orderSn");
        }
        appealTypes = new String[]{getString(R.string.appeal_type_paied), getString(R.string.appeal_type_unpay)};
        tvSymbol.setText(appealTypes[0]);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
    }

    @Override
    protected void setListener() {
        super.setListener();
        etRemark.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(300)});
    }

    private InputFilter inputFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtils.showToast(getString(R.string.no_input_emoji));
                return "";
            }
            return null;
        }
    };

    @OnClick({R.id.tvSubmit, R.id.tvSymbol, R.id.ivImg1, R.id.ivImg2, R.id.ivImg3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                String remark = etRemark.getText().toString().trim();
                if (StringUtils.isEmpty(remark)) {
                    ToastUtils.showToast(getString(R.string.incomplete_information));
                } else {
                    showCofirmDialog();
                }
                break;
            case R.id.tvSymbol:
                showListDialog();
                break;
            case R.id.ivImg1:
                type = FACE;
                currentImg = ivImg1;
                actionSheetDialogNoTitle();
                break;
            case R.id.ivImg2:
                type = BACK;
                currentImg = ivImg2;
                actionSheetDialogNoTitle();
                break;
            case R.id.ivImg3:
                type = HOLD;
                currentImg = ivImg3;
                actionSheetDialogNoTitle();
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

    /**
     * check权限
     *
     * @param position
     */
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

    private void chooseFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GlobalConstant.CHOOSE_ALBUM);
    }

    void showListDialog() {
        if (normalDialog == null) {
            normalDialog = new NormalListDialog(activity, appealTypes);
            normalDialog.title(getString(R.string.appeal_type));
            normalDialog.titleBgColor(getResources().getColor(R.color.main_head_bg));
        }
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvSymbol.setText(appealTypes[position]);
                normalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

    /**
     * 对话框
     */
    private void showCofirmDialog() {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title(getString(R.string.warm_prompt))
                .titleTextColor(getResources().getColor(R.color.black))
                .bgColor(Color.parseColor("#ffffff"))
                .contentTextColor(getResources().getColor(R.color.black))
                .btnTextColor(getResources().getColor(R.color.black), getResources().getColor(R.color.black))
                //.btnPressColor(Color.parseColor("#2B2B2B"))
                .content(getString(R.string.confirm_go_on_appeal_tag)).btnText(getString(R.string.dialog_one_cancel), getString(R.string.continue_tag)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
//                        HashMap<String, String> map = new HashMap<>();
//                        map.put("orderId", orderId);
//                        map.put("orderSn", orderSn);
//                        map.put("remark", /*tvSymbol.getText() + ": " + */etRemark.getText().toString());
//                        map.put("picturesOne", idCardFront);
//                        map.put("picturesTwo", idCardBack);
//                        map.put("picturesThree", handHeldIdCard);
//                        presenter.appeal(map);

                        AppealApplyInTransitDto appealApplyInTransitDto = new AppealApplyInTransitDto();
                        appealApplyInTransitDto.setOrderId(Long.valueOf(orderSn));
                        appealApplyInTransitDto.setPicturesOne(idCardFront);
                        appealApplyInTransitDto.setPicturesTwo(idCardBack);
                        appealApplyInTransitDto.setPicturesThree(handHeldIdCard);
                        appealApplyInTransitDto.setRemark(etRemark.getText().toString());
                        presenter.appealApply(appealApplyInTransitDto);

                        dialog.superDismiss();
                    }
                });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.TAKE_PHOTO:
                    //BitmapUtils.compress(imageFile.getAbsolutePath(), BitmapUtils.p_1000);
                    int w = BitmapUtils.p_1000;
                    int h = BitmapUtils.zoomBitmapSize(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), w);
                    Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), w, h);
                    //Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    String base64Data = BitmapUtils.imgToBase64(bitmap);
                    bitmap.recycle();
                    upLoadBase64("data:image/jpeg;base64," + base64Data);
//                    }
                    break;
                case LoginActivity.RETURN_LOGIN:
                    if (MyApplication.getApp().isLogin()) hideToLoginView();
                    break;
                case GlobalConstant.CHOOSE_ALBUM:
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
                    //BitmapUtils.compress(imageFile.getAbsolutePath(), BitmapUtils.p_1000);
                    w = BitmapUtils.p_300;
                    h = BitmapUtils.zoomBitmapSize(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), w);
                    bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), w, h);
                    //Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    String base64Data2 = BitmapUtils.imgToBase64(bitmap);
                    bitmap.recycle();
                    upLoadBase64("data:image/jpeg;base64," + base64Data2);
//                    }
                    break;
            }
        }
    }

    /**
     * 上传base64位头像
     *
     * @param s
     */
    private void upLoadBase64(String s) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("base64Data", s);
//        presenter.uploadBase64Pic(map, type);

        presenter.base64UpLoad(s);
    }

//    /**
//     * 上传base64位头像
//     *
//     * @param bitmap
//     */
//    private void upLoadImageFile(Bitmap bitmap) {
//        try {
//            BitmapUtils.saveBitmapToFile(bitmap, imageFile, 80);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        bitmap.recycle();
////        presenter.uploadImageFile(imageFile, type);
//    }

    @Override
    public void appealApplySuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public void base64UpLoadSuccess(String obj) {
        if (StringUtils.isEmpty(obj)) {
            ToastUtils.showToast(getString(R.string.empty_address));
            return;
        }
        ToastUtils.showToast(getString(R.string.upload_success));
        if (!obj.contains("http")) {
//            obj = HttpUrls.HOST + "/" + obj;
        }
        Glide.with(this).load(obj).into(currentImg);
        switch (type) {
            case FACE:
                idCardFront = obj;
                break;
            case BACK:
                idCardBack = obj;
                break;
            case HOLD:
                handHeldIdCard = obj;
                break;
        }
    }


}
