package com.spark.newbitrade.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.library.ac.model.MessageResult;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.WalletAdapter;
import com.spark.newbitrade.base.BaseTransFragment;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.spark.newbitrade.utils.GlobalConstant.OTC;

/**
 * 交易账户
 */
public class TradeWalletFragment extends BaseTransFragment implements BaseWalletContract.WalletView {
    public static final String TAG = TradeWalletFragment.class.getSimpleName();
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvCnyAmount)
    TextView tvCnyAmount;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
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
        return R.layout.activity_trade_wallet;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new BaseWalletPresenterImpl(this);
        initRvWallet();
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

    @Override
    protected void setListener() {
        super.setListener();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (coins.size() > position) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Wallet", coins.get(position));
                    bundle.putBoolean("isBase", false);
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

    private void getWalletList() {
        if (MyApplication.getApp().isLogin()) {
            presenter.getWallet(OTC);
        } else {
            if (refreshLayout != null) {
                refreshLayout.setEnabled(true);
                refreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            getWalletList();
            isFirst = false;
        }
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

    @Override
    public void findSupportAssetUsingGETSuccess(List<Wallet> response) {
        LogUtils.e("response===" + response);
//        presenter.getWallet(OTC);
    }
}
