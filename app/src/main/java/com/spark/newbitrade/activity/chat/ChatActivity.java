package com.spark.newbitrade.activity.chat;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
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
import com.spark.newbitrade.activity.order.OrderFragment;
import com.spark.newbitrade.adapter.ChatAdapter;
import com.spark.newbitrade.MyApplication;
import com.spark.newbitrade.base.BaseActivity;
import com.spark.newbitrade.entity.ChatEntity;
import com.spark.newbitrade.entity.ChatEvent;
import com.spark.newbitrade.entity.ChatTable;
import com.spark.newbitrade.entity.ChatTipEvent;
import com.spark.newbitrade.entity.OrderDetial;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.ChatWebSocketService;
import com.spark.newbitrade.serivce.chatUtils.SocketMessage;
import com.spark.newbitrade.serivce.chatUtils.SocketResponse;
import com.spark.newbitrade.serivce.chatUtils.SendMsgListener;
import com.spark.newbitrade.utils.SharedPreferenceInstance;
import com.spark.newbitrade.utils.DatabaseUtils;
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
public class ChatActivity extends BaseActivity implements ChatContact.View {
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
    private NotificationCompat.Builder builder;

    //websocket
    private ChatWebSocketService webSocketService;//接单webSocketService
    private ServiceConnection mConnection;
    private String NOTIFY_ID = "10000";
    private Context mContext;

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (chatEntities != null && chatEntities.size() > 0) {
            pageNo = 0;
            getHistoyList(false);
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
        refreshLayout.setColorSchemeResources(android.R.color.black);
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
        bindSocketService();
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
                bundle.putSerializable("status", OrderFragment.Status.values()[orderDetial.getStatus()]);
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
        //SocketFactory.produceSocket(ISocket.C2C).sendRequest(ISocket.CMD.SEND_CHAT, obj.toString().getBytes(), this);
        if (webSocketService != null)
            webSocketService.sendData(new SocketMessage(0, ISocket.CMD.SEND_CHAT.getCode(), obj.toString().getBytes()), sendMsgListener);
        addChatEntity(obj.toString(), ChatEntity.Type.RIGHT);
        etContent.setText("");
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
        getHistoyList(false);
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
            obj.put("sendTime", System.currentTimeMillis());
            if (!StringUtils.isEmpty(content)) obj.put("content", content);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    private JSONObject buildBodyJsonUid() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("uid", MyApplication.getApp().getCurrentUser().getId());
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
                storageData(chatEvent.getResonpce());
            }
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
    public void getHistoryMessageSuccess(List<ChatEntity> entityList) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        if (entityList == null) return;
        if (pageNo == 1) chatEntities.clear();
        Collections.reverse(chatEntities);
        chatEntities.addAll(entityList);
        Collections.reverse(chatEntities);
        adapter.notifyDataSetChanged();
        if (chatEntities.size() > 1)
            rvMessage.smoothScrollToPosition(chatEntities.size() - 1);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void getHistoryMessageFail(Integer code, String toastMessage) {
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        NetCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    /**
     * 获取service对象
     */
    private void bindSocketService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ChatWebSocketService.MyBinder binder = (ChatWebSocketService.MyBinder) service;
                webSocketService = binder.getService();
//                if (webSocketService != null)
//                    webSocketService.sendData(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_GROUP_CHAT.getCode(), buildBodyJsonUid().toString().getBytes()), sendMsgListener);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        //启动Service
        startAllServices();
    }

