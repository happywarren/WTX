/*
 * Copyright (c) 2015 www.caniu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * luckin Group. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.util.utils.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 *
 * 描述:
 *
 * @author  boyce
 * @created 2015年7月23日 下午4:42:19
 * @since   v1.0.0
 */
public class LuckinDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (true) {
			if (in.readableBytes() <= 4) {
				break;
			}
			in.markReaderIndex();
			int length = in.readInt();
			if(length<=0){
				throw new Exception("a negative length occurd while decode!");
			}
			if (in.readableBytes() < length) {
				in.resetReaderIndex();
				break;
			}
			byte[] msg = new byte[length];
			in.readBytes(msg);
			out.add(new String(msg, "UTF-8"));
		}

	}
	public static void main(String[] args) {
		System.out.println((byte)255);
	}

}
