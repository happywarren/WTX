package com.lt.adapter.adapter;

import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

import java.util.Map;

/**
 * 作者: 邓玉明
 * 时间: 2017/2/24 上午11:33
 * email:cndym@163.com
 */
public class BaseFunction implements IFunction {

    @Override
    public Response response(Map<String, Object> paraMap){
        return LTResponseCode.getCode(LTResponseCode.ER400);
    }

}
