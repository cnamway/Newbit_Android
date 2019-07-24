package com.spark.newbitrade.serivce;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.chat.ChatActivity;
import com.spark.newbitrade.entity.ChatEntity;
import com.spark.newbitrade.entity.ChatEvent;
import com.spark.newbitrade.entity.ChatTable;
import com.spark.newbitrade.entity.ChatTipEvent;
import com.spark.newbitrade.entity.OrderDetial;
import com.spark.newbitrade.factory.HttpUrls;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.serivce.chatUtils.NettyInitDataUtils;
import com.spark.newbitrade.serivce.chatUtils.SendMsgListener;
import com.spark.newbitrade.serivce.chatUtils.ServiceOpenSuccessEvent;
import com.spark.newbitrade.serivce.chatUtils.SocketMessage;
import com.spark.newbitrade.serivce.chatUtils.SocketResponse;
import com.spark.newbitrade.utils.DatabaseUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;


/**
 * 聊天主服务
 */

public class ChatWebSocketService extends Service {

    private static final String TAG = "WebSocketService";
    private static WebSocketConnection webSocketConnection; // ws 对应的类
    private static WebSocketOptions options = new WebSocketOptions(); //ws的个选项，声明出来即可使用了
    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 30 * 1000;//每隔30秒进行一次对长连接的心跳检测
    private SendMsgListener sendMsgListener;
    private PowerManager.WakeLock mWakeLock = null;//唤醒锁
    private int CONNECT_AGAIN = 10;//重新连接间隔时间秒
    private static boolean isNeedReconnect = true;//是否需要重连
    private static boolean isClosed = true;//连接是否被关闭

    //WebSocket地址配置
    private static String WEBSOCKET_HOST_AND_PORT = HttpUrls.WEBSOCKET_HOST_AND_PORT;

