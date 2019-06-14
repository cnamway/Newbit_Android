package com.spark.newbitrade.activity.store;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spark.newbitrade.R;
import com.spark.newbitrade.adapter.SelectPayWayAdapter;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.PayWay;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.library.otc.model.AdvertiseDto;
import com.spark.library.otc.model.AuthMerchantApplyMarginType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 商家发布广告
 */
public class StorePublishActivity extends BaseActivity implements StorePublishContract.View {

    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvRelease)
    TextView tvRelease;

    private String coinName;
    private String priceStr;
    private StorePublishPresenterImpl presenter;
    private AuthMerchantApplyMarginType coinInfo;
    private List<AuthMerchantApplyMarginType> coinInfos = new ArrayList<>();
    private List<PayWay> payWays = new ArrayList<>();
    private View payWayView;
    private String currentPayway;
    private RecyclerView rvpayWay;
    private SelectPayWayAdapter payWayAdapter;
    private AlertDialog payWayDialog;
    private Ads ads;
    private List<PayWaySetting> payWaySettingsSelected = new ArrayList<>();//选择的收款方式
    private String payIds = "";

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_store_publish;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                payWaySettingsSelected = (List<PayWaySetting>) bundle.getSerializable("payWaySettingsSelected");
            doClickPayWayItem2();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        EventBus.getDefault().register(this);
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        presenter = new StorePublishPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ads = (Ads) bundle.getSerializable("ads");
            if (ads != null) {
                setTitle("修改OTC广告");

                coinName = ads.getCoinName();
                priceStr = ads.getPrice() + "";

                tvCoinName.setText(coinName);
                tvPrice.setText(priceStr);
                etPrice.setText(ads.getNumber() + "");
                String payType = getSetPayByCode(ads.getPayMode());
                tvPayWay.setText(payType);
                payIds = ads.getPayIds();
            } else {
                setTitle("发布OTC广告");
                coinName = bundle.getString("coinName");
                priceStr = bundle.getString("priceStr");

                tvCoinName.setText(coinName);
                tvPrice.setText(priceStr);
            }
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        presenter.listMerchantAdvertiseCoin();
    }

    @OnClick({R.id.tvPayWay, R.id.tvRelease})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvPayWay:
                Bundle bundle = new Bundle();
                bundle.putSerializable("payWaySettingsSelected", (Serializable) payWaySettingsSelected);
                showActivity(PayWaySelectActivity.class, bundle, 1);
