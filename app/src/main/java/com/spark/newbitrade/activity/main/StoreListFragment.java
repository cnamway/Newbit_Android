package com.spark.newbitrade.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.store.StorePublishActivity;
import com.spark.newbitrade.base.BaseLazyFragment;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.library.otc.model.MessageResult;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商家
 */

public class StoreListFragment extends BaseLazyFragment implements StoreListContract.View {
    public static final String TAG = StoreListFragment.class.getSimpleName();
    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.llBuy)
    RelativeLayout llBuy;

    private String coinName;
    private StoreListPresenterImpl presenter;
    private String priceStr;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1){

        }
    }

    public static StoreListFragment getInstance(String coinName) {
        StoreListFragment storeListFragment = new StoreListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("coinName", coinName);
        storeListFragment.setArguments(bundle);
        return storeListFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setShowBackBtn(true);
    }

    @Override
    protected void initData() {
        presenter = new StoreListPresenterImpl(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            coinName = bundle.getString("coinName");
            tvCoin.setText(coinName);
            tvCoinName.setText("出" + coinName);
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (StringUtils.isNotEmpty(coinName)) {
            presenter.priceFind(coinName, "CNY");
        }
    }

    @Override
    public void priceFindSuccess(MessageResult obj) {
        if (obj != null && obj.getData() != null) {
            priceStr = obj.getData().toString();
            tvPrice.setText(priceStr + " CNY");
        }
    }

    @OnClick({R.id.llBuy})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llBuy:
                Bundle bundle = new Bundle();
                bundle.putString("coinName", coinName);
                bundle.putString("priceStr", priceStr);
                showActivity(StorePublishActivity.class, bundle, 1);
                break;
        }
    }


}
