package com.spark.newbitrade.activity.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.aboutus.VersionContract;
import com.spark.newbitrade.activity.aboutus.VersionPresenter;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.newbitrade.activity.main.presenter.MainPresenter;
import com.spark.newbitrade.activity.setting.SettingContact;
import com.spark.newbitrade.activity.setting.SettingPresenterImpl;
import com.spark.newbitrade.activity.wallet.BaseWalletFragment;
import com.spark.newbitrade.adapter.PagerAdapter;
import com.spark.newbitrade.base.ActivityManage;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.base.BaseFragment;
import com.spark.newbitrade.base.BaseTransFragmentActivity;
import com.spark.newbitrade.entity.CasLoginEntity;
import com.spark.newbitrade.entity.ChatTipEvent;
import com.spark.newbitrade.entity.Currency;
import com.spark.newbitrade.entity.ExchangeLoginInfo;
import com.spark.newbitrade.entity.Favorite;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.LoadExceptionEvent;
import com.spark.newbitrade.entity.MarketSymbol;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.entity.Vision;
import com.spark.newbitrade.entity.VisionEntity;
import com.spark.newbitrade.event.CheckLoginEvent;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.event.LoginoutWithoutApiEvent;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.SocketMessage;
import com.spark.newbitrade.serivce.SocketResponse;
import com.spark.newbitrade.ui.AppVersionDialog;
import com.spark.newbitrade.utils.FileUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.utils.okhttp.AppUtils;
import com.spark.newbitrade.utils.okhttp.FileCallback;
import com.spark.newbitrade.utils.okhttp.OkhttpUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import config.Injection;
import okhttp3.Request;

import static com.spark.newbitrade.factory.HttpUrls.TYPE_AC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC_SYSTEM;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_UC;
import static com.spark.newbitrade.utils.GlobalConstant.JSON_ERROR;

public class MainActivity extends BaseTransFragmentActivity implements MainContract.View,
        MarketBaseFragment.MarketOperateCallback, SettingContact.View, VersionContract.View {

    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.llC2C)
    LinearLayout llC2C;
    @BindView(R.id.llOrder)
    LinearLayout llOrder;
    @BindView(R.id.llWallet)
    LinearLayout llWallet;
    @BindView(R.id.llMy)
    LinearLayout llMy;
    @BindView(R.id.llTab)
    LinearLayout llTab;
    @BindView(R.id.ivClose)
    ImageButton ivClose;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpMenu)
    ViewPager vpMenu;
    @BindView(R.id.dlRoot)
    DrawerLayout dlRoot;
    private List<String> titleList = new ArrayList<>();
    private int currentPage;
    private List<Currency> currencies = new ArrayList<>();
    private List<Currency> currenciesTwo = new ArrayList<>();
    private List<Currency> currencyListAll = new ArrayList<>();
    private HashMap<String, List<Currency>> marketMap = new HashMap<>();
    private List<BaseFragment> menusFragments = new ArrayList<>();

    private HomeFragment homeFragment;
    private BaseWalletFragment walletFragment;
    private C2CFragment c2cFragment;
    private StoreFragment storeFragment;
    private MyFragment myFragment;

    private SelfSelectionragment favoriteFragment;
    private MainContract.Presenter presenter;
    private SettingPresenterImpl settingPresenter;
    private VersionPresenter versionPresenter;

    private long lastPressTime = 0;
    private int type; // 1 去买币  2 去卖币
    private LinearLayout[] lls;
    private Gson gson = new Gson();
    private boolean hasNew = false;
    public static double rate = 1.0;
    public static boolean isAgain = false;
    public static List<Favorite> mFavorte = new ArrayList<>();
    private Bundle savedInstanceState;
    private boolean isAlreadExcute;// 此变量用来避免其他界面的指令，此界面也能接到
    //防止多次跳转401监听
    public static boolean isTokenUnUsed = false;
    private ProgressDialog progressDialog;
    private AppVersionDialog appVersionDialog;
    private boolean isCheckVersion = false;//首次检测版本更新

    public List<String> getTitleList() {
        return titleList;
    }

    public MainContract.Presenter getPresenter() {
        return presenter;
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAgain) {
            isAgain = false;
            find();
        }
        if (MyApplication.app.isLogin()) {
            if (!isCheckVersion) {
                allowUnKnowSrc(activity);
                isCheckVersion = true;
                checkPermission();
            }
            EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_CHAT, ISocket.CMD.SUBSCRIBE_GROUP_CHAT, buildGetBodyJson().toString().getBytes()));
        }
        hasNew = SharedPreferenceInstance.getInstance().getHasNew();
        SharedPreferenceInstance.getInstance().saveHasNew(false);
        if (!MyApplication.getApp().isLogin() && currencyListAll != null && currencyListAll.size() != 0) { // 退出登录后 刷新展示未登录状态的数据
            notLoginCurrencies();
        }

    }

    /**
     * 以下是点击了K线页面的买入 卖出 后的页面跳转逻辑
     */
    public void skipTradeView(Bundle bundle) {
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        int page = savedInstanceState.getInt("page");
        selecte(lls[page], page);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("page", currentPage);
        super.onSaveInstanceState(outState);
    }

    private void startTCP() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.SUBSCRIBE_THUMB, new Gson().toJson(GlobalConstant.getMAP()).getBytes()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.UN_SUBSCRIBE_THUMB, new Gson().toJson(GlobalConstant.getMAP()).getBytes()));
