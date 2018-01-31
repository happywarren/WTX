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
package com.otod.component.tcp.server;

public interface TcpServer {

    public String getServerIp();

    public void setServerIp(String serverIp);

    public int getServerPort();

    public void setServerPort(int serverPort);

    public int getBufferSize();

    public void setBufferSize(int bufferSize);

    public void setDataHandler(TcpServerDataHandler dataHandler);

    public TcpServerDataHandler getDataHandler();

    // ����������
    public void start();

    // ֹͣ������
    public void stop();

    // ��ͻ��˷�����Ϣ
    public  void send(int connectId, byte[] bytes);

    // ��ͻ��˷�����Ϣ
    public void send(int connectId, byte[] bytes, int offset, int len);

    // �Ͽ�ָ���Ŀͻ���
    public void disconnect(int connectId);

    // Server �Ƿ��Ѿ�����
    public boolean isStart();

    // ��ѯ��ǰ�Ŀͻ���������
    public int getConnectCount();
}
