package com.spark.newbitrade.activity.my.ads;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.adapter.PagerAdapter;
import com.spark.newbitrade.base.BaseFragment;
import com.spark.newbitrade.base.BaseNestingTransFragment;
import com.spark.newbitrade.entity.FilterBean;
import com.spark.newbitrade.ui.FilterPopView;
import com.spark.newbitrade.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的广告
 */

public class MyAdsFragment extends BaseNestingTransFragment {
    public static final String TAG = MyAdsFragment.class.getSimpleName();
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager vp;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.ivReleseAd)
    ImageView ivReleseAd;
    @BindView(R.id.ivFilter)
    ImageView ivFilter;
    @BindView(R.id.llBuyAndSell)
    LinearLayout llBuyAndSell;

    private List<String> coinInfos = new ArrayList<>();
    private PagerAdapter adapter;
    private ArrayList<String> tabs = new ArrayList<>();
    private FilterPopView filterPopView;
    private ArrayList<FilterBean> firList;
    private ArrayList<FilterBean> secList;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_ads;
    }

    @Override
    protected void initView() {
        ivReleseAd.setVisibility(View.GONE);
        ivFilter.setVisibility(View.INVISIBLE);
        setShowBackBtn(true);
        tvGoto.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        llBuyAndSell.setVisibility(View.VISIBLE);
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
        tvBuy.setText("C2C");
        tvSell.setText("OTC");
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void loadData() {
        super.loadData();
        coinInfos.clear();
        coinInfos.add("已下架");
        coinInfos.add("上架中");
        tabs.clear();
        fragments.clear();
        for (String coinInfo : coinInfos) {
            tabs.add(coinInfo);
            if ("已下架".equals(coinInfo)) {
                fragments.add(MyAdsListFragment.getInstance(1));
            } else {
                fragments.add(MyAdsListFragment.getInstance(2));
            }
        }
        if (fragments.size() > 4) tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        else tab.setTabMode(TabLayout.MODE_FIXED);
        if (adapter == null) {
            vp.setAdapter(adapter = new PagerAdapter(getChildFragmentManager(), fragments, tabs));
            tab.setupWithViewPager(vp);
            vp.setOffscreenPageLimit(fragments.size() - 1);
        } else adapter.notifyDataSetChanged();
        isNeedLoad = false;
    }

    @OnClick({R.id.tvBuy, R.id.tvSell, R.id.ivFilter})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (v.getId() == R.id.ivReleseAd) {
            if (!MyApplication.getApp().isLogin()) {
                ToastUtils.showToast(getString(R.string.text_login_first));
                showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                return;
            }
        }
        switch (v.getId()) {
            case R.id.tvBuy:
                clickTabBuy();
                break;
            case R.id.tvSell:
                clickTabSell();
                break;
            case R.id.ivFilter:

                break;
        }
    }

    /**
     * C2C
     */
    private void clickTabBuy() {
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
        for (BaseFragment fragment : fragments)
            ((MyAdsListFragment) fragment).setTradeType(0);
    }

    /**
     * OTC
     */
    private void clickTabSell() {
        tvBuy.setSelected(false);
        tvSell.setSelected(true);
        for (BaseFragment fragment : fragments)
            ((MyAdsListFragment) fragment).setTradeType(1);

    }

    /**
     * 弹出popwindow
     */
    private void showPopWind() {
        filterPopView.setFocusable(true);
        filterPopView.setOutsideTouchable(true);
        filterPopView.showAsDropDown(llTitle);
        filterPopView.update();
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    protected void addFragments() {
    }

    @Override
    protected void recoveryFragments() {
    }

    @Override
    protected String getmTag() {
        return TAG;
    }


}
