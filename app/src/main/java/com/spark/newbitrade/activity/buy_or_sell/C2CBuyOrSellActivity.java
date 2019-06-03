package com.spark.newbitrade.activity.buy_or_sell;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.library.otc.model.AuthMerchantFrontVo;
import com.spark.library.otc.model.OrderInTransitDto;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.MyAdvertiseShowVo;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 买入或卖出
 */
public class C2CBuyOrSellActivity extends BaseActivity implements C2CBuyOrSellContract.View {
    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvExchangeCount)
    TextView tvExchangeCount;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvLocalCoinText)
    TextView tvLocalCoinText;
    @BindView(R.id.etLocalCoin)
    EditText etLocalCoin;
    @BindView(R.id.tvOtherCoinText)
    TextView tvOtherCoinText;
    @BindView(R.id.etOtherCoin)
    EditText etOtherCoin;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.ivPay)
    ImageView ivPay;
    @BindView(R.id.ivWeChat)
    ImageView ivWeChat;
    @BindView(R.id.ivUnionPay)
    ImageView ivUnionPay;
    @BindView(R.id.tvRemainAmount)
    TextView tvRemainAmount;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvDeal)
    TextView tvDeal;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.ivPalpay)
    ImageView ivPalpay;
    @BindView(R.id.ivOther)
    ImageView ivOther;


    // private C2C.C2CBean c2cBean;
//    private C2CExchangeInfo c2CExchangeInfo;
    private C2CBuyOrSellPresenterImpl presenter;
    private String mode = "0";
    private MyTextWathcer baseWhater;
    private MyTextWathcer recWhater;
    private OrderConfirmDialog dialog;
    //    private String id;
//    private String avatar;
    private MyAdvertiseShowVo myAdvertiseShowVo;
    private int coinScale = 8;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_c2c_buy_sell;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        dialog = new OrderConfirmDialog(activity);
        baseWhater = new MyTextWathcer(0, etLocalCoin);
        recWhater = new MyTextWathcer(1, etOtherCoin);
        presenter = new C2CBuyOrSellPresenterImpl(this);
        tvGoto.setVisibility(View.INVISIBLE);
        tvGoto.setText(R.string.my_order);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            coinScale = bundle.getInt("coinScale", 8);
            myAdvertiseShowVo = (MyAdvertiseShowVo) bundle.getSerializable("myAdvertiseShowVo");
