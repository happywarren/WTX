/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.test;

import com.lt.quota.core.quota.bean.QuotaBean;
import com.otod.bean.ServerContext;
import com.otod.bean.WorkHandleParam;
import com.otod.bean.quote.snapshot.ForexSnapshot;

import com.otod.bean.quote.snapshot.Snapshot;
import com.otod.component.tcp.client.TcpClient;
import com.otod.component.tcp.client.TcpClientDataHandler;
import com.otod.component.tcp.client.impl.TcpClientImpl;
import com.otod.component.util.GeneralException;
import com.otod.test.timer.HeartbeatTimer;

import com.otod.util.StringUtil;
import com.otod.util.mutithread.TimerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Administrator
 */
public abstract class QuoteClient extends Thread implements TcpClientDataHandler {

    private TcpClient tcpClient;
    private boolean isConnect = false;
    private boolean reConnect = true;
    private String ip = "121.42.192.245";
    private int port = 6655;
    private boolean loginFlag = false;
    private TimerThread heartbeatTimer = new TimerThread();

    public static  Map<String,String> productListOuter = new ConcurrentHashMap<String,String>();
    public static  Map<String,String> productListInner = new ConcurrentHashMap<String,String>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    /*
    public static void main(String[] args) {
        QuoteClient quoteClient = new QuoteClient();
        quoteClient.start();
    }*/

    @Override
    public void run() {
        logger.info("启动行情数据！！！");
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        TimeZone.setDefault(tz);

        ServerContext.setQuoteClient(this);

        connect();
        doHeartbeatTimer();
    }

    public void doHeartbeatTimer() {
        heartbeatTimer.setPeriodDay(1000 * 10);
        WorkHandleParam workhandleParam = new WorkHandleParam();
        workhandleParam.workHandle = new HeartbeatTimer();
        heartbeatTimer.setWorkHandleParam(workhandleParam);
        heartbeatTimer.start();
    }

    public void stopClient() {
        logger.info("主动关闭连接socket");
        isConnect = false;
        reConnect = false;
        loginFlag = false;
        tcpClient.disconnect();
    }

    public void closeClient() {
        isConnect = false;
        reConnect = false;
        tcpClient.disconnect();
    }

