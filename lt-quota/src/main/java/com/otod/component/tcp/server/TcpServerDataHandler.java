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

public interface TcpServerDataHandler {
	/**
	 * Socket��������֪ͨӦ�ò㣬�пͻ�������������
	 * @param connectId ����Ψһ��ʶ
	 * @param ip �ͻ��˵�Ip
	 * @param port �ͻ��˵Ķ˿ں�
	 */
	public void onConnect(int connectId, String ip, int port);

	/**
	 * Socket��������֪ͨӦ�ò㣬�пͻ��˶���
	 * @param connectId ����Ψһ��ʶ
	 * @param ip �ͻ��˵�Ip
	 * @param port �ͻ��˵Ķ˿ں�
	 */
	public void onDisconnect(int connectId, String ip, int port);

	/**
	 * Socket�����������յ���ݺ󣬵ݽ�Ӧ�ò���а�߽�ʶ��
	 * @param connectId ����Ψһ��ʶ
	 * @param bytes Socket�����������յ���������ݣ����ܲ���һ���������ݰ�Ҳ���ܰ�����ݰ�
	 * @param byteCount bytes�����е���Ч�ֽ���
	 * @return ���Ӧ�ò���ʶ��bytes�����ٰ�һ��������ݰ��򷵻ص�һ����ݰ�ĳ��ȡ�
	 * Socket������ݷ��صĳ��ȣ�����һ���г�����Ȼ��ص�onReceiveMsg������
	 *         �����һ���������ݰ�Ӧ�ò���뷵��-1
	 */ 
	public int slice(int connectId, byte[] bytes, int byteCount);

	/**
	 * Socket����ص��˷�����֪ͨӦ�ò��յ���һ���������ݰ�
	 * @param connectId ����Ψһ��ʶ
	 * @param bytes �����ݵĻ����� 
	 * @param byteCount һ��������ݰ�ĳ���
	 * Socket���ͨ�����slice�������ж��Ƿ����������ݰ����У���ص�onReceiveMsg��
	 */
	public void onReceiveMsg(int connectId, byte[] bytes, int byteCount);

	/**
	 * Socket���֪ͨӦ�ò㣬��Ϣ�Ѿ��ɹ�����
	 * @param connectId ����Ψһ��ʶ
	 * @param bytes ��ݻ�����
	 * @param byteCount �ɹ����͵�һ��������ݰ��
	 */
	public void onSendMsg(int connectId, byte[] bytes, int byteCount);

}
