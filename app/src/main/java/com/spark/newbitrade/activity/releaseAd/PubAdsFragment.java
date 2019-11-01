package com.spark.newbitrade.activity.releaseAd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.kyleduo.switchbutton.SwitchButton;
import com.spark.library.otc.model.AdvertiseDto;
import com.spark.library.otc.model.AuthMerchantApplyMarginType;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.country.CountryActivity;
import com.spark.newbitrade.activity.my.ads.MyAdsActivity;
import com.spark.newbitrade.activity.store.PayWaySelectActivity;
import com.spark.newbitrade.adapter.SelectPayWayAdapter;
import com.spark.newbitrade.base.BaseLazyFragment;
import com.spark.newbitrade.dialog.ConfirmDialog;
import com.spark.newbitrade.entity.Ads;
import com.spark.newbitrade.entity.Country;
import com.spark.newbitrade.entity.CountryEntity;
import com.spark.newbitrade.entity.PayWay;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.IMyTextChange;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TextWatcher;
import com.spark.library.otc.model.MessageResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018/9/27 0027.
 */

/**
 * 发布或编辑c2c广告
 */
public class PubAdsFragment extends BaseLazyFragment implements ReleaseAdContract.View {
    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.tvSelectCountry)
    TextView tvSelectCountry;
    @BindView(R.id.tvCoinKind)
    TextView tvCoinKind;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.etOverflow)
    EditText etOverflow;
    @BindView(R.id.etMin)
    EditText etMin;
    @BindView(R.id.etMax)
    EditText etMax;
    @BindView(R.id.tvCountText)
    TextView tvCountText;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.tvPayWayText)
    TextView tvPayWayText;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.etPayTime)
    EditText etPayTime;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvRelease)
    TextView tvRelease;
    @BindView(R.id.llTime)
    LinearLayout llTime;
    @BindView(R.id.etReplyContent)
    EditText etReplyContent;
    @BindView(R.id.etjyPrice)
    EditText etjyPrice;
    @BindView(R.id.llOverflow)
    LinearLayout llOverflow;
    @BindView(R.id.tvLocalCurrency)
    TextView tvLocalCurrency;
    @BindView(R.id.tvjyPriceCurrency)
    TextView tvjyPriceCurrency;
    @BindView(R.id.tvMinCurrency)
    TextView tvMinCurrency;
    @BindView(R.id.tvMaxCurrency)
    TextView tvMaxCurrency;
    @BindView(R.id.llReply)
    LinearLayout llReply;
    @BindView(R.id.llMsg)
    LinearLayout llMsg;
    @BindView(R.id.sbPriceType)
    SwitchButton sbPriceType;
    @BindView(R.id.sbReply)
    SwitchButton sbReply;
    @BindView(R.id.llPrice)
    LinearLayout llPrice;
    @BindView(R.id.tvText)
    TextView tvText;
    @BindView(R.id.tvjyPriceTag)
    TextView tvjyPriceTag;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.llGdPrice)
    LinearLayout llGdPrice;
    @BindView(R.id.tvPriceCurrency)
    TextView tvPriceCurrency;

    private int advertiseType;
    private AuthMerchantApplyMarginType coinInfo;
    private List<AuthMerchantApplyMarginType> coinInfos = new ArrayList<>();
    private View payWayView;
    private List<PayWay> payWays = new ArrayList<>();
    private ReleasePresenterImpl presenter;
    private RecyclerView rvpayWay;
    private SelectPayWayAdapter payWayAdapter;
    private AlertDialog payWayDialog;
    private Ads ads;
    private String[] timeArray;
    private String currency;
    private String[] coinNames;
    private String strCountry = "China";
    private String currentPayway;
    private int type;
    private List<PayWaySetting> payWaySettingsSelected = new ArrayList<>();//选择的收款方式
    private String payIds = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                payWaySettingsSelected = (List<PayWaySetting>) bundle.getSerializable("payWaySettingsSelected");
            doClickPayWayItem2();
        }
        if (requestCode == CountryActivity.RETURN_COUNTRY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                CountryEntity country = (CountryEntity) data.getSerializableExtra("getCountry");

                currency = country.getLocalCurrency();
                tvSelectCountry.setText(country.getZhName());
                strCountry = country.getEnName();
                tvCoinKind.setText(currency);
                tvLocalCurrency.setText(currency);
                tvjyPriceCurrency.setText(currency);
                tvMinCurrency.setText(currency);
                tvMaxCurrency.setText(currency);
                tvPriceCurrency.setText(currency);
                getPrice(tvCoin.getText().toString(), tvLocalCurrency.getText().toString());
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pub_ads;
    }

    public static PubAdsFragment getInstance(int type, Ads ads) {
        PubAdsFragment fragment = new PubAdsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", type);
        bundle.putSerializable("ads", ads);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        payWays = new ArrayList<>();
        timeArray = getResources().getStringArray(R.array.timeLimit);
        presenter = new ReleasePresenterImpl(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            advertiseType = bundle.getInt("type");
            ads = (Ads) bundle.getSerializable("ads");
            if (advertiseType == 0) {
                tvCountText.setText(getString(R.string.text_buy_num));
            } else if (advertiseType == 1) {
                tvCountText.setText(getString(R.string.text_sell_num));
            }
            if (ads == null) {
                tvRelease.setText(getString(R.string.text_pub));
            } else {
                tvRelease.setText(getString(R.string.text_change));
                payIds = ads.getPayIds();
            }
        }
        setViewByFixPrice(true);

        currency = GlobalConstant.CNY;
        tvSelectCountry.setText(R.string.china);
        strCountry = "China";
        tvCoinKind.setText(currency);
        tvLocalCurrency.setText(currency);
        tvjyPriceCurrency.setText(currency);
        tvMinCurrency.setText(currency);
        tvMaxCurrency.setText(currency);
        tvPriceCurrency.setText(currency);
        getPrice(tvCoin.getText().toString(), tvLocalCurrency.getText().toString());
    }

    @Override
    protected void loadData() {
        presenter.listMerchantAdvertiseCoin();
        if (ads != null) {
            presenter.priceFind(ads.getCoinName(), "CNY");
        }
        sbPriceType.setChecked(true);
    }

    @OnClick({R.id.tvSelectCountry, R.id.tvCoin, R.id.tvPayWay, R.id.tvRelease, R.id.tvPayTime})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSelectCountry:
                showActivity(CountryActivity.class, null, CountryActivity.RETURN_COUNTRY);
                break;
            case R.id.tvCoin:
                if (coinNames != null) {
                    if (coinNames.length > 0) {
                        showListDialog(0);
                    }
                }
                break;
            case R.id.tvPayTime:
