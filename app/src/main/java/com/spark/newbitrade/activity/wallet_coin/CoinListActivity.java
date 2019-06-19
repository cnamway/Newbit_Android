package com.spark.newbitrade.activity.wallet_coin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.WalletAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Wallet;

import java.util.List;

import butterknife.BindView;

/**
 * 币种列表-充币-提币
 */
public class CoinListActivity extends BaseActivity {

    @BindView(R.id.rvWallet)
    RecyclerView rvWallet;

    private List<Wallet> coins;
    private boolean recharge;
    private WalletAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_coin_list;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            coins = (List<Wallet>) bundle.getSerializable("coin");
            recharge = bundle.getBoolean("recharge");
            if (coins != null && coins.size() > 0) {
                initRvWallet();
            }
        }
        if (recharge) {
            tvTitle.setText("接收");
        } else {
            tvTitle.setText("发送");
        }
    }

    /**
     * 列表初始化
     */
    private void initRvWallet() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvWallet.setLayoutManager(manager);
        adapter = new WalletAdapter(coins);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(true);
        rvWallet.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (coins.size() > position) {
                    if (recharge) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("coin", coins.get(position));
                        showActivity(RechargeActivity.class, bundle, 1);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("coin", coins.get(position));
                        showActivity(ExtractActivity.class, bundle, 1);
                    }
                }
            }
        });

    }

}
