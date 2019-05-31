package com.spark.newbitrade.activity.wallet;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.WalletDetailAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.WalletDetail;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

public class WalletDetailActivity extends BaseActivity implements WalletDetailContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.llHead)
    LinearLayout llHead;
    private List<WalletDetail> walletDetails = new ArrayList<>();
    private WalletDetailAdapter adapter;
    private int pageNo = 1;
    private WalletDetailContract.Presenter presenter;


    @Override
    protected int getActivityLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        llHead.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.detail));
        new WalletDetailPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        initRvDetail();
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                pageNo = 1;
                getList();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getList();
            }
        }, rvContent);

    }

    /**
     * 获取钱包列表
     */
    private void getList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        presenter.allTransaction(map);
    }

    private void initRvDetail() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new WalletDetailAdapter(R.layout.item_lv_wallet_detail, walletDetails, activity);
        adapter.bindToRecyclerView(rvContent);
        adapter.setEnableLoadMore(false);
    }

    @Override
    protected void loadData() {
        getList();
    }


    @Override
    public void setPresenter(WalletDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void allTransactionSuccess(List<WalletDetail> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        View emptyView = getLayoutInflater().inflate(R.layout.empty_no_message, null);
        if (obj == null) {
            adapter.setEmptyView(emptyView);
        } else {
            if (pageNo == 1) {
                this.walletDetails.clear();
                if (obj.size() == 0) {
                    adapter.setEmptyView(emptyView);
                }
            } else if (obj.size() == 0) adapter.loadMoreEnd();
            walletDetails.addAll(obj);
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void allTransactionFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
