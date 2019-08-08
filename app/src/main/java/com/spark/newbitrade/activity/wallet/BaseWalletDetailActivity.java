package com.spark.newbitrade.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.wallet_coin.ExtractActivity;
import com.spark.newbitrade.activity.wallet_coin.RechargeActivity;
import com.spark.newbitrade.activity.wallet_coin.TransferActivity;
import com.spark.newbitrade.adapter.AssetRecordAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.AssetRecord;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BaseWalletDetailActivity extends BaseActivity implements BaseWalletDetailContract.WalletView {

    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.tvToalAmount)
    TextView tvToalAmount;
    @BindView(R.id.tvUse)
    TextView tvUse;
    @BindView(R.id.tvFrozon)
    TextView tvFrozon;
    @BindView(R.id.llRecharge)
    LinearLayout llRecharge;
    @BindView(R.id.llExtract)
    LinearLayout llExtract;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.tvCNY)
    TextView tvCNY;
    @BindView(R.id.llHead)
    LinearLayout llHead;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.ivFilter)
    ImageView ivFilter;

    private BaseWalletDetailPresenterImpl presenter;

    private Wallet wallet;
    private int pageNo = 1;
    private ArrayList<AssetRecord> mDatas = new ArrayList<>();
    private AssetRecordAdapter adapter;
    //private Integer[] types = {null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private Integer[] types = {null, 1, 2, 7, 8, 9, 10};
    //1充值，2提币，3转入，4转出，5资金交易-扣除，6资金交易-增加，7法币卖出，8法币买入，9.缴纳商家认证保证金，10退回商家认证保证金
    Integer type;
    private boolean isBase = false;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        llHead.setVisibility(View.GONE);
        ivFilter.setVisibility(View.GONE);
        tvGoto.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            wallet = (Wallet) bundle.getSerializable("Wallet");

            isBase = bundle.getBoolean("isBase");
            if (!isBase) {
                llRecharge.setVisibility(View.GONE);
                llExtract.setVisibility(View.GONE);
            }

            if (wallet != null) {
                tvCoinName.setText(wallet.getCoinId());
                tvUse.setText(getString(R.string.text_can_used) + "\n" + MathUtils.subZeroAndDot(String.valueOf(wallet.getBalance().toPlainString())));
                tvFrozon.setText(getString(R.string.freeze_coin) +"\n" + MathUtils.subZeroAndDot(String.valueOf(wallet.getFrozenBalance().toPlainString())));
                tvToalAmount.setText(MathUtils.subZeroAndDot(String.valueOf((wallet.getBalance().add(wallet.getFrozenBalance())).toPlainString())));
                tvCNY.setText("≈" + MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(new BigDecimal(wallet.getTotalLegalAssetBalance()).toString()), 2, null)) + " CNY");
            }
        }

        initRvDetail();
        presenter = new BaseWalletDetailPresenterImpl(this);
    }

    @OnClick({R.id.llRecharge, R.id.llExtract, R.id.llTransfer, R.id.ivSelect, R.id.ivBack2})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);

        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.llRecharge:
                if (wallet != null) {
                    bundle.putSerializable("coin", wallet);
                    showActivity(RechargeActivity.class, bundle, 1);
                }
                break;
            case R.id.llExtract:
                if (wallet != null) {
                    bundle.putSerializable("coin", wallet);
                    showActivity(ExtractActivity.class, bundle, 1);
                }
                break;
            case R.id.llTransfer:
                bundle = new Bundle();
                bundle.putString("coinName", wallet.getCoinId());
                showActivity(TransferActivity.class, bundle, 1);
                break;
            case R.id.ivSelect://筛选
                showOrderDialog();
                break;
            case R.id.ivBack2://返回
                finish();
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setEnableLoadMore(false);
                pageNo = 1;
                getList(false);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                refreshLayout.setEnabled(false);
                pageNo = pageNo + 1;
                getList(false);
            }
        }, recyclerView);

    }

    /**
     * 获取钱包列表
     */
    private void getList(boolean isShow) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", GlobalConstant.PageSize + "");
        if (isBase) {
            presenter.getRecordList(isShow, type, map, GlobalConstant.COMMON, wallet.getCoinId());
        } else {
            presenter.getRecordList(isShow, type, map, GlobalConstant.OTC, wallet.getCoinId());
        }
    }

    private void initRvDetail() {
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
        adapter = new AssetRecordAdapter(mDatas);
        adapter.bindToRecyclerView(recyclerView);
    }

    @Override
    protected void loadData() {
        getList(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    public void getRecordListSuccess(List<AssetRecord> list) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        if (list == null) return;
        if (pageNo == 1) {
            mDatas.clear();
            if (list.size() == 0) {
                adapter.loadMoreEnd();
                adapter.setEmptyView(R.layout.empty_layout);
                adapter.notifyDataSetChanged();
            } else {
                mDatas.addAll(list);
            }
        } else {
            if (list.size() != 0) mDatas.addAll(list);
            else adapter.loadMoreEnd();
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    /**
     * 选择交易类型
     */
    private void showOrderDialog() {
        final String[] stringItems = getResources().getStringArray(R.array.trade_type);
        final ActionSheetDialog dialog = new ActionSheetDialog(activity, stringItems, null);
        dialog.isTitleShow(false).itemTextColor(getResources().getColor(R.color.font_main_title))
                .cancelText(getResources().getColor(R.color.font_main_content))
                .cancelText(getResources().getString(R.string.cancle)).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                pageNo = 1;
                type = types[position];
                //获取钱包列表
                getList(false);
            }
        });
    }

    //   type
    //        <item>充币</item>1
    //        <item>提币</item>2
    //        <item>转入</item>3
    //        <item>转出</item>4
    //        <item>资金交易-扣除</item>5
    //        <item>资金交易-增加</item>6
//    1充值，2提币，3转入，4转出，5资金交易-扣除，6资金交易-增加，7法币卖出，8法币买入，9.缴纳商家认证保证金，10退回商家认证保证金

}
