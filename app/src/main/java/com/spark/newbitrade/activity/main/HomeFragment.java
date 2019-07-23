package com.spark.newbitrade.activity.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.chat.ChatListActivity;
import com.spark.newbitrade.activity.main.presenter.HomePresenterImpl;
import com.spark.newbitrade.activity.message.WebViewActivity;
import com.spark.newbitrade.activity.mining.MiningActivity;
import com.spark.newbitrade.adapter.BannerImageLoader;
import com.spark.newbitrade.adapter.HomeFragmentPagerAdapter;
import com.spark.newbitrade.adapter.HomeOneAdapter;
import com.spark.newbitrade.adapter.HomeSortAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.base.BaseTransFragment;
import com.spark.newbitrade.entity.BannerEntity;
import com.spark.newbitrade.entity.Currency;
import com.spark.newbitrade.entity.HomeNewTenBean;
import com.spark.newbitrade.entity.Notice;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.factory.UrlFactory;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.chatUtils.SocketMessage;
import com.spark.newbitrade.serivce.chatUtils.SocketResponse;
import com.spark.newbitrade.ui.intercept.MyScrollView;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2018/1/7.
 */

public class HomeFragment extends BaseTransFragment implements MainContract.HomeView {
    public static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.rvMessageTag)
    RelativeLayout rvMessageTag;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.mSortContent)
    RecyclerView mSortContent; // 涨幅榜
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.points)
    LinearLayout llPoints;
    @BindView(R.id.ivMining)
    ImageView ivMining;
    private HomeSortAdapter sortAdapter;
    @BindView(R.id.scrollView)
    MyScrollView scrollView;
    @BindView(R.id.ivChatTip)
    ImageView ivchatTip;
    @BindView(R.id.llHomeContent)
    LinearLayout llHomeContent;
    @BindView(R.id.rlEmpty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    private List<String> imageUrls = new ArrayList<>();
    private List<Currency> currencies = new ArrayList<>();
    private List<Currency> currenciesOne = new ArrayList<>();
    private List<Currency> currenciesTwo = new ArrayList<>();
    private HomeOneAdapter adapterOne;
    private HomeOneAdapter adapterTwo;
    private HomePresenterImpl presenter;
    private List<Notice> messageList = new ArrayList<>();
    private List<String> info = new ArrayList<>();
    //    private CommonPresenter commonPresenter;
    private List<BannerEntity> objList;//轮播图列表
    private int position;
    private ImageView[] ivPoints;
    private ArrayList<HomeNewTenBean> c2cs = new ArrayList<>();
    private PopupWindow popWnd;
//    private boolean isFirst = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        banner.stopAutoPlay();
    }

    @Override
    public void onStart() {
        super.onStart();
        //  marqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        //  marqueeView.stopFlipping();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (marqueeView != null) {
            if (!hidden) {
                marqueeView.startFlipping();
            } else {
                marqueeView.stopFlipping();
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    protected void initView() {
        super.initView();
        //rvMessageTag.setVisibility(View.VISIBLE);
        scrollView.requestDisallowInterceptTouchEvent(false);
        //tvGoto.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        super.initData();
        //setTitle(getString(R.string.home));
//        new CommonPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        presenter = new HomePresenterImpl(this);
        initSortContent();
    }

    @Override
    public void setPresenter(HomePresenterImpl presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void loadData() {
        notifyData();
        isNeedLoad = false;
        getImage();
        getMarqueeText();
    }


    /**
     * 获取滚动图片
     */
    private void getImage() {
        if (imageUrls == null || imageUrls.size() == 0) {
            HashMap<String, String> map = new HashMap<>();
            map.put("merchantCode", GlobalConstant.MerchantCode);
            presenter.banners(map);
        }
    }

    /**
     * 获取滚动text
     */
    private void getMarqueeText() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNo", "1");
        map.put("pageSize", "5");
        presenter.getMarqueeText(map);
    }


    /**
     * 初始化三个固定的grid
     */
    private void initThreeContent() {
        List<View> viewPagerList = new ArrayList<View>();
        RecyclerView recyclerViewOne = (RecyclerView) View.inflate(getActivity(), R.layout.item_recycleview_layout, null);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewOne.setLayoutManager(manager);
        adapterOne = new HomeOneAdapter(R.layout.item_home_one, currenciesOne);
        recyclerViewOne.setAdapter(adapterOne);
        adapterOne.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*Currency currency = (Currency) adapter.getItem(position);
                skipKline(currency);*/
            }
        });
        RecyclerView recyclerViewTwo = (RecyclerView) View.inflate(getActivity(), R.layout.item_recycleview_layout, null);
        LinearLayoutManager managerTwo = new LinearLayoutManager(getActivity());
        managerTwo.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTwo.setLayoutManager(managerTwo);
        adapterTwo = new HomeOneAdapter(R.layout.item_home_one, currenciesTwo);
        recyclerViewTwo.setAdapter(adapterTwo);
        adapterTwo.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*Currency currency = (Currency) adapter.getItem(position);
                skipKline(currency);*/
            }
        });
        viewPagerList.add(recyclerViewOne);
        if (currenciesTwo.size() == 0) {
            ivPoints = new ImageView[1];
        } else {
            ivPoints = new ImageView[2];
            viewPagerList.add(recyclerViewTwo);
        }
        for (int i = 0; i < ivPoints.length; i++) {
            ivPoints[i] = new ImageView(getActivity());
            if (i == 0) {
                ivPoints[i].setImageResource(R.mipmap.page_focuese);
            } else {
                ivPoints[i].setImageResource(R.mipmap.page_unfocused);
            }
            ivPoints[i].setPadding(8, 8, 8, 8);
            llPoints.addView(ivPoints[i]);
        }
        isFirst = false;
        viewpager.setAdapter(new HomeFragmentPagerAdapter(viewPagerList));
        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                for (int i = 0; i < ivPoints.length; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.mipmap.page_focuese);
                    } else {
                        ivPoints[i].setImageResource(R.mipmap.page_unfocused);
                    }
                }
            }
        });
    }
        /*LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //manager.setOrientation(GridLayoutManager.HORIZONTAL);
        rvContent.setLayoutManager(manager);
        adapter = new HomeOneAdapter(R.layout.item_home_one, currenciesTwo);
        adapter.isFirstOnly(true);
        rvContent.setAdapter(adapter);
    }*/


    /**
     * 初始化排行榜
     */
    private void initSortContent() {
        mSortContent.setLayoutManager(new LinearLayoutManager(getActivity()));
        sortAdapter = new HomeSortAdapter(currencies);
        sortAdapter.isFirstOnly(true);
        sortAdapter.setLoad(true);
        mSortContent.setAdapter(sortAdapter);
    }


    @OnClick({R.id.ivChat, R.id.tvMessage, R.id.ivMining, R.id.ivDownload})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivChat:
                ivchatTip.setVisibility(View.INVISIBLE);
                showActivity(ChatListActivity.class, null);
                break;
            case R.id.tvMessage:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getPresenter().allCurrency();
                break;
            case R.id.ivMining:
                //showActivity(MiningActivity.class, null);
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.str_heyue));
                bundle.putString("url", GlobalConstant.He_Yue);
                bundle.putBoolean("isImage", true);
                showActivity(WebViewActivity.class, bundle);
                break;
            case R.id.ivDownload:
                showPop();
                //showActivity(MiningActivity.class, null);
                break;
        }
    }

    private void showPop() {
        View contentView = LayoutInflater.from(getmActivity()).inflate(R.layout.view_pop_home_download, null);
        popWnd = new PopupWindow(getmActivity());
        popWnd.setContentView(contentView);
        popWnd.setWidth((int) (MyApplication.getApp().getmWidth() * 0.7));
        popWnd.setHeight(popWnd.getWidth());
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWnd.setOutsideTouchable(true);
        popWnd.setTouchable(true);
        popWnd.setFocusable(true);
        darkenBackground(0.4f);
        ImageView ivDownload = contentView.findViewById(R.id.ivDownload);
        ivDownload.setImageResource(R.mipmap.icon_home_download);
        View rootview = LayoutInflater.from(getmActivity()).inflate(R.layout.fragment_home, null);
        popWnd.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }

    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getmActivity().getWindow().getAttributes();
        lp.alpha = bgcolor;
        getmActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getmActivity().getWindow().setAttributes(lp);
    }

    @Override
    protected void setListener() {
        super.setListener();
        /*adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                doCollect(position);
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Currency currency = (Currency) adapter.getItem(position);
                skipKline(currency);
            }
        });*/
        sortAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Currency currency = (Currency) adapter.getItem(position);
                skipKline(currency);
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerEntity bannerEntity = objList.get(position);
                if (bannerEntity != null && StringUtils.isNotEmpty(bannerEntity.getLinkUrl())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", getString(R.string.detail_title));
                    bundle.putString("url", bannerEntity.getLinkUrl());
                    bundle.putBoolean("isImage", true);
                    showActivity(WebViewActivity.class, bundle);
                }
            }
        });
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Notice message = messageList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.detail_title));
                bundle.putString("content", message.getBody());
                showActivity(WebViewActivity.class, bundle);
            }
        });
    }

    /**
     * 跳转到k线界面
     */
    private void skipKline(Currency currency) {
        if (currency != null) {
            Bundle bundle = new Bundle();
            bundle.putString("symbol", currency.getSymbol());
            bundle.putInt("baseCoinScale", currency.getBaseCoinScale());
            bundle.putInt("coinScale", currency.getCoinScale());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            ((MainActivity) getActivity()).skipTradeView(bundle);
        }
    }

    /**
     * 收藏
     *
     * @param position
     */
    private void doCollect(int position) {
        this.position = position;
        if (!MyApplication.getApp().isLogin()) {
            ToastUtils.showToast("请先登录再添加收藏！");
            return;
        }
        String symbol = currencies.get(position).getSymbol();
        HashMap<String, String> map = new HashMap<>();
        map.put("symbol", symbol);
        String json = new Gson().toJson(map);
        LogUtils.i("json==" + json);
        if (currencies.get(position).isCollect()) {
            EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.DELETE_FAVOR.getCode(), json.getBytes()));