//                if (payWays.size() > 0) {
//                    showPayWayDialog();
//                } else {
//                    presenter.queryPayWayList();
//                }
                break;
            case R.id.tvRelease:
                releaseOrEditAd();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destory();
    }

    @Override
    public void listMerchantAdvertiseCoinSuccess(List<AuthMerchantApplyMarginType> obj) {
        if (obj == null) return;
        coinInfos.clear();
        coinInfos.addAll(obj);
        for (AuthMerchantApplyMarginType type : coinInfos) {
            if (coinName.equals(type.getCoinName())) {
                coinInfo = type;
                etPrice.setHint(">=" + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + " 且 " + "<=" + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
            }
        }
    }

    /**
     * 选择支付方式
     */
    private void showPayWayDialog() {
        if (payWayView == null) {
            for (int i = 0; i < payWays.size(); i++) {
                if (StringUtils.isNotEmpty(currentPayway)) {
                    if (currentPayway.contains(payWays.get(i).getName())) {
                        payWays.get(i).setSelect(true);
                    }
                }
            }
            payWayView = getLayoutInflater().inflate(R.layout.dialog_coins_view, null);
            rvpayWay = payWayView.findViewById(R.id.rvCoinList);
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvpayWay.setLayoutManager(manager);
            payWayAdapter = new SelectPayWayAdapter(R.layout.item_pub_ads_dialog_coin_info, payWays);
            payWayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    payWays.get(position).setSelect(!payWays.get(position).isSelect());
                    payWayAdapter.notifyDataSetChanged();
                }
            });
            rvpayWay.setAdapter(payWayAdapter);
            payWayDialog = new AlertDialog.Builder(this).setView(payWayView)
                    .setPositiveButton(getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doClickPayWayItem();
                        }
                    }).setNegativeButton(getString(R.string.dialog_one_cancel), null).create();
        }
        payWayDialog.show();
        payWayDialog.getWindow().setBackgroundDrawableResource(R.color.main_head_bg);
    }

    /**
     * 点击选择支付方式
     */
    private void doClickPayWayItem() {
        String content = "";
        for (PayWay payWay : payWays) {
            if (payWay.isSelect()) {
                content = content + "," + payWay.getName();
            }
        }
        if (StringUtils.isEmpty(content)) {
            tvPayWay.setText("");
        } else {
            tvPayWay.setText(content.length() > 1 ? content.substring(content.indexOf(",") + 1) : content);
        }
    }

    /**
     * 点击选择支付方式
     */
    private void doClickPayWayItem2() {
        String content = "";
        payIds = "";
        for (PayWaySetting payWay : payWaySettingsSelected) {
            payIds = payIds + "," + payWay.getId();
            content = content + "," + getSetPayByCode(payWay.getPayType());
        }
        if (StringUtils.isEmpty(content)) {
            tvPayWay.setText("");
        } else {
            tvPayWay.setText(content.length() > 1 ? content.substring(content.indexOf(",") + 1) : content);
            payIds = payIds.length() > 1 ? payIds.substring(payIds.indexOf(",") + 1) : payIds;
        }
    }

    /**
     * 发布或修改广告
     */
    private void releaseOrEditAd() {
        String number = etPrice.getText().toString().trim();
        String pay = getPayByCode(tvPayWay.getText().toString());
        if (StringUtils.isEmpty(number)) {
            ToastUtils.showToast(getString(R.string.str_prompt_trade_count));
            etPrice.requestFocus();
            return;
        }
        if (coinInfo != null) {
            if (Double.valueOf(number) > MathUtils.getDoudleByBigDecimal(coinInfo.getAdvMaxLimit())) {
                ToastUtils.showToast(getString(R.string.text_sell_num) + "必须" + "大于等于" + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + " 且 " + "小于等于" + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
                etPrice.requestFocus();
                return;
            } else if (Double.valueOf(number) < MathUtils.getDoudleByBigDecimal(coinInfo.getAdvMinLimit())) {
                ToastUtils.showToast(getString(R.string.text_sell_num) + "必须" + "大于等于" + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + " 且 " + "小于等于" + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
                etPrice.requestFocus();
                return;
            }
        }
        if (StringUtils.isEmpty(coinName)) {
            ToastUtils.showToast(getString(R.string.str_prompt_coin_kind));
        } else if (StringUtils.isEmpty(priceStr)) {
            ToastUtils.showToast(getString(R.string.text_enter_trade_price));
        } else if (Double.valueOf(priceStr) == 0) {
            ToastUtils.showToast(getString(R.string.text_trade_price_not_zero));
        } else if (Double.valueOf(number) == 0) {
            ToastUtils.showToast(getString(R.string.text_trade_amount_not_zero));
            etPrice.requestFocus();
        } else if (StringUtils.isEmpty(pay)) {
            ToastUtils.showToast(getString(R.string.str_prompt_recieve_kind));
        } else {

            AdvertiseDto advertiseDto = new AdvertiseDto();
            advertiseDto.setPrice(new BigDecimal(priceStr));
            advertiseDto.setAdvertiseType(1);
            advertiseDto.setCoinName(coinName);
//            advertiseDto.setMinLimit(new BigDecimal(minLimit));
//            advertiseDto.setMaxLimit(new BigDecimal(maxLimit));
//            advertiseDto.setTimeLimit(Integer.valueOf(timeLimit));
//            advertiseDto.setCountry(strCountry);
            advertiseDto.setPriceType(0);
//            advertiseDto.setPremiseRate(new BigDecimal(strOverFlow));
//            advertiseDto.setRemark(remark);
            advertiseDto.setPayMode(pay);
            advertiseDto.setNumber(new BigDecimal(number));
//            if (sbReply.isChecked()) {
//                advertiseDto.setAutoReply(1);
//                advertiseDto.setAutoword(autoword);
//            } else {
            advertiseDto.setAutoReply(0);
            advertiseDto.setAutoword("");
            advertiseDto.setPayIds(payIds);
//            }

            /* 广告商家类型 0 普通 1 商家 */
            advertiseDto.setTradeType(1);

            if (ads == null) {
                presenter.createAdvertise(advertiseDto);
            } else {
                presenter.updateAdvertise(advertiseDto, ads.getId());
            }

        }
    }

    /**
     * 将选择的支付方式转换成中文
     *
     * @param payway
     * @return
     */
    private String getPayByCode(String payway) {
        if (StringUtils.isNotEmpty(payway)) {
            StringBuffer stringBuffer = new StringBuffer();
            if (payway.contains(getString(R.string.str_payway_ali))) {
                stringBuffer = stringBuffer.append(GlobalConstant.alipay).append(",");
            }
            if (payway.contains(getString(R.string.str_payway_wechat))) {
                stringBuffer = stringBuffer.append(GlobalConstant.wechat).append(",");
            }
            if (payway.contains(getString(R.string.str_payway_union))) {
                stringBuffer = stringBuffer.append(GlobalConstant.card).append(",");
            }
            if (payway.contains(getString(R.string.str_paypal))) {
                stringBuffer = stringBuffer.append(GlobalConstant.PAYPAL).append(",");
            }
            if (payway.contains(getString(R.string.str_other))) {
                stringBuffer = stringBuffer.append(GlobalConstant.other).append(",");
            }
            return StringUtils.getRealString(stringBuffer.toString());
        }
        return payway;
    }

    /**
     * 将返回的中文根据系统语言
     *
     * @param payway
     * @return
     */
    private String getSetPayByCode(String payway) {
        if (StringUtils.isNotEmpty(payway)) {
            StringBuffer stringBuffer = new StringBuffer();
            if (payway.contains(GlobalConstant.alipay)) {
                stringBuffer = stringBuffer.append(getString(R.string.str_payway_ali)).append(",");
            }
            if (payway.contains(GlobalConstant.wechat)) {
                stringBuffer = stringBuffer.append(getString(R.string.str_payway_wechat)).append(",");
            }
            if (payway.contains(GlobalConstant.card)) {
                stringBuffer = stringBuffer.append(getString(R.string.str_payway_union)).append(",");
            }
            if (payway.toLowerCase().contains(GlobalConstant.PAYPAL)) {
                stringBuffer = stringBuffer.append(getString(R.string.str_paypal)).append(",");
            }
            if (payway.contains(GlobalConstant.other)) {
                stringBuffer = stringBuffer.append(getString(R.string.str_other)).append(",");
            }
            return StringUtils.getRealString(stringBuffer.toString());
        }
        return payway;
    }

    @Override
    public void createAdvertiseSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            ToastUtils.showToast(obj);
        }
        finish();
    }

    @Override
    public void queryPayWayListSuccess(List<PayWaySetting> obj) {
        if (obj != null && obj.size() > 0) {

            Set<String> payWaySet = new HashSet<>();

            for (PayWaySetting payWaySetting : obj) {
                if (payWaySetting.getStatus() == 1) {
                    if (payWaySetting.getPayType().contains(GlobalConstant.alipay)) {
                        payWaySet.add(getString(R.string.str_payway_ali));
                    }
                    if (payWaySetting.getPayType().contains(GlobalConstant.wechat)) {
                        payWaySet.add(getString(R.string.str_payway_wechat));
                    }
                    if (payWaySetting.getPayType().contains(GlobalConstant.card)) {
                        payWaySet.add(getString(R.string.str_payway_union));
                    }
                    if (payWaySetting.getPayType().toLowerCase().contains(GlobalConstant.PAYPAL)) {
                        payWaySet.add(getString(R.string.str_paypal));
                    }
                    if (payWaySetting.getPayType().contains(GlobalConstant.other)) {
                        payWaySet.add(getString(R.string.str_other));
                    }
                }
            }

            Iterator<String> value = payWaySet.iterator();
            while (value.hasNext()) {
                String pay = value.next();
                payWays.add(new PayWay(pay));
            }

            if (payWays.size() > 0) {
                showPayWayDialog();
            } else {
                ToastUtils.showToast(getString(R.string.bind_account));
            }
        } else {
            ToastUtils.showToast(getString(R.string.bind_account));
        }
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        presenter.listMerchantAdvertiseCoin();
    }

    @Override
    public void updateAdvertiseSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            ToastUtils.showToast(obj);
        }
        setResult(RESULT_OK);
        finish();
    }
}
