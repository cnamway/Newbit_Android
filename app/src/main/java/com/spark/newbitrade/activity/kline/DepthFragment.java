package com.spark.newbitrade.activity.kline;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.DepthAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.base.BaseFragment;
import com.spark.newbitrade.entity.DepthResult;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.SocketMessage;
import com.spark.newbitrade.serivce.SocketResponse;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.NetCodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

/**
 * kline_深度
 * Created by daiyy on 2018/6/4 0004.
 */

public class DepthFragment extends BaseFragment {
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvNumberR)
    TextView tvNumberR;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.rvDepth)
    RecyclerView recyclerView;
    @BindView(R.id.depthBar)
    ProgressBar depthBar;
    private String symbol;
    private DepthAdapter adapter;
    private ArrayList<DepthResult.DepthListInfo> objBuyList;
    private ArrayList<DepthResult.DepthListInfo> objSellList;
    private Gson gson;
    private HashMap<String, String> map;
    private int baseCoinScale;
    private int coinScale;


    public static DepthFragment getInstance(String symbol, int baseCoinScale, int coinScale) {
        DepthFragment depthFragment = new DepthFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        bundle.putInt("baseCoinScale", baseCoinScale);
        bundle.putInt("coinScale", coinScale);
        depthFragment.setArguments(bundle);
        return depthFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_MARKET, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE, new Gson().toJson(map).getBytes()));
        EventBus.getDefault().unregister(this);
    }

    private void startTCP() {
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_MARKET, ISocket.CMD.SUBSCRIBE_EXCHANGE_TRADE, new Gson().toJson(map).getBytes()));
    }


    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        switch (response.getCmd()) {
            case PUSH_EXCHANGE_DEPTH:
                try {
                    LogUtils.d("深度图 推送过来的信息==" + response.getResponse());
                    if (gson == null)
                        gson = new Gson();
                    DepthResult.DepthList result = gson.fromJson(response.getResponse(), new TypeToken<DepthResult.DepthList>() {
                    }.getType());
                    String strSymbol = result.getSymbol();
                    if (strSymbol != null && strSymbol.equals(symbol)) {
                        ArrayList<DepthResult.DepthListInfo> depthListInfos = result.getItems();
                        int direct = result.getDirection();
                        if (direct == GlobalConstant.INT_SELL) {
                            objSellList.removeAll(objSellList);
                            objSellList = initData(depthListInfos);
                            adapter.setObjSellList(objSellList);
                            adapter.setBaseCoinScale(baseCoinScale, coinScale);
                            adapter.notifyDataSetChanged();
                        } else {
                            objBuyList.removeAll(objBuyList);
                            objBuyList = initData(depthListInfos);
                            adapter.setObjBuyList(objBuyList);
                            adapter.setBaseCoinScale(baseCoinScale, coinScale);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GET_TRADE_PLATE:
                String json = response.getResponse();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        DepthResult result = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), new TypeToken<DepthResult>() {
                        }.getType());
                        getDepthtSuccess(result);
                    } else {
                        dpPostFail(JSON_ERROR, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dpPostFail(JSON_ERROR, null);
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_depth;
    }

    @Override
    protected void initView() {
        intLv();
        recyclerView.setNestedScrollingEnabled(false);
    }

    /**
     * 初始化列表数据
     */
    private void intLv() {
        activity = getActivity();
        objBuyList = new ArrayList<>();
        objSellList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            baseCoinScale = bundle.getInt("baseCoinScale");
            coinScale = bundle.getInt("coinScale");
            LogUtils.i("baseCoinScale==" + baseCoinScale + ",,,coinScale==" + coinScale);
            String[] strings = symbol.split("/");
            tvNumber.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvNumberR.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvPrice.setText(getResources().getString(R.string.price) + "(" + strings[1] + ")");
            map = new HashMap<>();
            map.put("symbol", symbol);
            getDepth();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    /**
     * 获取深度图数据
     */
    private void getDepth() {
        if (depthBar != null)
            depthBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("size", "20");
        String json = new Gson().toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_MARKET, ISocket.CMD.GET_TRADE_PLATE, json.getBytes()));
//        presenter.getDepth(map);
    }

    /**
     * 填充数据
     */
    private ArrayList<DepthResult.DepthListInfo> initData(ArrayList<DepthResult.DepthListInfo> objList) {
        ArrayList<DepthResult.DepthListInfo> depthListInfos = new ArrayList<>();
        if (objList != null) {
            for (int i = 0; i < 20; i++) {
                if (objList.size() > i) { // list里有数据
                    depthListInfos.add(objList.get(i));
                } else {
                    DepthResult.DepthListInfo depthListInfo = new DepthResult().new DepthListInfo();
                    depthListInfo.setAmount(-1);
                    depthListInfo.setPrice(-1);
                    depthListInfos.add(depthListInfo);
                }
            }
        } else {
            for (int i = 0; i < 20; i++) {
                DepthResult.DepthListInfo depthListInfo = new DepthResult().new DepthListInfo();
                depthListInfo.setAmount(-1);
                depthListInfo.setPrice(-1);
                depthListInfos.add(depthListInfo);
            }
        }
        return depthListInfos;
    }


    public void getDepthtSuccess(DepthResult result) {
        ArrayList<DepthResult.DepthListInfo> askItems = null;
        ArrayList<DepthResult.DepthListInfo> bidItems = null;
        if (depthBar != null)
            depthBar.setVisibility(View.GONE);
        try {
            if (result != null) {
                askItems = result.getAsk();
                bidItems = result.getBid();
            }
            objSellList = initData(askItems);
            objBuyList = initData(bidItems);
            if (adapter == null) {
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                adapter = new DepthAdapter(activity);
                adapter.setObjSellList(objSellList);
                adapter.setObjBuyList(objBuyList);
                adapter.setWidth(width / 2);
                adapter.setBaseCoinScale(baseCoinScale, coinScale);
                LogUtils.i("屏幕宽度==" + (width / 2));
                recyclerView.setAdapter(adapter);
            }
            startTCP();
        } catch (Exception e) {
            e.printStackTrace();
            if (isAdded())
                LogUtils.i(getResources().getString(R.string.parse_error));
        }
    }

    public void dpPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
