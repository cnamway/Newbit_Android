package com.spark.newbitrade.activity.entrust;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.TrustAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.EntrustDialog;
import com.spark.newbitrade.entity.Entrust;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.SocketMessage;
import com.spark.newbitrade.serivce.SocketResponse;
import com.spark.newbitrade.utils.DpPxUtils;
import com.spark.newbitrade.utils.GlobalConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * 当前委托的界面
 */
public class CurrentTrustActivity extends BaseActivity {
    @BindView(R.id.mRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView; // 列表控价
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    private List<Entrust.ListBean> mData = new ArrayList<>();
    private TrustAdapter mAdapter;
    private int page = 1; // 页码
    private String symbol;
    private EntrustDialog dialog;
    private String orderId;
    private int baseCoinScale = 2;
    private int coinScale = 2;
    private Entrust entrust;
    private PopupWindow popWnd;
    private boolean isSelf;
    private String side = "";

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_all_trust;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        tvGoto.setVisibility(View.VISIBLE);
        tvGoto.setText(getString(R.string.history_record));
    }

    @Override
    protected void initData() {
        super.initData();
        dialog = new EntrustDialog(activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isSelf = bundle.getBoolean("isSelf");
            symbol = bundle.getString("symbol");
            coinScale = bundle.getInt("coinScale");
            baseCoinScale = bundle.getInt("baseCoinScale");
            dialog.setBaseCoinScale(baseCoinScale, coinScale);
            setTitle(symbol);
            if (isSelf) {
                tvTag.setText(getString(R.string.history_record));
                tvGoto.setVisibility(View.INVISIBLE);
            }else {
                tvTag.setText(getString(R.string.current_trust));
            }
        }
        initRv();
        showPopWindow();
    }