    private SocketMessage lastMessage; // 聊天订阅指令消息
    private Gson gson = new Gson();
    private String NOTIFY_ID = "10000";
    private Context mContext;
    private NotificationCompat.Builder builder;
    private DatabaseUtils databaseUtils;
    private List<ChatTable> list;
    private List<ChatTable> findByOrderList;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        initWebSocketConnect();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        initWebSocketConnect();
        int pol = Settings.System.WIFI_SLEEP_POLICY_NEVER;
        Settings.System.putInt(getContentResolver(), Settings.System.WIFI_SLEEP_POLICY, pol);
        mTimeHandler.sendEmptyMessageDelayed(3, 5 * 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * 获取唤醒锁
     */
    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager mPM = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPM.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            if (mWakeLock != null) {
                mWakeLock.acquire();
            }
        }
    }

    /**
     * 释放锁
     */
    private void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWebsocket(false);
        releaseWakeLock();
        EventBus.getDefault().unregister(this);
    }

    //这里使用binder 主要是为了在activity中进行通讯， 大家也可以使用EventBus进行通讯
    public class MyBinder extends Binder {

        public ChatWebSocketService getService() {
            return ChatWebSocketService.this;
        }
    }

    /**
     * 连接ws
     */
    public synchronized ChatWebSocketService initSocket() {
        try {
            webSocketConnection = new WebSocketConnection();
            webSocketConnection.connect(WEBSOCKET_HOST_AND_PORT, new WebSocketHandler() {

                //WebSocketService启动时候的回调
                @Override
                public void onOpen() {
                    Log.e(TAG, "WebSocketService- onOpen: 开启成功！！WEBSOCKET_HOST_AND_PORT==" + WEBSOCKET_HOST_AND_PORT);
                    isClosed = false;
                    isNeedReconnect = true;
                    EventBus.getDefault().post(new ServiceOpenSuccessEvent(1));
                }

                //WebSocketService接收到消息后的回调
                @Override
                public void onTextMessage(String content) {
                }

                @Override
                public void onBinaryMessage(byte[] content) {
                    //这里可以使用EventBus将内容传递到activity
                    InputStream is = new ByteArrayInputStream(content);
                    DataInputStream dis = new DataInputStream(is);
                    SocketResponse socketResponse = NettyInitDataUtils.startRecTask(sendMsgListener, dis);
                    if (socketResponse != null && socketResponse.getCmd() != ISocket.CMD.SEND_CHAT.getCode()) {
                        int cmd = socketResponse.getCmd();
                        String str = socketResponse.getResponse();

                        startAlarm(getApplicationContext());

                        if (!isAppOnForeground()) {
                            showNotice(str);
                        }
                        ISocket.CMD cmd2 = ISocket.CMD.PUSH_GROUP_CHAT;
                        storageData(str);
                        ChatEvent chatEvent = new ChatEvent();
                        chatEvent.setResonpce(str);
                        chatEvent.setType("left");
                        chatEvent.setCmd(cmd2);
                        EventBus.getDefault().post(chatEvent);

                    }

                }

                @Override
                public void onRawTextMessage(byte[] content) {
                }


                //WebSocketService关闭时候的回调
                @Override
                public void onClose(int code, String reason) {
                    isClosed = true;
                    Log.e(TAG, "WebSocketService- 服务器关闭！！" + "isNeedReconnect==" + isNeedReconnect + ",WEBSOCKET_HOST_AND_PORT==" + WEBSOCKET_HOST_AND_PORT);
                    mHandler.removeCallbacks(heartBeatRunnable);
                    if (isNeedReconnect) {
                        //EventBus.getDefault().post(new ConnectCloseEvent(1));
                        mTimeHandler.sendEmptyMessageDelayed(1, CONNECT_AGAIN * 1000);
                    } else {
                        mTimeHandler.removeMessages(1);
                    }
                    Log.e(TAG, "WebSocketService- 1111111111111111111111111");
                    closeWebsocket(true);
                    Log.e(TAG, "WebSocketService- 5555555555555555555555555");
                }

            }, options);
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//开启心跳检测
        } catch (WebSocketException e) {
            e.printStackTrace();
            Log.e(TAG, "WebSocketService- 打开异常==================================================");
            isClosed = true;
            closeWebsocket(true);
        }
        return this;
    }

    /**
     * 关闭ws
     */
    public void closeWebsocket(boolean isNeedReconnect) {
        this.isNeedReconnect = isNeedReconnect;
        try {
            Log.e(TAG, "WebSocketService- 222222222222222222222222");
            if (webSocketConnection != null && webSocketConnection.isConnected()) {
                webSocketConnection.disconnect();
                webSocketConnection = null;
                Log.e(TAG, "WebSocketService- 33333333333333333333333");
            }
        } catch (Exception e) {
            Log.e(TAG, "WebSocketService- 444444444444444444444");
        }
    }

    /**
     * 发送数据
     *
     * @param socketMessage
     * @param sendMsgListener
     */
    public void sendData(final SocketMessage socketMessage, final SendMsgListener sendMsgListener) {
        this.sendMsgListener = sendMsgListener;
        if (webSocketConnection != null && !isClosed) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e(TAG, "WebSocketService- 发送数据指令==" + socketMessage.getCmd() + ",,,,发送数据==" + socketMessage.getBody());
                        //发送消息
                        byte[] requestBytes = NettyInitDataUtils.buildRequest(socketMessage.getCmd(), socketMessage.getBody());
                        if (webSocketConnection != null && requestBytes != null) {
                            webSocketConnection.sendBinaryMessage(requestBytes);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (sendMsgListener != null) {
                            Log.e(TAG, "WebSocketService- 发送  message  异常" + e.toString() + "==============================");
                            sendMsgListener.error();
                        }
                    }
                }
            }).start();
        } else {
            if (sendMsgListener != null) {
                Log.e(TAG, "WebSocketService- 发送数据指令error==webSocketConnection==" + webSocketConnection + ",isClosed==" + isClosed);
                sendMsgListener.error();
            }
        }

    }


    private long sendTime = 0L;
    // 发送心跳包
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                try {
                    if (!isClosed) {
                        byte[] requestBytes = NettyInitDataUtils.buildRequest(ISocket.CMD.HEART_BEAT.getCode(), null);
                        if (webSocketConnection != null && requestBytes != null) {
                            webSocketConnection.sendBinaryMessage(requestBytes);//发送一个空消息给服务器，通过发送消息的成功失败来判断长连接的连接状态
                            sendTime = System.currentTimeMillis();
                            //Log.e(TAG, "WebSocketService- 发送一次心跳检测");
                        }
                        mHandler.postDelayed(this, HEART_BEAT_RATE);//每隔一定的时间，对长连接进行一次心跳检测
                    }
                } catch (Exception e) {

                }
            }
        }
    };

    Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initWebSocketConnect();
                    break;
                case 3:
                    acquireWakeLock();
                    break;
            }
        }
    };


    /**
     * 重新连接websocket
     */
    public void reconnect() {
        try {
            if (isClosed) {//连接已关闭
                Log.e(TAG, "WebSocketService- 重新连接websocket0000000000000000000000000000");
                closeWebsocket(false);
                initWebSocketConnect();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 初始化WebSocket连接
     */
    private synchronized void initWebSocketConnect() {
        if (isClosed) {
            if (checkNetwork(this)) {
                Log.e(TAG, "WebSocketService- 创建一个新的连接WEBSOCKET_HOST_AND_PORT==" + WEBSOCKET_HOST_AND_PORT);
                new InitSocketThread().start();//创建一个新的连接
            } else {
                Log.e(TAG, "WebSocketService- 无网络连接   不在重新连接");
            }
        }
    }

    private class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetMessage(SocketMessage message) {
        int code = message.getCode();
        if (code == GlobalConstant.CODE_CHAT) {
            LogUtils.e("WebSocketService-  " + "接受的信息" + message.getCmd());
            getSocketGroup(message);
            lastMessage = message;
        }
    }

    /**
     * 发送订阅指令
     *
     * @param message
     */
    private void getSocketGroup(SocketMessage message) {
        if (webSocketConnection != null && !isClosed) {
            sendData(message, null);
        } else {
            initWebSocketConnect();
        }
    }


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
        boolean hasNew = true;
        SharedPreferenceInstance.getInstance().saveHasNew(hasNew);
        databaseUtils = new DatabaseUtils();
        list = databaseUtils.findAll();
        if (list == null || list.size() == 0) {
            ChatTable table = new ChatTable();
            table.setContent(chatEntity.getContent());
            table.setFromAvatar(chatEntity.getFromAvatar());
            table.setNameFrom(chatEntity.getNameFrom());
            table.setNameTo(chatEntity.getNameTo());
            table.setUidFrom(chatEntity.getUidFrom());
            table.setUidTo(chatEntity.getUidTo());
            table.setOrderId(chatEntity.getOrderId());
            table.setRead(false);
            table.setHasNew(true);
            table.setSendTime(System.currentTimeMillis());
            databaseUtils.saveChat(table);
            ChatTipEvent tipEvent = new ChatTipEvent();
            tipEvent.setHasNew(hasNew);
            tipEvent.setOrderId(chatEntity.getOrderId());
            EventBus.getDefault().post(tipEvent);
            return;
        }
        ChatTable table = new ChatTable();
        for (int i = 0; i < list.size(); i++) {
            if (chatEntity.getOrderId().equals(list.get(i).getOrderId())) {
                findByOrderList = databaseUtils.findByOrder(chatEntity.getOrderId());
                table = findByOrderList.get(findByOrderList.size() - 1);
                table.setRead(false);
                table.setHasNew(true);
                table.setContent(chatEntity.getContent());
                table.setSendTime(System.currentTimeMillis());
                databaseUtils.update(table);
                ChatTipEvent tipEvent = new ChatTipEvent();
                tipEvent.setHasNew(hasNew);
                tipEvent.setOrderId(chatEntity.getOrderId());
                EventBus.getDefault().post(tipEvent);
                return;
            }
        }
        table.setContent(chatEntity.getContent());
        table.setFromAvatar(chatEntity.getFromAvatar());
        table.setNameFrom(chatEntity.getNameFrom());
        table.setNameTo(chatEntity.getNameTo());
        table.setUidFrom(chatEntity.getUidFrom());
        table.setUidTo(chatEntity.getUidTo());
        table.setOrderId(chatEntity.getOrderId());
        table.setRead(false);
        table.setHasNew(true);
        table.setSendTime(System.currentTimeMillis());
        databaseUtils.saveChat(table);
        ChatTipEvent tipEvent = new ChatTipEvent();
        tipEvent.setHasNew(hasNew);
        tipEvent.setOrderId(chatEntity.getOrderId());
        EventBus.getDefault().post(tipEvent);
    }

    /**
     * 检查网络连接
     *
     * @param context
     * @return
     */
    public static boolean checkNetwork(Context context) {
        boolean isNetStatus = false;
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
                if (wifiNetworkInfo.isAvailable() || dataNetworkInfo.isAvailable()) {
                    isNetStatus = true;
                }
            } else {
                isNetStatus = false;
            }
        } else {//API大于23时使用下面的方式进行网络监听
            System.out.println("API level 大于23");
            //获得ConnectivityManager对象
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取所有网络连接的信息
            Network[] networks = connectivityManager.getAllNetworks();
            //通过循环将网络信息逐个取出来
            for (int i = 0; i < networks.length; i++) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networks[i]);
                if (networkInfo != null) {
                    String typeName = networkInfo.getTypeName();
                    if (typeName.equalsIgnoreCase("WIFI")) {
                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                            isNetStatus = true;
                            return isNetStatus;
                        }
                    } else if (typeName.equalsIgnoreCase("MOBILE")) {
                        if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                            isNetStatus = true;
                            return isNetStatus;
                        }
                    } else {
                        isNetStatus = false;
                    }
                }
            }
        }
        return isNetStatus;
    }
}