/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.test.timer;

import com.otod.bean.ServerContext;
import com.otod.component.util.GeneralException;
import com.otod.test.QuoteClient;

import com.otod.util.mutithread.WorkHandle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Administrator
 */
public class HeartbeatTimer extends WorkHandle {

    public HeartbeatTimer() {
    }

    @Override
    public void doWork() {

        QuoteClient quoteClient = ServerContext.getQuoteClient();
        if (quoteClient != null) {
            if (quoteClient.isLoginFlag()) {
                ByteBuffer buffer = ByteBuffer.allocate(8);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(8);
                buffer.putInt(10);
                buffer.flip();
                byte[] datas = new byte[buffer.limit()];
                buffer.get(datas);
                try {
                    if (quoteClient.getTcpClient().isConnected()) {
                        quoteClient.getTcpClient().send(datas);
                    } else {
                        System.out.println("心跳监测 socket关闭 重连");
                        quoteClient.closeClient();
                        quoteClient.connect();
                    }
                } catch (GeneralException ex) {
                    System.out.println("心跳监测 发送心跳不正常 重连");
                    quoteClient.closeClient();
                    quoteClient.connect();
                }
            }
        }
    }
}
