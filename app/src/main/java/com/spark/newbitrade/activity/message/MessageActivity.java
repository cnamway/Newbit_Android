package com.spark.newbitrade.activity.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.MessageAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Message;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.NetCodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import config.Injection;

/**
 * 平台消息
 */
public class MessageActivity extends BaseActivity implements MessageContract.View {
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int pageNo = 1;
    private MessageContract.Presenter presenter;
    private List<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
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
                Message message = (Message) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.detail_title));
                bundle.putString("content", message.getContent());
                showActivity(WebViewActivity.class, bundle);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                pageNo = pageNo + 1;
                getList(false);
            }
        }, rvMessage);
    }

    /**
     * 获取消息列表
     */
    private void getList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize+"");
        presenter.message(map);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_message));
        new MessagePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        initRv();
    }


    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMessage.setLayoutManager(manager);
        adapter = new MessageAdapter(R.layout.item_message, messages, this);
        rvMessage.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        getList(true);
    }


    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void messageSuccess(List<Message> obj) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (obj == null) return;
        if (pageNo == 1) {
            messages.clear();
            if (obj.size() == 0) {
                adapter.loadMoreEnd();
                adapter.setEmptyView(R.layout.empty_no_message);
                adapter.notifyDataSetChanged();
            } else {
                messages.addAll(obj);
            }
        } else {
            if (obj.size() != 0) messages.addAll(obj);
            else adapter.loadMoreEnd();
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void messageFail(Integer code, String toastMessage) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