    /**
     * 开启所有Service
     */
    private void startAllServices() {
        Intent intent = new Intent(getApplicationContext(), ChatWebSocketService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    /**
     * TCP连接socket监听
     */
    SendMsgListener sendMsgListener = new SendMsgListener() {

        @Override
        public void onMessageResponse(SocketResponse socketResponse) {
            LogUtils.e("socket监听===========" + socketResponse.toString());
            if (socketResponse != null) {
                try {
                    int cmd = socketResponse.getCmd();
                    String str = socketResponse.getResponse();
                    switch (cmd) {
                        case 20034://发送聊天消息
                            storageData(obj.toString());
                            break;
                        case 20039://收到聊天消息
                            //{"content":"哈哈哈","messageType":"1","nameFrom":"1234243","nameTo":"u1559627767899","orderId":"195247908350115840","partitionKey":201906,"sendTime":1560426151273,"uidFrom":"119","uidTo":"126"}
                            /*if (isAppOnForeground()) {
                                startAlarm(getApplicationContext());
                            }*/
                            //ISocket.CMD cmd2 = ISocket.CMD.PUSH_GROUP_CHAT;
                            //showNotice(str);
                            //storageData(str);
//                            ChatEvent chatEvent = new ChatEvent();
//                            chatEvent.setResonpce(str);
//                            chatEvent.setType("left");
//                            chatEvent.setCmd(cmd2);
                            //EventBus.getDefault().post(chatEvent);

//                            if (chatEvent.getCmd().equals(ISocket.CMD.PUSH_GROUP_CHAT)) {
//                                ChatEntity chatEntity = gson.fromJson(chatEvent.getResonpce(), ChatEntity.class);
//                                if (chatEntity.getOrderId().equals(orderDetial.getOrderSn())) {
//                                    addChatEntity(chatEvent.getResonpce(), ChatEntity.Type.LEFT);
//                                }
//                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e("socket监听===========================================================解析异常");
                }
            }

        }

        @Override
        public void error() {
            LogUtils.e("socket监听===========================================================发送消息异常");
            /*if (!isTokenUnUsed) {
                //ToastUtils.showToast("发送消息异常===退出登录");
                isTokenUnUsed = true;
                presenter.loginOut();
            }*/

//            isNeedReconnect = true;
//            reconnect();

        }
    };

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    //消息提示音
    private static void startAlarm(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    private void showNotice(String response) {
        if (!isAppOnForeground()) {
            mContext = getApplicationContext();
            ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
            int id = Integer.valueOf(chatEntity.getUidFrom());
            OrderDetial orderDetial = new OrderDetial();
            orderDetial.setOrderSn(chatEntity.getOrderId());
            orderDetial.setMyId(chatEntity.getUidTo());
            orderDetial.setHisId(chatEntity.getUidFrom());
            orderDetial.setOtherSide(chatEntity.getNameFrom());
            Intent intent = new Intent(mContext, ChatActivity.class);
            intent.putExtra("orderDetial", orderDetial);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notification = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(NOTIFY_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(mChannel);
                builder = new NotificationCompat.Builder(mContext)
                        .setChannelId(NOTIFY_ID)
                        .setContentTitle(getString(R.string.app_name))
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                        .setContentIntent(PendingIntent.getActivity(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(chatEntity.getNameFrom() + ": " + chatEntity.getContent());
                notification = builder.build();
            } else {
                builder = new NotificationCompat.Builder(mContext)
                        .setContentTitle(getString(R.string.app_name))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setOngoing(false)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                        .setContentIntent(PendingIntent.getActivity(mContext, id, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(chatEntity.getNameFrom() + ": " + chatEntity.getContent());
                notification = builder.build();
            }
            notificationManager.notify(id, notification);
        }
    }

    private void storageData(String response) {
        ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
        if (chatEntity == null) return;
        LogUtils.e("chatEntity  " + chatEntity.toString());
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
            table.setSendTime(System.currentTimeMillis());
            table.setRead(true);
            table.setHasNew(false);
            databaseUtils.update(table);
            ChatTipEvent tipEvent = new ChatTipEvent();
            tipEvent.setHasNew(hasNew);
            tipEvent.setOrderId(chatEntity.getOrderId());
            EventBus.getDefault().post(tipEvent);
            LogUtils.e("再次进入");
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            unbindService(mConnection);
        }
        presenter.destory();
    }

}
