package com.spark.newbitrade.activity.main;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spark.library.otc.model.MessageResultAuthMerchantFrontVo;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.library.otc.model.Coin;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.main.presenter.C2CPresenterImpl;
import com.spark.newbitrade.activity.releaseAd.PubAdsActivity;
import com.spark.newbitrade.adapter.PagerAdapter;
import com.spark.newbitrade.base.BaseFragment;
import com.spark.newbitrade.base.BaseNestingTransFragment;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.entity.FilterBean;
import com.spark.newbitrade.event.CheckLoginEvent;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.ui.FilterPopView;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC_SYSTEM;

/**
 * Created by Administrator on 2018/1/29.
 */

public class C2CFragment extends BaseNestingTransFragment implements C2CContract.C2CView, FilterPopView.FilterCallBack {
    public static final String TAG = C2CFragment.class.getSimpleName();
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewPager)
    ViewPager vp;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.ivReleseAd)
    ImageView ivReleseAd;
    @BindView(R.id.ivFilter)
    ImageView ivFilter;
    @BindView(R.id.llBuyAndSell)
    LinearLayout llBuyAndSell;
    private C2CPresenterImpl presenter;
    private List<Coin> coinInfos = new ArrayList<>();
    private PagerAdapter adapter;
    private ArrayList<String> tabs = new ArrayList<>();
    //    private Country country;
//    private BuyOrSellDialog dialog;
    private FilterPopView filterPopView;
    private ArrayList<FilterBean> firList;
    private ArrayList<FilterBean> secList;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            immersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_c2c;
    }

    @Override
    protected void initView() {
        ivReleseAd.setVisibility(View.VISIBLE);
        ivFilter.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.GONE);
        tvGoto.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
        llBuyAndSell.setVisibility(View.VISIBLE);
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new C2CPresenterImpl(this);
//        dialog = new BuyOrSellDialog(activity);
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (coinInfos.size() <= 0) {
            EventBus.getDefault().post(new CheckLoginEvent(TYPE_OTC_SYSTEM));
            presenter.listOtcTradeCoin();
        }
    }


    @OnClick({R.id.tvBuy, R.id.tvSell, R.id.ivReleseAd, R.id.ivFilter})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (v.getId() == R.id.ivReleseAd) {
            if (!MyApplication.getApp().isLogin()) {
                ToastUtils.showToast(getString(R.string.text_login_first));
                showActivity(LoginActivity.class, null, LoginActivity.RETURN_LOGIN);
                return;
            }
        }
        switch (v.getId()) {
            case R.id.tvBuy:
                clickTabBuy();
                break;
            case R.id.tvSell:
                clickTabSell();
                break;
            case R.id.ivReleseAd://发布广告
                presenter.findAuthMerchantStatus();
                break;
            case R.id.ivFilter:
                if (filterPopView == null) {
                    presenter.country();
                } else {
                    showPopWind();
                }
                break;
        }
    }

    /**
     * 卖出
     */
    private void clickTabSell() {
        tvBuy.setSelected(false);
        tvSell.setSelected(true);
        for (BaseFragment fragment : fragments)
            ((C2CListFragment) fragment).setAdvertiseType(0);

    }

    /**
     * 买入
     */
    private void clickTabBuy() {
        tvBuy.setSelected(true);
        tvSell.setSelected(false);
        for (BaseFragment fragment : fragments)
            ((C2CListFragment) fragment).setAdvertiseType(1);
    }

    /**
     * 弹出popwindow
     */
    private void showPopWind() {
        filterPopView.setFocusable(true);
        filterPopView.setOutsideTouchable(true);
        filterPopView.showAsDropDown(llTitle);
        filterPopView.update();
    }

    @Override
    protected void setListener() {
        super.setListener();

    }


