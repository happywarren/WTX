package com.lt.adapter.adapter;

import com.lt.util.utils.model.Response;

import java.util.Map;

/**
 * 作者: 邓玉明
 * 时间: 2017/2/24 上午11:33
 * email:cndym@163.com
 */
public interface IFunction {
    Response response(Map<String, Object> paraMap);
}
