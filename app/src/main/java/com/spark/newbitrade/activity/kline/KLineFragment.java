package com.spark.newbitrade.activity.kline;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineChartAdapter;
import com.github.fujianlian.klinechart.KLineChartView;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.formatter.DateFormatter;
import com.spark.newbitrade.R;

import java.util.ArrayList;

/**
 * K 线的封装
 */
public class KLineFragment extends Fragment {

    private KLineChartView mKChartView; // K线的布局控件
    private KLineChartAdapter mAdapter; // K线的适配器

    /**
     * @return KLineFragment
     */
    public static KLineFragment newInstance() {
        return newInstance(false, 2);
    }

    /**
     * @param scale 右侧保留的位数
     * @return KLineFragment
     */
    public static KLineFragment newInstance(int scale) {
        return newInstance(false, scale);
    }

    /**
     * @param isFen 是不是分时图
     * @param scale 右侧保留的位数
     * @return KLineFragment
     */
    public static KLineFragment newInstance(boolean isFen, int scale) {
        Bundle args = new Bundle();
        KLineFragment fragment = new KLineFragment();
        args.putBoolean("isFen", isFen);
        args.putInt("scale", scale);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kline, container, false);
        mKChartView = view.findViewById(R.id.kChartView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mAdapter = new KLineChartAdapter();
        mKChartView.setAdapter(mAdapter);
        mKChartView.setDateTimeFormatter(new DateFormatter());
        mKChartView.setGridRows(4);
        mKChartView.setGridColumns(4);
        assert getArguments() != null;
        {
            mKChartView.setMainDrawLine(getArguments().getBoolean("isFen", false)); // 设置默认是否为分时图
            mKChartView.setBaseCoinScale(getArguments().getInt("scale", 2)); // 设置右侧的保留的小数位数
        }
    }

    /**
     * 头部添加数据
     *
     * @param data        数据
     * @param isAnimation 是否展示动画
     */
    public void setKHeaderData(ArrayList<KLineEntity> data, boolean isAnimation) {
        if (mKChartView == null) return;
        if (data != null) {
            mKChartView.justShowLoading();
            DataHelper.calculate(data);
            mAdapter.addHeaderData(data);
            if (isAnimation) mKChartView.startAnimation();
            mKChartView.refreshEnd();
        } else {
            mKChartView.justShowLoading();
            mAdapter.clearData();
            mKChartView.refreshEnd();
        }
    }

    /**
     * 尾部添加数据
     *
     * @param data        数据
     * @param isAnimation 是否展示动画
     */
    public void setKFooterData(ArrayList<KLineEntity> data, boolean isAnimation) {
        if (mKChartView == null) return;
        if (data != null) {
            mKChartView.justShowLoading();
            DataHelper.calculate(data);
            if(mAdapter.getCount()==0) mKChartView.startAnimation();
            mAdapter.addFooterData(data);
            //if (isAnimation) mKChartView.startAnimation();
            mKChartView.refreshEnd();
        } else {
            mKChartView.justShowLoading();
            mAdapter.clearData();
            mKChartView.refreshEnd();
        }
    }

    /**
     * 设置主图是否为MA 和BOll
     */
    public void setMainDrawType(int type) {
        if (mKChartView == null) return;
        if (type == 1) {
            mKChartView.changeMainDrawType(Status.MA);
        } else if (type == 2) {
            mKChartView.changeMainDrawType(Status.BOLL);
        } else {
            mKChartView.changeMainDrawType(Status.NONE);
        }
    }

    /**
     * 设置副图的样式
     *
     * @param type macd--0  kdj--1  rsi--2  wr --3 隐藏---1
     */
    public void setChildDrawType(int type) {
        if (mKChartView == null) return;
        if (type == -1) {
            mKChartView.hideChildDraw();
        } else {
            mKChartView.setChildDraw(type);
        }
    }

    /**
     * 横竖屏切换
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mKChartView.setGridRows(3);
            mKChartView.setGridColumns(8);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mKChartView.setGridRows(4);
            mKChartView.setGridColumns(4);
        }
    }
}
