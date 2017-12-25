package com.lt.adapter.adapter.feedback.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.feedback.api.FeedbackRpcApi;
import com.lt.feedback.exception.FeedbackErrCode;
import com.lt.feedback.exception.FeedbackException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 已回复未看反馈意见
 */
@Component
public class ReplyYesFeedbackFunc extends BaseFunction {

    @Autowired
    private FeedbackRpcApi feedbackRpcApi;

    @Override
    public Response response(Map<String, Object> paraMap) {
        String userId = StringTools.formatStr(paraMap.get("userId"), "");
        if (StringTools.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }
        Integer num = feedbackRpcApi.countReplyYesFeedback(userId);
        Map<String, Integer> dataMap = new HashMap<>();
        dataMap.put("replyNum", num);
        return new Response(LTResponseCode.SUCCESS, "操作成功", dataMap);
    }
}
