package com.lt.trade.netty;

/**
 * Created by lvx on 2017/10/17.
 */
public interface OnClientStartupListener {

    void onCompletion(boolean result, BaseClient baseClient);

}