//            commonPresenter.delete(map, position);
        } else {
//            commonPresenter.add(map, position);
            EventBus.getDefault().post(new SocketMessage(GlobalConstant.CODE_TRADE, ISocket.CMD.ADD_FAVOR.getCode(), json.getBytes()));
        }
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        immersionBar.flymeOSStatusBarFontColor("#FFFFFF").init();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }


//    @Override
//    public void setPresenter(CommonPresenter presenter) {
//        this.commonPresenter = presenter;
//    }

    public void dataLoaded(List<Currency> currencies, List<Currency> tow) {
        // llHomeContent.setVisibility(View.VISIBLE);
        rlEmpty.setVisibility(View.GONE);
        this.currencies.clear();
        this.currencies.addAll(currencies);
        this.currenciesTwo.clear();
        this.currenciesOne.clear();
        if (tow != null) {
            if (tow.size() < 6) {
                if (tow.size() <= 3) {
                    for (int i = 0; i < tow.size(); i++) {
                        currenciesOne.add(tow.get(i));
                    }
                } else {
                    for (int i = 3; i < tow.size(); i++) {
                        currenciesTwo.add(tow.get(i));
                    }
                    for (int i = 0; i < 3; i++) {
                        currenciesOne.add(tow.get(i));
                    }
                }
            } else {
                for (int i = 3; i < 6; i++) {
                    currenciesTwo.add(tow.get(i));
                }
                for (int i = 0; i < 3; i++) {
                    currenciesOne.add(tow.get(i));
                }
            }
        }
        if (isFirst) {
            initThreeContent();
        }
        adapterOne.notifyDataSetChanged();
        adapterTwo.notifyDataSetChanged();
        sortAdapter.notifyDataSetChanged();
    }

    public void showEmptyView() {
        llHomeContent.setVisibility(View.GONE);
        rlEmpty.setVisibility(View.VISIBLE);
        tvMessage.setText(getString(R.string.str_try_refresh));
    }

    public void setChatTip(boolean hasNew) {
        if (hasNew) ivchatTip.setVisibility(View.INVISIBLE);
        else ivchatTip.setVisibility(View.INVISIBLE);
    }

