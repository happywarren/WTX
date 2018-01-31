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

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class ConnectInfo {
	String ip;		// �ͻ���Ip
	int port;		// �ͻ���Port
	SocketChannel socketChannel;
	int connectId;
	SelectionKey key;
	ByteBuffer recvBuffer;
	LinkedList sendList;
	
	ConnectInfo(String ip, int port, SocketChannel socketChannel, 
			int connectId, int bufferSize) {
		this.ip = ip;
		this.port = port;
		this.socketChannel = socketChannel;
		this.connectId = connectId;
		recvBuffer = ByteBuffer.allocateDirect(bufferSize);
		sendList = new LinkedList();		
	}
	
	void setKey(SelectionKey key) {
		this.key = key;
	}
	
	ByteBuffer getRecvBuffer() {
		return recvBuffer;
	}
	
	int read() {
		try {
			return socketChannel.read(recvBuffer);
		} catch (Exception e) {
			return -1;	// ��ʾ���ӶϿ�
		}
	}
	
	void close() {
		key.cancel();
		key = null;
		try {
			socketChannel.close();
		} catch (Exception e) {}
		socketChannel = null;
		recvBuffer = null;
		sendList = null;
	}

	void addSendData(byte[] bytes, int offset, int len) {
		byte[] data = new byte[len];
		System.arraycopy(bytes, offset, data, 0, len);
		synchronized(sendList) {
			sendList.add(data);
		}
		
	}
}
