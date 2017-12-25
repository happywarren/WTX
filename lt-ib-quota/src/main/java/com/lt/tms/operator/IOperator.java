package com.lt.tms.operator;


import com.lt.tms.quota.ib.api.bean.ContractBean;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface IOperator {

    void operator(ChannelHandlerContext ctx, String data);

}
