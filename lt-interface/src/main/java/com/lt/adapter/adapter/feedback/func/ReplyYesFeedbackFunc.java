package com.lt.adapter.adapter.feedback.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 已回复未看反馈意见
 */
@Component
public class ReplyYesFeedbackFunc extends BaseFunction {


    @Override
    public Response response(Map<String, Object> paraMap) {
        return new Response(LTResponseCode.SUCCESS, "操作成功", "");
    }
}
