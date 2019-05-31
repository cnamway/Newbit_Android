package com.spark.newbitrade.activity.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.MarketAdapter;
import com.spark.newbitrade.entity.Currency;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/29.
 */

public class ETHMarketFragment extends MarketBaseFragment {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    private MarketAdapter adapter;
    private List<Currency> currencies = new ArrayList<>();
    private int type;

    public static ETHMarketFragment getInstance(int type) {
        ETHMarketFragment ethMarketFragment = new ETHMarketFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        ethMarketFragment.setArguments(bundle);
        return ethMarketFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_coin;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = getArguments().getInt("type");
        }
        initRvContent();
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((MarketOperateCallback) getActivity()).itemClick(ETHMarketFragment.this.adapter.getItem(position), type);
            }
        });
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new MarketAdapter(currencies, 1);
        adapter.isFirstOnly(true);
        adapter.bindToRecyclerView(rvContent);
//        rvContent.setAdapter(adapter);
        if (currencies.size() == 0)
            adapter.setEmptyView(R.layout.empty_no_message);
    }

    @Override
    protected void loadData() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void dataLoaded(List<Currency> baseEth) {
        this.currencies.clear();
        this.currencies.addAll(baseEth);
        if (adapter != null) {
            if (currencies.size() == 0)
                adapter.setEmptyView(R.layout.empty_no_message);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getActivity().getSupportFragmentManager().putFragment(outState, "USDT", this);
    }

    public void tcpNotify() {
        if (getUserVisibleHint() && adapter != null) adapter.notifyDataSetChanged();
    }
}
