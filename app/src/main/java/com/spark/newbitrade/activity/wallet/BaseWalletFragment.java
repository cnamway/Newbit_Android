package com.spark.newbitrade.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.wallet_coin.CoinListActivity;
import com.spark.newbitrade.adapter.WalletAdapter;
import com.spark.newbitrade.base.BaseTransFragment;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.utils.MathUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 默认账户
 */
public class BaseWalletFragment extends BaseTransFragment implements BaseWalletContract.WalletView {
    public static final String TAG = BaseWalletFragment.class.getSimpleName();
    @BindView(R.id.tvCnyAmount)
    TextView tvCnyAmount;
    @BindView(R.id.rvWallet)
    RecyclerView rvWallet;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private List<Wallet> coins = new ArrayList<>();
    private WalletAdapter adapter;
    private BaseWalletPresenterImpl presenter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getWalletList();
        }
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.flymeOSStatusBarFontColor("#FFFFFF").init();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BaseWalletPresenterImpl(this);
        initRvWallet();
    }

    @Override
    protected void loadData() {
        getWalletList();
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (coins.size() > position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Wallet", coins.get(position));
                    bundle.putBoolean("isBase", true);
                    showActivity(BaseWalletDetailActivity.class, bundle, 1);
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWalletList();
            }
        });
    }

    /**
     * 列表初始化
     */
    private void initRvWallet() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvWallet.setLayoutManager(manager);
        adapter = new WalletAdapter(coins);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(true);
        rvWallet.setAdapter(adapter);
    }

    private void getWalletList() {
        presenter.findSupportAssetUsingGET();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (isFirst) {
            getWalletList();
            isFirst = false;
        }*/
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public void getWalletSuccess(List<Wallet> list) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
        if (list == null) return;

        if (list.size() > 0) {
            this.coins.clear();
            this.coins.addAll(list);
            adapter.notifyDataSetChanged();
            calcuTotal(coins);
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
        super.dealError(httpErrorEntity);
    }

    @Override
    public void dealError(VolleyError volleyError) {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
        super.dealError(volleyError);
    }

    private void calcuTotal(List<Wallet> coins) {
        double sumTrade = 0;
        for (Wallet coin : coins) {
            sumTrade += coin.getTotalLegalAssetBalance();
        }
        tvCnyAmount.setText("≈" + MathUtils.subZeroAndDot(MathUtils.getRundNumber(sumTrade, 2, null)) + " CNY");
    }

    @OnClick({R.id.llAccept, R.id.llExtract})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llAccept:
                if (coins != null && coins.size() > 0) {
                    bundle.putSerializable("coin", (Serializable) coins);
                    bundle.putBoolean("recharge", true);
                    showActivity(CoinListActivity.class, bundle, 1);
                }
                break;
            case R.id.llExtract:
                if (coins != null && coins.size() > 0) {
                    bundle.putSerializable("coin", (Serializable) coins);
                    bundle.putBoolean("recharge", false);
                    showActivity(CoinListActivity.class, bundle, 1);
                }
                break;
        }
    }

    @Override
    public void findSupportAssetUsingGETSuccess(List<Wallet> list) {
        if (list != null) {
            for (Wallet wallet : list) {
                if (wallet.getIsDefault() == 1) {
                    presenter.getWallet(wallet.getName());
                }
            }
        }
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        getWalletList();
    }

    /*@Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getWalletList();
        }
    }*/
}
