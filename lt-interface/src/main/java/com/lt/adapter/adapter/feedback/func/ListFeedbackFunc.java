package com.lt.adapter.adapter.feedback.func;

import com.alibaba.fastjson.JSON;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.feedback.api.FeedbackRpcApi;
import com.lt.feedback.dto.FeedbackListDTO;
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
 * 反馈意见列表
 */
@Component
public class ListFeedbackFunc extends BaseFunction {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackRpcApi feedbackRpcApi;

    @Override
    public Response response(Map<String, Object> paraMap) {
        String userId = StringTools.formatStr(paraMap.get("userId"), "");
        int page = StringTools.formatInt(paraMap.get("page"), 1);
        int rows = StringTools.formatInt(paraMap.get("rows"), 10);
        if (StringTools.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }
        Pagination<FeedbackListDTO> pagination = feedbackRpcApi.feedbackList(userId, page, rows);
        List<FeedbackListDTO> dataList = pagination.getDataList();
        logger.info("userId {} data {} ", userId, JSON.toJSONString(dataList));
        return new Response(LTResponseCode.SUCCESS, "操作成功", dataList);
    }
}