//        stopService(new Intent(MainActivity.this, TradeService.class));
        settingPresenter.destory();
        versionPresenter.destory();
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (lastPressTime == 0 || now - lastPressTime > 2 * 1000) {
            ToastUtils.showToast(getString(R.string.exit_again));
            lastPressTime = now;
        } else if (now - lastPressTime < 2 * 1000) super.onBackPressed();

    }


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initProgressDialog();
        new MainPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        lls = new LinearLayout[]{llHome, llC2C, llOrder, llWallet, llMy};
        reCoveryView();
        initPresenter();
        initService();
        startTCP();
        dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    /**
     * 初始化服务
     */
    private void initService() {
//        String json = new Gson().toJson(GlobalConstant.getMAP());
//        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.PUSH_REQUEST, json.getBytes()));
    }

    /**
     * 初始化tab栏
     */
    private void initPager() {
        vpMenu.setOffscreenPageLimit(3);
        vpMenu.setAdapter(new PagerAdapter(getSupportFragmentManager(), menusFragments, titleList));
        tab.setupWithViewPager(vpMenu);
    }

    /**
     * 避免出现activity被销毁，fragment重新创建，造成界面重叠
     */
    private void reCoveryView() {
        if (fragments.size() == 0) {
            recoverFragment();
        }
        if (savedInstanceState == null) {
//            hideFragment(homeFragment);
//            selecte(llOrder, 2);
            selecte(llHome, 0);
//            selecte(llWallet, 0);
        }

    }

    private void addFragments() {
        int type = MarketBaseFragment.MarketOperateCallback.TYPE_SWITCH_SYMBOL;
        for (int i = 0; i < titleList.size() - 1; i++) {
            menusFragments.add(MarketSecFragment.getInstance(type));
        }
        if (favoriteFragment == null)
            menusFragments.add(favoriteFragment = SelfSelectionragment.getInstance(type));
    }

    private void recoverMenuFragment() {
        for (int i = 0; i < titleList.size() - 1; i++) {
            MarketSecFragment usdtMarketFragment = (MarketSecFragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), i));
            menusFragments.add(usdtMarketFragment);
        }
        favoriteFragment = (SelfSelectionragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), titleList.size() - 1));
        menusFragments.add(favoriteFragment);
    }

    /**
     * 初始化presenter
     */
    private void initPresenter() {
        settingPresenter = new SettingPresenterImpl(this);
        versionPresenter = new VersionPresenter(this);
    }

    @OnClick({R.id.llHome, R.id.llC2C, R.id.llOrder, R.id.llWallet, R.id.llMy, R.id.ivClose})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.llHome://首页
                selecte(v, 0);
                break;
            case R.id.llWallet://钱包
                if (MyApplication.getApp().isLogin()) {
                    selecte(v, 1);
                } else {
                    showActivity(LoginActivity.class, null);
                }
                break;
            case R.id.llC2C://OTC
                selecte(v, 2);
                break;
            case R.id.llOrder://商家
                if (MyApplication.getApp().isLogin()) {
                    selecte(v, 3);
                } else {
                    showActivity(LoginActivity.class, null);
                }
                break;
            case R.id.llMy://我的
                selecte(v, 4);
                break;
            case R.id.ivClose:
                dlRoot.closeDrawers();
                break;

        }

    }

    @Override
    protected void loadData() {
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.ENABLE_SYMBOL, new Gson().toJson(GlobalConstant.getMAP()).getBytes()));
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.GET_OVERVIEW_THUMB, new Gson().toJson(GlobalConstant.getMAP()).getBytes()));
    }

    /**
     * 获取费率
     */
    private void getRate() {
        HashMap<String, String> map = new HashMap<>();
        map.put("currency", GlobalConstant.CNY);
        String json = new Gson().toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.USD_CNY_RATE, json.getBytes()));
    }

    private void tcpNotify() {
//        homeFragment.tcpNotify();
        if (menusFragments != null) {
            for (int i = 0; i < menusFragments.size(); i++) {
                if (i != menusFragments.size() - 1) {
                    MarketSecFragment fragment = (MarketSecFragment) menusFragments.get(i);
                    fragment.tcpNotify();
                } else {
                    favoriteFragment.tcpNotify();
                }
            }
        }
    }

    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        hideLoadingPopup();
        if (response.getCmd() == null) return;
        String json = response.getResponse();
        JSONObject object = null;
        switch (response.getCmd()) {
            case PUSH_THUMB: // 首页缩略图订阅的消息
                try {
                    Currency temp = gson.fromJson(json, Currency.class);
                    if (temp == null) return;
                    for (Currency currency : currencies) {
                        if (temp.getSymbol().equals(currency.getSymbol())) {
                            Currency.shallowClone(currency, temp);
                            break;
                        }
                    }
                    for (Currency currency : currenciesTwo) {
                        if (temp.getSymbol().equals(currency.getSymbol())) {
                            Currency.shallowClone(currency, temp);
                            break;
                        }
                    }
                    for (Currency currency : currencyListAll) {
                        if (temp.getSymbol().equals(currency.getSymbol())) {
                            Currency.shallowClone(currency, temp);
                            break;
                        }
                    }
                    tcpNotify();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;
            case GET_ALL_THUMB:
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        List<Currency> currencies = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Currency>>() {
                        }.getType());
                        allCurrencySuccess(currencies);
                    } else {
                        allCurrencyFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    allCurrencyFail(JSON_ERROR, null);
                }
                break;
            case GET_OVERVIEW_THUMB:
                try {
                    homeCurrencySuccess(json);
                } catch (Exception e) {
                    e.printStackTrace();
                    homeCurrencyFail(JSON_ERROR, null);
                }
                break;
            case ENABLE_SYMBOL:
                if (!isAlreadExcute) {
                    isAlreadExcute = true;
                    try {
                        object = new JSONObject(json);
                        if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                            List<MarketSymbol> objs = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<MarketSymbol>>() {
                            }.getType());
                            if (objs != null) {
                                String symbol = "";
                                for (int i = 0; i < objs.size(); i++) {
                                    if (StringUtils.isNotEmpty(objs.get(i).getBaseSymbol())) {
                                        if (!symbol.contains(objs.get(i).getBaseSymbol())) {
                                            titleList.add(objs.get(i).getBaseSymbol());
                                        }
                                        symbol = objs.get(i).getBaseSymbol() + "," + symbol;
                                    }
                                }
                                titleList.add(getString(R.string.self));
                                if (savedInstanceState == null) {
                                    addFragments();
                                } else {
                                    recoverMenuFragment();
                                }
                                initPager();
                                EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_KLINE, ISocket.CMD.GET_ALL_THUMB, new Gson().toJson(GlobalConstant.getMAP()).getBytes()));
                            }
                        } else {
                            NetCodeUtils.checkedErrorCode((BaseActivity) activity, object.optInt("code"), object.optString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        NetCodeUtils.checkedErrorCode((BaseActivity) activity, JSON_ERROR, null);
                    }
                }
                break;
            case JSONLOGIN:
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        User user = MyApplication.getApp().getCurrentUser();
                        MyApplication.getApp().deleteCurrentUser();
                        MyApplication.getApp().setCurrentUser(user);
                        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.PUSH_REQUEST, null));
                    } else {
                        NetCodeUtils.checkedErrorCode((BaseActivity) activity, object.optInt("code"), object.optString("'message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    NetCodeUtils.checkedErrorCode((BaseActivity) activity, JSON_ERROR, null);
                }
                break;
            case USD_CNY_RATE:
                try {
                    JsonObject jsonObject = new JsonParser().parse(response.getResponse()).getAsJsonObject();
                    double rate = jsonObject.getAsJsonPrimitive("data").getAsDouble();
                    getRateSuccess(rate);
                } catch (Exception e) {
                    e.printStackTrace();
                    getRateFail(JSON_ERROR, null);
                }
                break;
            case FIND_FAVOR:
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        List<Favorite> objs = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Favorite>>() {
                        }.getType());
                        findSuccess(objs);
                    } else {
                        doPostFail(object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    doPostFail(JSON_ERROR, null);
                }
                break;
            case PUSH_REQUEST:
                try {
                    object = new JSONObject(json);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        find();
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
//新消息提醒
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getGroupChatEvent(ChatTipEvent tipEvent) {
//        homeFragment.setChatTip(tipEvent.isHasNew());
//    }

    /**
     * 自选
     */
    public void find() {
        //  EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.FIND_FAVOR, null));
    }

    @Override
    public void findSuccess(List<Favorite> obj) {
        if (obj == null) return;
        mFavorte.clear();
        mFavorte.addAll(obj);
        for (Currency currency : currencyListAll) {
            currency.setCollect(false);
        }
        for (Currency currency : currencyListAll) {
            for (Favorite favorite : mFavorte) {
                if (favorite.getSymbol().equals(currency.getSymbol())) {
                    currency.setCollect(true);
                }
            }
        }
        if (favoriteFragment != null) favoriteFragment.dataLoaded(currencyListAll);
    }

    @Override
    public void doPostFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
    }

    @Override
    public void getNewVersionSuccess(Vision obj) {
    }

    @Override
    public void checkVersionSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            VisionEntity entity = new Gson().fromJson(response, VisionEntity.class);
            if (entity != null && entity.getData() != null && (AppUtils.compareVersion(entity.getData().getVersion(), AppUtils.getVersionName(activity)) == 1)) {
                if (StringUtils.isNotEmpty(entity.getData().getUrl())) {
                    showVersionDialog(entity);
                } else {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                }
            }
        }
    }

    @Override
    public void getRateSuccess(double obj) {
        rate = obj;
    }

    @Override
    public void tradeLoginSuccess(Object obj) {
        Gson gson = new Gson();
        String code = (String) obj;
        ExchangeLoginInfo exchangeLoginInfo = new ExchangeLoginInfo();
        exchangeLoginInfo.setType(2);
        exchangeLoginInfo.setValue(code);
        HashMap<String, String> map = new HashMap<>();
        map.put("username", MyApplication.getApp().getCurrentUser().getId() + "");
        map.put("password", gson.toJson(exchangeLoginInfo));
        String json = gson.toJson(map);
        EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.JSONLOGIN, json.getBytes()));
    }

    @Override
    public void getUserInfoSuccess() {
    }


    @Override
    public void getRateFail(Integer code, String toastMessage) {
        rate = 0;
    }

    @Override
    public void allCurrencySuccess(Object obj) {
        LogUtils.i("allCurrencySuccess");
        setCurrency((List<Currency>) obj);
    }

    @Override
    public void allCurrencyFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
