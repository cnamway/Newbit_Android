package com.spark.newbitrade.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.aboutus.AboutUsActivity;
import com.spark.newbitrade.activity.chat.ChatListActivity;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.activity.my.ads.MyAdsActivity;
import com.spark.newbitrade.activity.my.help.HelpActivity;
import com.spark.newbitrade.activity.my.order.MyOrderActivity;
import com.spark.newbitrade.activity.my_promotion.PromotionActivity;
import com.spark.newbitrade.activity.myinfo.MyInfoActivity;
import com.spark.newbitrade.activity.safe.SafeActivity;
import com.spark.newbitrade.activity.setting.SettingActivity;
import com.spark.newbitrade.base.BaseTransFragment;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.ui.CircleImageView;
import com.spark.newbitrade.utils.DatabaseUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 个人中心
 * Created by Administrator on 2018/1/29.
 */

public class MyFragment extends BaseTransFragment {
    public static final String TAG = MyFragment.class.getSimpleName();
    @BindView(R.id.ivHeader)
    CircleImageView ivHeader;
    @BindView(R.id.tvNickName)
    TextView tvNickName;
    @BindView(R.id.etAccount)
    TextView etAccount;
    @BindView(R.id.llMyinfo)
    LinearLayout llMyinfo;
    @BindView(R.id.llAds)
    LinearLayout llAds;
    @BindView(R.id.llPromotion)
    LinearLayout llPromotion;
    @BindView(R.id.llSafe)
    LinearLayout llSafe;
    @BindView(R.id.llSettings)
    LinearLayout llSettings;
    @BindView(R.id.llAboutUs)
    LinearLayout llAboutUs;
    @BindView(R.id.llHelp)
    LinearLayout llHelp;
    @BindView(R.id.ivChatTip)
    ImageView ivchatTip;

    private User user;
    private DatabaseUtils databaseUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ivchatTip != null) {
            boolean hasNew = databaseUtils.getHasNew();
            if (hasNew) ivchatTip.setVisibility(View.VISIBLE);
            else ivchatTip.setVisibility(View.INVISIBLE);
        }
        //loadData();
    }

    /*@Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadData();
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    loadData(); // 修改登录密码后，手动重新显示view
                    break;
                case 2:
                    notLoginViewText();
                    break;
            }
        }

    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        databaseUtils = new DatabaseUtils();
    }

    @Override
    protected void loadData() {
        if (MyApplication.getApp().isLogin()) {
            user = MyApplication.getApp().getCurrentUser();
            loginingViewText();
        } else {
            notLoginViewText();
        }
    }

    @OnClick({R.id.llMyinfo, R.id.llAds, R.id.llSafe, R.id.llSettings, R.id.llPromotion, R.id.llAboutUs, R.id.llHelp, R.id.llOrder, R.id.llMessage})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (!MyApplication.getApp().isLogin()) {
            showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llMyinfo:
                showActivity(MyInfoActivity.class, null, 1);
                break;
            case R.id.llAds:
                showActivity(MyAdsActivity.class, null);
                break;
            case R.id.llSafe:
                showActivity(SafeActivity.class, null);
                break;
            case R.id.llSettings:
                showActivity(SettingActivity.class, null, 2);
                break;
            case R.id.llPromotion:
                showActivity(PromotionActivity.class, null);
                break;
            case R.id.llAboutUs:
                showActivity(AboutUsActivity.class, null);
                break;
            case R.id.llHelp:
                showActivity(HelpActivity.class, null);
                break;
            case R.id.llOrder:
                showActivity(MyOrderActivity.class, null);
                break;
            case R.id.llMessage:
                showActivity(ChatListActivity.class, null);
                break;
        }
    }


    /**
     * 未登录显示
     */
    private void notLoginViewText() {
        tvNickName.setText(getString(R.string.not_login));
        Glide.with(getActivity().getApplicationContext()).load(R.mipmap.icon_avatar).into(ivHeader);
        etAccount.setText(getString(R.string.welcome_to));
    }

    /**
     * 登录显示
     */
    private void loginingViewText() {
        if (user != null) {
            tvNickName.setText(user.getMobilePhone());
            if (!StringUtils.isEmpty(user.getAvatar()))
                Glide.with(getActivity().getApplicationContext()).load(user.getAvatar()).into(ivHeader);
            etAccount.setText(getString(R.string.welcome_to));
        }
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        if (MyApplication.getApp().isLogin()) {
            user = MyApplication.getApp().getCurrentUser();
            loginingViewText();
        } else {
            notLoginViewText();
        }
    }

    public void setChatTip(boolean hasNew) {
        if (ivchatTip != null) {
            if (hasNew) ivchatTip.setVisibility(View.VISIBLE);
            else ivchatTip.setVisibility(View.INVISIBLE);
        }
    }
}
