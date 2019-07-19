package com.spark.newbitrade.activity.skip;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.geetest.sdk.Bind.GT3GeetestUtilsBind;
import com.spark.newbitrade.activity.login.LoginActivity;
import com.spark.library.ac.model.MemberWalletVo;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.base.ActivityManage;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.PasswordDialog;
import com.spark.newbitrade.entity.CasLoginEntity;
import com.spark.newbitrade.entity.ExtractInfo;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.User;
import com.spark.newbitrade.event.CheckLoginEvent;
import com.spark.newbitrade.event.CheckLoginSuccessEvent;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;
import com.spark.newbitrade.widget.TimeCount;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.spark.newbitrade.factory.HttpUrls.TYPE_AC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_OTC_SYSTEM;
import static com.spark.newbitrade.factory.HttpUrls.TYPE_UC;

/**
 * 其它app跳转进来的支付页面
 */

public class SkipPayActivity extends BaseActivity implements SkipPayContract.View {

    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvUse)
    TextView tvUse;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.tvFinalCount)
    TextView tvFinalCount;
    @BindView(R.id.tvFlag)
    TextView tvFlag;
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;

    private String orderNo;
    private String amount;
    private String address;
    private String coinName;
    private MemberWalletVo memberWalletVo;

    private SkipPayPresnetImpl presnet;

    private TimeCount timeCount;
    private GT3GeetestUtilsBind gt3GeetestUtils;
    private String cid;
    private String phone = "";
    private String code = "";
    private int withdrawFeeType = 1;//提币手续费类型：1-固定金额 2-按比例
    private boolean isCan = true;//到账数量不能为负值
    private boolean isCan2 = true;//可用余额不足

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_skip_pay;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    LogUtils.e("SkipPayActivity==onActivityResult==loadData==");
                    loadData();
                    break;
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        LogUtils.e("SkipPayActivity==onActivityResult==EventBus.getDefault().register==");
        EventBus.getDefault().register(this);
        setSetTitleAndBack(false, false);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.activity_skip_pay_title));

        presnet = new SkipPayPresnetImpl(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderNo = bundle.getString("orderNo");
            amount = bundle.getString("amount");
            address = bundle.getString("address");
            coinName = bundle.getString("coinName");

            if (StringUtils.isNotEmpty(orderNo)) {
                tvId.setText(orderNo);
            }

            if (StringUtils.isNotEmpty(amount) && StringUtils.isNotEmpty(coinName)) {
                tvAmount.setText(amount + " " + coinName);
                tvCoinName.setText(coinName + "余额：");
            }

        }
        User user = MyApplication.getApp().getCurrentUser();
        if (user != null) {
            phone = user.getMobilePhone();
        }
        timeCount = new TimeCount(60000, 1000, tvGetCode);
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (MyApplication.getApp().isLogin()) {
            if (StringUtils.isNotEmpty(coinName)) {
                LogUtils.e("SkipPayActivity==loadData()==开始加载数据==");
                presnet.getCoinMessage(coinName);
                presnet.getExtractInfo(coinName);
            }
        } else {
            goLogin();
        }
    }

    @OnClick({R.id.tvPay, R.id.tvGetCode})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (!isCan2) {
            ToastUtils.showToast(R.string.str_no_enough_balance);
            return;
        }
        if (!isCan) {
            ToastUtils.showToast("到账数量不能为负值!");
            return;
        }
        switch (v.getId()) {
            case R.id.tvPay:
                if (MyApplication.getApp().isLogin()) {
                    code = StringUtils.getText(etCode);
                    //if (StringUtils.isNotEmpty(amount, address, coinName, code)) {
                    if (StringUtils.isNotEmpty(amount, address, coinName)) {
                        LogUtils.e("SkipPayActivity==setOnClickListener()==memberWalletVo==" + memberWalletVo);
                        if (memberWalletVo != null) {
                            LogUtils.e("SkipPayActivity==setOnClickListener()==memberWalletVo==" + memberWalletVo.toString());
                            if (Double.valueOf(memberWalletVo.getBalance().toString()) >= Double.valueOf(amount)) {
                                showPasswordDialog();
                            } else {
                                ToastUtils.showToast(R.string.str_no_enough_balance);
                            }
                        } else {
                            LogUtils.e("SkipPayActivity==点击立即支付()==memberWalletVo是空重新加载数据==");
                            loadData();
                        }
                    } else {
                        loadData();
                        ToastUtils.showToast(getString(R.string.incomplete_information));
                    }
                } else {
                    goLogin();
                }
                break;
            case R.id.tvGetCode:
                getCode();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getCode() {
        if (StringUtils.isEmpty(phone)) {
            User user = MyApplication.getApp().getCurrentUser();
            if (user != null) {
                phone = user.getMobilePhone();
            }
        }

        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showToast(R.string.phone_empty);
            goLogin();
        } else {
            presnet.getPhoneCode(phone);
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
    protected void setListener() {
        super.setListener();

    }

    private void showPasswordDialog() {
        LogUtils.e("SkipPayActivity==showPasswordDialog()==弹出资金密码弹框==");
        final PasswordDialog passwordDialog = new PasswordDialog(this);
        passwordDialog.show();
        passwordDialog.setClicklistener(new PasswordDialog.ClickListenerInterface() {
            @Override
            public void doConfirm(String password) {
                passwordDialog.dismiss();
                presnet.walletWithdraw(address, new BigDecimal(amount), coinName, password, code, phone, orderNo);
            }

            @Override
            public void doCancel() {
                passwordDialog.dismiss();
            }
        });
    }

    @Override
    public void walletWithdrawSuccess(String response) {
        if (StringUtils.isNotEmpty(response)) {
            ToastUtils.showToast("支付成功");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", "success");
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void getCoinMessageSuccess(MemberWalletVo obj) {
        if (obj == null) return;
        memberWalletVo = obj;
        tvUse.setText(MathUtils.subZeroAndDot(MathUtils.getRundNumber(Double.valueOf(obj.getBalance().toString()), 8, null)) + " " + coinName);
        if (Double.valueOf(memberWalletVo.getBalance().toString()) < Double.valueOf(amount)) {
            ToastUtils.showToast(R.string.str_no_enough_balance);
            tvFlag.setBackgroundColor(getResources().getColor(R.color.grey_a5a5a5));
            tvGetCode.setTextColor(getResources().getColor(R.color.grey_a5a5a5));
            tvPay.setBackgroundResource(R.drawable.shape_gray_corner_background);
            tvPay.setTextColor(getResources().getColor(R.color.main_font_content));
            isCan2 = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presnet.destory();
    }

    @Override
    public void getExtractInfoSuccess(List<ExtractInfo> list) {
        if (list != null && list.size() > 0) {
            HashMap<String, ExtractInfo> map = new HashMap<>();
            for (ExtractInfo extractInfo : list) {
                map.put(extractInfo.getCoinName(), extractInfo);
            }
            ExtractInfo extractInfo = map.get(coinName);
            if (extractInfo != null) {
                String isMoney = "0";
                //提币手续费类型：1-固定金额 2-按比例
                withdrawFeeType = extractInfo.getWithdrawFeeType();
                if (withdrawFeeType == 2) {
                    double money = Double.parseDouble(amount);

                    double fee = 0;
                    if (extractInfo != null && extractInfo.getWithdrawFee() != null) {
                        fee = extractInfo.getWithdrawFee().doubleValue();
                    }

                    double minFee = 0;
                    if (extractInfo != null && extractInfo.getMinWithdrawFee() != null) {
                        minFee = extractInfo.getMinWithdrawFee().doubleValue();
                    }

                    if (money * fee < minFee) {
                        isMoney = MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(money + "", minFee + "", 8));
                        tvFinalCount.setText(isMoney + " " + coinName);
                    } else {
                        isMoney = MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(money + "", MathUtils.getBigDecimalMultiplyWithScale(money + "", fee + "", 8), 8));
                        tvFinalCount.setText(isMoney + " " + coinName);
                    }
                } else {
                    double fee = 0;
                    if (extractInfo != null && extractInfo.getWithdrawFee() != null) {
                        fee = extractInfo.getWithdrawFee().doubleValue();
                    }
                    isMoney = MathUtils.subZeroAndDot(MathUtils.getBigDecimalSubtractWithScale(amount + "", fee + "", 8));
                    tvFinalCount.setText(isMoney + " " + coinName);
                }
                try {
                    if (Double.parseDouble(isMoney) < 0) {
                        tvFlag.setBackgroundColor(getResources().getColor(R.color.grey_a5a5a5));
                        tvGetCode.setTextColor(getResources().getColor(R.color.grey_a5a5a5));
                        tvPay.setBackgroundResource(R.drawable.shape_gray_corner_background);
                        tvPay.setTextColor(getResources().getColor(R.color.main_font_content));
                        isCan = false;
                    } else {
                        isCan = true;
                    }
                } catch (Exception e) {

                }

            }
        }
    }

    /**
     * check uc、ac、acp成功后，通知刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginSuccessEvent(CheckLoginSuccessEvent response) {
        loadData();
    }

    @Override
    public void dealError(HttpErrorEntity httpErrorEntity) {
        if (httpErrorEntity != null) {
            LogUtils.e("SkipPayActivity===ccse==response==" + "dealError===HttpErrorEntity===" + httpErrorEntity.toString());
            if (httpErrorEntity.getCode() == GlobalConstant.LOGIN_ERROR) {
                if (StringUtils.isNotEmpty(httpErrorEntity.getUrl())) {
                    LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",httpErrorEntity.getUrl()==" + httpErrorEntity.getUrl());
                    if (httpErrorEntity.getUrl().contains("/" + TYPE_AC)) {
                        presnet.checkBusinessLogin(TYPE_AC);
                    } else {
                        LogUtils.e("HttpErrorEntity===" + httpErrorEntity.getCode() + ",new LoadExceptionEvent()==退出登录=======");
                        logOut();
                    }
                }
            } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCH2) {
                Message message = new Message();
                message.what = 1;
                message.obj = getString(R.string.str_code_error);
                mToastHandler.sendMessage(message);
            } else if (httpErrorEntity.getCode() == GlobalConstant.CAPTCHA_HADBEEN_SEND) {
                Message message = new Message();
                message.what = 1;
                message.obj = getString(R.string.str_no_repeat);
                mToastHandler.sendMessage(message);
            } else if (httpErrorEntity.getCode() == GlobalConstant.SERVER_ERROR_CODE) {
                LogUtils.e("SkipPayActivity==dealError==HttpErrorEntity==" + httpErrorEntity.getCode());
            } else if (StringUtils.isNotEmpty(httpErrorEntity.getMessage())) {
                Message message = new Message();
                message.what = 1;
                message.obj = httpErrorEntity.getMessage();
                mToastHandler.sendMessage(message);
            } else {
                if (GlobalConstant.isDebug) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = "" + httpErrorEntity.getCode();
                    mToastHandler.sendMessage(message);
                }
            }
        } else {
            logOut();
        }
    }

    private static Handler mToastHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtils.showToast(msg.obj.toString());
                    break;
            }
        }
    };

    private void logOut() {
        MyApplication.getApp().deleteCurrentUser();
        SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
        SharedPreferenceInstance.getInstance().saveLockPwd("");
        MyApplication.getApp().getCookieManager().getCookieStore().removeAll();
        ActivityManage.finishAll();
        goLogin();
    }

    @Override
    public void checkBusinessLoginSuccess(CasLoginEntity casLoginEntity) {
        if (casLoginEntity != null) {
            String type = casLoginEntity.getType();
            if (!casLoginEntity.isLogin()) {
                String gtc = SharedPreferenceInstance.getInstance().getTgt();
                presnet.doLoginBusiness(gtc, type);
            }
        }
    }

    @Override
    public void doLoginBusinessSuccess(String type) {
        loadData();
    }

    private void goLogin() {
        ToastUtils.showToast(getString(R.string.text_login_first) + getString(R.string.app_name));
        Bundle bundle = new Bundle();
        bundle.putBoolean("isJumpApp", true);
        showActivity(LoginActivity.class, bundle, 1);
        finish();
    }

    /**
     * 检测登录状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCheckLoginEvent(CheckLoginEvent event) {
        LogUtils.e("SkipPayActivity==onCheckLoginEvent==event.type==" + event.type);
        if (event.type.contains(TYPE_OTC_SYSTEM)) {
            presnet.checkBusinessLogin(TYPE_OTC_SYSTEM);
        } else if (event.type.contains(TYPE_OTC)) {
            presnet.checkBusinessLogin(TYPE_OTC);
        } else if (event.type.contains(TYPE_UC)) {
            presnet.checkBusinessLogin(TYPE_UC);
        } else if (event.type.contains(TYPE_AC)) {
            presnet.checkBusinessLogin(TYPE_AC);
        }
    }
}
