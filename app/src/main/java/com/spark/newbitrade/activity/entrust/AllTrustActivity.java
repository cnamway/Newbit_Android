package com.spark.newbitrade.activity.entrust;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.TrustHistoryAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Entrust;
import com.spark.newbitrade.entity.FilterBean;
import com.spark.newbitrade.entity.MarketSymbol;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.chatUtils.SocketMessage;
import com.spark.newbitrade.serivce.chatUtils.SocketResponse;
import com.spark.newbitrade.ui.FilterPopView;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.NetCodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * 全部委托/我的委托
 */
public class AllTrustActivity extends BaseActivity implements FilterPopView.FilterCallBack {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.llBuyAndSell)
    LinearLayout llBuyAndSell;
    @BindView(R.id.ivFilter)
    ImageView ivFilter;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    private TrustHistoryAdapter mAdapter;
    private List<Entrust.ListBean> mData = new ArrayList<>();
    private String symbol;
    private int page = 1;
    private int baseCoinScale = 2;
    private int coinScale = 2;
    private Entrust entrust;
    private List<MarketSymbol> symbolList = new ArrayList<>();
    private String[] symbolName;
    private NormalListDialog normalDialog;
    private FilterPopView filterPopView;
    private ISocket.CMD cmd = ISocket.CMD.CURRENT_ORDER;
    private int popViewType = FilterPopView.CALLFROM_CURREBT_ENTRUST;
    private ArrayList<FilterBean> secList;
    private ArrayList<FilterBean> thdList;
    private String side = "";
    private String status = "";

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
        tvTitle.setVisibility(View.GONE);
        ivFilter.setVisibility(View.VISIBLE);
        llBuyAndSell.setVisibility(View.VISIBLE);
        tvBuy.setText(R.string.current_trust);
        tvSell.setText(R.string.history_trust);
        tvSymbol.setVisibility(View.VISIBLE);
        tvTag.setVisibility(View.GONE);
        tvSelect.setVisibility(View.GONE);
        tvGoto.setVisibility(View.GONE);
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
    }

    @Override
    protected void initData() {
        super.initData();
        initRv();
        initPopView();
    }

    private void initPopView() {
        filterPopView = new FilterPopView(activity, this, popViewType, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        secList = new ArrayList<>();
        secList.add(new FilterBean(getString(R.string.all), "", true));
        secList.add(new FilterBean(getString(R.string.text_buy_in), "0", false));
        secList.add(new FilterBean(getString(R.string.text_sale_out), "1", false));

        thdList = new ArrayList<>();
        thdList.add(new FilterBean(getString(R.string.all), "", true));
        thdList.add(new FilterBean(getString(R.string.text_actual_deal), "5", false));
        thdList.add(new FilterBean(getString(R.string.undone), "6", false));
        filterPopView.setSecList(secList);
        filterPopView.setThdList(thdList);
    }

    /**
     * 初始化列表
     */
    private void initRv() {
        mAdapter = new TrustHistoryAdapter(mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
    }


    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mAdapter.setEnableLoadMore(false);
                getList(false, cmd);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Entrust.ListBean entrustHistory = (Entrust.ListBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString("symbol", symbol);
                bundle.putSerializable("order", entrustHistory);
                bundle.putString("orderId", entrustHistory.getOrderId());
                bundle.putInt("coinScale", coinScale);
                bundle.putInt("baseCoinScale", baseCoinScale);
                showActivity(TrustDetailActivity.class, bundle);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                page = page + 1;
                getList(false, cmd);
            }
        }, mRecyclerView);

        tvSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListDialog();
            }
        });
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBuy.setSelected(true);
                tvSell.setSelected(false);
                page = 1;
                cmd = ISocket.CMD.CURRENT_ORDER;
                getList(false, cmd);
                popViewType = FilterPopView.CALLFROM_CURREBT_ENTRUST;
                filterPopView.setEntrustType(popViewType);
            }
        });
        tvSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBuy.setSelected(false);
                tvSell.setSelected(true);
                page = 1;
                cmd = ISocket.CMD.HISTORY_ORDER;
                getList(false, cmd);
                popViewType = FilterPopView.CALLFROM_HISTORY_ENTRUST;
                filterPopView.setEntrustType(popViewType);
            }
        });
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWind();
            }
        });
    }

    private void showPopWind() {
        filterPopView.setFocusable(true);
        filterPopView.setOutsideTouchable(true);
        filterPopView.showAsDropDown(llTitle);
        filterPopView.setSmbol(tvSymbol.getText().toString());
        filterPopView.update();
    }

    @Override
    protected void loadData() {
        displayLoadingPopup();
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.ENABLE_SYMBOL.getCode(), null));
    }

    /**
     * 获取全部委托数据
     */
    private void getList(boolean isShow, ISocket.CMD cmd) {
        if (isShow)
            displayLoadingPopup();
        /*if (entrust != null && entrust.getTotalPage() < page) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            mAdapter.setEnableLoadMore(false);
            mAdapter.loadMoreComplete();
            hideLoadingPopup();
        } else {*/
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", page + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        map.put("symbol", symbol);
        map.put("side", side);
        map.put("status", status);//5 成交  6 撤单
        String json = new Gson().toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, cmd.getCode(), json.getBytes())); // app端请求数据
        //}
    }

    /**
     * @param response SocketResponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
       /// if (response.getCmd() == null) return;
        String json = response.getResponse();
        JSONObject object = null;
        switch (response.getCmd()) {
            case 0:
                hideLoadingPopup();
                refreshLayout.setEnabled(true);
                refreshLayout.setRefreshing(false);
                mAdapter.setEnableLoadMore(true);
                mAdapter.loadMoreComplete();
                try {
                    object = new JSONObject(json);
                    LogUtils.i("code==" + object.optInt("code"));
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        Entrust objs = new Gson().fromJson(object.getJSONObject("data").toString(), new TypeToken<Entrust>() {
                        }.getType());
                        getAllSuccess(objs);
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            case 1:
                hideLoadingPopup();
                refreshLayout.setEnabled(true);
                refreshLayout.setRefreshing(false);
                mAdapter.setEnableLoadMore(true);
                mAdapter.loadMoreComplete();
                try {
                    object = new JSONObject(json);
                    LogUtils.i("code==" + object.optInt("code"));
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        Entrust objs = new Gson().fromJson(object.getJSONObject("data").toString(), new TypeToken<Entrust>() {
                        }.getType());
                        getAllSuccess(objs);
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            case 2:
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        List<MarketSymbol> objs = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<MarketSymbol>>() {
                        }.getType());
                        getSymbolSucccess(objs);
                    } else {
                        hideLoadingPopup();
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoadingPopup();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void callBack(HashMap<String, String> map) {
        if (map != null) {
            side = map.get("side");
            status = map.get("status");
            page = 1;
            getList(false, cmd);
        }
    }

    public void getSymbolSucccess(List<MarketSymbol> objs) {
        if (objs != null && objs.size() != 0) {
            symbolList.clear();
            symbolList.addAll(objs);
            symbolName = new String[symbolList.size()];
            for (int i = 0; i < symbolList.size(); i++) {
                symbolName[i] = symbolList.get(i).getSymbol();
            }
            symbol = symbolName[0];
            tvSymbol.setText(symbol);
            MarketSymbol marketSymbol = objs.get(0);
            baseCoinScale = marketSymbol.getBaseCoinScale();
            coinScale = marketSymbol.getCoinScale();
            getList(false, cmd);
        }
    }

    public void getAllSuccess(Entrust obj) {
        entrust = obj;
        mAdapter.setBaseScale(baseCoinScale, coinScale);
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
        mAdapter.disableLoadMoreIfNotFullPage();
        mAdapter.notifyDataSetChanged();
    }


    public void doPostFail(int code, String message) {
        NetCodeUtils.checkedErrorCode(this, code, message);
    }

    void showListDialog() {
        if (normalDialog == null) {
            normalDialog = new NormalListDialog(activity, symbolName);
            normalDialog.title(getString(R.string.text_coin_type));
            normalDialog.titleBgColor(Color.parseColor("#262a3e"))
                    .itemTextColor(getResources().getColor(R.color.main_font_content))
                    .itemPressColor(Color.parseColor("#2d3246"))
                    .lvBgColor(Color.parseColor("#262a3e"))
                    .dividerColor(getResources().getColor(R.color.transparent));
        }
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!symbol.equals(symbolName[position])) {
                    symbol = symbolName[position];
                    tvSymbol.setText(symbol);
                    page = 1;
                    getList(true, cmd);
                }
                normalDialog.dismiss();
            }
        });
        normalDialog.show();
    }



}
