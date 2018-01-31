/*
 * Source code of the book of Thinking in Java Component Design
 * ��������Java ������
 * ����: �׵���
 * Email: kshark2008@gmail.com
 * Date: 2008-12
 * Copyright 2008-2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.otod.component.tcp.server.impl;

import com.otod.component.tcp.server.*;
import com.otod.component.util.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class TcpServerImpl implements TcpServer, Runnable {

    private String serverIp = null;		// �����IP��ַ���粻ָ������Ϊȫ��Ip��ַ
    private int serverPort;				// ����Ķ˿ں�
    private int bufferSize = 4 * 1024;	// ������ݵĻ������С
    private TcpServerDataHandler dataHandler = null;
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;
    private boolean bStart = false;		// Server �Ƿ��Ѿ�����
    private Thread recvThread = null;	// �������ӡ�����շ����߳�
    private boolean bCanRun = true;		// �����̵߳ı�־
    private int nextConnectId = 1;		// ��ǰ��������
    private Map<Integer, ConnectInfo> connectMap = new Hashtable<Integer, ConnectInfo>();

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setDataHandler(TcpServerDataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public TcpServerDataHandler getDataHandler() {
        return dataHandler;
    }

    //  Server �Ƿ��Ѿ�����
    public boolean isStart() {
        return bStart;
    }

    // ��ѯ��ǰ�Ŀͻ���������
    public int getConnectCount() {
        return connectMap.size();
    }

    // ����������
    public void start() {
        try {
            InetSocketAddress socketAddress = null;
            if (serverIp != null && serverIp.length() > 0) {
                socketAddress = new InetSocketAddress(serverIp, serverPort);
            } else {
                socketAddress = new InetSocketAddress(serverPort);
            }
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(socketAddress);
            serverSocketChannel.configureBlocking(false);	// ��Ϊ������

            // ע�� accept �¼�
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // ������ݴ����߳�
            bCanRun = true;
            recvThread = new Thread(this);
            recvThread.start();

            bStart = true;
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    // ֹͣ������
    public void stop() {
        bStart = false;
        bCanRun = false;

        // ֹͣ�����߳�
        if (recvThread != null) {
            ThreadUtil.stop(recvThread, 5000);
            recvThread = null;
        }

        // �ر�Selector
        if (selector != null) {
            try {
                selector.close();
            } catch (Exception e) {
            }
            selector = null;
        }

        // �ر�ServerSocketChannel
        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
            } catch (Exception e) {
            }
            serverSocketChannel = null;
        }

        // ���ͻ�����Ϣ
        connectMap.clear();
    }

    //  ��ͻ��˷���Ϣ
    @Override
    public synchronized void send(int connectId, byte[] bytes) {
        send(connectId, bytes, 0, bytes.length);
    }

    @Override
    public synchronized void send(int connectId, byte[] bytes, int offset, int len) {
        if (!bStart) {
            throw new GeneralException("server is not started!");
        }

        ConnectInfo ci = (ConnectInfo) connectMap.get(connectId);
        if (ci == null) {
            throw new GeneralException("connectId " + connectId + " is invalid!");
        }

        ci.addSendData(bytes, offset, len);
        // �Զ�д�����м��
        ci.key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        selector.wakeup();
    }

    // �Ͽ�ָ���Ŀͻ���
    public void disconnect(int connectId) {
        ConnectInfo ci = (ConnectInfo) connectMap.get(connectId);
        if (ci != null) {
            removeConnection(ci);
        }
    }

    public void run() {
        while (bCanRun) {
            try {
                int iCount = selectKeys();
                if (iCount > 0) {
                    Thread.yield();	// ����һֱռ��CPU
                }
            } catch (Exception e) {
            }
        }
    }

    private int selectKeys() throws Exception {
        int iCount = selector.select(500);
        if (iCount < 1) {
            return iCount;
        }

        Iterator it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = (SelectionKey) it.next();
            it.remove();

            if (key.isValid() == false) {
                continue;
            }

            if (key.isAcceptable()) {
                try {
                    accept(key);
                } catch (Exception e) {
                }
            } else {
                try {
                    // �����ӿɶ���ʱ����Ӧ
                    if (key.isReadable()) {
                        recvData(key);
                    }
                    // �����ӿ�д��ʱ����Ӧ
                    if (key.isWritable()) {
                        sendData(key);
                    }
                } catch (CancelledKeyException ex) {
                    removeConnection((ConnectInfo) key.attachment());
                }
            }
        }

        return iCount;
    }

    private void accept(SelectionKey key) throws Exception {
        // ���ܿͻ��˵����ӣ���ȡ�ͻ��˵�Ip��Port
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = server.accept();
        socketChannel.configureBlocking(false);
        Socket socket = socketChannel.socket();
        String ip = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();

        // �������Ӵ��connectMap��
        int connectId = nextConnectId++;
        ConnectInfo ci = new ConnectInfo(ip, port, socketChannel, connectId, bufferSize);
        SelectionKey sk = socketChannel.register(selector, SelectionKey.OP_READ, ci);
        ci.setKey(sk);

        connectMap.put(new Integer(connectId), ci);

        dataHandler.onConnect(connectId, ip, port);
    }

    private void recvData(SelectionKey key) {
        ConnectInfo ci = (ConnectInfo) key.attachment();
        boolean broken = false;
        int iRead = 0;
        while (true) {
            int iCount = ci.read();		// ��ȡ���
            if (iCount == -1) {
                broken = true;	// ���ӶϿ�
                break;
            } else if (iCount == 0) {	// û�����
                break;
            } else {
                iRead += iCount;
            }
        }

        if (!broken) {	// ������
            if (iRead < 1) {	// δ�յ����
                return;
            }

            // ���а�߽�ʶ��
            ByteBuffer buffer = ci.recvBuffer;
            int dataLen = buffer.position();
            buffer.position(0);
            byte[] data = new byte[dataLen];
            buffer.get(data);
            int curDataLen = dataLen;
            while (true) {
                int iLen = dataHandler.slice(ci.connectId, data, curDataLen);
                if (iLen > 0) {	// ���������ݰ�
                    dataHandler.onReceiveMsg(ci.connectId, data, iLen);

                    curDataLen -= iLen;
                    if (curDataLen > 0) {	// ����ʣ�����
                        System.arraycopy(data, iLen, data, 0, curDataLen);
                    } else {
                        break;
                    }
                } else {	// ����һ���������ݰ�
                    break;
                }
            }

            if (curDataLen < dataLen) {	// ����������
                buffer.position(dataLen - curDataLen);
                buffer.limit(dataLen);
                buffer.compact();
            }
        } else {		// �����Ѿ��Ͽ�
            removeConnection(ci);
        }
    }

    private void sendData(SelectionKey key) throws Exception {
        ConnectInfo ci = (ConnectInfo) key.attachment();
        List sendList = ci.sendList;
        synchronized (sendList) {
            while (!sendList.isEmpty()) {
                byte[] bytes = (byte[]) sendList.remove(0);
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    ci.socketChannel.write(buffer);
                }

                dataHandler.onSendMsg(ci.connectId, bytes, bytes.length);
            }
        }

        key.interestOps(SelectionKey.OP_READ);	// �ָ���
    }

    private void removeConnection(ConnectInfo ci) {
        ci.close();
        synchronized (connectMap) {
            connectMap.remove(ci.connectId);
        }

        dataHandler.onDisconnect(ci.connectId, ci.ip, ci.port);
    }
}
