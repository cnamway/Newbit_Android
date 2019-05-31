/**
 * Copyright (c) 2016-2017  All Rights Reserved.
 * <p>
 * <p>FileName: ClientDemo.java</p>
 * <p>
 * Description:
 *
 * @author zhangyaliang
 * @date 2017年6月26日
 * @version 1.0
 * History:
 * v1.0.0, 章亚亮, 2017年6月26日, Create
 */
package com.spark.newbitrade.instance;

//import com.spark.example.netty.constant.NettyCommand;

import java.io.*;
import java.net.Socket;

/**
 * <p>
 * Title: ClientDemo
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author zhangyaliang
 * @date 2017年6月26日
 */
public class ClientDemo {
    static String ip = "114.55.94.18";
    static int port = 28984;
    static long sequenceId = 10001;

    public static void main(String[] args) {
        try {
            ClientDemo cd = new ClientDemo();
            //创建正常请求通道
            Socket socket = new Socket(ip, port);
            OutputStream os = socket.getOutputStream();
            //获取输入流
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            //由Socket对象得到输入流，并构造相应的BufferedReader对象
            //发起订阅请求
//			cd.sendAndDealResponse(dis,os, NettyCommand.SUBSCRIBE_SYMBOL_THUMB,null);
            int i = 0;
            while (i < 10) {
                if (i == 5) {
                    //接收5次消息后取消订阅
//					cd.sendAndDealResponse(dis,os,NettyCommand.UNSUBSCRIBE_SYMBOL_THUMB,null);
                }
                cd.dealResponse(dis);
                i++;
            }
            os.close(); //关闭Socket输出流
            is.close(); //关闭Socket输入流
            socket.close(); //关闭Socket
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error" + e);//出错，则打印出错信息
        }
    }


    public void dealResponse(DataInputStream dis) throws IOException {
        int length = dis.readInt();
        long sequenceId = dis.readLong();
        short cmd = dis.readShort();
        int responseCode = dis.readInt();
        int requestId = dis.readInt();

        byte[] buffer = new byte[length - 22];
        System.out.println("cmd:" + cmd + ",length:" + length + ",sequenceId:" + sequenceId + ",responseCode:" + responseCode + ",sequenceId:" + requestId);
        int nIdx = 0;
        int nReadLen = 0;
        while (nIdx < buffer.length) {
            nReadLen = dis.read(buffer, nIdx, buffer.length - nIdx);
            System.out.println("本次读取字节数为：" + nReadLen);
            if (nReadLen > 0) {
                nIdx += nReadLen;
            } else {
                break;
            }
        }
//		if(cmd == NettyCommand.PUSH_SYMBOL_THUMB){
//			System.out.println("返回结果: "+new String(buffer));
//		}
        /*else {
            QuoteMessage.SimpleResponse result =
					QuoteMessage.SimpleResponse.newBuilder()
							.mergeFrom(buffer).build();
			System.out.println("返回结果为：" + result.getCode() + "-" + result.getMessage());
		}*/
    }

    public byte[] buildRequest(short cmd, int version, long sequenceId, byte[] body) throws IOException {
        System.out.println("会话Id为：" + sequenceId);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        int length = body == null ? 26 : (26 + body.length);
        dos.writeInt(length);
        dos.writeLong(sequenceId);
        dos.writeShort(cmd);
        dos.writeInt(version);
        //安卓:1001,苹果:1002,WEB:1003,PC:1004
        byte[] terminalBytes = "1001".getBytes();
        dos.write(terminalBytes);
        dos.writeInt(0);//sequenceId
        if (body != null) dos.write(body);
        return bos.toByteArray();
    }

}
