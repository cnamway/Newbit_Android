package com.spark.newbitrade.activity.wallet_coin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.spark.library.ac.model.AssetTransferDto;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.COMMON;
import static com.spark.newbitrade.utils.GlobalConstant.OTC;

/**
 * 资金划转
 */

public class TransferActivity extends BaseActivity implements TransferContract.TransferView {
    @BindView(R.id.tvToAccount)
    TextView tvToAccount;
    @BindView(R.id.tvFromAccount)
    TextView tvFromAccount;
    @BindView(R.id.edtNumber)
    EditText edtNumber;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvGetUnit)
    TextView tvGetUnit;

    private boolean isFromBaseToTrade = false;
    //    private String[] coinArrays;
    private Wallet wallet;
    private TransferPresenterImp presenter;
    //    private double baseBalance;
//    private double tradeBalance;
    //    private ArrayList<Wallet> wallets;
    private double useBanlance = 0;
    private String coinName;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        presenter = new TransferPresenterImp(this);
        tvTitle.setText(getString(R.string.str_assets_transfer));
    }

    @Override
    protected void initData() {
        super.initData();
        if (isFromBaseToTrade) {
            tvFromAccount.setText(getString(R.string.str_base_account));
            tvToAccount.setText(getString(R.string.str_trade_account));
        } else {
            tvFromAccount.setText(getString(R.string.str_trade_account));
            tvToAccount.setText(getString(R.string.str_base_account));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            coinName = bundle.getString("coinName");
            tvGetUnit.setText(coinName);
        }
    }

    @Override
    protected void loadData() {
        super.loadData();

        getCurWallet();
    }

    private void getCurWallet() {
        if (StringUtils.isNotEmpty(coinName)) {
            if (isFromBaseToTrade) {
                presenter.findWalletByCoinName(COMMON, coinName);
            } else {
                presenter.findWalletByCoinName(OTC, coinName);
            }
        }
    }

    /**
     * 设置可选币种
     */
//    private void setCoinArray() {
//        coinArrays = new String[wallets.size()];
//        for (int i = 0; i < wallets.size(); i++) {
//            Wallet w = wallets.get(i);
//            coinArrays[i] = w.getCoinId();
//        }
//        tvCoin.setText(coinArrays[0]);
//        tvGetUnit.setText(coinArrays[0]);
//        wallet = wallets.get(0);
//    }
    @OnClick({R.id.ivSwitchAccount, R.id.tvAll, R.id.tvSure})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);

        if (wallet == null) {
            getCurWallet();
            return;
        }

        switch (v.getId()) {
            case R.id.ivSwitchAccount://切换
                if (StringUtils.isNotEmpty(coinName)) {
                    if (isFromBaseToTrade) {
                        isFromBaseToTrade = false;
                        tvFromAccount.setText(getString(R.string.str_trade_account));
                        tvToAccount.setText(getString(R.string.str_base_account));
                        presenter.findWalletByCoinName(OTC, coinName);
                    } else {
                        isFromBaseToTrade = true;
                        tvFromAccount.setText(getString(R.string.str_base_account));
                        tvToAccount.setText(getString(R.string.str_trade_account));
                        presenter.findWalletByCoinName(COMMON, coinName);
                    }
                }
                break;
            case R.id.tvAll://全部
                if (wallet != null) {
                    edtNumber.setText(wallet.getBalance() + "");
                }
                break;
            case R.id.tvSure://确定
                checkInput();
                break;
        }
    }

    protected void checkInput() {
        String count = StringUtils.getText(edtNumber);
        String tradePassword = StringUtils.getText(etPassword);
        if (StringUtils.isEmpty(count)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.str_transfer_number));
        } else if (StringUtils.isEmpty(tradePassword)) {
            ToastUtils.showToast(activity, getString(R.string.str_please_input) + getString(R.string.text_money_pwd));
        } else if (Double.parseDouble(count) <= 0) {
            ToastUtils.showToast(activity, getString(R.string.str_min_coin_amount));
        } else {
            BigDecimal bigDecimal = new BigDecimal(count).setScale(8, BigDecimal.ROUND_HALF_UP);
            String coinName = wallet.getCoinId();
            if (isFromBaseToTrade) {
                if (Double.parseDouble(count) > useBanlance) {
                    ToastUtils.showToast(activity, getString(R.string.str_transfer_banlance) + useBanlance);
                } else {
                    presenter.doWithDraw(bigDecimal, coinName, AssetTransferDto.FromEnum.COMMON, AssetTransferDto.ToEnum.OTC, tradePassword);
                }
            } else {
                if (Double.parseDouble(count) > useBanlance) {
                    ToastUtils.showToast(activity, getString(R.string.str_transfer_banlance) + useBanlance);
                } else {
                    presenter.doWithDraw(bigDecimal, coinName, AssetTransferDto.FromEnum.OTC, AssetTransferDto.ToEnum.COMMON, tradePassword);
                }

            }
        }
    }

    @Override
    public void doWithDrawSuccess(String response) {
        if (StringUtils.isNotEmpty()) {
            ToastUtils.showToast(this, response);
        } else {
            ToastUtils.showToast(this, getString(R.string.savesuccess));
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void findWalletByCoinNameSuccess(Wallet obj) {
        if (obj != null) {
            wallet = obj;
            useBanlance = obj.getBalance();
            setCurBalance();
        }
    }

    private void setCurBalance() {
        tvBalance.setText(getString(R.string.str_transfer_banlance) + " " + useBanlance + " " + wallet.getCoinId());
    }


}
