package com.lt.feedback.api.impl;

import com.lt.feedback.api.FeedbackRpcApi;
import com.lt.feedback.constant.FeedbackConstants;
import com.lt.feedback.dto.FeedbackDetailDTO;
import com.lt.feedback.dto.FeedbackListDTO;
import com.lt.feedback.dto.FeedbackSubmitDTO;
import com.lt.feedback.dto.Pagination;
import com.lt.feedback.exception.FeedbackErrCode;
import com.lt.feedback.exception.FeedbackException;
import com.lt.feedback.model.Feedback;
import com.lt.feedback.service.FeedbackService;
import com.lt.feedback.utils.DateUtils;
import com.lt.feedback.utils.OrderIdBuildUtils;
import com.lt.feedback.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(version = "1.0.0")
public class FeedbackRpcApiImpl implements FeedbackRpcApi {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackService feedbackService;

    @Override
    public void submitFeedback(FeedbackSubmitDTO feedbackSubmitDTO) {
        String userId = feedbackSubmitDTO.getUserId();
        if (Utils.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }
        String content = feedbackSubmitDTO.getContent();
        if (Utils.isEmpty(content)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00002);
        }
        if (content.length() > 1000) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00004);
        }
        String imageUrl = feedbackSubmitDTO.getImageUrl();
        if (Utils.isNotEmpty(imageUrl)) {
            if (imageUrl.split(",").length > 9) {
                throw new FeedbackException(FeedbackErrCode.ERR_FB00005);
            }
        }
        Feedback feedback = new Feedback();
        feedback.setParentId(feedbackSubmitDTO.getParentId());
        feedback.setUserId(userId);
        feedback.setFeedbackId(OrderIdBuildUtils.buildSubjectId());
        feedback.setContent(content);
        feedback.setImageUrl(feedbackSubmitDTO.getImageUrl());
        feedbackService.saveFeedback(feedback);
    }

    @Override
    public Pagination<FeedbackListDTO> feedbackList(String userId, Integer currentPage, Integer pageSize) {
        logger.info("feedbackList userId {} currentPage {} pageSize {} ", userId, currentPage, pageSize);
        if (Utils.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }
        Map<String, Integer> feedbackMap = feedbackService.findReplyYesParentFeedBack(userId);
        Page<Feedback> subjectPage = feedbackService.findFeedbackList(userId, currentPage, pageSize);
        Integer totalSize = (int) subjectPage.getTotalElements();
        if (null == totalSize) {
            totalSize = 0;
        }
        List<Feedback> feedbackList = subjectPage.getContent();
        List<FeedbackListDTO> feedbackDTOList = new ArrayList<FeedbackListDTO>();
        for (Feedback feedback : feedbackList) {
            FeedbackListDTO feedbackListDTO = new FeedbackListDTO();
            feedbackListDTO.setUserId(feedback.getUserId());
            String feedbackId = feedback.getFeedbackId();
            feedbackListDTO.setFeedbackId(feedbackId);
            feedbackListDTO.setContent(feedback.getContent());
            feedbackListDTO.setReplyStatus(feedback.getReplyStatus());
            if (feedbackMap.containsKey(feedbackId)) {
                feedbackListDTO.setReplyStatus(FeedbackConstants.FEEDBACK_REPLY_YES);
            }
            feedbackListDTO.setCreateTime(DateUtils.formatDate2Str(feedback.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            feedbackDTOList.add(feedbackListDTO);
        }
        logger.info("feedbackList totalSize {} currentPage {} pageSize {} ", totalSize, currentPage, pageSize);
        Pagination<FeedbackListDTO> pagination = new Pagination<FeedbackListDTO>(currentPage, totalSize, pageSize);
        pagination.setDataList(feedbackDTOList);
        return pagination;
    }

    @Override
    public Integer countReplyYesFeedback(String userId) {
        if (Utils.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }
        return feedbackService.countReplyYesFeedback(userId);
    }

    @Override
    public Pagination<FeedbackDetailDTO> detailFeedback(String userId, String feedbackId, Integer currentPage, Integer pageSize) {
        if (Utils.isEmpty(userId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00001);
        }
        if (Utils.isEmpty(feedbackId)) {
            throw new FeedbackException(FeedbackErrCode.ERR_FB00003);
        }
        try {
            feedbackService.updateFeedbackSeeReply(userId, feedbackId);
        } catch (Exception e) {

        }

        Page<Feedback> subjectPage = feedbackService.findFeedbackList(userId, feedbackId, currentPage, pageSize);
        Integer totalSize = (int) subjectPage.getTotalElements();
        if (null == totalSize) {
            totalSize = 0;
        }
        List<Feedback> feedbackList = subjectPage.getContent();
        List<FeedbackDetailDTO> feedbackDTOList = new ArrayList<FeedbackDetailDTO>();
        for (Feedback feedback : feedbackList) {
            FeedbackDetailDTO feedbackDetailDTO = new FeedbackDetailDTO();
            feedbackDetailDTO.setFeedbackId(feedback.getFeedbackId());
            feedbackDetailDTO.setContent(feedback.getContent());
            feedbackDetailDTO.setReplyContent(feedback.getReplyContent());
            String imageUrl = feedback.getImageUrl();
            feedbackDetailDTO.setImageUrl(new ArrayList<String>());
            if (Utils.isNotEmpty(imageUrl)) {
                List<String> imageUrlList = new ArrayList<String>();
                for (String url : imageUrl.split(",")) {
                    imageUrlList.add(url);
                }
                feedbackDetailDTO.setImageUrl(imageUrlList);
            }
            feedbackDetailDTO.setReplyStatus(feedback.getReplyStatus());
            feedbackDetailDTO.setCreateTime(DateUtils.formatDate2Str(feedback.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            feedbackDetailDTO.setReplyTime(DateUtils.formatDate2Str(feedback.getReplyTime(), "yyyy-MM-dd HH:mm:ss"));
            feedbackDTOList.add(feedbackDetailDTO);
        }
        Pagination<FeedbackDetailDTO> pagination = new Pagination<FeedbackDetailDTO>(currentPage, totalSize, pageSize);
        pagination.setDataList(feedbackDTOList);
        return pagination;
    }
}
