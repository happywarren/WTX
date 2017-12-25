package com.lt.adapter.adapter.feedback.func;

import com.alibaba.fastjson.JSON;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.feedback.api.FeedbackRpcApi;
import com.lt.feedback.dto.FeedbackSubmitDTO;
import com.lt.feedback.exception.FeedbackErrCode;
import com.lt.feedback.exception.FeedbackException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 提交反馈意见
 */
@Component
public class SubmitFeedbackFunc extends BaseFunction {

    @Autowired
    private FeedbackRpcApi feedbackRpcApi;

    @Override
    public Response response(Map<String, Object> paraMap) {
        String userId = StringTools.formatStr(paraMap.get("userId"), "");
        String feedbackId = StringTools.formatStr(paraMap.get("feedbackId"), "");
        String content = StringTools.formatStr(paraMap.get("content"), "");
        String imageUrl = StringTools.formatStr(paraMap.get("imageUrl"), "");

        if (StringTools.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }

        FeedbackSubmitDTO feedbackSubmitDTO = new FeedbackSubmitDTO();
        feedbackSubmitDTO.setUserId(userId);
        feedbackSubmitDTO.setParentId(feedbackId);
        feedbackSubmitDTO.setContent(content);
        feedbackSubmitDTO.setImageUrl(imageUrl);
        StringBuilder stringBuilder = new StringBuilder();
        if (StringTools.isNotEmpty(imageUrl)) {
            try {
                Map<String, Object> imageMap = JSON.parseObject(imageUrl, Map.class);
                for (Map.Entry<String, Object> entry : imageMap.entrySet()) {
                    stringBuilder.append(",").append(entry.getValue());
                }
            } catch (Exception e) {
            }
        }
        if (StringTools.isNotEmpty(stringBuilder)) {
            feedbackSubmitDTO.setImageUrl(stringBuilder.substring(1));
        }
        if (feedbackSubmitDTO.getImageUrl().equals("{}")) {
            feedbackSubmitDTO.setImageUrl("");
        }
        feedbackRpcApi.submitFeedback(feedbackSubmitDTO);

        return new Response(LTResponseCode.SUCCESS, "操作成功");
    }
}
