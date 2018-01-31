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

package com.otod.component.tcp.client;

public interface TcpClient {
	public String getServerIp();
	public void setServerIp(String serverIp);
	
	public int getServerPort();
	public void setServerPort(int serverPort);
	
	public void setAutoReconnect(boolean autoReconnect);
	public boolean isAutoReconnect();
	
	public int getReconnectInterval();
	public void setReconnectInterval(int reconnectInterval);
	
	public int getBufferSize();
	public void setBufferSize(int bufferSize);
	
	
	public TcpClientDataHandler getDataHandler();
	public void setDataHandler(TcpClientDataHandler tcpClientDataHandler);

	// ���ӷ�����
    public void connect();

    // ��������������
    public void send(byte[] bytes);
    
    // ��������������
    public void send(byte[] bytes, int offset, int len);

    // �Ͽ��ͷ�����������
    public void disconnect();
    
    // ��ѯ��ǰ������״̬
    public boolean isConnected();
}
