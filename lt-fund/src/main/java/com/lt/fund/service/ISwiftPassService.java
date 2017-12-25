package com.lt.fund.service;

import java.util.Map;

/**
 * Created by guodawang on 2017/9/4.
 */
public interface ISwiftPassService {

    /**
     * 威富通回调处理
     * @param map
     * @return
     */
    public String swiftPassResult(Map<String, String> map);
}
