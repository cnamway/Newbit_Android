package com.spark.newbitrade.serivce;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.spark.newbitrade.R;
import com.spark.newbitrade.activity.chat.ChatActivity;
import com.spark.newbitrade.entity.ChatEntity;
import com.spark.newbitrade.entity.ChatEvent;
import com.spark.newbitrade.entity.ChatTable;
import com.spark.newbitrade.entity.ChatTipEvent;
import com.spark.newbitrade.entity.OrderDetial;
import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.utils.DatabaseUtils;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;
import com.spark.newbitrade.utils.SharedPreferenceInstance;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * author: wuzongjie
 * time  : 2018/4/27 0027 10:40
 * desc  :
 */

public class GroupService extends Service {
    private static final String TAG = "GroupService";
    private static final long sequenceId = 0;//以后用于token
    private static final int requestid = 0;//请求ID
    private static final int version = 1;
    private static final String terminal = "1001";  //安卓:1001,苹果:1002,WEB:1003,PC:1004
    private static final String ip = "47.74.48.49";
    private static final int port = 28905;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Socket socket = null;
    private SocketThread socketThread;
    private Gson gson = new Gson();
    private boolean hasNew = false;
    private DatabaseUtils databaseUtils;
    private List<ChatTable> list;
    private List<ChatTable> findByOrderList;
    private PingThread pingThread; // Ping包的线程
    private SocketMessage lastMessage; // 最新的请求指令
    private boolean isOpen;
    private static final String lock = "grouplock";
    private int code;
    private NotificationCompat.Builder builder;
    private String NOTIFY_ID = "10000";
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetMessage(SocketMessage message) {
        code = message.getCode();
        if (code == GlobalConstant.CODE_CHAT) {
            LogUtils.e("GroupService " + "接受的信息" + message.getCmd());
            getSocketGroup(message);
            lastMessage = message;
        }
    }

    private void getSocketGroup(SocketMessage message) {
        if (socketThread != null && socketThread.isAlive()) {
            if (socket == null || !socket.isConnected()) {
                releaseSocket(message);
            }
            toRequest(message.getCmd(), message.getBody());
        } /*else {
            socketThread = new SocketThread();
            socketThread.start();
        }*/
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("GroupService服务开启了");
        EventBus.getDefault().register(this);
        isOpen = true;
        if (socketThread == null || !socketThread.isAlive()) {
            socketThread = new SocketThread();
            socketThread.start();
            LogUtils.e("GroupService   onCreate   socketThread.start()");
        }
    }

    class PingThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isOpen) {
                try {
                    byte[] requestBytes = buildRequest(ISocket.CMD.HEART_BEAT, null);
                    dos.write(requestBytes);
                    dos.flush();
                } catch (Exception e) {
                    LogUtils.e("GroupService发送心跳包失败");
                    releaseSocket(lastMessage);
                }
                SystemClock.sleep(5000); // 每隔5s发送一次心跳包
            }
        }
    }

    private void releaseSocket(SocketMessage message) {
        if (socket != null) {
            try {
                socket.close();
                dos.close();
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
            dis = null;
            dos = null;
        }
        try {
            socket = new Socket(ip, port);
            if (socket.isConnected()) {
                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                LogUtils.e("GroupService重连成功   message不清楚");
                if (message != null) {
                    toRequest(message.getCmd(), message.getBody()); // 这里我推送最后一次发送来的数据
                    LogUtils.e("GroupService重连成功   message不为空  " + message.getCmd());
                }
            }
        } catch (IOException e) {
            LogUtils.e("GroupService重连失败");
            e.printStackTrace();
        }
    }

    private void toRequest(ISocket.CMD cmd, byte[] body) {
        try {
            byte[] requestBytes = buildRequest(cmd, body);
            dos.write(requestBytes);
            dos.flush();
        } catch (Exception e) {
            //Log.d(TAG, "3");
            e.printStackTrace();
        }
    }

    public static byte[] buildRequest(ISocket.CMD cmd, byte[] body) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            int length = body == null ? 26 : (26 + body.length);
            dos.writeInt(length);
            dos.writeLong(sequenceId);
            dos.writeShort(cmd.getCode());
            dos.writeInt(version);
            byte[] terminalBytes = terminal.getBytes();
            dos.write(terminalBytes);
            dos.writeInt(requestid);
            if (body != null) dos.write(body);
            return bos.toByteArray();
        } catch (IOException ex) {
            try {
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void dealResponse(DataInputStream dis) throws Exception {
        int length = dis.readInt();
        long sequenceId = dis.readLong();
        short code = dis.readShort();
        final int responseCode = dis.readInt();
        int requestId = dis.readInt();
        byte[] buffer = new byte[length - 22];
        final ISocket.CMD cmd = ISocket.CMD.findObjByCode(code);
        int nIdx = 0;
        int nReadLen = 0;
        while (nIdx < buffer.length) {
            nReadLen = dis.read(buffer, nIdx, buffer.length - nIdx);
            if (nReadLen > 0) {
                nIdx += nReadLen;
            } else {
                break;
            }
        }
        String str = new String(buffer);
        LogUtils.e("GroupService接受的信息" + cmd + " --" + str);
        //EventBus.getDefault().post(new SocketResponse(cmd,str));

        if (cmd == ISocket.CMD.PUSH_GROUP_CHAT) {
            //startAlarm(getApplicationContext());
            if (isAppOnForeground()) {
                startAlarm(getApplicationContext());
            }
            showNotice(str);
            storageData(str);
            ChatEvent chatEvent = new ChatEvent();
            chatEvent.setResonpce(str);
            chatEvent.setType("left");
            chatEvent.setCmd(cmd);
            EventBus.getDefault().post(chatEvent);
           /* if(cmd== ISocket.CMD.PUSH_EXCHANGE_KLINE){
                Log.d("jiejie","----" + cmd + "  " + str);
            }*/
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socket != null) try {
            socket.close();
            dis.close();
            dos.close();
            socket = null;
            socketThread = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        isOpen = false;
        socketThread = null;
        pingThread = null;
        EventBus.getDefault().unregister(this);
    }

    class SocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (lock) {
                LogUtils.e("GroupService   socketThread.start()    synchronized");
                if (socket == null) {
                    try {
                        socket = new Socket(ip, port);
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        isOpen = true;
                        pingThread = new PingThread();
                        pingThread.start(); // 开启ping包线程
                        LogUtils.e("GroupService 连接成功");
                    } catch (IOException e) {
                        isOpen = false;
                        socket = null;
                        e.printStackTrace();
                    }
                }
            }
            while (isOpen) {
                try {
                    dealResponse(dis);
                } catch (Exception e) {
                    socketThread = null;
                }
            }
        }
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

    private void storageData(String response) {
        ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
        if (chatEntity == null) return;
        hasNew = true;
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
            //table.setSendTimeStr(chatEntity.getSendTimeStr());
            //table.setSendTime(chatEntity.getSendTime());
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
                //table.setSendTimeStr(chatEntity.getSendTimeStr());
                //table.setSendTime(chatEntity.getSendTime());
                //WonderfulLogUtils.logi("MyService","  content  "+chatEntity.getContent());
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
        //table.setSendTimeStr(chatEntity.getSendTimeStr());
        //table.setSendTime(chatEntity.getSendTime());
        databaseUtils.saveChat(table);
        ChatTipEvent tipEvent = new ChatTipEvent();
        tipEvent.setHasNew(hasNew);
        tipEvent.setOrderId(chatEntity.getOrderId());
        EventBus.getDefault().post(tipEvent);
    }

    //消息提示音
    private static void startAlarm(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }


}