//    @Override
//    public void deleteFail(Integer code, String toastMessage) {
//        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
//    }
//
//    @Override
//    public void deleteSuccess(String obj, int position) {
//        this.currencies.get(position).setCollect(false);
//        //adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void addFail(Integer code, String toastMessage) {
//        NetCodeUtils.checkedErrorCode((BaseActivity) activity, code, toastMessage);
//    }
//
//    @Override
//    public void addSuccess(String obj, int position) {
//        this.currencies.get(position).setCollect(true);
//        //adapter.notifyDataSetChanged();
//    }

    public void notifyData() {
        if (adapterOne != null && adapterTwo != null) {
            adapterOne.notifyDataSetChanged();
            adapterTwo.notifyDataSetChanged();
        }
    }

    @Override
    public void bannersSuccess(final List<BannerEntity> obj) {
        if (obj == null) return;
        objList = obj;
        for (BannerEntity bannerEntity : obj) {
            String url = bannerEntity.getPicture();
            if (StringUtils.isNotEmpty(url)) {
                if (!url.contains("http")) {
                    url = UrlFactory.getHost() + "/" + url;
                }
                imageUrls.add(url);
            }

        }
        if (imageUrls.size() > 0) {
            banner.setImages(imageUrls); // 设置图片集合
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setIndicatorGravity(BannerConfig.CENTER)// 设置样式
                    .setImageLoader(new BannerImageLoader());
            banner.setDelayTime(3000); // 设置轮播时间
            banner.start();
        } else {
            banner.setBackground(getResources().getDrawable(R.mipmap.icon_banner));
        }
    }

    @Override
    public void bannersFail(Integer code, String toastMessage) {

    }

    @Override
    public void getMarqueeSuccess(List<Notice> messages) {
        messageList.clear();
        messageList.addAll(messages);
        setMarqueeView(messageList);
    }

    @Override
    public void getMarqueeFail(Integer code, String toastMessage) {

    }

    public void tcpNotify() {
        if (adapterOne != null && adapterTwo != null) {
            adapterOne.notifyDataSetChanged();
            adapterTwo.notifyDataSetChanged();
        }
        if (sortAdapter != null) sortAdapter.notifyDataSetChanged();
    }


    private void setMarqueeView(List<Notice> messageList) {
        info.clear();
        for (Notice message : messageList) {
            info.add(message.getHeader());
        }
        marqueeView.startWithList(info);
    }

    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        hideLoadingPopup();
        String obj = response.getResponse();
        switch (response.getCmd()) {
            case 0:
            case 1:
                try {
                    JSONObject object = new JSONObject(obj);
                    if (object.optInt("code") == GlobalConstant.SUCCESS_CODE) {
                        if (response.getCmd() == ISocket.CMD.ADD_FAVOR.getCode()) {
//                            addSuccess(object.optString("message"), position);
                        } else {
//                            deleteSuccess(object.optString("message"), position);
                        }
                    } else {
                        NetCodeUtils.checkedErrorCode((BaseActivity) activity, object.optInt("code"), object.optString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    deleteFail(JSON_ERROR, null);
                }
                break;
        }
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        getImage();
        getMarqueeText();
    }

}
