package com.spark.newbitrade.activity.order;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.PagerAdapter;
import com.spark.newbitrade.base.BaseTransFragment;
import com.spark.newbitrade.entity.FilterBean;
import com.spark.newbitrade.ui.FilterPopView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * 我的订单界面
 */
public class MyOrderFragment extends BaseTransFragment implements FilterPopView.FilterCallBack {
    public static final String TAG = MyOrderFragment.class.getSimpleName();
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpPager)
    ViewPager vpPager;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindArray(R.array.order_status)
    String[] status;
    //    @BindView(R.id.ivFilter)
//    ImageView ivFilter;
    private int type = 0;
    private FilterPopView filterPopView;
    private ArrayList<FilterBean> firList;
    private ArrayList<FilterBean> secList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order;
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

    @Override
    protected void initView() {
//        tvGoto.setVisibility(View.GONE);
//        ivFilter.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        setShowBackBtn(true);
        setTitle(getString(R.string.my_order));
        setFragments();
        vpPager.setCurrentItem(type);
    }

    @Override
    protected void setListener() {
        super.setListener();
        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    ((OrderFragment) fragments.get(vpPager.getCurrentItem())).loadData();
                }
            }
        });
    }

    private void setFragments() {
        addFragments();
        vpPager.setOffscreenPageLimit(fragments.size() - 1);
        vpPager.setAdapter(new PagerAdapter(getmActivity().getSupportFragmentManager(), fragments, Arrays.asList(status)));
        tab.setupWithViewPager(vpPager);
    }

    private void addFragments() {
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.UNPAID));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.PAID));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.DONE));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.CANC));
        fragments.add(OrderFragment.getInstance(OrderFragment.Status.COMPLAINING));
    }


    @Override
    public void callBack(HashMap<String, String> map) {
        if (map != null) {
            ((OrderFragment) fragments.get(vpPager.getCurrentItem())).setSelectResult(map.get("unit"), map.get("referenceNumber"), map.get("type"));
        }
    }

//    @OnClick(R.id.ivFilter)
//    public void onViewClicked() {
//        if (filterPopView == null) {
//            presenter.all();
//        } else {
//            showPopWind();
//        }
//    }

//    @Override
//    public void allSuccess(List<CoinInfo> obj) {
//        if (obj == null || obj.size() == 0) return;
//        firList = new ArrayList<>();
//        for (CoinInfo coinInfo : obj) {
//            FilterBean filterBean = new FilterBean();
//            filterBean.setName(coinInfo.getUnit());
//            filterBean.setStrUpload(coinInfo.getUnit());
//            firList.add(filterBean);
//        }
//        secList = new ArrayList<>();
//        secList.add(new FilterBean(getString(R.string.all), "", true));
//        secList.add(new FilterBean(getString(R.string.text_buy), GlobalConstant.INT_BUY + "", false));
//        secList.add(new FilterBean(getString(R.string.text_sell), GlobalConstant.INT_SELL + "", false));
//        filterPopView = new FilterPopView(activity, this, FilterPopView.CALLFROM_ORDER,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        filterPopView.setFirstList(firList);
//        filterPopView.setSecList(secList);
//        showPopWind();
//    }

    @Override
    protected String getmTag() {
        return TAG;
    }
}
