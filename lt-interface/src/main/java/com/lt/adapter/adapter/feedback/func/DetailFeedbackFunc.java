package com.lt.adapter.adapter.feedback.func;

import com.alibaba.fastjson.JSON;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.feedback.api.FeedbackRpcApi;
import com.lt.feedback.dto.FeedbackDetailDTO;
import com.lt.feedback.dto.Pagination;
import com.lt.feedback.exception.FeedbackErrCode;
import com.lt.feedback.exception.FeedbackException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 反馈意见详情
 */
@Component
public class DetailFeedbackFunc extends BaseFunction {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackRpcApi feedbackRpcApi;

    @Override
    public Response response(Map<String, Object> paraMap) {
        String userId = StringTools.formatStr(paraMap.get("userId"), "");
        String feedbackId = StringTools.formatStr(paraMap.get("feedbackId"), "");
        int page = StringTools.formatInt(paraMap.get("page"), 1);
        int rows = StringTools.formatInt(paraMap.get("rows"), 10);
        if (StringTools.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }
        if (StringTools.isEmpty(feedbackId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00003);
        }
        Pagination<FeedbackDetailDTO> pagination = feedbackRpcApi.detailFeedback(userId, feedbackId, page, rows);
        List<FeedbackDetailDTO> dataList = pagination.getDataList();
        logger.info("userId {} feedbackId {} data {} ", userId, feedbackId, JSON.toJSONString(dataList));
        return new Response(LTResponseCode.SUCCESS, "操作成功", dataList);
    }
}