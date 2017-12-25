package com.lt.tms.operator;

import com.lt.tms.comm.exception.TmsErrCode;
import com.lt.tms.quota.bean.ErrorBean;
import io.netty.channel.ChannelHandlerContext;


public class BaseOperator implements IOperator {

    @Override
    public void operator(ChannelHandlerContext ctx, String data) {

    }

    protected ErrorBean errorBean(String errorType, String errorCode) {
        ErrorBean errorBean = new ErrorBean();
        errorBean.setErrorType(errorType);
        errorBean.setErrorCode(errorCode);
        errorBean.setErrorMessage(TmsErrCode.getMsg(errorCode));
        return errorBean;
    }
}
