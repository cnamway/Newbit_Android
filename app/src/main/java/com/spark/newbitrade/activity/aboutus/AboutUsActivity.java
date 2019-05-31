package com.spark.newbitrade.activity.aboutus;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.spark.library.cms.model.MessageResultWebConfigVo;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.feed.FeedbackActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.Vision;
import com.spark.newbitrade.entity.VisionEntity;
import com.spark.newbitrade.ui.AppVersionDialog;
import com.spark.newbitrade.utils.CommonUtils;
import com.spark.newbitrade.utils.FileUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.utils.okhttp.AppUtils;
import com.spark.newbitrade.utils.okhttp.FileCallback;
import com.spark.newbitrade.utils.okhttp.OkhttpUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Request;

public class AboutUsActivity extends BaseActivity implements com.spark.newbitrade.activity.aboutus.AboutUsContract.View {
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvWeb)
    TextView tvWeb;
    @BindView(R.id.tvVersionNum)
    TextView tvVersionNum;

    private com.spark.newbitrade.activity.aboutus.AboutUsPresenter presenter;
    private ProgressDialog progressDialog;
    private AppVersionDialog appVersionDialog;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        setTitle(getString(R.string.about_us));
        tvName.setText(getString(R.string.app_name) + "  " + "V" + CommonUtils.getVersionName(this));
        tvVersionNum.setText("V" + CommonUtils.getVersionName(this));
        initProgressDialog();
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new com.spark.newbitrade.activity.aboutus.AboutUsPresenter(this);
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.getWebConfig();
    }

    @OnClick({R.id.llVersion, R.id.llFeed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llVersion:
                checkPermission();
                break;
            case R.id.llFeed:
                showActivity(FeedbackActivity.class, null);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    public void getWebConfigSuccess(MessageResultWebConfigVo response) {
        if (response != null && response.getData() != null) {
            tvPhone.setText(response.getData().getPhone());
            tvWeb.setText(response.getData().getWebsite());
        }

    }

    /**
     * check权限
     *
     * @param position
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        presenter.checkVersion();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(activity, data)) {
                            AndPermission.permissionSetting(activity).execute();
                            return;
                        }
                        ToastUtils.showToast(activity, getString(R.string.str_no_permission));
                    }
                }).start();
    }



    /**
     * 提示框
     */
    private void showReleaseDialog(final VisionEntity visionEntity) {
        appVersionDialog = new AppVersionDialog(activity, visionEntity);
        appVersionDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visionEntity.getData().getUrl() == null || "".equals(visionEntity.getData().getUrl())) {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                } else {
                    download(visionEntity.getData().getUrl());
                }
                appVersionDialog.dismiss();
            }
        });
        appVersionDialog.show();
    }

    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(this, "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                progressDialog.show();
                progressDialog.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Request request, HttpErrorEntity e) {
                progressDialog.dismiss();
                String msg = e.getMessage();
                if (StringUtils.isNotEmpty(msg) && msg.contains("Canceled")) {
                    NetCodeUtils.checkedErrorCode(AboutUsActivity.this, GlobalConstant.SERVER_ERROR, null);
                }
            }

            @Override
            public void onResponse(File response) {
                progressDialog.dismiss();
                AppUtils.installAPk(response, activity);
            }
        });
    }

    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getString(R.string.downloading_tag));
        //设置信息
        progressDialog.setMessage(getString(R.string.downloading_crazy_tag));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    @Override
    public void checkVersionSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            VisionEntity entity = new Gson().fromJson(response, VisionEntity.class);
            if (entity == null) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (entity.getCode() == 404) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (entity.getCode() == 500) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (entity.getData() == null) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (!(AppUtils.compareVersion(entity.getData().getVersion(), AppUtils.getVersionName(activity)) == 1)) {
                ToastUtils.showToast(activity, getString(R.string.str_version_new));
                return;
            }
            if (StringUtils.isNotEmpty(entity.getData().getUrl())) {
                showReleaseDialog(entity);
            } else {
                ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
            }

        } else {
            ToastUtils.showToast(activity, getString(R.string.str_version_new));
        }
    }
}
