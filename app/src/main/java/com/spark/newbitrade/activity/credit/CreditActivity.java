package com.spark.newbitrade.activity.credit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Credit;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.utils.BitmapUtils;
import com.spark.newbitrade.utils.FileUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.StringFormatUtils;
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

import static com.spark.newbitrade.utils.GlobalConstant.CHOOSE_ALBUM;

/**
 * 身份验证
 */
public class CreditActivity extends BaseActivity implements CreditContract.View {
    @BindView(R.id.ivIdFace)
    ImageView ivIdFace;
    @BindView(R.id.ivIdBack)
    ImageView ivIdBack;
    @BindView(R.id.ivHold)
    ImageView ivHold;
    @BindView(R.id.tvCredit)
    TextView tvCredit;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etIdNumber)
    EditText etIdNumber;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.ivNoticeIcon)
    ImageView ivNoticeIcon;
    @BindView(R.id.llNotice)
    LinearLayout llNotice;
    @BindView(R.id.llFace)
    LinearLayout llFace;
    @BindView(R.id.llBack)
    LinearLayout llBack;
    @BindView(R.id.llHold)
    LinearLayout llHold;
    @BindView(R.id.tvLeft)
    TextView tvIdCrad;
    @BindView(R.id.tvRight)
    TextView tvPassport;
    @BindView(R.id.llTabSwitch)
    LinearLayout llTabSwitch;
    @BindView(R.id.llIdCard)
    LinearLayout llIdCard;
    @BindView(R.id.etPassPortNumber)
    EditText etPassPortNumber;
    @BindView(R.id.llPassPortNum)
    LinearLayout llPassPortNum;
    @BindView(R.id.rvFace)
    RelativeLayout rvFace;
    @BindView(R.id.ivPassportFace)
    ImageView ivPassportFace;
    @BindView(R.id.llPassPortImg)
    LinearLayout llPassPortImg;
    @BindView(R.id.rvBack)
    RelativeLayout rvBack;
    @BindView(R.id.rvHold)
    RelativeLayout rvHold;
    @BindView(R.id.ivPassportHand)
    ImageView ivPassportHand;
    @BindView(R.id.llPassPortHand)
    LinearLayout llPassPortHand;
    @BindView(R.id.ivBackLine)
    View ivBackLine;
    @BindView(R.id.ivIconFace)
    ImageView ivIconFace;
    @BindView(R.id.ivIconBack)
    ImageView ivIconBack;
    @BindView(R.id.ivIconHold)
    ImageView ivIconHold;
    @BindView(R.id.ivIconPort)
    ImageView ivIconPort;
    @BindView(R.id.ivIconPortHold)
    ImageView ivIconPortHold;
    private int intFace = 1; // 身份证正面
    private int intBack = 2; // 身份证反面
    private int intHold = 3; // 手持身份证
    private int intPortFace = 4; // 护照正面
    private int intPortHold = 5; // 手持护照
    private int intStateSuccess = 3; // 审核成功
    private int intStateAuting = 1; // 审核中
    private int intStateFailed = 2; // 审核失败
    private int intStateUnAuting = 0; // 未审核
    private int type;
    private int noticeType;//实名认证状态 0-未认证 1待审核 2-审核不通过  3-已认证
    private ImageView currentImg;
    private File imageFile;
    private String filename = "idCard.jpg";
    private String strCardFrontUrl = "";
    private String strCardBackUrl = "";
    private String strCardHoldUrl = "";
    private String strPortFaceUrl = "";
    private String strPortHoldUrl = "";
    private String notice;//认证失败原因
    private Uri imageUri;
    private CreditPresenterImpl presenter;
    private Credit.DataBean dataBean;
    private boolean isIDCard = true;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GlobalConstant.CHOOSE_ALBUM:
                case GlobalConstant.TAKE_PHOTO:
                    if (requestCode == GlobalConstant.CHOOSE_ALBUM) {
                        if (resultCode != RESULT_OK) return;
                        imageUri = data.getData();
                        if (Build.VERSION.SDK_INT >= 19)
                            imageFile = UriUtils.getUriFromKitKat(this, imageUri);
                        else
                            imageFile = UriUtils.getUriBeforeKitKat(this, imageUri);
                        if (imageFile == null) {
                            ToastUtils.showToast(getString(R.string.str_library_file_exception));
                            return;
                        }
                    }
                    Bitmap bitmap = BitmapUtils.zoomBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), currentImg.getWidth(), currentImg.getHeight());
                    String base64Data = BitmapUtils.imgToBase64(bitmap);
                    bitmap.recycle();

                    presenter.uploadBase64Pic("data:image/jpeg;base64," + base64Data);
                    break;
            }
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_credit;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        tvTitle.setVisibility(View.GONE);
        llTabSwitch.setVisibility(View.VISIBLE);
        tvIdCrad.setText(getString(R.string.str_id_card_credit));
        tvIdCrad.setSelected(true);
        tvPassport.setText(getString(R.string.str_port_credit));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new CreditPresenterImpl(this);
        imageFile = FileUtils.getCacheSaveFile(this, filename);
    }

    @Override
    protected void loadData() {
        super.loadData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            noticeType = bundle.getInt("NoticeType", 0);
            notice = bundle.getString("Notice");
            if (noticeType == intStateUnAuting) {
                llNotice.setVisibility(View.GONE);
            } else {
                presenter.getCreditInfo();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @OnClick({R.id.tvCredit, R.id.rvHold, R.id.rvBack, R.id.rvFace, R.id.ivPassportFace, R.id.ivPassportHand, R.id.tvLeft, R.id.tvRight})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvCredit:
                checkInput();
                return;
            case R.id.rvHold:
                type = intHold;
                currentImg = ivHold;
                break;
            case R.id.rvBack:
                type = intBack;
                currentImg = ivIdBack;
                break;
            case R.id.rvFace:
                type = intFace;
                currentImg = ivIdFace;
                break;
            case R.id.ivPassportFace:
                type = intPortFace;
                currentImg = ivPassportFace;
                break;
            case R.id.ivPassportHand:
                type = intPortHold;
                currentImg = ivPassportHand;
                break;
            case R.id.tvLeft:
            case R.id.tvRight:
                showBySwitchTab(v.getId());
                return;
        }
        actionSheetDialogNoTitle();
    }

    protected void checkInput() {
        String strCardNum = StringUtils.getText(etIdNumber);
        String strRealName = StringUtils.getText(etName);
        String strPortNum = StringUtils.getText(etPassPortNumber);
        if (StringUtils.isEmpty(strRealName)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_name));
        } else if (isIDCard && StringUtils.isEmpty(strCardNum)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_id_num));
        } else if (isIDCard && !StringFormatUtils.isIDCard(strCardNum)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_please_input_correct_id_num));
        } else if (isIDCard && StringUtils.isEmpty(strCardFrontUrl)) {
            ToastUtils.showToast(getString(R.string.str_please_up) + getString(R.string.str_image_face_id_card));
        } else if (isIDCard && StringUtils.isEmpty(strCardBackUrl)) {
            ToastUtils.showToast(getString(R.string.str_please_up) + getString(R.string.str_image_back_id_card));
        } else if (isIDCard && StringUtils.isEmpty(strCardHoldUrl)) {
            ToastUtils.showToast(getString(R.string.str_please_up) + getString(R.string.str_image_hold_id_card));
        } else if (!isIDCard && StringUtils.isEmpty(strPortNum)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_port_num));
        } else if (!isIDCard && StringUtils.isEmpty(strPortNum)) {
            ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.str_port_num));
        } else if (!isIDCard && StringUtils.isEmpty(strPortFaceUrl)) {
            ToastUtils.showToast(getString(R.string.str_please_up) + getString(R.string.str_passportImage));
        } else if (!isIDCard && StringUtils.isEmpty(strPortHoldUrl)) {
            ToastUtils.showToast(getString(R.string.str_please_up) + getString(R.string.str_takePassportImage));
        } else {
            doCredit(strRealName, strCardNum, strPortNum);
        }
    }

    /**
     * 认证
     */
    private void doCredit(String strRealName, String strCardNum, String strPortNum) {
        if (isIDCard) {
            presenter.credit((long) 0, strCardNum, strCardFrontUrl, strCardHoldUrl, strCardBackUrl, strRealName);
        } else {
            presenter.credit((long) 1, strPortNum, strPortFaceUrl, "", strPortHoldUrl, strRealName);
        }
    }


    /**
     * 切换tab
     */
    private void showBySwitchTab(int intID) {
        if (intID == R.id.tvLeft) {
            isIDCard = true;
            tvIdCrad.setSelected(true);
            tvPassport.setSelected(false);
            llIdCard.setVisibility(View.VISIBLE);
            llFace.setVisibility(View.VISIBLE);
            llBack.setVisibility(View.VISIBLE);
            llHold.setVisibility(View.VISIBLE);
            ivBackLine.setVisibility(View.VISIBLE);
            llPassPortNum.setVisibility(View.GONE);
            llPassPortImg.setVisibility(View.GONE);
            llPassPortHand.setVisibility(View.GONE);
        } else {
            isIDCard = false;
            tvIdCrad.setSelected(false);
            tvPassport.setSelected(true);
            llIdCard.setVisibility(View.GONE);
            llFace.setVisibility(View.GONE);
            llBack.setVisibility(View.GONE);
            llHold.setVisibility(View.GONE);
            ivBackLine.setVisibility(View.GONE);
            llPassPortNum.setVisibility(View.VISIBLE);
            llPassPortImg.setVisibility(View.VISIBLE);
            llPassPortHand.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void uploadBase64PicSuccess(String obj) {
        ToastUtils.showToast(getString(R.string.str_upload_success));
        Glide.with(this).load(obj).into(currentImg);
        if (type == intFace) {
            strCardFrontUrl = obj;
        } else if (type == intBack) {
            strCardBackUrl = obj;
        } else if (type == intHold) {
            strCardHoldUrl = obj;
        } else if (type == intPortFace) {
            strPortFaceUrl = obj;
        } else if (type == intPortHold) {
            strPortHoldUrl = obj;
        }
    }

    @Override
    public void doCreditSuccess(String obj) {
        String strRealName = StringUtils.getText(etName);
        User user = MyApplication.getApp().getCurrentUser();
        user.setRealName(strRealName);
        MyApplication.getApp().setCurrentUser(user);

        ToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void getCreditInfoSuccess(Credit.DataBean obj) {
        Credit credit = new Credit();
        credit.setData(obj);
        dataBean = credit.getData();
        if (noticeType == intStateUnAuting) {//未认证
            llNotice.setVisibility(View.GONE);
            tvNotice.setVisibility(View.GONE);
            ivNoticeIcon.setVisibility(View.GONE);
        } else if (noticeType == intStateAuting) {//认证中
            llNotice.setVisibility(View.VISIBLE);
            rvFace.setEnabled(false);
            rvBack.setEnabled(false);
            llHold.setEnabled(false);
            rvHold.setEnabled(false);
            ivPassportFace.setEnabled(false);
            ivPassportHand.setEnabled(false);
            llTabSwitch.setVisibility(View.GONE);
            tvNotice.setText(R.string.unverified_notice);
            tvCredit.setClickable(false);
            tvCredit.setVisibility(View.GONE);
            tvCredit.setBackgroundColor(getResources().getColor(R.color.third_font_content));
            etName.setEnabled(false);
            if (dataBean.getCertifiedType().equals("0")) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.str_id_card_credit);
                etName.setText(dataBean.getRealName());
                etIdNumber.setText(dataBean.getIdCard());
                Glide.with(this).load(dataBean.getIdentityCardImgFront()).into(ivIdFace);
                Glide.with(this).load(dataBean.getIdentityCardImgReverse()).into(ivIdBack);
                Glide.with(this).load(dataBean.getIdentityCardImgInHand()).into(ivHold);
                strCardFrontUrl = dataBean.getIdentityCardImgFront();
                strCardBackUrl = dataBean.getIdentityCardImgReverse();
                strCardHoldUrl = dataBean.getIdentityCardImgInHand();
                etIdNumber.setEnabled(false);
                ivIconFace.setVisibility(View.GONE);
                ivIconBack.setVisibility(View.GONE);
                ivIconHold.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.str_port_credit);
                showBySwitchTab(R.id.tvRight);
                etName.setText(dataBean.getRealName());
                try {
                    etPassPortNumber.setText(dataBean.getIdCard());
                } catch (Exception e) {
                    etPassPortNumber.setText(dataBean.getIdCard());
                }
                Glide.with(this).load(dataBean.getIdentityCardImgFront()).into(ivPassportFace);
                Glide.with(this).load(dataBean.getIdentityCardImgReverse()).into(ivPassportHand);
                strPortFaceUrl = dataBean.getIdentityCardImgFront();
                strPortHoldUrl = dataBean.getIdentityCardImgReverse();
                etPassPortNumber.setEnabled(false);
                ivIconPort.setVisibility(View.GONE);
                ivIconPortHold.setVisibility(View.GONE);
            }
        } else if (noticeType == intStateFailed) {//认证失败
            llNotice.setVisibility(View.VISIBLE);
            tvCredit.setText(R.string.str_detify_again);
            tvNotice.setText(getString(R.string.creditfail_notice) + (StringUtils.isEmpty(notice) ? "" : "\n" + getString(R.string.creditfail_notice_reason) + notice));
            tvNotice.setTextColor(Color.parseColor("#D48000"));
            ivNoticeIcon.setImageResource(R.mipmap.icon_check_failed);
            if (dataBean.getCertifiedType().equals("0")) {
                etName.setText(dataBean.getRealName());
                etIdNumber.setText(dataBean.getIdCard());
                Glide.with(this).load(dataBean.getIdentityCardImgFront()).into(ivIdFace);
                Glide.with(this).load(dataBean.getIdentityCardImgReverse()).into(ivIdBack);
                Glide.with(this).load(dataBean.getIdentityCardImgInHand()).into(ivHold);
                strCardFrontUrl = dataBean.getIdentityCardImgFront();
                strCardBackUrl = dataBean.getIdentityCardImgReverse();
                strCardHoldUrl = dataBean.getIdentityCardImgInHand();
            } else {
                showBySwitchTab(R.id.tvRight);
                etName.setText(dataBean.getRealName());
                try {
                    etPassPortNumber.setText(dataBean.getIdCard());
                } catch (Exception e) {
                    etPassPortNumber.setText(dataBean.getIdCard());
                }
                Glide.with(this).load(dataBean.getIdentityCardImgFront()).into(ivPassportFace);
                Glide.with(this).load(dataBean.getIdentityCardImgReverse()).into(ivPassportHand);
                strPortFaceUrl = dataBean.getIdentityCardImgFront();
                strPortHoldUrl = dataBean.getIdentityCardImgReverse();
            }
        } else if (noticeType == intStateSuccess) {//认证成功
            llNotice.setVisibility(View.VISIBLE);
            llTabSwitch.setVisibility(View.GONE);
            tvNotice.setText(R.string.certifySuccessful);
            rvFace.setEnabled(false);
            rvBack.setEnabled(false);
            llHold.setEnabled(false);
            rvHold.setEnabled(false);
            ivPassportFace.setEnabled(false);
            ivPassportHand.setEnabled(false);
            ivNoticeIcon.setImageResource(R.mipmap.icon_check_success);
            etName.setEnabled(false);
            if (dataBean.getCertifiedType().equals("0")) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.str_id_card_credit);
                etName.setText(dataBean.getRealName());
                try {
                    etIdNumber.setText(dataBean.getIdCard());
                } catch (Exception e) {
                    etIdNumber.setText(dataBean.getIdCard());
                }
                etIdNumber.setEnabled(false);
                Glide.with(this).load(dataBean.getIdentityCardImgFront()).into(ivIdFace);
                Glide.with(this).load(dataBean.getIdentityCardImgReverse()).into(ivIdBack);
                Glide.with(this).load(dataBean.getIdentityCardImgInHand()).into(ivHold);
                llFace.setVisibility(View.VISIBLE);
                llBack.setVisibility(View.VISIBLE);
                llHold.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(R.string.str_port_credit);
                showBySwitchTab(R.id.tvRight);
                etName.setText(dataBean.getRealName());
                try {
                    etPassPortNumber.setText(dataBean.getIdCard());
                } catch (Exception e) {
                    etPassPortNumber.setText(dataBean.getIdCard());
                }
                etPassPortNumber.setEnabled(false);
                Glide.with(this).load(dataBean.getIdentityCardImgFront()).into(ivPassportFace);
                Glide.with(this).load(dataBean.getIdentityCardImgReverse()).into(ivPassportHand);
                llPassPortImg.setVisibility(View.VISIBLE);
                llPassPortHand.setVisibility(View.VISIBLE);
            }
            tvCredit.setVisibility(View.GONE);
        }

    }


    /**
     * 选择头像弹框
     */
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


    /**
     * 照相机
     */
    private void startCamera() {
        imageUri = UriUtils.getUriForFile(this, imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, GlobalConstant.TAKE_PHOTO);
    }

    /**
     * 相册选择
     */
    private void chooseFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_ALBUM);
    }


}

