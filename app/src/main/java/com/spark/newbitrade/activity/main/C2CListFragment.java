package com.spark.newbitrade.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.spark.library.otcSys.model.MessageResult;
import com.spark.library.otcSys.model.TradeDto;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.library.otc.model.AdvertiseShowVo;
import com.spark.library.otc.model.PageAdvertiseShowVo;
import com.spark.library.otc.model.QueryCondition;
import com.spark.library.otc.model.QueryParamAdvertiseShowVo;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.buy_or_sell.C2CBuyOrSellActivity;
import com.spark.newbitrade.activity.main.presenter.C2CListPresenterImpl;
import com.spark.newbitrade.activity.order.OrderDetailActivity;
import com.spark.newbitrade.activity.order.OrderFragment;
import com.spark.newbitrade.adapter.C2CListAdapter;
import com.spark.newbitrade.base.BaseLazyFragment;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.MyAdvertiseShowVo;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CListFragment extends BaseLazyFragment implements C2CListContract.C2CListView {
    public static final String TAG = C2CListFragment.class.getSimpleName();
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private List<AdvertiseShowVo> advertiseShowVoList = new ArrayList<>();
    private C2CListAdapter adapter;
    private String coinName;
    private int coinScale = 8;
    private C2CListPresenterImpl presenter;
    private int pageNo = 1;
    private int advertiseType = 1; // 卖
    private String country = "";
    private String payMode = "";
    private String lowQuota = "";
    private String highQuota = "";

    private TextView tvBuyType;
    private EditText etCount;
    private TextView tvCoinName;
    private TextView tvBuy;
    private int buyType = 1;//1按数量购买  2按金额购买

    public static C2CListFragment getInstance(String coinName, int coinScale) {
        C2CListFragment c2CFragment = new C2CListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("coinName", coinName);
        bundle.putInt("coinScale", coinScale);
        c2CFragment.setArguments(bundle);
        return c2CFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c_list;
    }

    @Override
    protected void initView() {
        super.initView();
        setShowBackBtn(true);
    }

    @Override
    protected void initData() {
        presenter = new C2CListPresenterImpl(this);
        initRvContent();
        Bundle bundle = getArguments();
        if (bundle != null) {
            coinName = bundle.getString("coinName");
            coinScale = bundle.getInt("coinScale");
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        Log.e("C2CList", "loadData: " + getUserVisibleHint());
        if (StringUtils.isNotEmpty(coinName)) {
            isNeedLoad = false;
            getC2cList(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (getUserVisibleHint()) {
//            loadData();
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
                getC2cList(false);
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo = pageNo + 1;
                getC2cList(false);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (MyApplication.getApp().isLogin()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("coinScale", coinScale);

                    AdvertiseShowVo advertiseShowVo = advertiseShowVoList.get(position);
                    MyAdvertiseShowVo myAdvertiseShowVo = new MyAdvertiseShowVo(
                            advertiseShowVo.getMemberId(),
                            advertiseShowVo.getAdvertiseType(),
                            advertiseShowVo.getCoinName(),
                            advertiseShowVo.getCountry(),
                            advertiseShowVo.getCreateTime(),
                            advertiseShowVo.getId(),
                            advertiseShowVo.getLocalCurrency(),
                            advertiseShowVo.getMaxLimit(),
                            advertiseShowVo.getMinLimit(),
                            advertiseShowVo.getNumber(),
                            advertiseShowVo.getPayMode(),
                            advertiseShowVo.getPlanFrozenFee(),
                            advertiseShowVo.getPremiseRate(),
                            advertiseShowVo.getPrice(),
                            advertiseShowVo.getPriceType(),
                            advertiseShowVo.getRangeTimeOrder(),
                            advertiseShowVo.getRangeTimeSuccessOrder(),
                            advertiseShowVo.getRealName(),
                            advertiseShowVo.getRemainAmount(),
                            advertiseShowVo.getRemainFrozenFee(),
                            advertiseShowVo.getRemark(),
                            advertiseShowVo.getTimeLimit(),
                            advertiseShowVo.getUsername());

                    bundle.putSerializable("myAdvertiseShowVo", myAdvertiseShowVo);
//                bundle.putString("id", advertiseShowVoList.get(position).getId() + "");
//                bundle.putString("avatar", advertiseShowVoList.get(position).getAvatar());
//                Intent intent = new Intent(getmActivity(), C2CBuyOrSellActivity.class);
//                intent.putExtras(bundle);
//                getmActivity().startActivityForResult(intent, 1);
                    getmActivity().showActivity(C2CBuyOrSellActivity.class, bundle, 1);
                } else {
                    ToastUtils.showToast(getString(R.string.text_login_first));
                    showActivity(LoginActivity.class, null);
                }
            }
        });

    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new C2CListAdapter(getActivity(), R.layout.item_c2c_list, advertiseShowVoList);
        adapter.bindToRecyclerView(rvContent);
        adapter.isFirstOnly(true);
        adapter.setEnableLoadMore(false);
        addHeadView(1);
    }

    /**
     * 获取列表数据
     */
    public void getC2cList(boolean isShow) {
        if (StringUtils.isNotEmpty(coinName)) {
            if (isShow)
                showLoading();

//            HashMap<String, String> map = new HashMap<>();
//            map.put("pageNo", pageNo + "");
//            map.put("pageSize", GlobalConstant.PageSize + "");
//            map.put("advertiseType", advertiseType + "");
//            map.put("unit", coinName);
//            map.put("conuty", conuty);
//            map.put("payMode", payMode);
//            map.put("lowQuota", lowQuota);
//            map.put("highQuota", highQuota);
//            presenter.advertise(map);

            QueryParamAdvertiseShowVo queryParam = new QueryParamAdvertiseShowVo();
            queryParam.setPageIndex(pageNo);
            queryParam.setPageSize(GlobalConstant.PageSize);

            List<QueryCondition> queryConditionList = new ArrayList<>();
            queryConditionList.add(getQueryCondition("coinName", coinName, "and", "="));
            queryConditionList.add(getQueryCondition("advertiseType", advertiseType, "and", "="));

            if (StringUtils.isNotEmpty(payMode)) {
                queryConditionList.add(getQueryCondition("payMode", payMode, "and", "="));
            }
            if (StringUtils.isNotEmpty(country)) {
                queryConditionList.add(getQueryCondition("country", country, "and", "="));
            }
            if (StringUtils.isNotEmpty(lowQuota)) {
                queryConditionList.add(getQueryCondition("minLimit", lowQuota, "and", "="));
            }
            if (StringUtils.isNotEmpty(highQuota)) {
                queryConditionList.add(getQueryCondition("maxLimit", highQuota, "and", "="));
            }
            queryParam.setQueryList(queryConditionList);

            presenter.findPageForCoin(queryParam);
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

    public void setSelectResult(String country, String payMode, String lowQuota, String highQuota) {
        this.country = country;
        this.payMode = payMode;
        this.lowQuota = lowQuota;
        this.highQuota = highQuota;
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getC2cList(true);
    }

    public void setAdvertiseType(int advertiseType) {
        this.advertiseType = advertiseType;
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getC2cList(true);
    }

    @Override
    public void findPageForCoinSuccess(PageAdvertiseShowVo obj) {
        hideAll();
        if (obj == null) return;
        try {
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            List<AdvertiseShowVo> list = obj.getRecords();
            if (list != null && list.size() > 0) {
                if (pageNo == 1) {
                    //addHeadView(1);
                    this.advertiseShowVoList.clear();
                } else {
                    adapter.loadMoreEnd();
                }
                this.advertiseShowVoList.addAll(list);
                adapter.notifyDataSetChanged();
            } else {
                if (pageNo == 1) {
                    this.advertiseShowVoList.clear();
                    addHeadView(0);
                    adapter.notifyDataSetChanged();
                }
            }
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        super.dealError(httpErrorEntity);
        hideAll();
        adapter.setEnableLoadMore(true);
    }

    @Override
    public void dealError(VolleyError volleyError) {
        super.dealError(volleyError);
        hideAll();
        adapter.setEnableLoadMore(true);
    }

    private void hideAll() {
        hideLoading();
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }

    //    @Override
//    public void advertiseSuccess(C2C obj) {
//        hideLoadingPopup();
//        try {
//            adapter.setEnableLoadMore(true);
//            adapter.loadMoreComplete();
//            refreshLayout.setEnabled(true);
//            refreshLayout.setRefreshing(false);
//            List<C2C.C2CBean> c2cs = obj.getRecords();
//            if (c2cs != null && c2cs.size() > 0) {
//                if (pageNo == 1) {
//                    this.c2cs.clear();
//                } else {
//                    adapter.loadMoreEnd();
//                }
//                this.c2cs.addAll(c2cs);
//                adapter.notifyDataSetChanged();
//            } else {
//                if (pageNo == 1) {
//                    this.c2cs.clear();
//                    adapter.setEmptyView(R.layout.empty_no_message);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//            adapter.disableLoadMoreIfNotFullPage();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void advertiseFail(Integer code, String toastMessage) {
//        hideLoadingPopup();
//        adapter.setEnableLoadMore(true);
//        refreshLayout.setEnabled(true);
//        refreshLayout.setRefreshing(false);
//        NetCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
//    }

    //type 0 空列表 1非空
    private void addHeadView(int type) {
        View headerView = getLayoutInflater().inflate(R.layout.view_buy, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout emptyLayout = headerView.findViewById(R.id.emptyLayout);
        if (type == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            adapter.setEmptyView(headerView);
        } else {
            emptyLayout.setVisibility(View.GONE);
            adapter.addHeaderView(headerView);
        }

        tvBuyType = headerView.findViewById(R.id.tvBuyType);
        etCount = headerView.findViewById(R.id.etCount);
        tvCoinName = headerView.findViewById(R.id.tvCoinName);
        tvBuy = headerView.findViewById(R.id.tvBuy);

        tvBuyType.setText("按金额购买");
        tvCoinName.setText(coinName);
        etCount.setHint("请输入购买数量");
        buyType = 1;
        tvBuyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvBuyType.getText().toString().equals("按金额购买")) {
                    tvBuyType.setText("按数量购买");
                    tvCoinName.setText("CNY");
                    etCount.setHint("请输入购买金额");
                    buyType = 2;
                } else {
                    tvBuyType.setText("按金额购买");
                    tvCoinName.setText(coinName);
                    etCount.setHint("请输入购买数量");
                    buyType = 1;
                }
            }
        });

        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = StringUtils.getText(etCount);
                if (StringUtils.isEmpty(count)) {
                    ToastUtils.showToast("请输入购买数量或金额");
                } else {
                    TradeDto tradeDto = new TradeDto();
                    if (buyType == 1) {//1按数量购买  2按金额购买
                        tradeDto.setTradeType(0);
                        tradeDto.setAmount(new BigDecimal(count));
                        tradeDto.setCoinName(coinName);
                        tradeDto.setCurrency(GlobalConstant.CNY);
                        tradeDto.setMoney(null);
                    } else {
                        tradeDto.setTradeType(1);
                        tradeDto.setAmount(null);
                        tradeDto.setCoinName(coinName);
                        tradeDto.setCurrency(GlobalConstant.CNY);
                        tradeDto.setMoney(new BigDecimal(count));
                    }
                    presenter.createOrder(tradeDto);
                }
            }
        });
    }

    @Override
    public void createOrderSuccess(MessageResult response) {
        if (response != null && response.getData() != null) {
            try {
                String gson = new Gson().toJson(response.getData());
                JSONObject jsonObject = new JSONObject(gson);
                String orderSn = jsonObject.getString("orderSn").toString();
                Bundle bundle = new Bundle();
                bundle.putString("orderSn", orderSn);
                bundle.putSerializable("status", OrderFragment.Status.UNPAID);
                showActivity(OrderDetailActivity.class, bundle, 1);
            } catch (Exception e) {
                ToastUtils.showToast("订单解析错误");
            }
        } else {
            ToastUtils.showToast("创建订单失败，请重试");
        }
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        adapter.setEnableLoadMore(false);
        pageNo = 1;
        getC2cList(false);
    }
}
