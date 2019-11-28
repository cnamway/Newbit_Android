package com.spark.newbitrade.activity.wallet_coin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestBindListener;
import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.google.gson.Gson;
import com.spark.library.ac.model.MessageResult;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.Address;
import com.spark.newbitrade.entity.Captcha;
import com.spark.newbitrade.entity.ExtractInfo;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.entity.Wallet;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.KeyboardUtils;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH;
import static com.spark.newbitrade.utils.GlobalConstant.CAPTCH2;

/**
 * 提币
 */
public class ExtractActivity extends BaseActivity implements ExtractContract.ExtractView {
    @BindView(R.id.tvSelectAdress)
    TextView tvSelectAdress;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvServiceFee)
    TextView tvServiceFee;
    @BindView(R.id.tvFinalCount)
    TextView tvFinalCount;
    @BindView(R.id.tvCollectUnit)
    TextView tvCollectUnit;
    @BindView(R.id.tvBuyCanUse)
    TextView tvBuyCanUse;
    @BindView(R.id.etPassword)
    EditText etPassword;
    //    @BindView(R.id.tvBalance)
//    TextView tvBalance;
    @BindView(R.id.tvCanUseUnit)
    TextView tvCanUseUnit;
    @BindView(R.id.tvAddress)
    EditText tvAddress;
    @BindView(R.id.tvGetUnit)
    TextView tvGetUnit;
    @BindView(R.id.tvExtract)
    TextView tvExtract;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.llCount22)
    LinearLayout llCount22;
    @BindView(R.id.llView)
    View llView;

    private ExtractPresnetImpl presnet;
    private Wallet wallet;
    private HashMap<String, ExtractInfo> map;
    private ExtractInfo extractInfo;
    private int withdrawFeeType = 1;//提币手续费类型：1-固定金额 2-按比例

    private TimeCount timeCount;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;
    private String phone;
    private boolean isFeeZero = false;//false 外部提币  true内部提币

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.cancelUtils();
            gt3GeetestUtils = null;
        }
        presnet.destory();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extract;
    }

    @Override
    protected void initView() {
        super.initView();
        setSetTitleAndBack(false, true);
        tvTitle.setText(getString(R.string.mention_money));
        tvAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    String address = tvAddress.getText().toString().trim();
                    if (StringUtils.isNotEmpty(address)) {
                        checkAddress(address);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        map = new HashMap<>();
        presnet = new ExtractPresnetImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            wallet = (Wallet) bundle.getSerializable("coin");
            if (wallet != null) {
                tvTitle.setText(getString(R.string.str_send) + " " + wallet.getCoinId());
                tvCanUseUnit.setText(wallet.getCoinId());
                tvGetUnit.setText(wallet.getCoinId());
                tvCollectUnit.setText(wallet.getCoinId());
                tvBuyCanUse.setText(MathUtils.subZeroAndDot(String.valueOf(wallet.getBalance().toPlainString())));
            }
        }
        timeCount = new TimeCount(60000, 1000, tvGetCode);
        gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
        User user = MyApplication.getApp().getCurrentUser();
        if (user != null) {
            phone = user.getMobilePhone();
        }
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (wallet != null)
            presnet.getExtractInfo(wallet.getCoinId());
    }

    @OnClick({R.id.tvSelectAdress, R.id.tvAll, R.id.tvExtract, R.id.tvGetCode, R.id.llCount22, R.id.llView})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
