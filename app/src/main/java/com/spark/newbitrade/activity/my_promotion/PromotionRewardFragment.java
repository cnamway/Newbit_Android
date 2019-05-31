package com.spark.newbitrade.activity.my_promotion;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.library.uc.model.MemberRewardRecord;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.PromotionRewardAdapter;
import com.spark.newbitrade.base.BaseLazyFragment;
import com.spark.newbitrade.entity.PromotionReward;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.NetCodeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * 我的佣金
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRewardFragment extends BaseLazyFragment implements PromotionRewardContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private PromotionRewardPresenter presenter;
    private List<MemberRewardRecord> rewards = new ArrayList<>();
    private PromotionRewardAdapter adapter;
    private int pageNo = 1;

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }


    @Override
    protected void initData() {
        initRv();
        presenter =new PromotionRewardPresenter(this);
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                adapter.setEnableLoadMore(false);
                adapter.loadMoreEnd(false);
                getList(false);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                refreshLayout.setEnabled(false);
                getList(false);
            }
        }, rvContent);
    }

    public void dataLoad() {

    }

    private void getList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        presenter.getPromotionReward(map);
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new PromotionRewardAdapter(R.layout.item_promotion_reward, rewards);
        adapter.bindToRecyclerView(rvContent);
        adapter.setEnableLoadMore(false);
        rvContent.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvContent.setNestedScrollingEnabled(false);
    }

    @Override
    protected void loadData() {
        getList(true);
        isNeedLoad = false;
    }

    @Override
    public void getPromotionRewardFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void getPromotionRewardSuccess(List<MemberRewardRecord> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
        if (obj == null) {
            adapter.setEmptyView(R.layout.empty_no_message);
        } else {
            if (pageNo == 1) {
                rewards.clear();
                if (obj.size() == 0) {
                    adapter.setEmptyView(R.layout.empty_no_message);
                }
            } else {
                adapter.loadMoreEnd();
            }
            rewards.addAll(obj);
        }
//        ((PromotionActivity) activity).getTvProRew().setText(MathUtils.getRundNumber(count,2,null)+"USDT");
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }
}