//        homeFragment.showEmptyView();
    }

    @Override
    public void homeCurrencySuccess(String obj) {
        try {
            JSONObject object = new JSONObject(obj);
            if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                List<Currency> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Currency>>() {
                }.getType());
                List<Currency> currency1 = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Currency>>() {
                }.getType());
                this.currenciesTwo.clear();
                this.currenciesTwo.addAll(currency1);
                this.currencies.clear();
                this.currencies.addAll(objs);
//                if (homeFragment != null) homeFragment.dataLoaded(currencies, currenciesTwo);
            } else {
                homeCurrencyFail(JSON_ERROR, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            homeCurrencyFail(JSON_ERROR, null);
        }

    }

    @Override
    public void homeCurrencyFail(Integer code, String toastMessage) {
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    private void selecte(View v, int page) {
        currentPage = page;
        llHome.setSelected(false);
        llC2C.setSelected(false);
        llOrder.setSelected(false);
        llWallet.setSelected(false);
        llMy.setSelected(false);
        v.setSelected(true);
        showFragment(fragments.get(page));
        /*if (currentFragment == fragments.get(2)) {
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        dlRoot.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override public void onDrawerClosed(View drawerView) {
                dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        dlRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dlRoot.closeDrawers();
                        break;
                }
                return false;
            }
        });*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }


    @Override
    protected void initFragments() {
        if (homeFragment == null) fragments.add(homeFragment = new HomeFragment());
        if (walletFragment == null) fragments.add(walletFragment = new BaseWalletFragment());
        if (c2cFragment == null) fragments.add(c2cFragment = new C2CFragment());
        if (storeFragment == null) fragments.add(storeFragment = new StoreFragment());
        if (myFragment == null) fragments.add(myFragment = new MyFragment());
    }

    @Override
    protected void recoverFragment() {
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        walletFragment = (BaseWalletFragment) getSupportFragmentManager().findFragmentByTag(WalletFragment.TAG);
        c2cFragment = (C2CFragment) getSupportFragmentManager().findFragmentByTag(C2CFragment.TAG);
        storeFragment = (StoreFragment) getSupportFragmentManager().findFragmentByTag(StoreFragment.TAG);
        myFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag(MyFragment.TAG);
        if (homeFragment == null) fragments.add(homeFragment = new HomeFragment());
        else fragments.add(homeFragment);
        if (walletFragment == null) fragments.add(walletFragment = new BaseWalletFragment());
        else fragments.add(walletFragment);
        if (c2cFragment == null) fragments.add(c2cFragment = new C2CFragment());
        else fragments.add(c2cFragment);
        if (storeFragment == null) fragments.add(storeFragment = new StoreFragment());
        else fragments.add(storeFragment);
        if (myFragment == null) fragments.add(myFragment = new MyFragment());
        else fragments.add(myFragment);
    }

    @Override
    public int getContainerId() {
        return R.id.flContainer;
    }

    public DrawerLayout getDlRoot() {
        return dlRoot;
    }

    private void notLoginCurrencies() {
        for (Currency currency : currencyListAll) {
            currency.setCollect(false);
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void setCurrency(List<Currency> obj) {
        if (obj == null || obj.size() == 0) return;
        this.currencyListAll.clear();
        this.currencyListAll.addAll(obj);
        if (titleList != null) {
            for (int i = 0; i < titleList.size() - 1; i++) {
                List<Currency> objCurrencyList = Currency.baseCurrencies(currencyListAll, titleList.get(i));
                marketMap.put(titleList.get(i), objCurrencyList);
                ((MarketSecFragment) menusFragments.get(i)).dataLoaded(objCurrencyList);
            }
        }
//        homeFragment.dataLoaded(currencies, currenciesTwo);
        favoriteFragment.dataLoaded(currencyListAll);
    }


    @Override
    public void itemClick(Currency currency, int type) {
        if (type == MarketBaseFragment.MarketOperateCallback.TYPE_SWITCH_SYMBOL) {
            dlRoot.closeDrawers();
        } else if (type == MarketBaseFragment.MarketOperateCallback.TYPE_TO_KLINE) {
            Bundle bundle = new Bundle();
            bundle.putString("symbol", currency.getSymbol());
            bundle.putInt("baseCoinScale", currency.getBaseCoinScale());
            bundle.putInt("coinScale", currency.getCoinScale());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            skipTradeView(bundle);
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selecte(llOrder, 2);
        }
    }

    private JSONObject buildGetBodyJson() {
        JSONObject obj = new JSONObject();
        try {
            if (MyApplication.getApp().getCurrentUser() == null) {
                obj.put("uid", null);
            } else {
                obj.put("uid", MyApplication.getApp().getCurrentUser().getId());
            }
            LogUtils.i(String.valueOf(obj.get("uid")));
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 允许安装未知来源的应用
     * 其中的变量：1代表允许，如果换成0则代表禁止
     */
    public static void allowUnKnowSrc(Context context) {
        try {
            android.provider.Settings.Global.putInt(context.getContentResolver(),
                    android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS, 1);
        } catch (SecurityException e) {
            //LogUtils.getInstance().d(e);
        }
    }


    /**
     * check权限
     *
     * @param position
     */
    private void checkPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("permission: " + data.get(0));
                        versionPresenter.checkVersion();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (AndPermission.hasAlwaysDeniedPermission(activity, data)) {
                            AndPermission.permissionSetting(activity).execute();
                            return;
                        }
                        ToastUtils.showToast(activity, getString(R.string.str_no_permission));
                    }
                }).start();
    }

    /**
     * 版本更新提示框
     */
    private void showVersionDialog(final VisionEntity visionEntity) {
        appVersionDialog = new AppVersionDialog(activity, visionEntity);
        appVersionDialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visionEntity.getData().getUrl() == null || "".equals(visionEntity.getData().getUrl())) {
                    ToastUtils.showToast(activity, getString(R.string.update_address_error_tag));
                } else {
                    download(visionEntity.getData().getUrl());
                }
                appVersionDialog.dismiss();
            }
        });
        appVersionDialog.show();
    }

    /**
     * 下载
     *
     * @param url
     */
    private void download(String url) {
        OkhttpUtils.get().url(url).build().execute(new FileCallback(FileUtils.getCacheSaveFile(this, "application.apk").getAbsolutePath()) {
            @Override
            public void inProgress(float progress) {
                progressDialog.show();
                progressDialog.setProgress((int) (progress * 100));
            }

            @Override
            public void onError(Request request, HttpErrorEntity e) {
                progressDialog.dismiss();
                String msg = e.getMessage();
                if (StringUtils.isNotEmpty(msg) && msg.contains("Canceled")) {
                    NetCodeUtils.checkedErrorCode(MainActivity.this, GlobalConstant.SERVER_ERROR, null);
                }
            }

            @Override
            public void onResponse(File response) {
                progressDialog.dismiss();
                AppUtils.installAPk(response, activity);
            }
        });
    }

    /**
     * 初始化进度条
     */
    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getString(R.string.downloading_tag));
        //设置信息
        progressDialog.setMessage(getString(R.string.downloading_crazy_tag));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    /**
     * 检测登录状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginEvent(CheckLoginEvent event) {
        if (event.type.contains(TYPE_OTC_SYSTEM)) {
            presenter.checkBusinessLogin(TYPE_OTC_SYSTEM);
        } else if (event.type.contains(TYPE_OTC)) {
            presenter.checkBusinessLogin(TYPE_OTC);
        } else if (event.type.contains(TYPE_UC)) {
            presenter.checkBusinessLogin(TYPE_UC);
        } else if (event.type.contains(TYPE_AC)) {
            presenter.checkBusinessLogin(TYPE_AC);
        }
    }

    /**
     * 退出登录-不调用退出接口
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginoutWithoutApiEvent(LoginoutWithoutApiEvent event) {
        MyApplication.getApp().deleteCurrentUser();
        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        SharedPreferenceInstance.getInstance().saveLockPwd("");
        MyApplication.getApp().getCookieManager().getCookieStore().removeAll();
        isTokenUnUsed = false;
        ActivityManage.finishAll();
        showActivity(LoginActivity.class, null);
    }

    /**
     * 数据加载异常通知
     * 401登录无效监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadExceptionEvent(LoadExceptionEvent event) {
        if (!isTokenUnUsed) {
            isTokenUnUsed = true;
            settingPresenter.loginOut();
        }
    }

    @Override
    public void loginOutSuccess(String obj) {
        if (obj != null) {
            ToastUtils.showToast(getString(R.string.login_invalid));
            MyApplication.getApp().deleteCurrentUser();
            SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
            SharedPreferenceInstance.getInstance().saveLockPwd("");
            MyApplication.getApp().getCookieManager().getCookieStore().removeAll();
            isTokenUnUsed = false;
            ActivityManage.finishAll();
            showActivity(LoginActivity.class, null);
        }
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        super.dealError(httpErrorEntity);
        if (isTokenUnUsed) {
            isTokenUnUsed = false;
        }
    }

    @Override
    public void dealError(VolleyError volleyError) {
        super.dealError(volleyError);
        if (isTokenUnUsed) {
            isTokenUnUsed = false;
        }
    }

    @Override
    public void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity) {
        if (casLoginEntity != null) {
            String type = casLoginEntity.getType();
            if (!casLoginEntity.isLogin()) {
                String gtc = MyApplication.getApp().getCurrentUser().getGtc();
                presenter.doLoginBusiness(gtc, type);
            }
        }
    }

    @Override
    public void doLoginBusinessSuccess(String type) {
        versionPresenter.checkVersion();
        EventBus.getDefault().post(new CheckLoginSuccessEvent());
    }

}
