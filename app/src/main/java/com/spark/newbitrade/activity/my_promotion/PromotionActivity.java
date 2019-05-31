package com.spark.newbitrade.activity.my_promotion;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.base.BaseFragment;
import com.spark.newbitrade.entity.PromotionRecord;
import com.spark.newbitrade.entity.PromotionReward;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.ui.CustomViewPager;
import com.spark.newbitrade.utils.BitmapUtils;
import com.spark.newbitrade.utils.CommonUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.PermissionUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.Injection;

/**
 * 我的推广
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionActivity extends BaseActivity implements PromotionContract.View {
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tvPromoteFriend)
    TextView tvPromoteFriend;
    @BindView(R.id.tvMyCommission)
    TextView tvMyCommission;
    @BindView(R.id.ivShare)
    ImageView ibShare;
    @BindView(R.id.vpPager)
    CustomViewPager vpPager;
    @BindView(R.id.tvProNum)
    TextView tvProNum;
    @BindView(R.id.tvProRew)
    TextView tvProRew;
    @BindView(R.id.tvPromotionCode)
    TextView tvPromotionCode;
    @BindView(R.id.tvCopy)
    TextView tvCopy;
    private List<String> tabs = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private PopupWindow popWnd;
    private Bitmap saveBitmap;
    private RelativeLayout rLayoutCode;
    private PromotionPresenter presenter;
    private PromotionRecordFragment recordFragment;
    private PromotionRewardFragment rewardFragment;
    private int pageNo = 1;
    private BaseFragment currentFragment;
    private String promotionPrefix = "";//推广前缀

    public TextView getTvProNum() {
        return tvProNum;
    }

    public TextView getTvProRew() {
        return tvProRew;
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_promotion;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        ibShare.setVisibility(View.VISIBLE);
        tvGoto.setVisibility(View.GONE);
        addFragment();
        selecte(tvPromoteFriend, 0);
    }

    @OnClick({R.id.tvPromoteFriend, R.id.tvMyCommission, R.id.ivShare, R.id.tvCopy})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvPromoteFriend:
                selecte(tvPromoteFriend, 0);
                break;
            case R.id.tvMyCommission:
                selecte(tvMyCommission, 1);
                break;
            case R.id.ivShare:
                showPop();
                break;
            case R.id.tvCopy:
                CommonUtils.copyText(this, MyApplication.getApp().getCurrentUser().getPromotionCode());
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (tvPromoteFriend.isSelected()) {
                    recordFragment.getRefreshLayout().setEnabled(scrollView.getScrollY() == 0);
                } else {
                    rewardFragment.getRefreshLayout().setEnabled(scrollView.getScrollY() == 0);
                }
            }
        });
    }

    private void addFragment() {
        recordFragment = new PromotionRecordFragment();
        rewardFragment = new PromotionRewardFragment();
        fragments.add(recordFragment);
        fragments.add(rewardFragment);
    }

    private void selecte(View v, int page) {
        tvPromoteFriend.setSelected(false);
        tvMyCommission.setSelected(false);
        v.setSelected(true);
        showFragment(page);
    }

    private void showFragment(int page) {
        BaseFragment fragment = fragments.get(page);
        if (currentFragment == fragment) return;
        currentFragment = fragment;
        hideFragments();
        if (!fragment.isAdded()) {
            String tag = "";
            if (page == 0) {
                tag = PromotionRecordFragment.class.getSimpleName();
            } else {
                tag = PromotionRewardFragment.class.getSimpleName();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.flContainer, fragment, tag).commit();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(fragment).commit();
    }

    private void hideFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            if (!fragments.get(i).isHidden() && currentFragment != fragments.get(i)) {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
    }

    @Override
    protected void initData() {
        super.initData();
        //new PromotionPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        setTitle(getString(R.string.my_promotion));
        String html = "<font color=#6c6e8a>" + getString(R.string.invite_code_tag) + "</font>" + MyApplication.getApp().getCurrentUser().getPromotionCode();
        tvPromotionCode.setText(Html.fromHtml(html));
        presenter = new PromotionPresenter(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getWebConfig();
    }

    private void showPop() {
        View contentView = LayoutInflater.from(PromotionActivity.this).inflate(R.layout.view_pop_promotion_share, null);
        popWnd = new PopupWindow(PromotionActivity.this);
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWnd.setOutsideTouchable(true);
        popWnd.setTouchable(true);
        popWnd.setFocusable(true);
        darkenBackground(0.4f);
        rLayoutCode = contentView.findViewById(R.id.rLayoutCode);
        TextView tvSave = contentView.findViewById(R.id.tvSave);
        TextView tvPopviewPromotionCode = contentView.findViewById(R.id.tvPopviewPromotionCode);
        TextView tvCopy = contentView.findViewById(R.id.tvCopy);
        final ImageView ivPromotion = contentView.findViewById(R.id.ivPromotion);
        final User user = MyApplication.getApp().getCurrentUser();
        ivPromotion.post(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.isEmpty(promotionPrefix, user.getPromotionCode()))
                    return;
                saveBitmap = createQRCode(promotionPrefix + user.getPromotionCode(), Math.min(ivPromotion.getWidth(), ivPromotion.getHeight()));
                ivPromotion.setImageBitmap(saveBitmap);
            }
        });
        //tvPopviewPromotionCode.setText(user.getPromotionCode());
        ImageView iv_cancel = contentView.findViewById(R.id.iv_cancel);
        TextView tvWebSide = contentView.findViewById(R.id.tvWebSide);
        //tvWebSide.setText(promotionPrefix + user.getPromotionCode());
        tvWebSide.setText(user.getPromotionCode());
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWnd.dismiss();
            }
        });
        View rootview = LayoutInflater.from(PromotionActivity.this).inflate(R.layout.activity_promotion, null);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.copyText(activity, promotionPrefix + user.getPromotionCode());
                ToastUtils.showToast(R.string.copy_success);
            }
        });
        popWnd.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
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

    private void save(Bitmap bitmap) {
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
        if (bitmap != null) try {
            BitmapUtils.saveBitmapToFile(bitmap, file, 100);
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
     * @param view 需要截取图片的view
     * @return 截图
     */
    private Bitmap getBitmap(View view) throws Exception {
        if (view == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenshot);
        canvas.translate(-view.getScrollX(), -view.getScrollY());//我们在用滑动View获得它的Bitmap时候，获得的是整个View的区域（包括隐藏的），如果想得到当前区域，需要重新定位到当前可显示的区域
        view.draw(canvas);// 将 view 画到画布上
        return screenshot;
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        try {
                            save(getBitmap(rLayoutCode));
                        } catch (Exception e) {

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

    @Override
    public void getWebConfigSuccess(MessageResultWebConfigVo response) {
        if (response != null && response.getData() != null) {
            promotionPrefix = response.getData().getPromotionPrefix();
        }

    }
}
