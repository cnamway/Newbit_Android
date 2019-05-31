package com.spark.newbitrade.serivce;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.spark.newbitrade.factory.socket.ISocket;
import com.spark.newbitrade.utils.GlobalConstant;
import com.spark.newbitrade.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * 行情服务
 */

public class MarketService extends Service {
    private static final long sequenceId = 0;// 以后用于token
    private static final int requestid = 0;// 请求ID
    private static final int version = 1;
    private static final String terminal = "1001";  // 安卓:1001,苹果:1002,WEB:1003,PC:1004
        public static final String ip = "36.7.147.3";
//    public static final String ip = "192.168.2.35";
    private static final int port = 28902;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private Socket socket = null;
    private SocketThread socketThread; // Socket线程
    private PingThread pingThread; // Ping包的线程
    private boolean isOpen;
    private static final String lock = "marketLock";
    private int code;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        isOpen = true;
        if (socketThread == null || !socketThread.isAlive()) {
            socketThread = new SocketThread();
            socketThread.start();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onGetMessage(SocketMessage message) {
        code = message.getCode();
        if (code == GlobalConstant.CODE_MARKET) {
            try {
                String json = "";
                if (message.getBody() != null) {
                    byte[] bytes = message.getBody();
                    json = new String(bytes, "utf-8");
                }
                LogUtils.i("发送的行情指令==" + message.getCmd() + ",,,内容===" + json);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            getSocketOne(message);
        }
    }

    /**
     * 发送指令
     *
     * @param message
     */
    private void getSocketOne(SocketMessage message) {
        if (socketThread != null && socketThread.isAlive()) {
            if (socket == null || !socket.isConnected()) {
                releaseSocket();
            }
            toRequest(message.getCmd(), message.getBody());
        }
    }

    /**
     * Socket重新创建
     */
    private void releaseSocket() {
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
                LogUtils.i("行情重连成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.i("行情重连失败");
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
            dos = null;
            dis = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        socketThread = null;
        pingThread = null;
        isOpen = false;
        EventBus.getDefault().unregister(this);
    }

    /**
     * 数据发送到socket
     */
    private void toRequest(ISocket.CMD cmd, byte[] body) {
        try {
            byte[] requestBytes = buildRequest(cmd, body);
            dos.write(requestBytes);
            dos.flush();
        } catch (Exception e) {
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

    class SocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            synchronized (lock) {
                if (socket == null || !socket.isConnected()) {
                    try {
                        socket = new Socket(ip, port);
                        dis = new DataInputStream(socket.getInputStream());
                        dos = new DataOutputStream(socket.getOutputStream());
                        isOpen = true;
                        pingThread = new PingThread();
                        pingThread.start(); // 开启ping包线程
                        LogUtils.i("行情socket   连接成功");
                    } catch (IOException e) {
                        LogUtils.i("行情socket   连接异常");
                        e.printStackTrace();
                        isOpen = false;
                        socket = null;
                    }
                }
                while (isOpen) {
                    try {
                        dealResponse(dis);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    /**
     * 接受的数据
     */
    private void dealResponse(DataInputStream dis) throws Exception {
        int length = dis.readInt();
        long sequenceId = dis.readLong();
        short code = dis.readShort();
        final int responseCode = dis.readInt();
        int requestId = dis.readInt();
        ;
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
        LogUtils.v("返回行情指令==" + "cmd====" + cmd + ",,,返回数据==" + str);
        if (responseCode == 200) {
            EventBus.getDefault().post(new SocketResponse(cmd, str, code));
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
                    LogUtils.i("行情心跳包发送失败");
                    releaseSocket();
                }
                SystemClock.sleep(8000); // 每隔5s发送一次心跳包
            }
        }
    }

}
