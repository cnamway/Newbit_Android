package com.spark.newbitrade.activity.my.ads;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseTransFragmentActivity;

import butterknife.BindView;

/**
 * 我的广告
 */
public class MyAdsActivity extends BaseTransFragmentActivity {

    @BindView(R.id.flContainer)
    FrameLayout flContainer;

    private MyAdsFragment orderFragment;
    private boolean adUp;//是否上架
    private boolean isOTC;//是否OTC

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_order2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            adUp = bundle.getBoolean("adUp");
            isOTC = bundle.getBoolean("isOTC");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        reCoveryView();
    }

    /**
     * 避免出现activity被销毁，fragment重新创建，造成界面重叠
     */
    private void reCoveryView() {
        if (fragments.size() == 0) {
            recoverFragment();
        }
        selecte(0);
    }


    private void selecte(int page) {
        showFragment(fragments.get(page));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void initFragments() {
        if (orderFragment == null) fragments.add(MyAdsFragment.getInstance(adUp, isOTC));
    }

    @Override
    protected void recoverFragment() {
        orderFragment = (MyAdsFragment) getSupportFragmentManager().findFragmentByTag(MyAdsFragment.TAG);
        if (orderFragment == null) fragments.add(MyAdsFragment.getInstance(adUp, isOTC));
        else fragments.add(orderFragment);
    }

    @Override
    public int getContainerId() {
        return R.id.flContainer;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selecte(0);
        }
    }

}
