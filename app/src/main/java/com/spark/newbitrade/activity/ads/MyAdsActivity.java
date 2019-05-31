package com.spark.newbitrade.activity.ads;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.library.otc.model.QueryCondition;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.releaseAd.PubAdsActivity;
import com.spark.newbitrade.adapter.AdsAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.PasswordDialog;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的广告列表
 */
public class MyAdsActivity extends BaseActivity {

    @BindView(R.id.ivReleseAd)
    ImageView ivReleseAd;
    @BindView(R.id.ivOpen)
    ImageView ivOpen;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.llBuyAndSell)
    LinearLayout llBuyAndSell;
    @BindView(R.id.tvLeft)
    TextView tvLeft;
    @BindView(R.id.tvRight)
    TextView tvRight;
    @BindView(R.id.llTabSwitch)
    LinearLayout llTabSwitch;
    @BindView(R.id.ivSwitch)
    ImageButton ivSwitch;
    @BindView(R.id.ivCollect)
    ImageView ivCollect;
    @BindView(R.id.ivMarket)
    ImageButton ivMarket;
    @BindView(R.id.ivFilter)
    ImageView ivFilter;
    @BindView(R.id.ivHelp)
    ImageButton ivHelp;
    @BindView(R.id.ivShare)
    ImageView ivShare;
    @BindView(R.id.ivMessage)
    ImageButton ivMessage;
    @BindView(R.id.ivchatTip)
    ImageView ivchatTip;
    @BindView(R.id.rvMessageTag)
    RelativeLayout rvMessageTag;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.llHead)
    LinearLayout llHead;
    @BindView(R.id.llVersion)
    LinearLayout llVersion;
    @BindView(R.id.llIng)
    LinearLayout llIng;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_ads;
    }

    @Override
    protected void initView() {
        super.initView();
        llHead.setVisibility(View.VISIBLE);
        setSetTitleAndBack(false, true);
//        tvGoto.setVisibility(View.GONE);
//        ivFilter.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_ads));
    }

    @OnClick({R.id.llVersion, R.id.llIng})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llVersion:
                bundle.putInt("position", 1);
                showActivity(AdsActivity.class, null);
                break;
            case R.id.llIng:
                bundle.putInt("position", 2);
                showActivity(AdsActivity.class, bundle);
                break;
        }
    }


}
