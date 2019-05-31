package com.spark.newbitrade.activity.my_account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.library.otc.model.MessageResult;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.bind_account.BindAccountActivity;
import com.spark.newbitrade.activity.bind_account.BindAliActivity;
import com.spark.newbitrade.activity.bind_account.BindBankActivity;
import com.spark.newbitrade.activity.bind_account.BindPayPalActivity;
import com.spark.newbitrade.adapter.PayWayAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.DeleteDialog;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.SUCCESS_CODE;


/**
 * 收款账户
 */
public class MyAccountActivity extends BaseActivity implements MyAccountContract.View {
    @BindView(R.id.recyView)
    RecyclerView recyclerView;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;

    private MyAccountPresenterImpl presenter;
    private List<PayWaySetting> payWaySettings;
    private PayWayAdapter adapter;
    private DeleteDialog deleteDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            refreshData();
        }
    }

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
        tvGoto.setVisibility(View.GONE);
        ivAdd.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.text_receive_methods);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new MyAccountPresenterImpl(this);
        initRv();
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

    private void initRv() {
        payWaySettings = new ArrayList<>();
        adapter = new PayWayAdapter(payWaySettings);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.ivAdd})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.ivAdd:
                showActivity(BindAccountActivity.class, null, 0);
                break;
        }
    }

    @Override
    public void queryPayWayListSuccess(List<PayWaySetting> response) {
        if (response != null) {
            payWaySettings.clear();
            payWaySettings.addAll(response);
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
                Bundle bundle = new Bundle();
                switch (payType) {
                    case GlobalConstant.alipay:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        bundle.putString("payWay", GlobalConstant.alipay);
                        showActivity(BindAliActivity.class, bundle, 0);
                        break;
                    case GlobalConstant.wechat:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        bundle.putString("payWay", GlobalConstant.wechat);
                        showActivity(BindAliActivity.class, bundle, 0);
                        break;
                    case GlobalConstant.card:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        showActivity(BindBankActivity.class, bundle, 0);
                        break;
                    case GlobalConstant.PAYPAL:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        bundle.putBoolean("isPayPal", true);
                        showActivity(BindPayPalActivity.class, bundle, 0);
                        break;
                    case GlobalConstant.other:
                        bundle = new Bundle();
                        bundle.putSerializable("data", payWaySetting);
                        bundle.putBoolean("isPayPal", false);
                        showActivity(BindPayPalActivity.class, bundle, 0);
                        break;
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ivStatus:
                        PayWaySetting orderInTransit = (PayWaySetting) adapter.getItem(position);
                        if (orderInTransit.getStatus() == 1) {
                            presenter.doUpdateStatusBank(orderInTransit.getId(), 0);
                        } else {
                            presenter.doUpdateStatusBank(orderInTransit.getId(), 1);
                        }
                        break;
                }
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                PayWaySetting payWaySetting = (PayWaySetting) adapter.getItem(position);
                showReleaseDialog(payWaySetting.getId());
                return true;
            }
        });
    }


    @Override
    public void updateSuccess(MessageResult response) {
        if (response != null) {
            if (response.getCode() == SUCCESS_CODE) {
                refreshData();
            } else {
                ToastUtils.showToast(activity, response.getMessage());
            }
        }
    }

    /**
     * 提示框-删除
     */
    private void showReleaseDialog(final Long id) {
        if (deleteDialog == null) {
            deleteDialog = new DeleteDialog(activity);
            deleteDialog.setPositiveOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd = StringUtils.getText(deleteDialog.getPwdEditText());
                    if (StringUtils.isEmpty(pwd)) {
                        ToastUtils.showToast(activity, getString(R.string.text_enter_money_pwd));
                    } else {
                        presenter.doDeleteBank(id, pwd);
                        deleteDialog.dismiss();
                    }

                }
            });
        }
        deleteDialog.show();
    }


}
