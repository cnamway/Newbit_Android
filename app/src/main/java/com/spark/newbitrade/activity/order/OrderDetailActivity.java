package com.spark.newbitrade.activity.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spark.library.otc.model.MemberPayType;
import com.spark.library.otc.model.OrderDetailVo;
import com.spark.library.otc.model.OrderPaymentDto;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.appeal.AppealActivity;
import com.spark.newbitrade.activity.chat.ChatActivity;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.dialog.PayWaySelectDialog;
import com.spark.newbitrade.entity.HttpErrorEntity;
import com.spark.newbitrade.entity.OrderDetial;
import com.spark.newbitrade.entity.PayWaySetting;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.ui.PayCodeDialog;
import com.spark.newbitrade.utils.CommonUtils;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.MathUtils;
import com.spark.newbitrade.utils.StringUtils;
import com.spark.newbitrade.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity implements OrderDetailContract.View, ISocket.TCPCallback {
    @BindView(R.id.tvOrderSn)
    TextView tvOrderSn;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvPayDone)
    TextView tvPayDone;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvAppeal)
    TextView tvAppeal;
    @BindView(R.id.tvRelease)
    TextView tvRelease;
    @BindView(R.id.llOperate)
    LinearLayout llOperate;
    @BindView(R.id.tvOtherSide)
    TextView tvOtherSide;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvLastTime)
    TextView tvLastTime;
    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.llPaymentTime)
    LinearLayout llPaymentTime;
    @BindView(R.id.tvReleseTime)
    TextView tvReleseTime;
    @BindView(R.id.llReleseTime)
    LinearLayout llReleseTime;
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvBank)
    TextView tvBank;
    @BindView(R.id.llBank)
    LinearLayout llBank;
    @BindView(R.id.tvAli)
    TextView tvAli;
    @BindView(R.id.ivAliCode)
    ImageView ivAliCode;
    @BindView(R.id.llAli)
    LinearLayout llAli;
    @BindView(R.id.tvWechat)
    TextView tvWechat;
    @BindView(R.id.ivWeChatCode)
    ImageView ivWeChatCode;
    @BindView(R.id.llWeChat)
    LinearLayout llWeChat;
    @BindView(R.id.tvPaypal)
    TextView tvPaypal;
    @BindView(R.id.llPalpay)
    LinearLayout llPalpay;
    @BindView(R.id.tvOther)
    TextView tvOther;
    @BindView(R.id.llOther)
    LinearLayout llOther;
    @BindView(R.id.llPayLayout)
    LinearLayout llPayLayout;
    @BindView(R.id.tvBankRealName)
    TextView tvBankRealName;
    @BindView(R.id.tvRemarks)
    TextView tvRemarks;
    @BindView(R.id.tvOpenBank)
    TextView tvOpenBank;
    @BindView(R.id.tvBranch)
    TextView tvBranch;
    @BindView(R.id.tvAliName)
    TextView tvAliName;
    @BindView(R.id.tvWechatName)
    TextView tvWechatName;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private String orderSn;
    private OrderFragment.Status status;//订单状态 0-已取消 1-未付款 2-已付款 3-已完成 4-申诉中
    private OrderDetailPresenterImpl presenter;
    private OrderDetailVo orderDetailVo;
    private String downloadUrl;
    private CountDownTimer timer;
    private boolean isAli = false;
    private boolean isWechat = false;
    private boolean isBank = false;
    private boolean isPaypal = false;
    private boolean isOther = false;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String qrCodeUrlAli;
    private String qrCodeUrlWechat;
    private PopupWindow popWnd;
    private String select;
    private PayWaySelectDialog selectDialog;
    private PayCodeDialog payCodeDialog;
    private String bankNum;//银行卡号
    private int tick_flush = 5;//每隔tick_flush秒钟加载一次数据

    private Handler handler_timeCurrent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:                    //每隔tick_flush秒钟加载一次数据
                    startFlush();
                    handler_timeCurrent.sendEmptyMessageDelayed(1, tick_flush * 1000);//tick_flush秒钟刷新一次
                    break;
            }
        }
    };

    /**
     * 开始刷新
     */
    private void startFlush() {
        stopFlush();
        LogUtils.e("开始刷新=============未付款");
        getOrdetDetail(false);
    }

    /**
     * 停止刷新
     */
    private void stopFlush() {
        handler_timeCurrent.removeCallbacksAndMessages(null);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_order_detial;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
    }

    @Override
    protected void initData() {
        super.initData();
        setTitle(getString(R.string.orderdetail));
        presenter = new OrderDetailPresenterImpl(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderSn = bundle.getString("orderSn");
            status = (OrderFragment.Status) bundle.getSerializable("status");
            boolean isChatList = bundle.getBoolean("isChatList");
            if (isChatList)
                tvGoto.setVisibility(View.INVISIBLE);
        }
        selectDialog = new PayWaySelectDialog(OrderDetailActivity.this, new PayWaySelectDialog.PayWaySelectListener() {
            @Override
            public void getSelectPayWay(String payWay) {
                select = payWay;
            }
        });
        if (status == OrderFragment.Status.UNPAID || status == OrderFragment.Status.PAID) {
            llPayLayout.setVisibility(View.VISIBLE);
        } else {
            llPayLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData() {
        getOrdetDetail(true);
    }

    @Override
    protected void setListener() {
        super.setListener();
        selectDialog.getTvOption().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(select)) {
                    OrderPaymentDto orderPaymentDto = new OrderPaymentDto();
                    orderPaymentDto.setActualPayment(select);
                    orderPaymentDto.setOrderSn(orderSn);
                    presenter.paymentOrderUsingPOST(orderPaymentDto);
                    selectDialog.dismiss();
                } else {
                    ToastUtils.showToast(getString(R.string.str_please_select_payway));
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrdetDetail(false);
            }
        });
    }

    @OnClick({R.id.tvPayDone, R.id.tvCancle, R.id.tvRelease, R.id.tvAppeal, R.id.ivGoChat, R.id.tvOrderSn, R.id.tvOrderId, R.id.ivAliCode, R.id.ivWeChatCode, R.id.tvBank})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        if (orderDetailVo == null) {
            ToastUtils.showToast(getString(R.string.order_details_failed));
            getOrdetDetail(true);
            return;
        }
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tvPayDone:
                showCofirmDialog(0);
                break;
            case R.id.tvCancle:
                showCofirmDialog(1);
                break;
            case R.id.tvRelease:
                showReleaseDialog();
                break;
            case R.id.tvAppeal:
                if (checkTime()) {
                    bundle.putString("orderSn", orderDetailVo.getOrderSn());
                    showActivity(AppealActivity.class, bundle, 1);
                } else {
                    ToastUtils.showLong(R.string.str_appeal_time);
                }
                break;
            case R.id.ivGoChat:
                OrderDetial orderDetial = new OrderDetial();
                orderDetial.setOrderSn(orderDetailVo.getOrderSn());
                long myId = MyApplication.getApp().getCurrentUser().getId();
                orderDetial.setMyId(myId + "");
                if (myId != orderDetailVo.getCustomerId()) {
                    orderDetial.setHisId(String.valueOf(orderDetailVo.getCustomerId()));
                } else {
                    orderDetial.setHisId(String.valueOf(orderDetailVo.getMemberId()));
                }
                orderDetial.setOtherSide(orderDetailVo.getTrateToRealname());
                bundle.putSerializable("orderDetial", orderDetial);
                showActivity(ChatActivity.class, bundle);
                break;
            case R.id.tvOrderSn:
                CommonUtils.copyText(OrderDetailActivity.this, tvOrderSn.getText().toString());
                break;
            case R.id.tvOrderId:
                CommonUtils.copyText(OrderDetailActivity.this, tvOrderId.getText().toString());
                break;
            case R.id.ivAliCode:
                if (StringUtils.isNotEmpty(qrCodeUrlAli)) {
                    showPayCodeDialog(1);
                }
                break;
            case R.id.ivWeChatCode:
                if (StringUtils.isNotEmpty(qrCodeUrlWechat)) {
                    showPayCodeDialog(2);
                }
                break;
            case R.id.tvBank:
                CommonUtils.copyText(OrderDetailActivity.this, bankNum);
                break;
        }
    }

    /**
     * 提示框
     */
    private void showPayCodeDialog(int type) {
        payCodeDialog = new PayCodeDialog(activity);
        if (type == 1) payCodeDialog.setImg(qrCodeUrlAli, 1);
        if (type == 2) payCodeDialog.setImg(qrCodeUrlWechat, 1);
        payCodeDialog.show();
    }

    private boolean checkTime() {
        long targetTime = orderDetailVo.getCreateTime().getTime();
        return (System.currentTimeMillis() - targetTime >= 30 * 60 * 1000);
    }


    /**
     * 订单放行
     */
    private void showReleaseDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_jypassword, null);
        TextView tvRelease = view.findViewById(R.id.tvRelease);
        final EditText etPassword = view.findViewById(R.id.etPassword);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        dialog.show();
        tvRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jyPassword = etPassword.getText().toString();
                if (StringUtils.isEmpty(jyPassword)) {
                    ToastUtils.showToast(getString(R.string.enter_money_pwd_tag));
                } else {
                    release(jyPassword);
                }
                dialog.dismiss();
            }
        });
    }


    /**
     * 对话框
     *
     * @param intType 0-付款完成，1-取消
     */
    private void showCofirmDialog(final int intType) {
        String content = "";
        if (intType == 0) {
            selectDialog.show();
        } else {
            content = getString(R.string.confirm_go_on_canel_order_tag);
            final NormalDialog dialog = new NormalDialog(activity);
            dialog.isTitleShow(false).bgColor(Color.parseColor("#ffffff"))
                    .content(content)
                    .contentGravity(Gravity.CENTER)
                    .contentTextColor(Color.parseColor("#6a6e8a"))
                    .btnTextColor(Color.parseColor("#6a6e8a"), Color.parseColor("#6a6e8a"))
                    //.btnPressColor(Color.parseColor("#2B2B2B"))
                    .btnText(getString(R.string.str_cancel), getString(R.string.dialog_sure))
                    .show();
            dialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    dialog.dismiss();
                }
            }, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    presenter.cancelOrderUsingGET(orderSn);
                    dialog.superDismiss();
                }
            });
        }

    }

    /**
     * 获取订单详情
     */
    private void getOrdetDetail(boolean isShow) {
        if (status.getStatus() == 0 || status.getStatus() == 3) {
            //查询我的归档订单（已完成，已取消）
            presenter.findOrderAchiveDetailUsingGET(orderSn, isShow);
            stopFlush();
        } else {
            //查询我的在途订单(未付款，已付款，申诉中)
            presenter.findOrderInTransitDetailUsingGET(orderSn, isShow);
            handler_timeCurrent.sendEmptyMessageDelayed(1, tick_flush * 1000);//tick_flush秒钟刷新一次
        }
    }

    /**
     * 订单放行
     *
     * @param jyPassword
     */
    private void release(String jyPassword) {
        presenter.releaseOrderUsingGET(orderSn, jyPassword);
    }

    private void setViews() {
        tvOtherSide.setText(orderDetailVo.getTrateToRealname());
        tvOrderSn.setText(orderDetailVo.getOrderSn());
        tvOrderId.setText(orderDetailVo.getPayRefer() + "");
        tvPrice.setText(MathUtils.subZeroAndDot(orderDetailVo.getPrice() + "") + " CNY");
        tvCount.setText(MathUtils.subZeroAndDot(orderDetailVo.getNumber() + "") + " " + orderDetailVo.getCoinName());
        tvTotal.setText(MathUtils.subZeroAndDot(orderDetailVo.getMoney() + "") + " CNY");
        tvTime.setText(simpleDateFormat.format(orderDetailVo.getCreateTime()));
        tvRemarks.setText(orderDetailVo.getRemark());

        OrderFragment.Status status = OrderFragment.Status.values()[orderDetailVo.getStatus()];
        switch (status.getStatus()) {
            case 0:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[3]);
                break;
            case 1:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[0]);
                break;
            case 2:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[1]);
                break;
            case 3:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[2]);
                break;
            case 4:
                tvStatus.setText(getResources().getStringArray(R.array.order_status)[4]);
                break;
        }

        String type;
        if (MyApplication.getApp().getCurrentUser() != null && orderDetailVo.getMemberId().equals(MyApplication.getApp().getCurrentUser().getId())) {
            if (orderDetailVo.getOrderType().equals("0")) {
                type = "1";
            } else {
                type = "0";
            }
        } else {
            type = orderDetailVo.getOrderType();
        }

        showWhichViews(type, status);
        List<PayWaySetting> payDatas = new Gson().fromJson(orderDetailVo.getPayData(), new TypeToken<List<PayWaySetting>>() {
        }.getType());
        if (payDatas != null) {
            String payMode = "";
            if (status == OrderFragment.Status.UNPAID) {
                llPayLayout.setVisibility(View.VISIBLE);
                //payMode = orderDetailVo.getPayMode();
                payMode = getPayMode(payDatas);
            } else if (status == OrderFragment.Status.PAID) {
                llPayLayout.setVisibility(View.VISIBLE);
                payMode = orderDetailVo.getActualPayment();
            } else {
                llPayLayout.setVisibility(View.GONE);
            }
            if (StringUtils.isEmpty(payMode)) {
                payMode = "";
            }
            if (payMode.contains(GlobalConstant.alipay)) {
                isAli = true;
                llAli.setVisibility(View.VISIBLE);
                for (PayWaySetting payData : payDatas) {
                    if (GlobalConstant.alipay.equals(payData.getPayType())) {
                        tvAli.setText(payData.getPayAddress());
                        qrCodeUrlAli = payData.getQrCodeUrl();
                        tvAliName.setText(payData.getRealName());
                    }
                }
            } else {
                isAli = false;
                llAli.setVisibility(View.GONE);
            }
            if (payMode.contains(GlobalConstant.wechat)) {
                isWechat = true;
                llWeChat.setVisibility(View.VISIBLE);
                for (PayWaySetting payData : payDatas) {
                    if (GlobalConstant.wechat.equals(payData.getPayType())) {
                        tvWechat.setText(payData.getPayAddress());
                        qrCodeUrlWechat = payData.getQrCodeUrl();
                        tvWechatName.setText(payData.getRealName());
                    }
                }
            } else {
                isWechat = false;
                llWeChat.setVisibility(View.GONE);
            }
            if (payMode.contains(GlobalConstant.card)) {
                isBank = true;
                llBank.setVisibility(View.VISIBLE);
                for (PayWaySetting payData : payDatas) {
                    if (GlobalConstant.card.equals(payData.getPayType())) {
                        tvBankRealName.setText(payData.getRealName());
                        tvBank.setText(payData.getPayAddress());
                        tvOpenBank.setText(payData.getBank());
                        tvBranch.setText(payData.getBranch());
                        bankNum = payData.getPayAddress();
                    }
                }
            } else {
                isBank = false;
                llBank.setVisibility(View.GONE);
            }
            if (payMode.toLowerCase().contains(GlobalConstant.PAYPAL)) {
                isPaypal = true;
                llPalpay.setVisibility(View.VISIBLE);
                for (PayWaySetting payData : payDatas) {
                    if (GlobalConstant.PAYPAL.equals(payData.getPayType())) {
                        tvPaypal.setText(payData.getPayAddress());
                    }
                }
            } else {
                isPaypal = false;
                llPalpay.setVisibility(View.GONE);
            }
            if (payMode.contains(GlobalConstant.other)) {
                isOther = true;
                llOther.setVisibility(View.VISIBLE);
                for (PayWaySetting payData : payDatas) {
                    if (GlobalConstant.other.equals(payData.getPayType())) {
                        tvOther.setText(payData.getPayAddress());
                    }
                }
            } else {
                isOther = false;
                llOther.setVisibility(View.GONE);
            }
            selectDialog.setView(isAli, isWechat, isBank, isPaypal, isOther);
        }

    }

    /**
     * 获取payMode
     *
     * @param payDatas
     * @return
     */
    private String getPayMode(List<PayWaySetting> payDatas) {
        String payType = "";
        for (PayWaySetting payWay : payDatas) {
            payType = payType + "," + payWay.getPayType();
        }
        return payType;
    }

    public void showPopWindow(String url) {
        downloadUrl = url;
        View contentView = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.view_pop_order_detail_code, null);
        popWnd = new PopupWindow(OrderDetailActivity.this);
        popWnd.setContentView(contentView);
        popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWnd.setOutsideTouchable(true);
        popWnd.setTouchable(true);
        darkenBackground(0.4f);
        ImageView ivQR = contentView.findViewById(R.id.ivQR);
        Glide.with(this).load(url).centerCrop().into(ivQR);
        ivQR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        View rootview = LayoutInflater.from(OrderDetailActivity.this).inflate(R.layout.activity_order_detial, null);
        popWnd.showAtLocation(rootview, Gravity.CENTER, 0, 0);
        popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                darkenBackground(1f);
            }
        });
    }

    /**
     * 改变背景颜色
     */
    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);

    }

    private void showWhichViews(String type, OrderFragment.Status status) {
        llOperate.setVisibility(View.GONE);
        tvRelease.setVisibility(View.GONE);
        tvPayDone.setVisibility(View.GONE);
        tvAppeal.setVisibility(View.GONE);
        tvCancle.setVisibility(View.GONE);
        tvLastTime.setVisibility(View.GONE);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        switch (status) {
            case CANC:
//                llPayInfo.setVisibility(View.GONE);
                llOperate.setVisibility(View.GONE);
                tvAppeal.setVisibility(View.GONE);
                break;
            case UNPAID:
                tvAppeal.setVisibility(View.GONE);
                if (type.equals("0")) {
                    llOperate.setVisibility(View.VISIBLE);
                    tvPayDone.setVisibility(View.VISIBLE);
                    tvCancle.setVisibility(View.VISIBLE);
                    tvLastTime.setVisibility(View.VISIBLE);
                    getTime();
                    tvRelease.setVisibility(View.GONE);
                } else if (type.equals("1")) {
                    tvLastTime.setVisibility(View.VISIBLE);
                    getTime();
                    llOperate.setVisibility(View.GONE);
                }
                break;
            case PAID:
                llOperate.setVisibility(View.VISIBLE);
                llPaymentTime.setVisibility(View.VISIBLE);
                tvPayTime.setText(simpleDateFormat.format(orderDetailVo.getPayTime()));
                if (type.equals("0")) {
                    tvLastTime.setVisibility(View.VISIBLE);
                    tvLastTime.setText(R.string.wait_relese);
                    tvLastTime.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    tvAppeal.setVisibility(View.VISIBLE);
                    tvCancle.setVisibility(View.VISIBLE);
                    tvAppeal.setVisibility(View.VISIBLE);
                } else if (type.equals("1")) {
                    tvPayDone.setVisibility(View.GONE);
                    tvCancle.setVisibility(View.GONE);
                    tvAppeal.setVisibility(View.VISIBLE);
                    tvRelease.setVisibility(View.VISIBLE);
                    tvAppeal.setBackgroundResource(R.drawable.ripple_grey_no_corner);
                    tvAppeal.setTextColor(getResources().getColor(R.color.main_font_content));
                }
                break;
            case DONE:
                llPaymentTime.setVisibility(View.VISIBLE);
                tvPayTime.setText(simpleDateFormat.format(orderDetailVo.getPayTime()));
                llReleseTime.setVisibility(View.VISIBLE);
                tvReleseTime.setText(DateUtils.getFormatTime(null, orderDetailVo.getReleaseTime()));
                llOperate.setVisibility(View.GONE);
                tvAppeal.setVisibility(View.GONE);
                break;
            case COMPLAINING:
                llOperate.setVisibility(View.GONE);
                tvAppeal.setVisibility(View.GONE);
                break;
        }
    }

    private void getTime() {
        long createTime = orderDetailVo.getCreateTime().getTime();
        long timeLimit = (long) (orderDetailVo.getTimeLimit() * 60 * 1000);
        long currentTime = System.currentTimeMillis();
        if (createTime + timeLimit - currentTime > 0) {
            fillCodeView(createTime + timeLimit - currentTime);
        } else tvLastTime.setText("00:00:00");
    }

    private void fillCodeView(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                String hms = formatter.format(millisUntilFinished);
                if (!StringUtils.isEmpty(hms)) {
                    tvLastTime.setText(formatter.format(millisUntilFinished));
                }
            }

            @Override
            public void onFinish() {
                tvLastTime.setText("00:00:00");
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        stopFlush();
    }


    @Override
    public void dataSuccess(ISocket.CMD cmd, String response) {
    }

    @Override
    public void dataFail(int code, ISocket.CMD cmd, String errorInfo) {
    }

    @Override
    public void cancelOrderUsingGETSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void releaseOrderUsingGETSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);

        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void paymentOrderUsingPOSTSuccess(String obj) {
        if (StringUtils.isNotEmpty(obj)) {
            if (obj.equals(getString(R.string.str_success)))
                ToastUtils.showToast(getString(R.string.str_success_tag));
            else ToastUtils.showToast(obj);
        }
//        setResult(RESULT_OK);
//        finish();
        //付完款之后 详情页不关闭
        getOrdetDetail(true);
    }

    @Override
    public void findOrderInTransitDetailUsingGETSuccess(OrderDetailVo obj) {
        hideAll();
        if (obj != null) {
            orderDetailVo = obj;
            setViews();
        }
    }

    @Override
    public void findOrderAchiveDetailUsingGETSuccess(OrderDetailVo obj) {
        hideAll();
        if (obj != null) {
            orderDetailVo = obj;
            setViews();
        }
    }

    @Override
    public void queryOrderPayTypeUsingGETSuccess(List<MemberPayType> obj) {

    }

    @Override
    public void findOrderAchiveDetailUsingGETFail(HttpErrorEntity httpErrorEntity) {
        hideAll();
        //查询我的在途订单(未付款，已付款，申诉中)
        presenter.findOrderInTransitDetailUsingGET(orderSn, false);
    }

    @Override
    public void findOrderInTransitDetailUsingGETFail(HttpErrorEntity httpErrorEntity) {
        hideAll();
        //查询我的归档订单（已完成，已取消）
        presenter.findOrderAchiveDetailUsingGET(orderSn, false);
    }

    private void hideAll() {
        if (refreshLayout != null) {
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
        }
    }
}
