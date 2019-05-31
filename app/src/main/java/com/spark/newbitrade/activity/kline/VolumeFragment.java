package com.spark.newbitrade.activity.kline;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.VolumeAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.base.BaseFragment;
import com.spark.newbitrade.entity.VolumeInfo;
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
 * kline_成交
 * Created by daiyy on 2018/6/4 0004.
 */

public class VolumeFragment extends BaseFragment {
    @BindView(R.id.tvNumberV)
    TextView tvNumberV;
    @BindView(R.id.tvPriceV)
    TextView tvPriceV;
    @BindView(R.id.rvVolume)
    RecyclerView recyclerView;
    @BindView(R.id.volumeBar)
    ProgressBar volumeBar;
    private String symbol;
    private ArrayList<VolumeInfo> objList;
    private VolumeAdapter adapter;
    private Gson gson;
    HashMap<String, String> map;
    private int baseCoinScale;
    private int coinScale;


    public static VolumeFragment getInstance(String symbol, int baseCoinScale, int coinScale) {
        VolumeFragment volumeFragment = new VolumeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        bundle.putInt("baseCoinScale", baseCoinScale);
        bundle.putInt("coinScale", coinScale);
        volumeFragment.setArguments(bundle);
        return volumeFragment;
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
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.UN_SUBSCRIBE_KLINE, new Gson().toJson(map).getBytes()));
        EventBus.getDefault().unregister(this);
    }

    private void startTCP() {
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.SUBSCRIBE_KLINE, new Gson().toJson(map).getBytes()));
    }


    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        String json = response.getResponse();
        switch (response.getCmd()) {
            case PUSH_TRADE:
                try {
                    LogUtils.d("成交 推送过来的信息==" + json);
                    if (gson == null)
                        gson = new Gson();
                    VolumeInfo volumeInfo = gson.fromJson(response.getResponse(), new TypeToken<VolumeInfo>() {
                    }.getType());
                    String strSymbol = volumeInfo.getSymbol();
                    if (strSymbol != null && strSymbol.equals(symbol)) {
                        objList.add(0, volumeInfo);
                        objList = initData(objList);
                        adapter.setObjList(objList);
                        adapter.setBaseCoinScale(baseCoinScale, coinScale);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LATEST_TRADE:
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        ArrayList<VolumeInfo> volumeInfos = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<ArrayList<VolumeInfo>>() {
                        }.getType());
                        getVolumeSuccess(volumeInfos);
                    } else {
                        dpPostFail(jsonObject.optInt("code"), jsonObject.optString("message"));
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
        return R.layout.fragment_volume;
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
        objList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        recyclerView.setLayoutManager(manager);
    }


    @Override
    protected void initData() {
        activity = getActivity();
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            baseCoinScale = bundle.getInt("baseCoinScale");
            coinScale = bundle.getInt("coinScale");
            LogUtils.i("baseCoinScale==" + baseCoinScale + ",,,coinScale==" + coinScale);
            String[] strings = symbol.split("/");
            tvNumberV.setText(getResources().getString(R.string.number) + "(" + strings[0] + ")");
            tvPriceV.setText(getResources().getString(R.string.price) + "(" + strings[1] + ")");
            map = new HashMap<>();
            map.put("symbol", symbol);
            getVolume();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        LogUtils.i("loadData");
    }

    /**
     * 获取成交数据
     */
    private void getVolume() {
        if (volumeBar != null)
            volumeBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("size", "20");
        String json = new Gson().toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.LATEST_TRADE, json.getBytes()));
    }

    /**
     * 填充数据
     */
    private ArrayList<VolumeInfo> initData(ArrayList<VolumeInfo> objList) {
        ArrayList<VolumeInfo> volumeInfos = new ArrayList<>();
        if (objList != null) {
            for (int i = 0; i < 20; i++) {
                if (objList.size() > i) { // list里有数据
                    volumeInfos.add(objList.get(i));
                } else {
                    VolumeInfo volumeInfo = new VolumeInfo();
                    volumeInfo.setTime(-1);
                    volumeInfo.setSide(-1);
                    volumeInfo.setPrice(-1);
                    volumeInfo.setAmount(-1);
                    volumeInfos.add(volumeInfo);
                }
            }
        } else {
            for (int i = 0; i < 20; i++) {
                VolumeInfo volumeInfo = new VolumeInfo();
                volumeInfo.setTime(-1);
                volumeInfo.setSide(-1);
                volumeInfo.setPrice(-1);
                volumeInfo.setAmount(-1);
                volumeInfos.add(volumeInfo);
            }
        }
        return volumeInfos;
    }

    public void getVolumeSuccess(ArrayList<VolumeInfo> list) {
        if (volumeBar != null)
            volumeBar.setVisibility(View.GONE);
        objList = initData(list);
        if (adapter == null) {
            adapter = new VolumeAdapter(activity, objList);
            adapter.setBaseCoinScale(baseCoinScale, coinScale);
            recyclerView.setAdapter(adapter);
        }
        startTCP();
    }

    public void dpPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }
}
