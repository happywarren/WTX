package com.lt.quota.core.quota.third.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

@ChannelHandler.Sharable
public class GzipDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws UnsupportedEncodingException {
        /*
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        String str = new String(data,"utf-8");
        System.out.println(str);*/

        /*
        int dwZipSize1 =  byteBuf.order(ByteOrder.LITTLE_ENDIAN).readInt();
        if(x> 16777216 || x < 0){
            String str = byteBuf.toString(Charset.forName("UTF-8"));
            System.out.println("字符串");
        }else{
            while(byteBuf.readableBytes()>0){
                int  byteBuf.order(ByteOrder.LITTLE_ENDIAN).readInt();
            }

        //    list.add(new String(output,"UTF-8"));
        }*/

        byteBuf.markReaderIndex();
        int dwZipSize1 =  byteBuf.order(ByteOrder.LITTLE_ENDIAN).readInt();

        if(dwZipSize1 > 16777216 || 16777216 < 0){
            byteBuf.resetReaderIndex();
            String str = byteBuf.toString(Charset.forName("UTF-8"));
        }else{
            byteBuf.resetReaderIndex();
            while(byteBuf.isReadable()){
                int dwZipSize =  byteBuf.order(ByteOrder.LITTLE_ENDIAN).readInt();
                byteBuf.readInt();
                byte[] buff = new byte[dwZipSize-4];
                byteBuf.readBytes(buff);
                byte[] output = decompress(buff);
                list.add(output);
            }
        }



    }

    private byte[] decompress(byte[] data){
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        byte[] output = new byte[0];
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        decompresser.end();
        return output;

    }
}