    /**
     * 初始化列表
     */
    private void initRv() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TrustAdapter(mData);
        mAdapter.setBaseScale(baseCoinScale, coinScale);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
    }


    @OnClick({R.id.tvGoto, R.id.tvSelect})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvGoto:
                if (!isSelf) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isSelf", !isSelf);
                    bundle.putString("symbol", symbol);
                    bundle.putInt("baseCoinScale", baseCoinScale);
                    bundle.putInt("coinScale", coinScale);
                    showActivity(CurrentTrustActivity.class, bundle);
                }
                break;
            case R.id.tvSelect:
                popWnd.showAsDropDown(tvSelect);
                break;
        }
    }

    private void showPopWindow() {
        View contentView = LayoutInflater.from(activity).inflate(R.layout.pop_type_select_layout, null);
        popWnd = new PopupWindow(activity);
        popWnd.setContentView(contentView);
        popWnd.setWidth(DpPxUtils.dip2px(CurrentTrustActivity.this,80));
        popWnd.setHeight(DpPxUtils.dip2px(CurrentTrustActivity.this,120));
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWnd.setOutsideTouchable(true);
        popWnd.setTouchable(true);
        popWnd.setFocusable(true);
        final TextView tvAllType = contentView.findViewById(R.id.tvAllType);
        final TextView tvBuyType = contentView.findViewById(R.id.tvBuyType);
        final TextView tvSellType = contentView.findViewById(R.id.tvSellType);

        tvAllType.setSelected(true);
        tvAllType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllType.setSelected(true);
                tvBuyType.setSelected(false);
                tvSellType.setSelected(false);
                tvSelect.setText(R.string.all);
                popWnd.dismiss();
                side = "";
                page = 1;
                getList(false,side);
            }
        });

        tvBuyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllType.setSelected(false);
                tvBuyType.setSelected(true);
                tvSellType.setSelected(false);
                tvSelect.setText(R.string.text_buy_in);
                popWnd.dismiss();
                side = "0";
                page = 1;
                getList(false,side);
            }
        });

        tvSellType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllType.setSelected(false);
                tvBuyType.setSelected(false);
                tvSellType.setSelected(true);
                tvSelect.setText(R.string.text_sale_out);
                popWnd.dismiss();
                side = "1";
                page = 1;
                getList(false,side);
            }
        });

    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }

    @Override
    protected void setListener() {
        super.setListener();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMoreText();
            }
        }, mRecyclerView);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Entrust.ListBean entrust = (Entrust.ListBean) adapter.getItem(position);
                orderId = entrust.getOrderId();
                if (entrust != null) {
                    dialog.setEntrust(entrust);
                    dialog.show();
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshText();
            }
        });

        dialog.setOnCancelOrder(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEntrust();
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void loadData() {
        getList(true,side);
    }

    /**
     * 刷新
     */
    private void refreshText() {
        page = 1;
        mAdapter.setEnableLoadMore(false);
        getList(false,side);
    }

    /**
     * 下拉
     */
    private void loadMoreText() {
        refreshLayout.setEnabled(false);
        page++;
        getList(false,side);
    }

    /**
     * 获取列表
     *
     * @param isShow
     */
    private void getList(boolean isShow,String side) {
        if (isShow)
            displayLoadingPopup();
        if (entrust != null && entrust.getTotalPage() < page) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            mAdapter.setEnableLoadMore(false);
            mAdapter.loadMoreComplete();
        }else {
            HashMap<String, String> map = new HashMap<>();
            map.put("pageNo", page + "");
            map.put("pageSize", GlobalConstant.PageSize + "");
            map.put("symbol", symbol);
            map.put("side", side);
            String json = new Gson().toJson(map);
            if (isSelf) {
                EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.HISTORY_ORDER, json.getBytes())); // app端请求数据
            }else {
                EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.CURRENT_ORDER, json.getBytes())); // app端请求数据
            }
        }
    }

    /**
     * 撤单
     */
    private void cancelEntrust() {
        displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("symbol", symbol);
        String json = new Gson().toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.CANCEL_ORDER, json.getBytes()));
    }

    /**
     * 当前委托
     *
     * @param response SocketResponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
        if (response.getCmd() == null) return;
        String json = response.getResponse();
        JSONObject object = null;
        switch (response.getCmd()) {
            case CURRENT_ORDER:
                hideLoadingPopup();
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        Entrust objs = new Gson().fromJson(object.getJSONObject("data").toString(), new TypeToken<Entrust>() {
                        }.getType());
                        getCurrentOrderSuccess(objs);
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            case HISTORY_ORDER:
                hideLoadingPopup();
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        Entrust objs = new Gson().fromJson(object.getJSONObject("data").toString(), new TypeToken<Entrust>() {
                        }.getType());
                        getCurrentOrderSuccess(objs);
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            case PUSH_EXCHANGE_ORDER_CANCELED://服务器确认撤单回报
                hideLoadingPopup();
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        for (int i = 0; i < mData.size(); i++) {
                            if (mData.get(i).getOrderId().equals(orderId)) {
                                mData.remove(i);
                                mAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                        if (mData.size() == 0) {
                            mAdapter.setEmptyView(R.layout.empty_no_message);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        doPostFail(object.getInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            /*case ENABLE_SYMBOL:
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        List<MarketSymbol> objs = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<MarketSymbol>>() {
                        }.getType());
                       // getSymbolSucccess(objs);

                    } else {
                        hideLoadingPopup();
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoadingPopup();
                    doPostFail(JSON_ERROR, null);
                }
                break;*/
            default:
                break;
        }
    }

    /**
     * 失败
     *
     * @param e
     * @param meg
     */
    public void doPostFail(int e, String meg) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
    }

    /**
     * 获取列表成功
     *
     * @param obj
     */
    public void getCurrentOrderSuccess(Entrust obj) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        entrust = obj;
        mAdapter.setEnableLoadMore(true);
        mAdapter.loadMoreComplete();
        if (obj != null && obj.getList().size() > 0) {
            if (page == 1) {
                this.mData.clear();
            } else {
                mAdapter.loadMoreEnd();
            }
            this.mData.addAll(obj.getList());
        } else {
            if (page == 1) {
                this.mData.clear();
                mAdapter.setEmptyView(R.layout.empty_no_message);
            } else {
                mAdapter.loadMoreEnd();
            }
        }
        mAdapter.notifyDataSetChanged();
        mAdapter.disableLoadMoreIfNotFullPage();
    }

    /**
     * 撤单
     *
     * @param message
     */
    public void cancelOrderSuccess(String message) {
        getList(true,side);
    }



}
