package com.spark.newbitrade.activity.wallet_coin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.AddressAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Address;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提币地址
 */
public class AddressActivity extends BaseActivity implements AddressContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvAddress;
    @BindView(R.id.llHead)
    LinearLayout llHead;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tvAddAddress)
    TextView tvAddAddress;

    private AddressAdapter adapter;
    private ArrayList<Address> addresses = new ArrayList<>();
    private String unit;
    //    private int pageNo;
    private AddresssPresenterImpl presenter;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        llHead.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new AddresssPresenterImpl(this);
        setTitle(getString(R.string.mention_money_address));
        unit = getIntent().getStringExtra("unit");
        initRvAddress();
        tvAddAddress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setListener() {
        super.setListener();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                itemClick(position);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                pageNo = 1;
                getAddressList(false);
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showDialog(AddressActivity.this, addresses.get(position).getId() + "");
                return false;
            }
        });

//        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                pageNo = pageNo + 1;
//                getAddressList(false);
//            }
//        });
    }

    private void initRvAddress() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAddress.setLayoutManager(manager);
        adapter = new AddressAdapter(AddressActivity.this, R.layout.item_address, addresses);
        adapter.bindToRecyclerView(rvAddress);
        adapter.isFirstOnly(true);
    }

    private void itemClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("address", addresses.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void loadData() {
        getAddressList(true);
    }

    /**
     * 获取提币地址
     */
    private void getAddressList(boolean isShow) {
        if (StringUtils.isNotEmpty(unit)) {
            if (isShow)
                showLoading();

//        HashMap<String, String> map = new HashMap<>();
//        map.put("pageNo", pageNo + "");
//        map.put("pageSize", GlobalConstant.PageSize + "");
//        map.put("unit", unit);
//        presenter.getAddress(map);

            presenter.findWalletWithdrawAddress(unit);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                unit = data.getStringExtra("unit");
                loadData();
            }
        }
    }


    @OnClick(R.id.tvAddAddress)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("unit", unit);
        showActivity(AddAddressActivity.class, bundle, 0);
    }

    private void showDialog(final Activity activity, final String id) {
        final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title(MyApplication.getApp().getString(R.string.warm_prompt))
                .titleTextColor(activity.getResources().getColor(R.color.black))
                .content(MyApplication.getApp().getString(R.string.str_delete_address))
                .setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                dialog.dismiss();
                            }
                        },
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                presenter.delWalletWithdrawAddressUsingGET(id);
                                dialog.superDismiss();
                            }
                        });
        dialog.show();
    }


    @Override
    public void findWalletWithdrawAddressSuccess(List<Address> obj) {
        try {
            hideAll();
            View view = LayoutInflater.from(activity).inflate(R.layout.empty_no_message, null);
            TextView textView = view.findViewById(R.id.tvMessage);
            adapter.setEmptyView(view);
            textView.setText(getString(R.string.no_extract_address_tag));
            if (obj != null && obj.size() > 0) {
//                if (pageNo == 1) {
                this.addresses.clear();
//                } else {
//                    adapter.loadMoreEnd();
//                }
                this.addresses.addAll(obj);
            } else {
//                if (pageNo == 1) {
                this.addresses.clear();
                adapter.setEmptyView(view);
//                }
            }
            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delWalletWithdrawAddressUsingGETSuccess(String obj) {
        hideAll();
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }

        getAddressList(true);
    }


    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        super.dealError(httpErrorEntity);
        hideAll();
    }

    @Override
    public void dealError(VolleyError volleyError) {
        super.dealError(volleyError);
        hideAll();
    }

    private void hideAll() {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }


}
