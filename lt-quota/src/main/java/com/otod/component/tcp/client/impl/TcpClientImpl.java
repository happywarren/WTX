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
package com.otod.component.tcp.client.impl;

import com.otod.component.tcp.client.*;
import com.otod.component.util.*;
import java.net.*;
import java.io.*;

public class TcpClientImpl implements TcpClient, Runnable {

    private String serverIp;
    private int serverPort;
    private boolean autoReconnect = false;
    private int reconnectInterval = 5000;
    private int bufferSize = 2 * 1024 * 1024;
    private TcpClientDataHandler dataHandler;
    private boolean bConnected = false;	// ��ǰ�Ƿ�����
    private Socket socket = null;		// ����
    private InputStream inputStream;	// ������ݵ���
    private OutputStream outputStream;	// ������ݵ���
    private Thread recvThread;			// ������ݵ��߳�
    private boolean bCanRun = true;		// ���ƽ����߳��˳��ı���
    private byte[] recvBuffer;			// ������ݵĻ�����
    private int recvCount = 0;			// ��ǰ���յ�����ݣ��ֽ���

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

    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    public int getReconnectInterval() {
        return reconnectInterval;
    }

    public void setReconnectInterval(int reconnectInterval) {
        this.reconnectInterval = reconnectInterval;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public TcpClientDataHandler getDataHandler() {
        return dataHandler;
    }

    public void setDataHandler(TcpClientDataHandler tcpClientDataHandler) {
        this.dataHandler = tcpClientDataHandler;
    }

    //	 ��ѯ��ǰ������״̬
    public boolean isConnected() {
        return bConnected;
    }

    // ���ӷ�����
    public void connect() {
        try {
            // 1. ��ʼ��Socket
            if (socket == null) {
                socket = new Socket();
                socket.setSoTimeout(3000);	// ���ó�ʱʱ��
                socket.setSendBufferSize(bufferSize);
                socket.setReceiveBufferSize(bufferSize);

                recvBuffer = new byte[bufferSize];	// ������ݵĻ�����
                recvCount = 0;
            }

            // 2. ��������
            try {
                InetSocketAddress address = new InetSocketAddress(serverIp, serverPort);
                socket.connect(address);


                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                bConnected = true;

                // 3. ֪ͨӦ���ϲ㣬�����Ѿ��ɹ�����
                if (dataHandler != null) {
                    dataHandler.onConnect(serverIp, serverPort);
                }
            } finally {
                // 4. ��������/�����߳�
                if ((bConnected || autoReconnect) && (recvThread == null)) {
                    bCanRun = true;
                    recvThread = new Thread(this);
                    recvThread.start();
                }
            }
        } catch (Exception e) {
            throw new GeneralException(e);
        }
    }

    // ��������������
    public void send(byte[] bytes) {
        send(bytes, 0, bytes.length);
    }

    public void send(byte[] bytes, int offset, int len) {
        if (bConnected) {
            try {
                synchronized (outputStream) {
                    outputStream.write(bytes, offset, len);
                }

                if (dataHandler != null) {
                    byte[] data = bytes;
                    if (len != bytes.length) {
                        data = new byte[len];
                        System.arraycopy(bytes, offset, data, 0, len);
                    }
                    dataHandler.onSendMsg(data, len);
                }
            } catch (Exception e) {
                //bConnected = false;		// �˴������ö�����־�������ļ��ȫ���ŵ��߳��н���
                throw new GeneralException(e);
            }
        } else {
            throw new GeneralException("connection is broken!");
        }
    }

    // �Ͽ��ͷ�����������
    public void disconnect() {
        bCanRun = false;
        bConnected = false;	// ��ֹӦ���ٷ������

        // ֹͣ�����߳�
        if (recvThread != null) {
            ThreadUtil.stop(recvThread, 5000);
            recvThread = null;
        }

        // �ر�����
        if (socket != null) {
            closeSocket();

            // ֪ͨӦ�ã������Ѿ��Ͽ�
            if (dataHandler != null) {
                dataHandler.onDisconnect();
            }
        }
    }

    private void closeSocket() {
        if (socket != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
            try {
                outputStream.close();
            } catch (Exception e) {
            }
            try {
                socket.close();
            } catch (Exception e) {
            }
            socket = null;
            inputStream = null;
            outputStream = null;
        }
    }

    public void run() {
        while (bCanRun) {
            if (bConnected) {	// ������ʱ����ȡ���
                try {
                    int iLen = bufferSize - recvCount;
                    int iCount = inputStream.read(recvBuffer, recvCount, iLen);
                    if (iCount < 1) {	// �����ر�
                        bConnected = false;
                    } else {
                        recvCount += iCount;
                        while (true) {
                            iCount = dataHandler.slice(recvBuffer, recvCount);
                            if (iCount > 0) {	// ��һ���������ݰ�
                                if (dataHandler != null) {
                                    dataHandler.onReceiveMsg(recvBuffer, iCount);
                                }

                                recvCount -= iCount;	// ʣ���ֽ���
                                if (recvCount > 0) {
                                    System.arraycopy(recvBuffer, iCount, recvBuffer, 0, recvCount);
                                } else {				// ������û�������
                                    break;
                                }
                            } else {	// ����һ���������ݰ�
                                break;
                            }
                        }
                    }
                } catch (InterruptedIOException e) {
                    // ��ʱû����ݵ���
                } catch (IOException e) {
                    // �����쳣�����ӶϿ�
                    bConnected = false;
                }

                if (bConnected == false) {	// �������
                    if (bCanRun && dataHandler != null) {
                        dataHandler.onDisconnect();
                    }
                }
            } else {	// �Ͽ�ʱ�������Զ�����
                if (!autoReconnect) {	// ���Զ����������˳�
                    break;
                }

                // ����
                boolean bSleep = ThreadUtil.sleep(reconnectInterval);

                // ��������
                if (bSleep) {
                    try {
                        connect();
                    } catch (Exception e) {
                    }
                }
            }
        }

    }
}