//            case R.id.tvCoin://选择币种
//                if (coinArrays == null) return;
//                showCoinDialog();
//                break;
            case R.id.tvSelectAdress://选择地址
                Bundle bundle = new Bundle();
                bundle.putSerializable("unit", wallet.getCoinId());
                showActivity(AddressActivity.class, bundle, 1);
                break;
            case R.id.tvAll://全部
                String address = tvAddress.getText().toString().trim();
                if (StringUtils.isEmpty(address)) {
                    handler.sendEmptyMessage(0);
                } else {
                    if (extractInfo != null && wallet != null) {
                        etCount.setText(MathUtils.subZeroAndDot(wallet.getBalance().toPlainString()));
                    }
                    clearFocus();
                }
                break;
            case R.id.tvExtract://确定
                clearFocus();
                checkInput();
                break;
            case R.id.tvGetCode:
                getCode();
                break;
            case R.id.llCount22:
                address = tvAddress.getText().toString().trim();
                if (StringUtils.isEmpty(address)) {
                    handler.sendEmptyMessage(0);
                } else {
                    llCount22.setVisibility(View.GONE);
                }
                break;
            case R.id.llView:
                clearFocus();
                break;
        }

    }

    private void clearFocus() {
        tvAddress.clearFocus();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtils.showToast(getResources().getString(R.string.str_assets_select_addrss));
        }
    };

    /**
     * 获取验证码
     */
    private void getCode() {
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showToast(R.string.phone_not_correct);
        } else {
            presnet.getPhoneCode(phone);
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        etCount.addTextChangedListener(localChangeWatcher);
    }

    private TextWatcher localChangeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String amount = StringUtils.getText(etCount);
            if (amount.startsWith(".")) {
                etCount.setText("0.");
                etCount.setSelection(etCount.length());
            } else {
                if (StringUtils.isNotEmpty(amount)) {
                    if (isFeeZero) {//false 外部提币  true内部提币
                        double fee = 0;
                        tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(amount + "", fee + "", 8)));
                        tvServiceFee.setText(0 + "");
                    } else {
                        double fee = 0;
                        //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                        if (withdrawFeeType == 2) {
                            double money = Double.parseDouble(amount);

                            if (extractInfo != null && extractInfo.getWithdrawFee() != null) {
                                fee = extractInfo.getWithdrawFee().doubleValue();
                            }

                            double minFee = 0;
                            if (extractInfo != null && extractInfo.getMinWithdrawFee() != null) {
                                minFee = extractInfo.getMinWithdrawFee().doubleValue();
                            }

                            double maxFee = 0;
                            if (extractInfo != null && extractInfo.getMaxWithdrawFee() != null) {
                                maxFee = extractInfo.getMaxWithdrawFee().doubleValue();
                            }

                            if (money * fee < minFee) {
                                tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(money + "", minFee + "", 8)));
                                tvServiceFee.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(minFee, 8, null)));
                            } else if (money * fee > maxFee) {
                                tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(money + "", maxFee + "", 8)));
                                tvServiceFee.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(maxFee, 8, null)));
                            } else {
                                tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(money + "", MathUtils.getBigDecimalMultiplyWithScale(money + "", fee + "", 8), 8)));
                                tvServiceFee.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money * fee, 8, null)));
                            }
                        } else {
                            if (extractInfo != null && extractInfo.getWithdrawFee() != null) {
                                fee = extractInfo.getWithdrawFee().doubleValue();
                            }
                            tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(amount + "", fee + "", 8)));
                        }
                    }
                } else {
                    tvFinalCount.setText(0 + "");
                    if (isFeeZero) {//false 外部提币  true内部提币
                        double fee = 0;
                        tvServiceFee.setText(MathUtils.subZeroAndDot(fee + ""));
                    } else {
                        tvServiceFee.setText(MathUtils.subZeroAndDot(String.valueOf(extractInfo.getWithdrawFee().toPlainString())));
                    }
                }
            }

        }
    };

    protected void checkInput() {
        if (wallet != null) {
            String address = tvAddress.getText().toString();
            String amount = StringUtils.getText(etCount);
            String tradePassword = StringUtils.getText(etPassword);
            String code = StringUtils.getText(etCode);
            double minWithdrawAmount = 0;
            if (extractInfo != null && extractInfo.getMinWithdrawAmount() != null) {
                minWithdrawAmount = extractInfo.getMinWithdrawAmount().doubleValue();
            }
            if (StringUtils.isEmpty(address)) {
                ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.mention_money_address));
            } else if (StringUtils.isEmpty(amount)) {
                ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.text_number));
            } else if (StringUtils.isEmpty(tradePassword)) {
                ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.text_money_pwd));
            }
