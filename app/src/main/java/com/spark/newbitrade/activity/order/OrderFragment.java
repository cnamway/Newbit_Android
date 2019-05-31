package com.spark.newbitrade.activity.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.library.otc.model.OrderVo;
import com.spark.library.otc.model.PageOrderVo;
import com.spark.library.otc.model.QueryCondition;
import com.spark.library.otc.model.QueryParamOrderVo;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.OrderAdapter;
import com.spark.newbitrade.base.BaseLazyFragment;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.utils.GlobalConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderFragment extends BaseLazyFragment implements OrderContract.View {
    public static final String TAG = OrderFragment.class.getSimpleName();
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private List<OrderVo> orders = new ArrayList<>();
    private OrderAdapter adapter;
    private Status status;
    private OrderPresenterImpl presenter;
    private int pageNo = 1;
    private String unit = "";
    private String orderid = "";
    private String advertiseType = "";
//    private boolean isFirst = false;

    public static OrderFragment getInstance(Status status) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            adapter.setEnableLoadMore(false);
            pageNo = 1;
            getList(true);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initData() {
        presenter = new OrderPresenterImpl(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = (Status) bundle.getSerializable("status");
        }
        initRv();
    }

    /**
     * 获取订单列表
     */
    private void getList(boolean isShow) {
        if (MyApplication.getApp().isLogin()) {
            if (isShow) {
                showLoading();
            }

//            HashMap<String, String> map = new HashMap<>();
//            map.put("pageNo", pageNo + "");
//            map.put("pageSize", GlobalConstant.PageSize + "");
//            map.put("status", status.getStatus() + "");
//            map.put("unit", unit);
//            map.put("orderid", orderid);
//            map.put("advertiseType", advertiseType);
//            presenter.myOrder(map);

            QueryParamOrderVo queryParam = new QueryParamOrderVo();
            queryParam.setPageIndex(pageNo);
            queryParam.setPageSize(GlobalConstant.PageSize);

            List<QueryCondition> queryConditionList = new ArrayList<>();
            queryConditionList.add(getQueryCondition("status", status.getStatus(), "and", "="));
            queryParam.setQueryList(queryConditionList);

            queryParam.setSortFields("createTime_d");

            if (status.getStatus() == 0 || status.getStatus() == 3) {
                //查询我的归档订单（已完成，已取消）
                presenter.findMyOrderAchive(queryParam);
            } else {
                //查询我的在途订单(未付款，已付款，申诉中)
                presenter.findMyOrderInTransit(queryParam);
            }
        }
    }

    private QueryCondition getQueryCondition(String key, Object value, String join, String oper) {
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setKey(key);
        queryCondition.setValue(value);
        queryCondition.setJoin(join);
        queryCondition.setOper(oper);
        return queryCondition;
    }

    public void setSelectResult(String unit, String orderid, String advertiseType) {
        this.unit = unit;
        this.orderid = orderid;
        this.advertiseType = advertiseType;
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getList(false);
    }

    @Override
    protected void loadData() {
        super.loadData();
        isNeedLoad = false;
//        isFirst = true;
        getList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (getUserVisibleHint() && isFirst) {
//            isNeedLoad = false;
//            getList(false);
//        }
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
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("orderSn", orders.get(position).getOrderSn());
                bundle.putSerializable("status", status);
                showActivity(OrderDetailActivity.class, bundle, 1);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getList(false);
            }
        }, rvContent);
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new OrderAdapter(getActivity(), orders);
        adapter.bindToRecyclerView(rvContent);
        adapter.setEnableLoadMore(false);
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void findMyOrderInTransitSuccess(PageOrderVo obj) {
        hideAll();
        adapter.loadMoreComplete();

        if (obj == null) {
            orders.clear();
            adapter.setEmptyView(R.layout.empty_no_message);
        } else {
            List<OrderVo> orderVoList = obj.getRecords();
            if (orderVoList == null) {
                orderVoList = new ArrayList<>();
            }

            if (pageNo == 1) {
                orders.clear();
                if (orderVoList.size() == 0) {
                    adapter.setEmptyView(R.layout.empty_no_message);
                }
            } else if (orderVoList.size() == 0) {
                adapter.loadMoreEnd();
            }
            orders.addAll(orderVoList);
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void findMyOrderAchiveSuccess(PageOrderVo obj) {
        hideAll();
        adapter.loadMoreComplete();

        if (obj == null) {
            orders.clear();
            adapter.setEmptyView(R.layout.empty_no_message);
        } else {
            List<OrderVo> orderVoList = obj.getRecords();
            if (orderVoList == null) {
                orderVoList = new ArrayList<>();
            }

            if (pageNo == 1) {
                orders.clear();
                if (orderVoList.size() == 0) {
                    adapter.setEmptyView(R.layout.empty_no_message);
                }
            } else if (orderVoList.size() == 0) {
                adapter.loadMoreEnd();
            }
            orders.addAll(orderVoList);
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        super.dealError(httpErrorEntity);
        hideAll();
    }

    @Override
    public void dealError(VolleyError volleyError) {
        super.dealError(volleyError);
        hideAll();
    }

    private void hideAll() {
        if (adapter != null) {
            adapter.setEnableLoadMore(true);
        }
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }

    //    @Override
//    public void doPostFail(Integer code, String toastMessage) {
//        adapter.setEnableLoadMore(true);
//        refreshLayout.setEnabled(true);
//        refreshLayout.setRefreshing(false);
//        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
//    }
//
//    @Override
//    public void myOrderSuccess(List<Order> obj) {
//        adapter.setEnableLoadMore(true);
//        adapter.loadMoreComplete();
//        if (refreshLayout != null) {
//            refreshLayout.setEnabled(true);
//            refreshLayout.setRefreshing(false);
//        }
//        if (obj == null) {
//            orders.clear();
//            adapter.setEmptyView(R.layout.empty_no_message);
//        } else {
//            if (pageNo == 1) {
//                orders.clear();
//                if (obj.size() == 0) {
//                    adapter.setEmptyView(R.layout.empty_no_message);
//                }
//            } else if (obj.size() == 0) {
//                adapter.loadMoreEnd();
//            }
//            orders.addAll(obj);
//        }
//        adapter.notifyDataSetChanged();
//        adapter.disableLoadMoreIfNotFullPage();
//    }

    public enum Status {
        CANC(0, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[3]),//已取消
        UNPAID(1, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[0]),//未付款
        PAID(2, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[1]),//已付款
        DONE(3, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[2]),//已完成
        COMPLAINING(4, MyApplication.getApp().getResources().getStringArray(R.array.order_status)[4]);//申诉中

        private int status;
        private String statusStr;

        Status(int status, String statusStr) {
            this.status = status;
            this.statusStr = statusStr;
        }

        public int getStatus() {
            return status;
        }

        public String getStatusStr() {
            return statusStr;
        }
    }


}