//                showListDialog(1);
                break;
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
    protected void setListener() {
        super.setListener();
        sbPriceType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setViewByFixPrice(isChecked);
            }
        });

        sbReply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llReply.setVisibility(View.VISIBLE);
                } else {
                    llReply.setVisibility(View.GONE);
                }
            }
        });

        etOverflow.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (ads == null || ads.getPriceType() == 1) {
                    String priceStr = tvPrice.getText().toString();
                    String overStr = etOverflow.getText().toString();
                    if (!StringUtils.isVaildText(etOverflow))
                        return;
                    if (!StringUtils.isEmpty(priceStr, overStr)) {
                        double price = Double.parseDouble(priceStr);
                        double over = Double.parseDouble(overStr);
                        double finalPrice = price * (0.01 * over);
                        etjyPrice.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(finalPrice, 2, null)));
                    }
                }
            }
        });
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                minChange();
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - (posDot + 1) > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });
        etMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                maxChange();
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - (posDot + 1) > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });

        etjyPrice.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                setEditText(etjyPrice, 2);
            }
        });

        etCount.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                countChange();
                setEditText(etCount, 6);
            }
        });

        etReplyContent.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(300)});
        etMessage.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(300)});
        etPrice.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                etjyPrice.setText(MathUtils.subZeroAndDot(etPrice.getText().toString()));
            }
        });
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
            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
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
            payWayDialog = new AlertDialog.Builder(getActivity()).setView(payWayView)
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
            if (payWay.isSelect()) content = content + "," + payWay.getName();
        }
        if (StringUtils.isEmpty(content)) tvPayWay.setText("");
        else
            tvPayWay.setText(content.length() > 1 ? content.substring(content.indexOf(",") + 1) : content);
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
     * 列表类选择框
     *
     * @param type 0-币种,1-付款期限
     */
    private void showListDialog(final int type) {
        NormalListDialog normalDialog = null;
        if (type == 0) {
            normalDialog = new NormalListDialog(getmActivity(), coinNames);
            normalDialog.title(getString(R.string.text_coin_type));
        } else if (type == 1) {
            normalDialog = new NormalListDialog(getmActivity(), timeArray);
            normalDialog.title(getString(R.string.text_pay_time));
        }
        normalDialog.titleBgColor(getResources().getColor(R.color.main_head_bg))
                .titleTextColor(getResources().getColor(R.color.main_font_content))
                .itemTextColor(getResources().getColor(R.color.main_font_content))
                .itemPressColor(getResources().getColor(R.color.sec_font_content))
                .lvBgColor(Color.parseColor("#ffffffff"));
        final NormalListDialog finalNormalDialog = normalDialog;
        normalDialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 0) {
                    doClickCoinClick(position);
                } else if (type == 1) {
                    tvPayTime.setText(timeArray[position]);
                }
                finalNormalDialog.dismiss();
            }
        });
        normalDialog.show();
    }

    /**
     * 选择币种的点击事件
     *
     * @param position
     */
    private void doClickCoinClick(int position) {
        coinInfo = coinInfos.get(position);
        if (coinInfo != null) {
            etCount.setHint(">= " + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + getString(R.string.str_and) + getString(R.string.str_ad_sell_must_max) + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
        }
        tvCoin.setText(coinInfo.getCoinName());
        presenter.priceFind(coinInfo.getCoinName(), "CNY");
    }

    /**
     * 确认是否直接去上架
     */
    private void showCofirmDialog() {
        final NormalDialog dialog = new NormalDialog(activity);
        dialog.isTitleShow(false).bgColor(Color.parseColor("#ffffff"))
                .content(getString(R.string.pub_ads_confirm))
                .contentGravity(Gravity.CENTER)
                .contentTextColor(Color.parseColor("#ff6a6e8a"))
                .btnTextColor(Color.parseColor("#ff6a6e8a"), Color.parseColor("#ff6a6e8a"))
                //.btnPressColor(Color.parseColor("#2B2B2B"))
                .btnText(getString(R.string.str_cancel), getString(R.string.dialog_sure))
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                finish();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                showActivity(MyAdsActivity.class, null);
                activity.finish();
                dialog.superDismiss();
            }
        });
        /*final MaterialDialog dialog = new MaterialDialog(activity);
        dialog.title(getString(R.string.warm_prompt)).titleTextColor(getResources().getColor(R.color.colorPrimary)).content(getString(R.string.pub_ads_confirm)).btnText(getString(R.string.cancle), getString(R.string.confirm)).setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        finish();
                        //setResult(RESULT_OK);
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {

                    }
                });
        dialog.show();*/
    }


    /**
     * 根据价格显示
     *
     * @param isChecked
     */

    private void setViewByFixPrice(Boolean isChecked) {
        if (isChecked) {   //固定价格
            llGdPrice.setVisibility(View.VISIBLE);
            llOverflow.setVisibility(View.GONE);
            tvText.setText(R.string.open_fixed_price_text);
            //tvjyPriceTag.setText(R.string.text_price_fixed);
            etjyPrice.setText("");
            etjyPrice.setHint("");
            etjyPrice.setEnabled(false);
        } else {//浮动价格
            llGdPrice.setVisibility(View.GONE);
            llOverflow.setVisibility(View.VISIBLE);
            llPrice.setVisibility(View.VISIBLE);
            tvText.setText(R.string.close_fixed_price_text);
            tvjyPriceTag.setText(R.string.text_trade_price);
            etjyPrice.setText("");
            etjyPrice.setHint(getString(R.string.text_auto_calculate_price));
            etjyPrice.setEnabled(false);
            String overStr = MathUtils.subZeroAndDot(etOverflow.getText().toString());
            if (StringUtils.isEmpty(overStr)) {
                etOverflow.setText("100");
            } else {
                etOverflow.setText(overStr);
            }
        }
    }

    /**
     * 最大量
     */
    private void maxChange() {
        String maxLimit = etMax.getText().toString();
        String price = etjyPrice.getText().toString();
        String number = etCount.getText().toString();
        if (!StringUtils.isEmpty(maxLimit, price, number) && Double.valueOf(price) != 0 && Double.valueOf(number) != 0) {
            Double max = Double.valueOf(price) * Double.valueOf(number);
            if (Double.valueOf(maxLimit) > max) {
                ToastUtils.showToast(getString(R.string.max_limit_tag) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(max, 2, null)) + currency);
            }
        }
    }

    /**
     * 最小量
     */
    private void minChange() {
        String minLimit = etMin.getText().toString();
        if (!StringUtils.isEmpty(minLimit, currency)) {
            if (Double.valueOf(minLimit) <= 0) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 0 + currency);
            }
            /*if (currency.equals(currency) && Double.valueOf(minLimit) < 100) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 100 + currency);
            }
            if (currency.contains("HK") && Double.valueOf(minLimit) < 100) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 100 + currency);
            }
            if (currency.contains("USD") && Double.valueOf(minLimit) < 10) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 10 + currency);
            }

            if (currency.equals("JPY") && Double.valueOf(minLimit) < 2000) {
                ToastUtils.showToast(getString(R.string.min_limit_tag) + 2000 + currency);
            }*/
        }

    }

    /**
     * 买入数量或卖出数量
     */
    private void countChange() {
        if (coinInfo != null) {
            String count = etCount.getText().toString();
            String max = MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString());
            String min = MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString());

            if (!StringUtils.isEmpty(count, max, min) && Double.valueOf(max) != 0 && Double.valueOf(min) != 0) {
                if (Double.valueOf(count) > Double.valueOf(max)) {
                    if (advertiseType == 0) {
                        ToastUtils.showToast(getString(R.string.str_ad_buy_must_min) + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + getString(R.string.str_and) + getString(R.string.str_ad_sell_must_max) + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
                    } else if (advertiseType == 1) {
                        ToastUtils.showToast(getString(R.string.str_ad_sell_must_min) + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + getString(R.string.str_and) + getString(R.string.str_ad_sell_must_max) + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
                    }
                    if (Double.valueOf(count) > Double.valueOf(max)) {
                        etCount.setText(max);
                    }
                }
            }
        }
    }


    /**
     * 发布或修改广告
     */
    private void releaseOrEditAd() {
        String coinName = "";
        if (coinInfo != null) {
            coinName = coinInfo.getCoinName();
        } else if (ads != null) {
            coinName = ads.getCoinName() + "";
        } else {
            ToastUtils.showToast(getString(R.string.str_prompt_coin_kind));
            return;
        }
        String price = MathUtils.subZeroAndDot(etjyPrice.getText().toString().trim());
        String minLimit = etMin.getText().toString().trim();
        String maxLimit = etMax.getText().toString().trim();
        String timeLimit = etPayTime.getText().toString().trim();
        int priceType = sbPriceType.isChecked() ? 0 : 1;
        String strOverFlow = "0";
        if (priceType == 1) {
            strOverFlow = etOverflow.getText().toString().trim();
        }
        String remark = etMessage.getText().toString();
        String number = etCount.getText().toString().trim();
        String pay = getPayByCode(tvPayWay.getText().toString());
//        String jyPassword = etPassword.getText().toString().trim();
        String auto = sbReply.isChecked() ? "1" : "0";
        String autoword = etReplyContent.getText().toString();
        String min = MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString());
        if (StringUtils.isEmpty(coinName)) {
            ToastUtils.showToast(getString(R.string.str_prompt_coin_kind));
        } else if (StringUtils.isEmpty(strCountry)) {
            ToastUtils.showToast(getString(R.string.str_prompt_country));
        } else if (!sbPriceType.isChecked() && StringUtils.isEmpty(strOverFlow)) {
            ToastUtils.showToast(getString(R.string.text_over_price));
            etOverflow.requestFocus();
        } else if (StringUtils.isEmpty(price)) {
            ToastUtils.showToast(getString(R.string.text_enter_trade_price));
            etjyPrice.requestFocus();
        } else if (Double.valueOf(price) == 0) {
            ToastUtils.showToast(getString(R.string.text_trade_price_not_zero));
            etjyPrice.requestFocus();
        } else if (StringUtils.isEmpty(number)) {
            ToastUtils.showToast(getString(R.string.str_prompt_trade_count));
            etCount.requestFocus();
        } else if (Double.valueOf(number) == 0) {
            ToastUtils.showToast(getString(R.string.text_trade_amount_not_zero));
            etCount.requestFocus();
        } else if (Double.valueOf(number) < Double.valueOf(min)) {
            if (advertiseType == 0) {
                ToastUtils.showToast(getString(R.string.str_ad_buy_must_min) + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + getString(R.string.str_and) + getString(R.string.str_ad_sell_must_max) + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
            } else if (advertiseType == 1) {
                ToastUtils.showToast(getString(R.string.str_ad_sell_must_min) + MathUtils.subZeroAndDot(coinInfo.getAdvMinLimit().toString()) + getString(R.string.str_and) + getString(R.string.str_ad_sell_must_max) + MathUtils.subZeroAndDot(coinInfo.getAdvMaxLimit().toString()));
            }
        }
//        else if (Double.valueOf(number) > MathUtils.getDoudleByBigDecimal(coinInfo.getAdvMaxLimit())) {
//            etCount.requestFocus();
//        } else if (Double.valueOf(number) < MathUtils.getDoudleByBigDecimal(coinInfo.getAdvMinLimit())) {
//            etCount.requestFocus();
//        }
        else if (StringUtils.isEmpty(minLimit)) {
            ToastUtils.showToast(getString(R.string.str_prompt_min_count));
            etMin.requestFocus();
        } else if (StringUtils.isEmpty(maxLimit)) {
            ToastUtils.showToast(getString(R.string.str_prompt_max_count));
            etMax.requestFocus();
        } else if (Double.valueOf(minLimit) > Double.valueOf(maxLimit)) {
            ToastUtils.showToast(getString(R.string.min_max_tag));
            etMax.requestFocus();
        } else if (StringUtils.isEmpty(pay)) {
            ToastUtils.showToast(getString(R.string.str_prompt_recieve_kind));
        } else if (StringUtils.isEmpty(timeLimit)) {
            ToastUtils.showToast(getString(R.string.str_prompt_pay_time));
            etPayTime.requestFocus();
        } else if (Double.valueOf(timeLimit) > 30 || Double.valueOf(timeLimit) < 15) {
            ToastUtils.showToast(getString(R.string.str_warrinig_pay_time));
            etPayTime.requestFocus();
        }
//        else if (StringUtils.isEmpty(jyPassword)) {
//            ToastUtils.showToast(getString(R.string.text_enter_money_pwd));
//            etPassword.requestFocus();
//        }
        else if (priceType == 1 && Double.valueOf(strOverFlow) <= 0) {
            ToastUtils.showToast(getString(R.string.str_warrinig_overflow));
            etPayTime.requestFocus();
        } else if (Double.valueOf(minLimit) <= 0) {
            ToastUtils.showToast(getString(R.string.min_limit_tag) + 0 + currency);
            etMin.requestFocus();
        } else if (Double.valueOf(maxLimit) > Double.valueOf(price) * Double.valueOf(number)) {
            ToastUtils.showToast(getString(R.string.max_limit_tag) + MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(price) * Double.valueOf(number), 2, null)) + currency);
            etMax.requestFocus();
        } else {

            AdvertiseDto advertiseDto = new AdvertiseDto();
            advertiseDto.setPrice(new BigDecimal(price));
            advertiseDto.setAdvertiseType(advertiseType);
            advertiseDto.setCoinName(coinName);
            advertiseDto.setMinLimit(new BigDecimal(minLimit));
            advertiseDto.setMaxLimit(new BigDecimal(maxLimit));
            advertiseDto.setTimeLimit(Integer.valueOf(timeLimit));
            advertiseDto.setCountry(strCountry);
            advertiseDto.setPriceType(priceType);
            advertiseDto.setPremiseRate(new BigDecimal(Double.parseDouble(strOverFlow) * 0.01));
            advertiseDto.setRemark(remark);
            advertiseDto.setPayMode(pay);
            advertiseDto.setNumber(new BigDecimal(number));
            if (sbReply.isChecked()) {
                advertiseDto.setAutoReply(1);
                advertiseDto.setAutoword(autoword);
            } else {
                advertiseDto.setAutoReply(0);
                advertiseDto.setAutoword("");
            }
            advertiseDto.setPayIds(payIds);
            /* 广告商家类型 0 普通 1 商家 */
            advertiseDto.setTradeType(0);

            if (ads == null) {
                presenter.createAdvertise(advertiseDto);
            } else {
                presenter.updateAdvertise(advertiseDto, ads.getId());
            }
        }
    }


    /**
     * 数据请求
     */
