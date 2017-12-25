package com.lt.trade.tradeserver.bean;

/**
 * Created by sunch on 2016/12/11.
 */
public class FutureMatchWrapper {

    private int messageId;
    private BaseMatchBean baseMatchBean;

    public FutureMatchWrapper() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public BaseMatchBean getBaseMatchBean() {
        return baseMatchBean;
    }

    public void setBaseMatchBean(BaseMatchBean baseMatchBean) {
        this.baseMatchBean = baseMatchBean;
    }

}
