package com.spark.newbitrade.activity.entrust;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.EntrustDetailAdapter;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Entrust;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.SocketMessage;
import com.spark.newbitrade.serivce.SocketResponse;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.NetCodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * 交易明细
 */
public class TrustDetailActivity extends BaseActivity {
    @BindView(R.id.mDetailType)
    TextView mDetailType; // 买入还是卖出
    @BindView(R.id.mDetailName)
    TextView mDetailName; // BTC/USDT
    @BindView(R.id.mDetailOne)
    TextView mDetailOne; // 成交总额
    @BindView(R.id.mDetailTwo)
    TextView mDetailTwo; // 成交均价
    @BindView(R.id.mDetailThree)
    TextView mDetailThree; // 成交量
    @BindView(R.id.detailLayout)
    LinearLayout mDetailLayout;
    @BindView(R.id.mDetailFour)
    TextView mDetailFour;
    @BindView(R.id.mDetailFive)
    TextView mDetailFive;
    @BindView(R.id.mDetailTotal)
    TextView mDetailTotal;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private Entrust.ListBean entrustHistory;
    private String symbol;
    private String orderId;
    private int coinScale;
    private int baseCoinScale;

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
        return R.layout.activity_trust_detail;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.my_order));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            orderId = bundle.getString("orderId");
            coinScale = bundle.getInt("coinScale");
            baseCoinScale = bundle.getInt("baseCoinScale");
            setTitle(symbol);
            mDetailName.setText(symbol);
            entrustHistory = (Entrust.ListBean) bundle.getSerializable("order");
            if (entrustHistory != null) {
                initEntrustData();
            }
        }
    }

    private void initEntrustData() {
        if (entrustHistory.getSide() == GlobalConstant.INT_BUY) {
            mDetailType.setText(MyApplication.getApp().getString(R.string.text_buy_in));
            mDetailType.setTextColor(ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_green));
        } else {
            mDetailType.setText(MyApplication.getApp().getString(R.string.text_sale_out));
            mDetailType.setTextColor(ContextCompat.getColor(MyApplication.getApp(), R.color.main_font_red));
        }
        mDetailTotal.setText(MyApplication.getApp().getString(R.string.text_total_deal) + "(" + entrustHistory.getBaseSymbol() + ")");
        mDetailFour.setText(MyApplication.getApp().getString(R.string.text_average_price) + "(" + entrustHistory.getBaseSymbol() + ")");
        mDetailFive.setText(MyApplication.getApp().getString(R.string.text_volume) + "(" + entrustHistory.getCoinSymbol() + ")");
        mDetailOne.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrustHistory.getTurnover(), 8, null)));
        if (entrustHistory.getTradedAmount() == 0.0 || entrustHistory.getTurnover() == 0.0) {
            mDetailTwo.setText(String.valueOf(0.0));
        } else {
            mDetailTwo.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber((entrustHistory.getTurnover() / entrustHistory.getTradedAmount()), 8, null)));
        }
        mDetailThree.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(entrustHistory.getTradedAmount(), coinScale, null)));
    }

    @Override
    protected void loadData() {
        super.loadData();
        getDetail();
    }

    /**
     * 获取交易详情
     */
    private void getDetail() {
        displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("orderId", orderId);
        String json = new Gson().toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.ORDER_DETAIL, json.getBytes()));
    }

    private void initRv(List<Entrust.ListBean.DetailBean> detail) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        EntrustDetailAdapter adapter = new EntrustDetailAdapter(detail);
        adapter.setBaseCoinScale(baseCoinScale, coinScale);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 委托详情
     *
     * @param response SocketResponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
        if (response.getCmd() == null) return;
        switch (response.getCmd()) {
            case ORDER_DETAIL:
                hideLoadingPopup();
                try {
                    JSONObject object = new JSONObject(response.getResponse());
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        List<Entrust.ListBean.DetailBean> detail = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Entrust.ListBean.DetailBean>>() {
                        }.getType());
                        if (detail != null && detail.size() > 0) {
                            initRv(detail);
                        } else {
                            mDetailLayout.setVisibility(View.GONE);
                        }
                    } else {
                        NetCodeUtils.checkedErrorCode((BaseActivity) activity, object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NetCodeUtils.checkedErrorCode((BaseActivity) activity, JSON_ERROR, null);
                }
                break;
            default:
                break;
        }
    }
}
