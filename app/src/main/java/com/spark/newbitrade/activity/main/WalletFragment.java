package com.spark.newbitrade.activity.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.wallet.BaseWalletFragment;
import com.spark.newbitrade.activity.wallet.TradeWalletFragment;
import com.spark.newbitrade.base.BaseTransFragment;
import com.spark.newbitrade.ui.MyfragmentPagerAdpter;

import butterknife.BindView;

/**
 * 资产
 */

public class WalletFragment extends BaseTransFragment {
    public static final String TAG = WalletFragment.class.getSimpleName();
    @BindView(R.id.ivFilter)
    ImageView ivFilter;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tab)
    TabLayout tab;

    private String[] strings;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.flymeOSStatusBarFontColor("#FFFFFF").init();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), tab);
            isSetTitle = true;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wallet;
    }


    @Override
    protected void initData() {
        super.initData();

        strings = MyApplication.getApp().getResources().getStringArray(R.array.str_wallet_type);
        initFragment();
        initPageAdapter();
    }

    private void initFragment() {
        fragments.add(new BaseWalletFragment());
        fragments.add(new TradeWalletFragment());
    }

    private void initPageAdapter() {
        MyfragmentPagerAdpter adpter = new MyfragmentPagerAdpter(getChildFragmentManager(), fragments, strings);
        viewPager.setAdapter(adpter);
        tab.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
    }

    @Override
    protected String getmTag() {
        return TAG;
    }


}
