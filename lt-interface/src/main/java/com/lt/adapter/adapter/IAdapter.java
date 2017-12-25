package com.lt.adapter.adapter;

import com.lt.util.utils.model.Response;

import java.util.Map;

/**
 * 作者: 邓玉明
 * 时间: 2017/2/24 上午11:31
 * email:cndym@163.com
 */
public interface IAdapter {

    Response execute(Map<String, Object> paraMap);

}