    public void connect() {
        tcpClient = new TcpClientImpl();
        tcpClient.setServerIp(ip);
        tcpClient.setServerPort(port);
        tcpClient.setDataHandler(this);
        try {
            tcpClient.connect();
        } catch (GeneralException e) {
            //System.out.println("连接不上");
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
    }

    public TcpClient getTcpClient() {
        return tcpClient;
    }

    public void setTcpClient(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public void onConnect(String serverIp, int serverPort) {
        //System.out.println("连接" + serverIp + "端口" + serverPort);
        logger.info("连接:{},端口:{}",serverIp,serverPort);
        isConnect = true;
        sendLogin();
    }

    //发送登录请求
    private void sendLogin() {

        logger.info("开始登陆！！！！");
        String username = "";
        String password = "";
        username = StringUtil.completeByBefore("DS-180", 12, " ");
        password = StringUtil.completeByBefore("567843", 12, " ");

        String str = username + password;
        int len = str.length();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(len + 8);
        buffer.putInt(0);
        buffer.put(str.getBytes());
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        tcpClient.send(bytes);
    }

    //分包处理
    @Override
    public int slice(byte[] bytes, int byteCount) {
//        System.out.println(byteCount);
        if (byteCount < 4) {
            return 0;
        }
        ByteBuffer buf = ByteBuffer.allocate(byteCount);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.put(bytes, 0, byteCount);
        buf.flip();
        while (buf.remaining() > 4) {

            buf.mark();

            int msgLen = buf.getInt();
            msgLen = msgLen - 4;
//            System.out.println("msgLen" + msgLen);
            if (msgLen <= 0 || msgLen > buf.limit() - buf.position()) {
                buf.reset();
                return buf.position();
            }
            byte datas[] = new byte[msgLen];
            buf.get(datas, 0, msgLen);
        }
        return buf.position();
    }

    //接受消息
    @Override
    public void onReceiveMsg(byte[] bytes, int byteCount) {
        ByteBuffer buf = ByteBuffer.allocate(byteCount);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.put(bytes, 0, byteCount);
        buf.flip();

        while (buf.remaining() > 4) {

            buf.mark();

            int msgLen = buf.getInt();
            msgLen = msgLen - 4;
            if (msgLen <= 0 || msgLen > buf.limit() - buf.position()) {
                buf.reset();
            }
            byte datas[] = new byte[msgLen];
            buf.get(datas, 0, msgLen);
            ByteBuffer dBuf = ByteBuffer.allocate(msgLen);
            dBuf.order(ByteOrder.LITTLE_ENDIAN);
            dBuf.put(datas);
            paseDatas(dBuf);
        }
    }

    //解包
    private void paseDatas(ByteBuffer buffer) {
        try {
            buffer.flip();
            int rtqType = buffer.getInt();
            //System.out.println(rtqType);
            if (rtqType == 0) {
                byte[] temp = new byte[12];
                buffer.get(temp);
                String userName = new String(temp);
                temp = new byte[1];
                buffer.get(temp);
                String loginFlagStr = new String(temp);
                logger.info("账号:" + userName + "||loginFlag:" + loginFlagStr);
                if (loginFlagStr.equals("1")) {
                   // System.out.println("登录成功");
                    logger.info("登陆到行情服务器成功！！！");
                    loginFlag = true;
                } else if (loginFlagStr.equals("0")) {
                    logger.info("登陆到行情服务器失败！！！");
                    //System.out.println("登录失败");
                    stopClient();
                    loginFlag = false;
                } else if (loginFlagStr.equals("3")) {
                    logger.info("用户已经在其它地方登陆到行情服务器！！！");
                    stopClient();
                    loginFlag = false;
                }

            } else if (rtqType == 2002) {
                //解析外汇，指数，贵金属协议
                byte[] temp = new byte[12];
                buffer.get(temp);//读取代码
                String symbol = new String(temp).trim();

                ForexSnapshot snapshot = new ForexSnapshot();
                snapshot.symbol = symbol;

                byte nameLen = buffer.get();//读取中文名称长度
                temp = new byte[nameLen];
                buffer.get(temp);//读取中文名称
                String name = new String(temp, "UTF-8");
                snapshot.cnName = name;
                temp = new byte[2];
                buffer.get(temp);
                String exchCode = new String(temp);
                snapshot.exchCode = exchCode;
                snapshot.date = buffer.getInt();
                snapshot.time = buffer.getInt();
                snapshot.bid1Price = buffer.getDouble();
                snapshot.bid1Volume = buffer.getInt();
                snapshot.ask1Price = buffer.getDouble();
                snapshot.ask1Volume = buffer.getInt();
                snapshot.open = buffer.getDouble();
                snapshot.high = buffer.getDouble();
                snapshot.low = buffer.getDouble();
                snapshot.close = buffer.getDouble();
                snapshot.pClose = buffer.getDouble();
                snapshot.tVolume = buffer.getDouble();
                snapshot.tValue = buffer.getDouble();

                onMessage(snapshot);

            } else if (rtqType == 10) {
                  //System.err.println("心跳包");
                  logger.info("行情服务器心跳包！！！");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveForexCsv(String symbol, ForexSnapshot snapshot) {
        try {
            File datafile = new File("datas");
            if (!datafile.exists()) {
                datafile.mkdir();
            }
            File file = new File("datas/" + symbol + ".csv");
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.canWrite()) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));// 

                bw.write("日期,时间,买一价,买一量,卖一价,卖一量,开盘,最高,最低,现价,昨收价,成交量,成交金额\n");
                bw.write(StringUtil.formatDate(snapshot.date) + "," + StringUtil.formatHHmmss(snapshot.time) + "," + snapshot.bid1Price + "," + snapshot.bid1Volume + "," + snapshot.ask1Price + "," + snapshot.ask1Volume + "," + snapshot.open + "," + snapshot.high + "," + snapshot.low + "," + snapshot.close + "," + snapshot.pClose + "," + snapshot.tValue + "," + snapshot.tValue + "," + "\n");

                bw.close();
            }
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
    }

    @Override
    public void onSendMsg(byte[] bytes, int byteCount) {
        //System.out.println("消息发送成功" + byteCount);
    }

    public abstract  void onMessage(ForexSnapshot snapshot);

    @Override
    public void onDisconnect() {
        //System.out.println("socket关闭");
        logger.info("socket关闭");
        isConnect = false;
        if (loginFlag == true) {
            if (reConnect) {
                connect();
            }
        }
    }
}
