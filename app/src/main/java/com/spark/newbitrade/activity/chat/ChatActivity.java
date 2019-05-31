package com.spark.newbitrade.activity.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.order.OrderDetailActivity;
import com.spark.newbitrade.adapter.ChatAdapter;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.ChatEntity;
import com.spark.newbitrade.entity.ChatEvent;
import com.spark.newbitrade.entity.ChatTable;
import com.spark.newbitrade.entity.ChatTipEvent;
import com.spark.newbitrade.entity.OrderDetial;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.factory.socket.SocketFactory;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.DatabaseUtils;
import com.spark.newbitrade.utils.DateUtils;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.NetCodeUtils;
import com.spark.newbitrade.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity implements ISocket.TCPCallback, ChatContact.View {
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private OrderDetial orderDetial;
    private List<ChatEntity> chatEntities = new ArrayList<>();
    private Gson gson = new Gson();
    private ChatAdapter adapter;
    private ChatPresenter presenter;
    private int pageNo = 0;
    private int pageSize = 20;
    private boolean hasNew = false;
    private DatabaseUtils databaseUtils = new DatabaseUtils();
    private List<ChatTable> findByOrderList;
    private List<ChatTable> list;
    private boolean isChatList;
    private JSONObject obj;

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (chatEntities != null && chatEntities.size() > 0) {
            pageNo = 0;
            getHistoyList(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        setSetTitleAndBack(false, true);
        if (immersionBar != null)
            immersionBar.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        isNeedhide = false;
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        refreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    @Override
    protected void initData() {
        super.initData();
        isNeedhide = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderDetial = (OrderDetial) bundle.getSerializable("orderDetial");
            isChatList = bundle.getBoolean("isChatList");
            if (isChatList) {
                tvGoto.setVisibility(View.VISIBLE);
                tvGoto.setText(getString(R.string.orderdetail));
            }
            setTitle(orderDetial.getOtherSide());
        }
        initRvChat();
        presenter = new ChatPresenter(this);
    }

    @OnClick({R.id.tvSend, R.id.tvGoto})
    @Override
    protected void setOnClickListener(View v) {
        super.setOnClickListener(v);
        switch (v.getId()) {
            case R.id.tvSend:
                sendMessage();
                break;
            case R.id.tvGoto:
                Bundle bundle = new Bundle();
                bundle.putString("orderSn", orderDetial.getOrderSn());
                bundle.putBoolean("isChatList", isChatList);
                showActivity(OrderDetailActivity.class, bundle);
                break;
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getHistoyList(false);
            }
        });
    }

    /**
     * 获取历史聊天记录
     */
    private void getHistoyList(boolean isShow) {
        if (isShow)
            displayLoadingPopup();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", pageSize + "");
        map.put("pageNo", (++pageNo) + "");
        presenter.getHistoryMessage(orderDetial.getOrderSn(), map);
    }

    private void sendMessage() {
        String content = etContent.getText().toString();
        if (StringUtils.isEmpty(content)) return;
        obj = buildBodyJson(content);
        LogUtils.e("buildBodyJson" + obj.toString());
        SocketFactory.produceSocket(ISocket.C2C).sendRequest(ISocket.CMD.SEND_CHAT, obj.toString().getBytes(), this);
        addChatEntity(obj.toString(), ChatEntity.Type.RIGHT);
        etContent.setText("");
    }

    private void storageData(String response) {
        ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
        LogUtils.e("chatEntity  " + chatEntity.toString());
        if (chatEntity == null) return;
        ChatTable table;
        hasNew = false;
        SharedPreferenceInstance.getInstance().saveHasNew(hasNew);
        list = databaseUtils.findAll();
        findByOrderList = databaseUtils.findByOrder(chatEntity.getOrderId());
        if (findByOrderList == null || findByOrderList.size() == 0) {
            LogUtils.e("初次建立");
            table = new ChatTable();
            table.setContent(chatEntity.getContent());
            table.setFromAvatar(chatEntity.getFromAvatar());
            table.setNameFrom(chatEntity.getNameTo());
            table.setNameTo(chatEntity.getNameFrom());
            table.setUidFrom(chatEntity.getUidTo());
            table.setUidTo(chatEntity.getUidFrom());
            table.setOrderId(chatEntity.getOrderId());
            table.setRead(true);
            table.setHasNew(false);
            table.setSendTimeStr(DateUtils.getDateTimeFromMillisecond(System.currentTimeMillis()));
            table.setSendTime(System.currentTimeMillis());
            databaseUtils.saveChat(table);
            ChatTipEvent tipEvent = new ChatTipEvent();
            tipEvent.setHasNew(hasNew);
            tipEvent.setOrderId(chatEntity.getOrderId());
            EventBus.getDefault().post(tipEvent);
            return;
        } else {
            table = findByOrderList.get(findByOrderList.size() - 1);
            table.setContent(chatEntity.getContent());
            table.setSendTimeStr(chatEntity.getSendTimeStr());
            table.setSendTime(chatEntity.getSendTime().getTime());
            databaseUtils.update(table);
            ChatTipEvent tipEvent = new ChatTipEvent();
            tipEvent.setHasNew(hasNew);
            tipEvent.setOrderId(chatEntity.getOrderId());
            EventBus.getDefault().post(tipEvent);
            LogUtils.e("再次进入");
            return;
        }
    }


    private void initRvChat() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        rvMessage.setLayoutManager(manager);
        adapter = new ChatAdapter(ChatActivity.this, chatEntities, String.valueOf(MyApplication.app.getCurrentUser().getId()));
        rvMessage.setAdapter(adapter);
    }

    @Override
    protected void loadData() {
        pageNo = 0;
        getHistoyList(true);
    }

    private JSONObject buildBodyJson(String content) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("orderId", orderDetial.getOrderSn());
            obj.put("uidFrom", MyApplication.getApp().getCurrentUser().getId());
            obj.put("uidTo", orderDetial.getHisId());
            obj.put("nameTo", orderDetial.getOtherSide());
            obj.put("nameFrom", MyApplication.getApp().getCurrentUser().getUsername());
            obj.put("messageType", "1");
            obj.put("avatar", MyApplication.getApp().getCurrentUser().getAvatar());
            obj.put("sendTimeStr", DateUtils.getFormatTime("", null));
            if (!StringUtils.isEmpty(content)) obj.put("content", content);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getChatEvent(ChatEvent chatEvent) {
        if (chatEvent.getCmd().equals(ISocket.CMD.PUSH_GROUP_CHAT)) {
            ChatEntity chatEntity = gson.fromJson(chatEvent.getResonpce(), ChatEntity.class);
            if (chatEntity.getOrderId().equals(orderDetial.getOrderSn())) {
                addChatEntity(chatEvent.getResonpce(), ChatEntity.Type.LEFT);
            }
        } else {
        }
    }

    @Override
    public void dataSuccess(ISocket.CMD cmd, String response) {
        switch (cmd) {
            case SEND_CHAT:
                LogUtils.e("-----" + response);
                storageData(obj.toString());
                break;
        }
    }

    private void addChatEntity(String response, ChatEntity.Type type) {
        try {
            ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
            chatEntity.setType(type);
            chatEntities.add(chatEntity);
            adapter.notifyDataSetChanged();
            rvMessage.smoothScrollToPosition(chatEntities.size() - 1);
        } catch (Exception ex) {

        }
    }

    @Override
    public void dataFail(int code, ISocket.CMD cmd, String errorInfo) {
        switch (cmd) {
            case SEND_CHAT:
                LogUtils.e(errorInfo);
                break;
        }
    }


    @Override
    public void getHistoryMessageSuccess(List<ChatEntity> entityList) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (entityList == null) return;
        if (pageNo == 1) chatEntities.clear();
        Collections.reverse(chatEntities);
        chatEntities.addAll(entityList);
        Collections.reverse(chatEntities);
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void getHistoryMessageFail(Integer code, String toastMessage) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


}