//            else if (StringUtils.isEmpty(code)) {
//                ToastUtils.showToast(getString(R.string.str_please_input) + getString(R.string.code));
//            }
            else if (Double.parseDouble(amount) > wallet.getBalance().doubleValue()) {
                ToastUtils.showToast(getString(R.string.str_coin_can_banlance) + wallet.getBalance().toPlainString());
            } else if (Double.parseDouble(amount) < minWithdrawAmount) {
                ToastUtils.showToast(getString(R.string.str_min_coin_num_tag) + minWithdrawAmount);
            } else {
                KeyboardUtils.hideSoftInput(activity);
                presnet.walletWithdraw(address, new BigDecimal(amount), wallet.getCoinId(), tradePassword, code, phone);
            }
        }
    }

    @Override
    public void getExtractInfoSuccess(List<ExtractInfo> list) {
        if (list != null && list.size() > 0) {
            for (ExtractInfo extractInfo : list) {
                map.put(extractInfo.getCoinName(), extractInfo);
            }
            extractInfo = map.get(wallet.getCoinId());
            if (extractInfo != null) {
                double minWithdrawAmount = 0;
                if (extractInfo != null && extractInfo.getMinWithdrawAmount() != null) {
                    minWithdrawAmount = extractInfo.getMinWithdrawAmount().doubleValue();
                }
                etCount.setHint(getString(R.string.str_min_coin_num_tag) + MathUtils.subZeroAndDot(String.valueOf(minWithdrawAmount)));
                /*double withdrawFee = 0;
                if (extractInfo != null && extractInfo.getWithdrawFee() != null) {
                    withdrawFee = extractInfo.getWithdrawFee().doubleValue();
                }*/
                tvServiceFee.setText(MathUtils.subZeroAndDot(String.valueOf(extractInfo.getWithdrawFee().toPlainString())));
                //提币手续费类型：1-固定金额 2-按比例
                withdrawFeeType = extractInfo.getWithdrawFeeType();
            }
        }
    }

    @Override
    public void walletWithdrawSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        } else {
            ToastUtils.showToast(getString(R.string.str_success_tag));
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            Address address = (Address) bundle.getSerializable("address");
            tvAddress.setText(address.getAddress());
            checkAddress(address.getAddress());
        }
    }

    /**
     * 用户选择地址后校验提币地址是否内部地址
     */
    private void checkAddress(String address) {
        if (StringUtils.isNotEmpty(address)) {
            etCount.setEnabled(true);
            llCount22.setVisibility(View.GONE);
            presnet.checkAddress(address);
        } else {
            etCount.setEnabled(false);
            llCount22.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void checkAddressSuccess(MessageResult response) {
        if (response != null) {
            isFeeZero = true;//false 外部提币  true内部提币
            String amount = StringUtils.getText(etCount);
            tvServiceFee.setText(0 + "");
            if (StringUtils.isNotEmpty(amount)) {
                double fee = 0;
                //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                if (withdrawFeeType == 2) {
                    double money = Double.parseDouble(amount);
                    tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money - money * fee, 8, null)));
                } else {
                    tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber((Double.parseDouble(amount) - fee), 8, null)));
                }
            }
        }
    }

    @Override
    public void checkAddressFail(HttpErrorEntity response) {
        if (response != null) {
            isFeeZero = false;//false 外部提币  true内部提币
            String amount = StringUtils.getText(etCount);
            if (StringUtils.isNotEmpty(amount)) {
                double fee = 0;
                if (extractInfo != null) {
                    fee = extractInfo.getWithdrawFee().doubleValue();
                }
                //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                if (withdrawFeeType == 2) {
                    double money = Double.parseDouble(amount);
                    tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money - money * fee, 8, null)));
                    tvServiceFee.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money * fee, 8, null)));
                } else {
                    tvFinalCount.setText(MathUtils.subZeroAndDot("" + (Double.parseDouble(amount) - fee)));
                    tvServiceFee.setText(MathUtils.subZeroAndDot(fee + ""));
                }
            } else {
                double fee = 0;
                amount = 0 + "";
                if (extractInfo != null) {
                    fee = extractInfo.getWithdrawFee().doubleValue();
                }
                //withdrawFeeType //提币手续费类型：1-固定金额 2-按比例
                if (withdrawFeeType == 2) {
                    double money = Double.parseDouble(amount);
                    tvFinalCount.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money - money * fee, 8, null)));
                    tvServiceFee.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(money * fee, 8, null)));
                } else {
                    tvFinalCount.setText(MathUtils.subZeroAndDot("" + (Double.parseDouble(amount) - fee)));
                    tvServiceFee.setText(MathUtils.subZeroAndDot(fee + ""));
                }
            }
        }
    }

    @Override
    public void getPhoneCodeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        timeCount.start();
        tvGetCode.setEnabled(false);
    }

    @Override
    public void captchSuccess(JSONObject obj) {
        gt3GeetestUtils.gtSetApi1Json(obj);
        gt3GeetestUtils.getGeetest(activity, HttpUrls.getGeetest(), null, null, new GT3GeetestBindListener() {
            @Override
            public boolean gt3SetIsCustom() {
                return true;
            }

            @Override
            public void gt3GetDialogResult(boolean status, String result) {
                if (status) {
                    Captcha captcha = new Gson().fromJson(result, Captcha.class);
                    String checkData = "gee::" + captcha.getGeetest_challenge() + "$" + captcha.getGeetest_validate() + "$" + captcha.getGeetest_seccode();
                    presnet.getPhoneCode(phone, checkData, cid);
                }
            }
        });
        gt3GeetestUtils.setDialogTouch(true);
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestClose();
            gt3GeetestUtils = null;
        }
        int code = httpErrorEntity.getCode();
        String msg = httpErrorEntity.getMessage();
        if (code == CAPTCH && StringUtils.isNotEmpty(msg) && msg.contains("captcha")) {
            cid = httpErrorEntity.getCid();
            gt3GeetestUtils = new GT3GeetestUtilsBind(activity);
            presnet.captch();
        } else if (code == CAPTCH2 && StringUtils.isNotEmpty(msg) && msg.contains("Captcha")) {//解决验证码失效问题
            ToastUtils.showToast(getResources().getString(R.string.str_code_error));
        } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCHA_HADBEEN_SEND) {
            ToastUtils.showToast(getResources().getString(R.string.str_no_repeat));
        } else {
            ToastUtils.showToast(activity, httpErrorEntity.getMessage());
        }
    }

    @Override
    public void codeSuccess(String obj) {
        if (gt3GeetestUtils != null) {
            gt3GeetestUtils.gt3TestFinish();
            gt3GeetestUtils = null;
        }
        ToastUtils.showToast(activity, obj);
        timeCount.start();
        tvGetCode.setEnabled(false);
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    public void onEvent(CheckLoginSuccessEvent event) {
        loadData();
    }
}
