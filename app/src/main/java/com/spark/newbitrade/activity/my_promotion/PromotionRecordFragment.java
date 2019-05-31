package com.spark.newbitrade.activity.my_promotion;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.library.uc.model.MemberPromotionVo;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.PromotionRecordAdapter;
import com.spark.newbitrade.base.BaseLazyFragment;
import com.spark.newbitrade.entity.PromotionRecord;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

/**
 * 推广好友
 */
public class PromotionRecordFragment extends BaseLazyFragment implements PromotionRecordContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private PromotionRecordPresenter presenter;
    private List<MemberPromotionVo> records = new ArrayList<>();
    private PromotionRecordAdapter adapter;
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
        presenter = new PromotionRecordPresenter(this);
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                pageNo = 1;
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


    private void getList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        presenter.getPromotion(map);
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new PromotionRecordAdapter(R.layout.item_promotion_record, records);
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
    public void getPromotionFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void getPromotionSuccess(List<MemberPromotionVo> obj, int total) {
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
                records.clear();
                if (obj.size() == 0) {
                    adapter.setEmptyView(R.layout.empty_no_message);
                }
            } else {
                adapter.loadMoreEnd();
            }
            records.addAll(obj);
        }
        ((PromotionActivity) activity).getTvProNum().setText(total + "");
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }


}
