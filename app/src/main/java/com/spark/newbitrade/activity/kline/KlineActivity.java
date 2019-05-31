package com.spark.newbitrade.activity.kline;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.activity.main.MainActivity;
import com.spark.newbitrade.activity.mychart.DataParse;
import com.spark.newbitrade.activity.mychart.KSocketBean;
import com.spark.newbitrade.activity.mychart.LoadDialog;
import com.spark.newbitrade.activity.mychart.SPManagerUtils;
import com.spark.newbitrade.adapter.PagerAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.base.BaseFragment;
import com.spark.newbitrade.entity.Currency;
import com.spark.newbitrade.entity.Favorite;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.SocketMessage;
import com.spark.newbitrade.serivce.SocketResponse;
import com.spark.newbitrade.ui.CustomViewPager;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

public class KlineActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.tvCurrencyName)
    TextView tvCurrencyName;
    @BindView(R.id.kDataText)
    TextView mDataText;
    @BindView(R.id.kDataOne)
    TextView mDataOne;
    @BindView(R.id.kCount)
    TextView kCount;
    @BindView(R.id.kUp)
    TextView kUp;
    @BindView(R.id.kLow)
    TextView kLow;
    @BindView(R.id.kLandDataText)
    TextView kLandDataText;
    @BindView(R.id.kLandDataOne)
    TextView kLandDataOne;
    @BindView(R.id.kLandCount)
    TextView kLandCount;
    @BindView(R.id.kLandUp)
    TextView kLandUp;
    @BindView(R.id.kLandLow)
    TextView kLandLow;
    @BindView(R.id.kLandRange)
    TextView kLandRange;
    @BindView(R.id.kRange)
    TextView kRange;
    @BindView(R.id.tv_collect)
    TextView mTvCollect; // 收藏的意思
    @BindView(R.id.tvMore)
    TextView tvMore;
    @BindView(R.id.tvIndex)
    TextView tvIndex;

    @BindView(R.id.llVertical)
    LinearLayout llVertical;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.vpDepth)
    CustomViewPager depthPager;
    @BindView(R.id.llDepthTab)
    TabLayout depthTab;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.ivFullScreen)
    ImageView ivFullScreen;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.llAllTab)
    LinearLayout llAllTab;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tv30M)
    TextView tv30M;
    @BindView(R.id.tvWeek)
    TextView tvWeek;
    @BindView(R.id.tvJan)
    TextView tvJan;
    @BindView(R.id.tabPop)
    LinearLayout tabPop;
    @BindView(R.id.tvMA)
    TextView tvMA;
    @BindView(R.id.tvBOLL)
    TextView tvBOLL;
    @BindView(R.id.tvMainHide)
    TextView tvMainHide;
    @BindView(R.id.tvMACD)
    TextView tvMACD;
    @BindView(R.id.tvKDJ)
    TextView tvKDJ;
    @BindView(R.id.tvRSI)
    TextView tvRSI;
    @BindView(R.id.tvChildHide)
    TextView tvChildHide;
    @BindView(R.id.llIndex)
    LinearLayout llIndex;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.llState)
    LinearLayout llState;
    @BindView(R.id.llLandText)
    LinearLayout llLandText;
    @BindView(R.id.mFrameLayout)
    FrameLayout mFrameLayout;
    private String resolution;
    private List<Currency> currencies = new ArrayList<>();
    private ProgressBar mProgressBar;
    private boolean isFace = false;
    private List<BaseFragment> fragmentBase = new ArrayList<>();
    private PagerAdapter adapter;
    private int coinScale;
    private boolean isVertical; // 横竖屏
    private int childType = -1;
    private int mainType = 1;
    private Currency mCurrency; // 当前的Currency
    private String symbol = ""; // 传入的币种
    private int baseCoinScale; //
    private List<Fragment> fragments = new ArrayList<>(); // 这个是K线的Fragment

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isVertical) {
                finish();
            } else {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 切换横竖屏
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams params = mFrameLayout.getLayoutParams();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            isVertical = false;
            llState.setVisibility(View.GONE);
            llLandText.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.GONE);
            depthTab.setVisibility(View.GONE);
            depthPager.setVisibility(View.GONE);
            params.height = FrameLayout.LayoutParams.MATCH_PARENT;
            mFrameLayout.setLayoutParams(params);
            llVertical.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) { // 竖屏
            isVertical = true;
            llState.setVisibility(View.VISIBLE);
            llLandText.setVisibility(View.INVISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            depthTab.setVisibility(View.VISIBLE);
            depthPager.setVisibility(View.VISIBLE);
            params.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics()));
            mFrameLayout.setLayoutParams(params);
            llVertical.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.UN_SUBSCRIBE_THUMB, null));
        EventBus.getDefault().unregister(this);
    }

    private void startTCP() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.SUBSCRIBE_THUMB, null));
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_kline;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        isVertical = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            baseCoinScale = bundle.getInt("baseCoinScale");
            coinScale = bundle.getInt("coinScale");
            tvCurrencyName.setText(symbol);
            isFace = addFace(symbol);
            if (symbol != null) {
                String[] s = symbol.split("/");
                tvBuy.setText(String.valueOf(getString(R.string.text_buy_in) + s[0]));
                tvSell.setText(String.valueOf(getString(R.string.text_sale_out) + s[0]));
            }
        }
        fragments.add(KLineFragment.newInstance(true, baseCoinScale)); // 这个是分时的
        fragments.add(KLineFragment.newInstance(baseCoinScale)); // 1分钟
        fragments.add(KLineFragment.newInstance(baseCoinScale)); // 5分钟
        fragments.add(KLineFragment.newInstance(baseCoinScale)); // 1小时
        fragments.add(KLineFragment.newInstance(baseCoinScale)); // 1天
        fragments.add(KLineFragment.newInstance(baseCoinScale)); // 1周或一个月
        //默认显示并加载1分钟的
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, fragments.get(1)).commit();
        mRadioGroup.setOnCheckedChangeListener(this);
        tvMA.setSelected(true);
        tvChildHide.setSelected(true);
        if (isFace) { // 已经收藏
            mTvCollect.setText(getString(R.string.text_collected));
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_hover), null, null);
        } else {
            mTvCollect.setText(getString(R.string.text_add_favorite));
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_normal), null, null);
        }
        startTCP();
        // 默认加载1分钟的数据
        loadKineData(1);
        initDepthData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private int currentSymType = 1; // 当前所选币种
    private int index;
    private int currentTabIndex = 1;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_fen: // 分时线
                currentSymType = 0;
                index = 0;
                break;
            case R.id.rb_oneM: // 1分钟
                currentSymType = 1;
                index = 1;
                break;
            case R.id.rb_fiveM: // 5分钟
                currentSymType = 2;
                index = 2;
                break;
            case R.id.rb_oneH: // 1小时
                currentSymType = 3;
                index = 3;
                break;
            case R.id.rb_oneD: // 1天
                currentSymType = 4;
                index = 4;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(fragments.get(currentTabIndex));
            if (!fragments.get(index).isAdded()) {
                transaction.add(R.id.fl_content, fragments.get(index));
                // 第一次展示，进行数据的加载 以后再次点击就不重新加载数据了
                //loadKineData(currentSymType);
            }
            // 这样的话就是每次切换tab都进行网络请求数据了
            loadKineData(currentSymType);
            transaction.show(fragments.get(index)).commit();
        }
        currentTabIndex = index;
        // 隐藏更多和指示的布局
        tvMore.setText(R.string.more);
        tabPop.setVisibility(View.GONE);
        llIndex.setVisibility(View.GONE);
        tvMore.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
        tvIndex.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
    }

    private void loadKineData(int type) {
        Long to = System.currentTimeMillis();
        Long from = to;
        resolution = "";
        switch (type) {
            case 0: // 分时
                from = to - 24L * 60 * 60 * 1000;
                resolution = 1 + "";
                break;
            case 1: // 1分钟
                from = to - 24L * 60 * 60 * 1000;
                resolution = 1 + "";
                break;
            case 2: // 5分钟
                from = to - 2 * 24L * 60 * 60 * 1000;//前两天数据
                resolution = 5 + "";
                break;
            case 3: // 1小时
                from = to - 24 * 24L * 60 * 60 * 1000;
                resolution = 1 + "H";
                break;
            case 4: // 1天
                from = to - 60 * 24L * 60 * 60 * 1000;
                resolution = 1 + "D";
                break;
            case 5: // 15分钟
                from = to - 15 * 2L * 6 * 60 * 60 * 1000;
                resolution = 15 + "";
                break;
            case 6: // 30分钟
                from = to - 12 * 24L * 60 * 60 * 1000;
                resolution = 30 + "";
                break;
            case 7: // 1周
                from = to - 730 * 24L * 60 * 60 * 1000;
                resolution = 1 + "W";
                break;
            case 8: // 1月
                from = to - 1095 * 24L * 60 * 60 * 1000;
                resolution = 1 + "M";
                break;
            default: // 默认1分钟
                from = to - 24L * 60 * 60 * 1000;
                resolution = 1 + "";
                break;
        }
        // 1先获取缓存的数据
        String data = SPManagerUtils.getInstance().getString(symbol + resolution);
        if (!TextUtils.isEmpty(data)) { // 数据不为空的话，就处理
            try {
                KDataSuccess(new JSONObject(data).getJSONArray("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("demo", "111111111--" + resolution + data);
        // 2再获取网络的数据
        getKLineData(symbol, from, to, resolution);
    }

    @Override
    protected void loadData() {
        super.loadData();
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.GET_ALL_THUMB, null));
    }


    @OnClick({R.id.ivBack, R.id.ivFullScreen, R.id.tvSell, R.id.tvBuy, R.id.tv_collect, R.id.tvMore, R.id.tvIndex,
            R.id.tv15M, R.id.tv30M, R.id.tvWeek, R.id.tvJan, R.id.tvMA, R.id.tvBOLL, R.id.tvMainHide, R.id.tvMACD, R.id.tvKDJ, R.id.tvRSI, R.id.tvChildHide})
    @Override
    protected void setOnClickListener(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                return;
            case R.id.ivFullScreen:
                if (isVertical) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                }
                return;
            case R.id.tvSell:
            case R.id.tvBuy:
                Bundle bundle = new Bundle();
                bundle.putString("symbol", symbol);
                if (view.getId() == R.id.tvBuy) {
                    bundle.putInt("type", 1);
                } else {
                    bundle.putInt("type", 2);
                }
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                return;
            case R.id.tv_collect:
                MainActivity.isAgain = true;
                deleteOrCollect();
                return;
            case R.id.tvMore: // 点击更多
                if (tabPop.getVisibility() == View.VISIBLE) {
                    tabPop.setVisibility(View.GONE);
                    tvMore.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
                } else {
                    tvMore.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_hover));
                    tvIndex.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
                    tabPop.setVisibility(View.VISIBLE);
                    llIndex.setVisibility(View.GONE);
                }
                break;
            case R.id.tvIndex: // 点击指标
                if (llIndex.getVisibility() == View.VISIBLE) {
                    llIndex.setVisibility(View.GONE);
                    tvIndex.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
                } else {
                    tvMore.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
                    tvIndex.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_hover));
                    llIndex.setVisibility(View.VISIBLE);
                    tabPop.setVisibility(View.GONE);
                }
                break;
            case R.id.tv15M: // 15分钟
            case R.id.tv30M: // 30 分钟
            case R.id.tvWeek: // 1周
            case R.id.tvJan: // 1月
                mRadioGroup.clearCheck(); // 清除上次RadioGroup点击的状态
                if (view.getId() == R.id.tv15M) {
                    currentSymType = 5;
                    index = 5;
                    tvMore.setText(R.string.sw_min);
                } else if (view.getId() == R.id.tv30M) {
                    currentSymType = 6;
                    tvMore.setText(R.string.ts_min);
                } else if (view.getId() == R.id.tvWeek) {
                    tvMore.setText(R.string.weekly);
                    currentSymType = 7;
                } else {
                    tvMore.setText(R.string.jan);
                    currentSymType = 8;
                }
                tabPop.setVisibility(View.GONE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(fragments.get(currentTabIndex));
                if (!fragments.get(5).isAdded()) {
                    transaction.add(R.id.fl_content, fragments.get(5));
                }
                transaction.show(fragments.get(5)).commit();
                currentTabIndex = 5; // 这里需要设置为5，不然无法隐藏
                loadKineData(currentSymType);
                break;
            case R.id.tvMA: // MA
            case R.id.tvBOLL: // BOLL
            case R.id.tvMainHide: // 隐藏
                tvMA.setSelected(false);
                tvBOLL.setSelected(false);
                tvMainHide.setSelected(false);
                if (view.getId() == R.id.tvMA) {
                    tvMA.setSelected(true);
                    mainType = 1;
                } else if (view.getId() == R.id.tvBOLL) {
                    tvBOLL.setSelected(true);
                    mainType = 2;
                } else {
                    mainType = 0;
                    tvMainHide.setSelected(true);
                }
                setMaChild(1, mainType);
                tvIndex.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
                llIndex.setVisibility(View.GONE);
                break;
            case R.id.tvMACD: // MACD
            case R.id.tvKDJ: // KDJ
            case R.id.tvRSI: // RSI
            case R.id.tvChildHide: // 隐藏
                tvMACD.setSelected(false);
                tvKDJ.setSelected(false);
                tvRSI.setSelected(false);
                tvChildHide.setSelected(false);
                if (view.getId() == R.id.tvMACD) {
                    tvMACD.setSelected(true);
                    childType = 0;
                } else if (view.getId() == R.id.tvKDJ) {
                    tvKDJ.setSelected(true);
                    childType = 1;
                } else if (view.getId() == R.id.tvRSI) {
                    tvRSI.setSelected(true);
                    childType = 2;
                } else {
                    childType = -1;
                    tvChildHide.setSelected(true);
                }
                setMaChild(2, childType);
                tvIndex.setBackground(getResources().getDrawable(R.drawable.shape_bg_kline_tab_normal));
                llIndex.setVisibility(View.GONE);
                break;
        }

    }

    /**
     * 添加收藏或取消收藏
     */
    private void deleteOrCollect() {
        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast(getString(R.string.text_login_first));
            showActivity(LoginActivity.class, null);
            return;
        }
        displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        String json = new Gson().toJson(map);
        if (isFace) { // 删除
            EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.DELETE_FAVOR, json.getBytes()));
        } else {
            EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.ADD_FAVOR, json.getBytes()));
        }
    }

    /**
     * 设置主图 和 副图的一些设置
     */
    private void setMaChild(int type, int childType) {
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i).isAdded()) {
                KLineFragment fragment = (KLineFragment) fragments.get(i);
                if (type == 1) { // 设置主图
                    fragment.setMainDrawType(childType);
                } else if (type == 2) { // 设置副图
                    fragment.setChildDrawType(childType);
                }
            }
        }
    }


    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        if (response.getCmd() == null) return;
        String json = response.getResponse();
        JSONObject object = null;
        switch (response.getCmd()) {
            case PUSH_KLINE: // K线的推送
                try {
                    KSocketBean kBean = new Gson().fromJson(response.getResponse(), KSocketBean.class);
                    KLineEntity lineEntity = DataParse.clone(kBean);
                    if (kCurrentEntityList == null || kCurrentEntityList.isEmpty()) return;
                    KLineEntity lastEntity = kCurrentEntityList.get(kCurrentEntityList.size() - 1);
                    // 我是拿最后一个的时间然后推送来的做比较，如果推送来的时间小于或等于最后一个则不处理
                    Log.d("demo", "JJJJ" + lineEntity.getDate() + " ------- " + lastEntity.getDate() + "---" + kCurrentEntityList.size());
                    if (KSocketBean.compare(lineEntity.getDate(), lastEntity.getDate(), currentSymType))  //
                    {
                        kCurrentEntityList.add(lineEntity);
                        DataHelper.calculate(kCurrentEntityList);
                        Log.d("demo", "JJJ-----------------------------" + lineEntity.getDate());
                        if ("m1".equals(kBean.getPeriod())) { // 一分钟的
                            ((KLineFragment) fragments.get(currentSymType)).setKFooterData(kCurrentEntityList, false);
                        } else if ("m5".equals(kBean.getPeriod())) {
                            ((KLineFragment) fragments.get(currentSymType)).setKFooterData(kCurrentEntityList, false);
                            // 5分钟
                        } else if ("h1".equals(kBean.getPeriod())) { // 1小时
                            ((KLineFragment) fragments.get(currentSymType)).setKFooterData(kCurrentEntityList, false);
                        } else if ("d1".equals(kBean.getPeriod())) { // 1天
                            ((KLineFragment) fragments.get(currentSymType)).setKFooterData(kCurrentEntityList, false);
                        } else if ("m15".equals(kBean.getPeriod())) { // 15分钟
                            ((KLineFragment) fragments.get(5)).setKFooterData(kCurrentEntityList, false);
                        } else if ("m30".equals(kBean.getPeriod())) { // 30分钟
                            ((KLineFragment) fragments.get(5)).setKFooterData(kCurrentEntityList, false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PUSH_THUMB: // 缩略图推送
                try {
                    Currency temp = new Gson().fromJson(json, Currency.class);
                    for (Currency currency : currencies) {
                        if (temp.getSymbol().equals(currency.getSymbol())) {
                            Currency.shallowClone(currency, temp);
                            break;
                        }
                    }
                    setCurrentcy(currencies);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.i("k线界面  缩略图推送 解析出错");
                }
                break;
            case GET_KLINE_HISTORY: // 获取数据
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        KDataSuccess(object.getJSONArray("data"));
                        // 这里进行数据的存贮
                        // 但是但是 这样写是有问题，没想到好的方法，就按照错误的方法来处理了
                        SPManagerUtils.getInstance().putString(symbol + resolution, response.getResponse()); //
                        Log.d("demo", "---" + response.getResponse());
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtils.i("k线界面  K线 推送 解析出错");
                } finally {
                    hideDialog();
                }
                break;
            case GET_ALL_THUMB:
                hideLoadingPopup();
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        List<Currency> currencies = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Currency>>() {
                        }.getType());
                        allCurrencySuccess(currencies);
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            case ADD_FAVOR:
            case DELETE_FAVOR:
                hideLoadingPopup();
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        doDeleteOrCollectSuccess(object.optString("message"));
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
        }
    }

    // 展示个弹窗，本来想用BaseActivity的Pop但是总崩
    LoadDialog mDialog;

    private void showDialog() {
        if (mDialog == null) {
            mDialog = new LoadDialog(this);
        }
        mDialog.show();
    }

    private void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    /**
     * 获取网络数据
     *
     * @param symbol     币种
     * @param from       开始时间
     * @param to         结束时间
     * @param resolution 描述
     */
    private void getKLineData(String symbol, Long from, Long to, String resolution) {
        // 这里显示一个弹窗
        showDialog();
        kCurrentEntityList.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        map.put("from", from + "");
        map.put("to", to + "");
        map.put("resolution", resolution);
        String json = new Gson().toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.GET_KLINE_HISTORY, json.getBytes()));
    }

    private ArrayList<KLineEntity> kCurrentEntityList = new ArrayList<>(); //

    private void KDataSuccess(final JSONArray obj) {
        // 下面的设置一定需要在主线程中设置 否则会有问题
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<KLineEntity> data = DataParse.getAll(obj, currentSymType);
                if (data != null) {
                    Log.d("jiejie", "JJJ----------------------------");
                    kCurrentEntityList.clear();
                    kCurrentEntityList.addAll(data); // 这里新加一条数据有问题，所以尝试这么写的
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        KLineFragment fragment = (KLineFragment) fragments.get(currentTabIndex); // 找到对应的Fragment，再在对应的Fragment上改变数据
                        fragment.setMainDrawType(mainType); // 设置主图
                        fragment.setChildDrawType(childType); // 设置幅图
                        fragment.setKFooterData(data, true); // 添加数据
                    }
                });
            }
        }).start();
    }

    /**
     * 头部显示内容
     *
     * @param objs
     */
    private void setCurrentcy(List<Currency> objs) {
        try {
            for (Currency currency : objs) {
                if (symbol.equals(currency.getSymbol())) {
                    mCurrency = currency;
                    break;
                }
            }
            if (mCurrency != null) {
                String strUp = MathUtils.getRundNumber(mCurrency.getHigh(), baseCoinScale, null);
                String strLow = MathUtils.getRundNumber(mCurrency.getLow(), baseCoinScale, null);
                String strCount = String.valueOf(mCurrency.getVolume());
                Double douChg = mCurrency.getChg();
                String strRang = MathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%";
                String strDataText = "≈" + MathUtils.getRundNumber(mCurrency.getClose() * MainActivity.rate * mCurrency.getBaseUsdRate(),
                        2, null) + GlobalConstant.CNY;
                String strDataOne = MathUtils.getRundNumber(mCurrency.getClose(), baseCoinScale, null);
                kUp.setText(strUp);
                kLow.setText(strLow);
                kCount.setText(strCount);
                kRange.setText(getString(R.string.gains) + strRang);
                mDataOne.setText(strDataOne);
                mDataText.setText(strDataText);
                mDataOne.setTextColor(douChg < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kRange.setTextColor(douChg < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kLandRange.setTextColor(douChg < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kLandDataOne.setTextColor(douChg < 0 ? getResources().getColor(R.color.main_font_red) : getResources().getColor(R.color.main_font_green));
                kLandUp.setText(strUp);
                kLandLow.setText(strLow);
                kLandCount.setText(strCount);
                kLandRange.setText(getString(R.string.gains) + strRang);
                kLandDataOne.setText(strDataOne);
                kLandDataText.setText(strDataText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean addFace(String symbol) {
        for (Favorite favorite : MainActivity.mFavorte) {
            if (symbol.equals(favorite.getSymbol())) return true;
        }
        return false;
    }


    public void allCurrencySuccess(List<Currency> obj) {
        if (obj != null) {
            currencies.clear();
            currencies.addAll(obj);
            setCurrentcy(currencies);
        }
    }

    public void doDeleteOrCollectSuccess(String msg) {
        if (isFace) {
            ToastUtils.showToast(getString(R.string.text_cancel_success));
            mTvCollect.setText(getString(R.string.text_add_favorite));
            isFace = false;
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_normal), null, null);
        } else {
            ToastUtils.showToast(getString(R.string.text_add_success));
            isFace = true;
            mTvCollect.setText(getString(R.string.text_collected));
            mTvCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_collect_hover), null, null);
        }

    }


    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }


    /**
     * 初始化深度图数据
     */
    private void initDepthData() {
        fragmentBase.add(DepthFragment.getInstance(symbol, baseCoinScale, coinScale));
        fragmentBase.add(VolumeFragment.getInstance(symbol, baseCoinScale, coinScale));
        String[] tabArray = getResources().getStringArray(R.array.k_line_depth);
        List<String> tabs = new ArrayList<>(Arrays.asList(tabArray));
        depthPager.setAdapter(adapter = new PagerAdapter(getSupportFragmentManager(), fragmentBase, tabs));
        depthTab.setTabMode(TabLayout.MODE_FIXED);
        depthTab.setupWithViewPager(depthPager);
//        depthPager.setOffscreenPageLimit(fragments.size() - 1);
        depthPager.setCurrentItem(0);
    }


}