//    private void doPost(int id, String price, String coinName, String minLimit, String maxLimit, Integer timeLimit,
//                        String priceType, String premiseRate, String remark, String number, String pay, String jyPassword, String auto, String autoword) {
//        HashMap<String, String> map = new HashMap<>();
//
//        map.put("price", price);
//        map.put("advertiseType", advertiseType + "");
//        map.put("coinName", coinName);
//        map.put("minLimit", minLimit);
//        map.put("maxLimit", maxLimit);
//        map.put("timeLimit", timeLimit + "");
//        map.put("country", strCountry);
//        map.put("priceType", priceType);
//        map.put("premiseRate", premiseRate);
//        map.put("remark", remark);
//        map.put("number", number);
//        map.put("pay[]", pay);
//        map.put("jyPassword", jyPassword);
//        map.put("autoReply", auto);
//        if (sbReply.isChecked())
//            map.put("autoword", autoword);
//        else
//            map.put("autoword", "");
//        if (id != -1) {
//            map.put("id", id + "");
//            presenter.updateAd(map);
//        } else {
//            presenter.create(map);
//        }
//    }
    private void getPrice(String coin, String type) {
        if (!StringUtils.isEmpty(coin) && StringUtils.isEmpty(type)) {
        } else if (!StringUtils.isEmpty(coin) && !StringUtils.isEmpty(type)) {
            if (coinInfo == null) {
                for (AuthMerchantApplyMarginType obj : coinInfos) {
                    if (obj.getCoinName().equals(tvCoin.getText().toString())) {
                        coinInfo = obj;
                    }
                }
            }
        }
    }

    /**
     * 根据返回数据显示
     *
     * @param ads
     */
    private void initAdsData(Ads ads) {
        advertiseType = ads.getAdvertiseType();
        tvCoin.setText(ads.getCoinName());
        strCountry = ads.getCountry();
        if (ads.getCountry().equals("China")) {
            tvSelectCountry.setText(R.string.china);
        } else {
            tvSelectCountry.setText(R.string.str_usa);
        }
        //tvSelectCountry.setText(ads.getCountry());
        tvCoinKind.setText(ads.getLocalCurrency());
        tvLocalCurrency.setText(ads.getLocalCurrency());
        tvjyPriceCurrency.setText(ads.getLocalCurrency());
        tvMinCurrency.setText(ads.getLocalCurrency());
        tvMaxCurrency.setText(ads.getLocalCurrency());
        tvPriceCurrency.setText(ads.getLocalCurrency());
        //tvPrice.setText(ads.getMarketPrice() + "");
        sbPriceType.setChecked(ads.getPriceType() == 0);
        etjyPrice.setText(MathUtils.subZeroAndDot(ads.getPrice() + ""));
        etMin.setText(ads.getMinLimit() + "");
        etMax.setText(ads.getMaxLimit() + "");
        etCount.setText(ads.getNumber() + "");
        currentPayway = getSetPayByCode(ads.getPayMode());
        tvPayWay.setText(currentPayway);
//        tvPayTime.setText(ads.getTimeLimit() + " " + getString(R.string.text_ad_minu));
        etPayTime.setText(ads.getTimeLimit() + "");
        sbReply.setChecked(ads.getAuto() == 1);
        etReplyContent.setText(StringUtils.isEmpty(ads.getAutoword()) ? "" : ads.getAutoword());
        etMessage.setText(StringUtils.isEmpty(ads.getRemark()) ? "" : ads.getRemark());
        if (ads.getPriceType() == 0) {
            etPrice.setText(MathUtils.subZeroAndDot(ads.getPrice() + ""));
            etOverflow.setText("100");
        } else {
            etOverflow.setText(MathUtils.subZeroAndDot(ads.getPremiseRate() * 100 + ""));
        }
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
     * 控制小数位数
     *
     * @param editText
     * @param decimalNum
     */
    private void setEditText(EditText editText, int decimalNum) {
        String charSequence = editText.getText().toString();
        if (StringUtils.isVaildText(editText)) {
            if (charSequence.toString().contains(".")) { // 删除“.”后面超过限制位数后的数据
                if (charSequence.length() - 1 - charSequence.toString().indexOf(".") > decimalNum) {
                    charSequence = charSequence.toString().subSequence(0, charSequence.toString().indexOf(".") + decimalNum + 1).toString();
                    editText.setText(charSequence);
                    editText.setSelection(charSequence.length());
                    ToastUtils.showToast(getString(R.string.edit_decimals_tag));
                }
            }
        }
    }

    private InputFilter inputFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtils.showToast(getString(R.string.no_input_emoji));
                return "";
            }
            return null;
        }
    };

    @Override
    public void createAdvertiseSuccess(String obj) {
        /*if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        finish();*/
        goUp();
    }

    @Override
    public void updateAdvertiseSuccess(String obj) {
        /*if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        finish();*/
        goUp();
    }

    @Override
    public void listMerchantAdvertiseCoinSuccess(List<AuthMerchantApplyMarginType> obj) {
        if (obj == null) return;
        coinInfos.clear();
        coinInfos.addAll(obj);
        coinNames = new String[coinInfos.size()];
        for (int i = 0; i < coinInfos.size(); i++) {
            coinNames[i] = coinInfos.get(i).getCoinName();
        }

        if (coinInfo == null && ads != null) {
            for (AuthMerchantApplyMarginType authMerchantApplyMarginType : coinInfos) {
                if (authMerchantApplyMarginType.getCoinName().equals(ads.getCoinName())) {
                    coinInfo = authMerchantApplyMarginType;
                }
            }
        }
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

    @Override
    public void findAdvertiseDetailSuccess(Ads obj) {
        if (obj == null) return;
        ads = obj;
        initAdsData(ads);
    }


    @Override
    public void priceFindSuccess(MessageResult obj) {
        if (obj != null && obj.getData() != null) {
            String priceStr = MathUtils.subZeroAndDot(MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(new BigDecimal(obj.getData().toString()).toString()), 2, null)));
            tvPrice.setText(priceStr);
            String overStr = etOverflow.getText().toString();
            if (StringUtils.isNotEmpty(overStr) && StringUtils.isNotEmpty(priceStr)) {
                double price = Double.parseDouble(priceStr);
                double over = Double.parseDouble(overStr);
                double finalPrice = price * (0.01 * over);
                etjyPrice.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(finalPrice, 2, null)));
            }
            if (ads != null)
                presenter.findAdvertiseDetail(ads.getId());
        }
    }

    private void goUp() {
        final ConfirmDialog dialog = new ConfirmDialog(activity);
        dialog.setPositiveOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ads == null) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("adUp", true);
                    showActivity(MyAdsActivity.class, bundle);
                } else {
                    getActivity().setResult(Activity.RESULT_OK);
                }
                finish();
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setData(getString(R.string.warm_prompt), getString(R.string.str_pulish_ad_to_up), getString(R.string.str_cancel), getString(R.string.dialog_sure));
        dialog.show();
    }

}
