package com.spark.newbitrade.activity.main;

import android.content.Context;
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
 * 行情--usdt
 * Created by Administrator on 2018/1/29.
 */

public class MarketSecFragment extends MarketBaseFragment {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    private MarketAdapter mAdapter;
    private List<Currency> currencies = new ArrayList<>();
    private int type;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static MarketSecFragment getInstance(int type) {
        MarketSecFragment usdtMarketFragment = new MarketSecFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        usdtMarketFragment.setArguments(bundle);
        return usdtMarketFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_coin;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
        initRvContent();
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        mAdapter = new MarketAdapter(currencies, 1);
        mAdapter.isFirstOnly(true);
        mAdapter.setLoad(true);
        mAdapter.bindToRecyclerView(rvContent);
//        rvContent.setAdapter(mAdapter);
        if (currencies.size() == 0)
            mAdapter.setEmptyView(R.layout.empty_no_message);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((MarketOperateCallback) getActivity()).itemClick(MarketSecFragment.this.mAdapter.getItem(position), type);
            }
        });
    }

    @Override
    protected void loadData() {
        notifyData();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void dataLoaded(List<Currency> baseUsdt) {
        if (baseUsdt != null && mAdapter != null) {
            this.currencies.clear();
            this.currencies.addAll(baseUsdt);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void isChange(boolean isLoad) {
        if (mAdapter != null) {
            mAdapter.setLoad(isLoad);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void notifyData() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void tcpNotify() {
        if (getUserVisibleHint() && mAdapter != null) mAdapter.notifyDataSetChanged();
    }
}
