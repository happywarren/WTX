package com.lt.trade.netty;

/**
 * 发送状态监听
 *
 * Created by sunch on 2016/11/10.
 */
public interface ResultListener<T> {

    void onFailure(T result);

    void onSuccess(T result);
}