//            id = bundle.getString("id");
//            avatar = bundle.getString("avatar");
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        etLocalCoin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etLocalCoin.addTextChangedListener(baseWhater);
                    etOtherCoin.removeTextChangedListener(recWhater);
                }
            }
        });

        etOtherCoin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    etLocalCoin.removeTextChangedListener(baseWhater);
                    etOtherCoin.addTextChangedListener(recWhater);
                }
            }
        });
        dialog.setOrderListener(new OrderConfirmDialog.OnOrderListener() {
            @Override
            public void onConfirm(String pwd) {
                buyOrSell(pwd);
                dialog.dismiss();
            }
        });
    }

    @OnClick({R.id.tvConfirm, R.id.tvGoto})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvConfirm:
                if (MyApplication.getApp().isLogin()) {
                    String countStr = etOtherCoin.getText().toString();
                    String totalStr = etLocalCoin.getText().toString();

                    String max = MathUtils.subZeroAndDot(myAdvertiseShowVo.getMaxLimit().toString());
                    String min = MathUtils.subZeroAndDot(myAdvertiseShowVo.getMinLimit().toString());

                    if (StringUtils.isEmpty(countStr, totalStr)) {
                        ToastUtils.showToast(getString(R.string.incomplete_information));
                    } else if (Double.valueOf(totalStr) == 0) {
                        ToastUtils.showToast(getString(R.string.text_trade_amount_not_zero));
                        etLocalCoin.requestFocus();
                    } else if (Double.valueOf(totalStr) > Double.valueOf(max) || Double.valueOf(totalStr) < Double.valueOf(min)) {
                        if (myAdvertiseShowVo.getAdvertiseType() == 0) {
                            ToastUtils.showToast(getString(R.string.text_sell) + "必须" + "大于等于" + min + " 且 " + "小于等于" + max);
                        } else if (myAdvertiseShowVo.getAdvertiseType() == 1) {
                            ToastUtils.showToast(getString(R.string.text_buy) + "必须" + "大于等于" + min + " 且 " + "小于等于" + max);
                        }
                    } else {
                        showConfirmDialog();
                    }
                } else {
                    ToastUtils.showToast(getString(R.string.text_login_first));
                    showActivity(LoginActivity.class, null);
                }
                break;
            case R.id.tvGoto:
                if (MyApplication.getApp().isLogin()) {
                    setResult(RESULT_OK, null);
                    finish();
                } else {
                    ToastUtils.showToast(getString(R.string.text_login_first));
                    showActivity(LoginActivity.class, null);
                }
                break;
        }
    }

    /**
     * 下单
     */
    private void showConfirmDialog() {
        if (myAdvertiseShowVo != null) {
            double count = Double.parseDouble(etOtherCoin.getText().toString().trim());
            String strCount = MathUtils.subZeroAndDot(MathUtils.getRundNumber(count, coinScale, null)) + myAdvertiseShowVo.getCoinName();
            String strPrice = myAdvertiseShowVo.getPrice() + myAdvertiseShowVo.getLocalCurrency();
            String strTotal = etLocalCoin.getText().toString().trim() + tvLocalCoinText.getText().toString();
            dialog.setData(myAdvertiseShowVo.getAdvertiseType() + "", strPrice, strCount, strTotal);
            dialog.show();
        }
    }

    /**
     * 买或者卖
     */
    public void buyOrSell(String pwd) {
//        if (c2CExchangeInfo == null) return;
//        String unit = c2CExchangeInfo.getUnit() + "";
//        String price = c2CExchangeInfo.getPrice() + "";

//        HashMap<String, String> map = new HashMap<>();
//        map.put("id", id);
//        map.put("coinId", unit);
//        map.put("price", price);
//        map.put("money", money);
//        map.put("amount", amount);
//        map.put("remark", remark);
//        map.put("mode", mode);
//        if ("0".equals(c2CExchangeInfo.getAdvertiseType())) {
//            presenter.c2cSell(map);
//        } else {
//            presenter.c2cBuy(map);
//        }

        String money = etLocalCoin.getText().toString();
        String amount = etOtherCoin.getText().toString();

        OrderInTransitDto orderInTransitDto = new OrderInTransitDto();
        orderInTransitDto.setAdvertiseId(myAdvertiseShowVo.getId());
        orderInTransitDto.setMoney(new BigDecimal(money));
        orderInTransitDto.setPrice(myAdvertiseShowVo.getPrice());
        orderInTransitDto.setNumber(new BigDecimal(amount));
        orderInTransitDto.setRemark(myAdvertiseShowVo.getRemark());
        orderInTransitDto.setTradePwd(pwd);

        if (myAdvertiseShowVo.getAdvertiseType() == 0) {
            orderInTransitDto.setOrderType("1");
        } else {
            orderInTransitDto.setOrderType("0");
        }

        presenter.createOrder(orderInTransitDto);
    }

    @Override
    protected void loadData() {
        fillViews();
        if (myAdvertiseShowVo != null) {
            presenter.getAvgTime(myAdvertiseShowVo.getMemberId());
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case LoginActivity.RETURN_LOGIN:
//                    setButtonText();
//                    break;
//            }
//        }
//    }


//    @Override
//    public void c2cInfoSuccess(C2CExchangeInfo obj) {
//        if (obj == null) return;
//        c2CExchangeInfo = obj;
//        fillViews();
//        if ("0".equals(c2CExchangeInfo.getAdvertiseType())) {
//            tvTitle.setText(getString(R.string.text_sell) + c2CExchangeInfo.getUnit());
//            tvInfo.setText(getString(R.string.text_much_sale));
//        } else {
//            tvTitle.setText(getString(R.string.text_buy) + c2CExchangeInfo.getUnit());
//            tvInfo.setText(getString(R.string.text_much_buy));
//        }
//        setButtonText();
//    }

    private void fillViews() {
        if (myAdvertiseShowVo != null) {
            tvLimit.setText(myAdvertiseShowVo.getMinLimit() + "~" + myAdvertiseShowVo.getMaxLimit() + myAdvertiseShowVo.getLocalCurrency());
            tvLocalCoinText.setText(myAdvertiseShowVo.getLocalCurrency());
            tvOtherCoinText.setText(myAdvertiseShowVo.getCoinName());
//        if (!StringUtils.isEmpty(avatar)) {
//            Glide.with(this).load(avatar).into(ivHeader);
//        } else {
            Glide.with(this).load(R.mipmap.icon_avatar).into(ivHeader);
//        }
            tvName.setText(myAdvertiseShowVo.getRealName());
            String payMode = myAdvertiseShowVo.getPayMode();
            LogUtils.i("payMode==" + payMode);
            if (payMode.contains(GlobalConstant.alipay)) {
                ivPay.setVisibility(View.VISIBLE);
            }
            if (payMode.contains(GlobalConstant.wechat)) {
                ivWeChat.setVisibility(View.VISIBLE);
            }
            if (payMode.contains(GlobalConstant.card)) {
                ivUnionPay.setVisibility(View.VISIBLE);
            }
            if (payMode.contains(GlobalConstant.PAYPAL)) {
                ivPalpay.setVisibility(View.VISIBLE);
            }
            if (payMode.contains(GlobalConstant.other)) {
                ivOther.setVisibility(View.VISIBLE);
            }
            tvPrice.setText(myAdvertiseShowVo.getPrice() + myAdvertiseShowVo.getLocalCurrency());

            String s = StringUtils.isNotEmpty(myAdvertiseShowVo.getRemark()) ? myAdvertiseShowVo.getRemark() : "";
            String html = "<font color=#A0B0BC>" + getString(R.string.text_ad_message) + "</font> <br>" + s;
            tvMessage.setText(Html.fromHtml(html));
            tvExchangeCount.setText(myAdvertiseShowVo.getRangeTimeOrder() + "");
//        tvRemainAmount.setText(getString(R.string.text_surplus_num) + MathUtils.getRundNumber(Double.valueOf(BigDecimal.valueOf(myAdvertiseShowVo.getMaxTradableAmount()).toString()), 8, null));
            tvRate.setText(MathUtils.getRate(myAdvertiseShowVo.getRangeTimeSuccessOrder(), myAdvertiseShowVo.getRangeTimeOrder()));
            tvDeal.setText(myAdvertiseShowVo.getRangeTimeSuccessOrder() + "");
//        tvTime.setText(StringUtils.isEmpty(myAdvertiseShowVo.getDischargedTime()) ? "0.00" : c2CExchangeInfo.getDischargedTime());
//        HashMap<String, String> map = new HashMap<>();
//        map.put("unit", c2CExchangeInfo.getUnit());
//        presenter.queryRate(map);

            setButtonText();
        }
    }

    private void setButtonText() {
        if (myAdvertiseShowVo != null) {
            if (myAdvertiseShowVo.getAdvertiseType() == 0) {
                if (MyApplication.getApp().isLogin()) {
                    tvConfirm.setText(getString(R.string.text_sell));
                } else {
                    tvConfirm.setText(getString(R.string.text_to_login));
                }
            } else {
                if (MyApplication.getApp().isLogin()) {
                    tvConfirm.setText(getString(R.string.text_buy));
                } else {
                    tvConfirm.setText(getString(R.string.text_to_login));
                }
            }

            if (myAdvertiseShowVo.getAdvertiseType() == 0) {
                tvTitle.setText(getString(R.string.text_sell) + myAdvertiseShowVo.getCoinName());
                tvInfo.setText(getString(R.string.text_much_sale));
            } else {
                tvTitle.setText(getString(R.string.text_buy) + myAdvertiseShowVo.getCoinName());
                tvInfo.setText(getString(R.string.text_much_buy));
            }
        }
    }


//    @Override
//    public void c2cBuyOrSellSuccess(String data, String message) {
//        ToastUtils.showToast(message);
//        Bundle bundle = new Bundle();
//        bundle.putString("orderSn", data);
//        Intent intent = new Intent(this, OrderDetailActivity.class);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, 0);
//        /*setResult(RESULT_OK, null);
//        finish();*/
//    }
//
//    @Override
//    public void queryRateSuccess(String obj) {
//        coinScale = Integer.valueOf(obj);
//    }
//
//    @Override
//    public void doPostFail(Integer code, String toastMessage) {
//        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
//    }

    @Override
    public void createOrderSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj))
            ToastUtils.showToast(obj);

        finish();
    }

    @Override
    public void getAvgTimeSuccess(AuthMerchantFrontVo obj) {
        if (obj != null && obj.getAvgReleaseTime() != null) {
            tvTime.setText(obj.getAvgReleaseTime() + "");
        }
    }

    private class MyTextWathcer implements TextWatcher {
        private int type; // 0-兑换币，1-接收币
        private EditText editText;

        public MyTextWathcer(int type, EditText editText) {
            this.type = type;
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (myAdvertiseShowVo != null) {
                CharSequence charSequence = editable.toString();
                LogUtils.i("charSequence===" + charSequence);
                // 如果"."在起始位置,则起始位置自动补0
                if (charSequence.equals(".")) {
                    charSequence = "0" + charSequence;
                    editText.setText(charSequence);
                    editText.setSelection(2);
                    return;
                }

                // 如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (charSequence.toString().startsWith("0")
                        && charSequence.toString().trim().length() > 1) {
                    if (!charSequence.toString().substring(1, 2).equals(".")) {
                        editText.setText(charSequence.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
                String str = charSequence.toString();
                if (type == 0) {
                    int posDot = str.indexOf(".");
                    if (posDot > 0) {
                        if (str.length() - (posDot + 1) > 2) {
                            str = editable.delete(posDot + 3, posDot + 4).toString();
                        }
                    }
                    mode = "1";
                    if (StringUtils.isEmpty(str) || myAdvertiseShowVo.getPrice().equals("0"))
                        etOtherCoin.setText("0");
                    else if (myAdvertiseShowVo != null)
                        etOtherCoin.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.parseDouble(str) / myAdvertiseShowVo.getPrice().doubleValue(), coinScale, null)));
                } else {
                    mode = "0";
                    int posDot = str.indexOf(".");
                    if (posDot > 0) {
                        if (str.length() - (posDot + 1) > coinScale) {
                            str = editable.delete(posDot + coinScale + 1, posDot + coinScale + 2).toString();
                        }
                    }
                    if (StringUtils.isEmpty(str) || myAdvertiseShowVo.getPrice().equals("0"))
                        etLocalCoin.setText("0");
                    else if (myAdvertiseShowVo != null)
                        etLocalCoin.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.parseDouble(str) * myAdvertiseShowVo.getPrice().doubleValue(), 2, null)));
                }

            }
        }
    }


}