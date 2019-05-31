package com.spark.newbitrade.ui;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.FilterAdapter;
import com.spark.newbitrade.entity.FilterBean;
import com.spark.newbitrade.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;


public class FilterPopView extends PopupWindow implements View.OnClickListener {
    public static final int CALLFROM_C2C = 0;
    public static final int CALLFROM_ORDER = 1;
    public static final int CALLFROM_ADS = 2;
    public static final int CALLFROM_CURREBT_ENTRUST = 3;
    public static final int CALLFROM_HISTORY_ENTRUST = 4;
    private TextView tvTagFir;
    private TextView tvTagSec;
    private RecyclerView rvFir;
    private RecyclerView rvSec;
    private LinearLayout llC2c;
    private EditText etLimitMin;
    private EditText etLimitMax;
    private TextView tvReset;
    private TextView tvFinish;
    private int intType;
    private ArrayList<FilterBean> firstList = new ArrayList<>();
    private ArrayList<FilterBean> secList = new ArrayList<>();
    private ArrayList<FilterBean> thdList = new ArrayList<>();
    private Context context;
    private FilterAdapter firAdapter;
    private FilterAdapter secAdapter;
    private FilterAdapter thdAdapter;
    private FilterCallBack filterCallBack;
    private TextView tvTagThird;
    private TextView tvSymbol;
    private LinearLayout llEntrust;
    private EditText etCoin;
    private EditText etBaseCoin;
    private EditText etReferenceNumber;
    private RecyclerView rvTri;
    private NormalListDialog normalDialog;
    private String[] symbolName;
    private String all = "";


    public void setFirstList(ArrayList<FilterBean> list) {
        this.firstList.clear();
        this.firstList.addAll(list);
        firAdapter.notifyDataSetChanged();
        switch (intType) {
            case CALLFROM_C2C:

                break;
            case CALLFROM_ORDER:
            case CALLFROM_ADS:
                tvSymbol.setText(firstList.get(0).getStrUpload());
                symbolName = new String[firstList.size()];
                for (int i = 0; i < firstList.size(); i++) {
                    symbolName[i] = firstList.get(i).getStrUpload();
                }
                break;
            case CALLFROM_CURREBT_ENTRUST:

                break;
            case CALLFROM_HISTORY_ENTRUST:

                break;
        }
    }

    public void setSecList(ArrayList<FilterBean> list) {
        this.secList.clear();
        this.secList.addAll(list);
        secAdapter.notifyDataSetChanged();
    }

    public void setThdList(ArrayList<FilterBean> list) {
        this.thdList.clear();
        this.thdList.addAll(list);
        thdAdapter.notifyDataSetChanged();
    }

    public void setSmbol(String symbol) {
        String[] strings = symbol.split("/");
        etCoin.setText(strings[0]);
        etBaseCoin.setText(strings[1]);
    }

    public void setEntrustType(int type) {
        intType = type;
        initData();
    }

    public FilterPopView(Context context, FilterCallBack filterCallBack, int intType, int width, int height) {
        super(context);
        this.intType = intType;
        this.context = context;
        this.filterCallBack = filterCallBack;
        setFocusable(true);
        all = context.getString(R.string.all);
        View mainView = LayoutInflater.from(context).inflate(R.layout.view_popwindow_c2c, null);
        setContentView(mainView);
        initView(mainView);
        initData();
        initRv();
        setWidth(width);
        setHeight(height);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);
        setTouchable(true);
        setAnimationStyle(R.style.AnimTools);

