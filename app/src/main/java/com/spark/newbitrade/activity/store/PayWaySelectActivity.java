package com.spark.newbitrade.activity.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.PayWaySelectAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 选择收款方式
 */
public class PayWaySelectActivity extends BaseActivity implements PayWaySelectContract.View {
    @BindView(R.id.recyView)
    RecyclerView recyclerView;

    private PayWaySelectPresenterImpl presenter;
    private List<PayWaySetting> payWaySettings;
    private PayWaySelectAdapter adapter;
    private List<PayWaySetting> payWaySettingsSelected = new ArrayList<>();//选择的收款方式
    private HashMap<String, PayWaySetting> hashMap = new HashMap<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        tvTitle.setText(R.string.str_prompt_recieve_kind);
        tvGoto.setVisibility(View.VISIBLE);
        tvGoto.setText(getString(R.string.dialog_sure));
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new PayWaySelectPresenterImpl(this);
        initRv();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payWaySettingsSelected = (List<PayWaySetting>) bundle.getSerializable("payWaySettingsSelected");
            for (PayWaySetting payWaySetting : payWaySettingsSelected) {
                String payType = payWaySetting.getPayType();
                hashMap.put(payType, payWaySetting);
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        refreshData();
    }

    private void refreshData() {
        if (MyApplication.getApp().isLogin()) {
            presenter.queryPayWayList();
        }
    }

    @OnClick({R.id.tvGoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGoto:
                List<PayWaySetting> payWaySettingsSelected = new ArrayList<>();//选择的收款方式
                for (PayWaySetting payWaySetting : payWaySettings) {
                    if (payWaySetting.getIsSelected() == 1) {
                        payWaySettingsSelected.add(payWaySetting);
                    }
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("payWaySettingsSelected", (Serializable) payWaySettingsSelected);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void initRv() {
        payWaySettings = new ArrayList<>();
        adapter = new PayWaySelectAdapter(payWaySettings);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void queryPayWayListSuccess(List<PayWaySetting> response) {
        if (response != null) {
            payWaySettings.clear();
            for (PayWaySetting payWaySetting : response) {
                if (payWaySetting.getStatus() == 1) {
                    payWaySettings.add(payWaySetting);
                }
            }
            for (PayWaySetting payWaySetting : payWaySettings) {
                for (PayWaySetting temp : payWaySettingsSelected) {
                    if (temp.getId().longValue() == payWaySetting.getId().longValue()) {
                        payWaySetting.setIsSelected(1);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PayWaySetting payWaySetting = (PayWaySetting) adapter.getItem(position);
                String payType = payWaySetting.getPayType();
                if (hashMap.containsKey(payType)) {
                    PayWaySetting temp = hashMap.get(payType);
                    if (temp.getId().longValue() != payWaySetting.getId().longValue()) {
                        ToastUtils.showToast(getString(R.string.str_payway_only_one));
                    } else {
                        hashMap.remove(payType);
                        ((PayWaySetting) adapter.getItem(position)).setIsSelected(0);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    hashMap.put(payType, payWaySetting);
                    ((PayWaySetting) adapter.getItem(position)).setIsSelected(1);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }


}