//    @Override
//    public void businessStatusSuccess(int status) {
//        if (status == 2 || status == 6) {
//            startActivity(new Intent(getActivity(), PubAdsActivity.class));
//        } else {
//            ToastUtils.showToast(getString(R.string.realname_business_first));
//        }
//    }
//
//    @Override
//    public void countrySuccess(List<Country> obj) {
//        if (obj == null) return;
//        firList = new ArrayList<>();
//        firList.add(new FilterBean(getString(R.string.all), "", true));
//        for (Country country : obj) {
//            FilterBean filterBean = new FilterBean();
//            filterBean.setName(country.getZhName());
//            filterBean.setStrUpload(country.getEnName());
//            firList.add(filterBean);
//        }
//        secList = new ArrayList<>();
//        secList.add(new FilterBean(getString(R.string.all), "", true));
//        secList.add(new FilterBean(getString(R.string.str_payway_ali), GlobalConstant.alipay, false));
//        secList.add(new FilterBean(getString(R.string.str_payway_wechat), GlobalConstant.wechat, false));
//        secList.add(new FilterBean(getString(R.string.str_payway_union), GlobalConstant.card, false));
//        filterPopView = new FilterPopView(activity, this, FilterPopView.CALLFROM_C2C,
//                LinearLayout.LayoutParams.MATCH_PARENT, (int) (MyApplication.getApp().getmHeight() * 0.5));
//        filterPopView.setFirstList(firList);
//        filterPopView.setSecList(secList);
//        showPopWind();
//    }

    public static String getTAG() {
        return TAG;
    }

    @Override
    protected void addFragments() {
    }

    @Override
    protected void recoveryFragments() {
//        if (saveCoinInfos == null || saveCoinInfos.size() == 0) return;
//        coinInfos.clear();
//        coinInfos.addAll(saveCoinInfos);
//        tabs.clear();
//        fragments.clear();
//        for (int i = 0; i < coinInfos.size(); i++) {
//            tabs.add(coinInfos.get(i).getCoinName());
//            fragments.add((C2CListFragment) getChildFragmentManager().findFragmentByTag(makeFragmentName(vp.getId(), i)));
//        }
//        if (fragments.size() > 4) tab.setTabMode(TabLayout.MODE_SCROLLABLE);
//        else tab.setTabMode(TabLayout.MODE_FIXED);
//        if (adapter == null) {
//            vp.setAdapter(adapter = new PagerAdapter(getChildFragmentManager(), fragments, tabs));
//            tab.setupWithViewPager(vp);
//            vp.setOffscreenPageLimit(fragments.size() - 1);
//        } else adapter.notifyDataSetChanged();
//        isNeedLoad = false;
    }


    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public void callBack(HashMap<String, String> map) {
        if (map != null) {
            for (BaseFragment baseFragment : fragments) {
                ((C2CListFragment) baseFragment).setSelectResult(map.get("country"), map.get("payway"), map.get("minlimit"), map.get("maxlimit"));
            }
//            ((C2CListFragment) fragments.get(vp.getCurrentItem())).setSelectResult(map.get("country"), map.get("payway"), map.get("minlimit"), map.get("maxlimit"));
        }
    }

    @Override
    public void listOtcTradeCoinSuccess(List<Coin> obj) {
        if (obj == null || obj.size() == 0) return;
        coinInfos.clear();
        coinInfos.addAll(obj);
        tabs.clear();
        fragments.clear();
//        country = new Country();
//        country.setLocalCurrency(GlobalConstant.CNY);
//        country.setZhName("中国");
        for (Coin coinInfo : coinInfos) {
            tabs.add(coinInfo.getCoinName());

            fragments.add(C2CListFragment.getInstance(coinInfo.getCoinName(), coinInfo.getCoinScale()));
        }
        if (fragments.size() > 4) tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        else tab.setTabMode(TabLayout.MODE_FIXED);
        if (adapter == null) {
            vp.setAdapter(adapter = new PagerAdapter(getChildFragmentManager(), fragments, tabs));
            tab.setupWithViewPager(vp);
            vp.setOffscreenPageLimit(fragments.size() - 1);
        } else adapter.notifyDataSetChanged();
        isNeedLoad = false;
    }

    @Override
    public void countrySuccess(List<Country> obj) {
        if (obj == null) return;
        firList = new ArrayList<>();
        firList.add(new FilterBean(getString(R.string.all), "", true));
        for (Country country : obj) {
            FilterBean filterBean = new FilterBean();
            filterBean.setName(country.getZhName());
            filterBean.setStrUpload(country.getEnName());
            firList.add(filterBean);
        }
        secList = new ArrayList<>();
        secList.add(new FilterBean(getString(R.string.all), "", true));
        secList.add(new FilterBean(getString(R.string.str_payway_ali), GlobalConstant.alipay, false));
        secList.add(new FilterBean(getString(R.string.str_payway_wechat), GlobalConstant.wechat, false));
        secList.add(new FilterBean(getString(R.string.str_payway_union), GlobalConstant.card, false));
        filterPopView = new FilterPopView(activity, this, FilterPopView.CALLFROM_C2C,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        filterPopView.setFirstList(firList);
        filterPopView.setSecList(secList);
        showPopWind();
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        if (coinInfos.size() <= 0) {
            presenter.listOtcTradeCoin();
        }
    }

    @Override
    public void findAuthMerchantStatusSuccess(MessageResultAuthMerchantFrontVo obj) {
        //认证商家状态 0：未认证 1：认证-待审核 2：认证-审核成功 3：认证-审核失败 5：退保-待审核 6：退保-审核失败 7:退保-审核成功
        if (obj != null && obj.getData() != null) {
            int certifiedBusinessStatus = obj.getData().getCertifiedBusinessStatus();
            if (certifiedBusinessStatus == 2) {
                showActivity(PubAdsActivity.class, null);
            } else {
                ToastUtils.showToast("请前往pc端进行商家认证后才能发布广告");
            }
        } else if (obj.getCode() == 30548) {
            ToastUtils.showToast("请前往pc端进行商家认证后才能发布广告");
        } else {
            ToastUtils.showToast("认证信息获取失败");
        }
    }
}
