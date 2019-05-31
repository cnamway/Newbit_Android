package com.spark.newbitrade.activity.my.order;

import android.content.Intent;
import android.widget.FrameLayout;

import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.order.MyOrderFragment;
import com.spark.newbitrade.base.BaseTransFragmentActivity;


import butterknife.BindView;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseTransFragmentActivity {

    @BindView(R.id.flContainer)
    FrameLayout flContainer;

    private MyOrderFragment orderFragment;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_order2;
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
        if (orderFragment == null) fragments.add(orderFragment = new MyOrderFragment());
    }

    @Override
    protected void recoverFragment() {
        orderFragment = (MyOrderFragment) getSupportFragmentManager().findFragmentByTag(MyOrderFragment.TAG);
        if (orderFragment == null) fragments.add(orderFragment = new MyOrderFragment());
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
