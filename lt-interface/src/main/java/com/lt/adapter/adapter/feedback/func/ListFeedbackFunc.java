package com.lt.adapter.adapter.feedback.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 反馈意见列表
 */
@Component
public class ListFeedbackFunc extends BaseFunction {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Response response(Map<String, Object> paraMap) {
        return new Response(LTResponseCode.SUCCESS, "操作成功", "");
    }
}