        setListener();
    }

    private void initView(View mainView) {
        //第一栏
        tvTagFir = mainView.findViewById(R.id.tvTagFir);
        rvFir = mainView.findViewById(R.id.rvFir);
        tvSymbol = mainView.findViewById(R.id.tvSymbol);
        llEntrust = mainView.findViewById(R.id.llEntrust);
        etCoin = mainView.findViewById(R.id.etCoin);
        etBaseCoin = mainView.findViewById(R.id.etBaseCoin);

        //第二栏
        tvTagSec = mainView.findViewById(R.id.tvTagSec);
        rvSec = mainView.findViewById(R.id.rvSec);

        //第三栏
        tvTagThird = mainView.findViewById(R.id.tvTagThird);
        llC2c = mainView.findViewById(R.id.llC2c);
        etLimitMin = mainView.findViewById(R.id.etLimitMin);
        etLimitMax = mainView.findViewById(R.id.etLimitMax);
        etReferenceNumber = mainView.findViewById(R.id.etReferenceNumber);
        rvTri = mainView.findViewById(R.id.rvTri);

        tvReset = mainView.findViewById(R.id.tvReset);
        tvFinish = mainView.findViewById(R.id.tvFinish);
    }


    /**
     * 根据intType设置显示隐藏项，显示的文字
     */
    private void initData() {
        switch (intType) {
            case CALLFROM_C2C:
                rvFir.setVisibility(View.VISIBLE);
                rvSec.setVisibility(View.VISIBLE);
                llC2c.setVisibility(View.VISIBLE);
                break;
            case CALLFROM_ORDER:
                tvSymbol.setVisibility(View.VISIBLE);
                rvSec.setVisibility(View.VISIBLE);
                etReferenceNumber.setVisibility(View.VISIBLE);
                tvTagFir.setText(R.string.currency);
                tvTagSec.setText(R.string.type);
                tvTagThird.setText(R.string.str_reference_number);
                break;
            case CALLFROM_ADS:
                tvSymbol.setVisibility(View.VISIBLE);
                rvSec.setVisibility(View.VISIBLE);
                rvTri.setVisibility(View.VISIBLE);
                tvTagFir.setText(R.string.currency);
                tvTagSec.setText(R.string.type);
                tvTagThird.setText(R.string.str_status);
                break;
            case CALLFROM_CURREBT_ENTRUST:
                tvTagThird.setVisibility(View.GONE);
                rvTri.setVisibility(View.GONE);
                llEntrust.setVisibility(View.VISIBLE);
                rvSec.setVisibility(View.VISIBLE);
                tvTagFir.setText(R.string.currency);
                tvTagSec.setText(R.string.type);
                break;
            case CALLFROM_HISTORY_ENTRUST:
                tvTagThird.setVisibility(View.VISIBLE);
                llEntrust.setVisibility(View.VISIBLE);
                rvSec.setVisibility(View.VISIBLE);
                rvTri.setVisibility(View.VISIBLE);
                tvTagFir.setText(R.string.currency);
                tvTagSec.setText(R.string.type);
                tvTagThird.setText(R.string.str_status);
                break;
        }
    }

    private void initRv() {
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        rvFir.setLayoutManager(manager);
        firAdapter = new FilterAdapter(context, firstList);
        rvFir.setAdapter(firAdapter);
        GridLayoutManager secManager = new GridLayoutManager(context, 3);
        rvSec.setLayoutManager(secManager);
        secAdapter = new FilterAdapter(context, secList);
        rvSec.setAdapter(secAdapter);
        GridLayoutManager thdManager = new GridLayoutManager(context, 3);
        rvTri.setLayoutManager(thdManager);
        thdAdapter = new FilterAdapter(context, thdList);
        rvTri.setAdapter(thdAdapter);
    }

    private void setListener() {
        tvReset.setOnClickListener(this);
        tvFinish.setOnClickListener(this);
        tvSymbol.setOnClickListener(this);

        firAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FilterBean filterBean = firstList.get(position);
                resetDataByClick(firstList, filterBean.getName());
                firAdapter.notifyDataSetChanged();
            }
        });

        secAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FilterBean filterBean = secList.get(position);
                resetDataByClick(secList, filterBean.getName());
                secAdapter.notifyDataSetChanged();
            }
        });

        thdAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FilterBean filterBean = thdList.get(position);
                resetDataByClick(thdList, filterBean.getName());
                thdAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 点击事件重置选择状态
     *
     * @param arrayList
     * @param name
     */
    private void resetDataByClick(ArrayList<FilterBean> arrayList, String name) {
        for (FilterBean filterBean : arrayList) {
            if (StringUtils.isNotEmpty(filterBean.getName()) && filterBean.getName().equals(name)) {
                filterBean.setSelected(!filterBean.isSelected());
            } else if (intType != CALLFROM_C2C) {  //C2C的筛选需要多选
                filterBean.setSelected(false);
            } else if (!name.equals(all)) {
                arrayList.get(0).setSelected(false);
            }
        }
    }

    /**
     * 重置状态
     *
     * @param arrayList
     */
    private void resetData(ArrayList<FilterBean> arrayList) {
        for (FilterBean filterBean : arrayList) {
            filterBean.setSelected(false);
        }
        arrayList.get(0).setSelected(true);
        firAdapter.notifyDataSetChanged();
        secAdapter.notifyDataSetChanged();
        thdAdapter.notifyDataSetChanged();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReset:
                resetView();
                break;
            case R.id.tvFinish:
                filterCallBack.callBack(getParams());
                break;
            case R.id.tvSymbol:
                showListDialog();
                break;
        }

    }

    private void resetView() {
        switch (intType) {
            case CALLFROM_C2C:
                resetData(firstList);
                resetData(secList);
                etLimitMin.setText("");
                etLimitMax.setText("");
                break;
            case CALLFROM_ORDER:
                tvSymbol.setText(symbolName[0]);
                resetData(secList);
                etReferenceNumber.setText("");
                break;
            case CALLFROM_ADS:
                tvSymbol.setText(symbolName[0]);
                resetData(secList);
                resetData(thdList);
                break;
            case CALLFROM_CURREBT_ENTRUST:
                /*etCoin.setText("");
                etBaseCoin.setText("");*/
                resetData(secList);
                break;
            case CALLFROM_HISTORY_ENTRUST:
                /*etCoin.setText("");
                etBaseCoin.setText("");*/
                resetData(secList);
                resetData(thdList);
                break;
        }
    }

    /**
     * 获取参数
     *
     * @return
     */
    private HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        switch (intType) {
            case CALLFROM_C2C: // c2c界面
                params.put("country", getChooseResult(firstList));
                params.put("payway", getChooseResult(secList));
                params.put("minlimit", etLimitMin.getText().toString());
                params.put("maxlimit", etLimitMax.getText().toString());
                break;
            case CALLFROM_ORDER: // 我的订单
                params.put("unit", tvSymbol.getText().toString());
                params.put("type", getChooseResult(secList));
                params.put("referenceNumber", etReferenceNumber.getText().toString());
                break;
            case CALLFROM_ADS: // 我的广告
                params.put("unit", tvSymbol.getText().toString());
                params.put("advertiseType", getChooseResult(secList));
                params.put("status", getChooseResult(thdList));
                break;
            case CALLFROM_CURREBT_ENTRUST: // 当前委托
                params.put("side", getChooseResult(secList));
                break;
            case CALLFROM_HISTORY_ENTRUST: // 历史委托
                params.put("side", getChooseResult(secList));
                params.put("status", getChooseResult(thdList));
                break;
        }
        dismiss();
        return params;
    }

    private String getChooseResult(ArrayList<FilterBean> arrayList){
        StringBuffer buffer = new StringBuffer();
        for (FilterBean filterBean : arrayList) {
            if (filterBean.isSelected()) {
                buffer.append(filterBean.getStrUpload()+",");
            }
        }
        return StringUtils.getRealString(buffer.toString());
    }

    void showListDialog() {
        if (normalDialog == null) {
            normalDialog = new NormalListDialog(context, symbolName);
            normalDialog.title(context.getString(R.string.text_coin_type));
            normalDialog.titleBgColor(context.getResources().getColor(R.color.main_font_content));
        }
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvSymbol.setText(symbolName[position]);
                normalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

    public interface FilterCallBack {
        void callBack(HashMap<String, String> map);
    }
}
