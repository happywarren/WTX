package com.lt.feedback.service.impl;

import com.lt.feedback.constant.FeedbackConstants;
import com.lt.feedback.dao.jpa.FeedbackDao;
import com.lt.feedback.model.Feedback;
import com.lt.feedback.service.FeedbackService;
import com.lt.feedback.utils.DateUtils;
import com.lt.feedback.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFeedback(Feedback feedback) {
        feedback.setFeedbackNum(1);
        feedback.setStatus(FeedbackConstants.FEEDBACK_STATUS_SHOW);
        feedback.setReplyStatus(FeedbackConstants.FEEDBACK_REPLY_NO);
        feedback.setCreateTime(new Date());
        logger.info("saveFeedback time: {} ", DateUtils.formatDate2Str(feedback.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
        String parentId = feedback.getParentId();
        feedbackDao.save(feedback);
        if (Utils.isNotEmpty(parentId)){
            Feedback parentFeedback = feedbackDao.getFeedbackByFeedbackId(parentId);
            parentFeedback.setFeedbackNum(parentFeedback.getFeedbackNum() + 1);
            feedbackDao.save(parentFeedback);
        }
    }

    @Override
    public Page<Feedback> findFeedbackList(String userId, Integer currentPage, Integer pageSize) {
        int page = currentPage - 1 < 0 ? 0 : currentPage - 1;
        Pageable pageable = new PageRequest(page, pageSize);
        return feedbackDao.findFeedbackList(userId, pageable);
    }

    @Override
    public Page<Feedback> findFeedbackList(String userId, String feedbackId, Integer currentPage, Integer pageSize) {
        int page = currentPage - 1 < 0 ? 0 : currentPage - 1;
        Pageable pageable = new PageRequest(page, pageSize);
        return feedbackDao.findFeedbackList(userId, feedbackId, pageable);
    }

    @Override
    public Integer countReplyYesFeedback(String userId) {
        return feedbackDao.countReplyYesFeedback(userId);
    }

    @Override
    public Map<String, Integer> findReplyYesParentFeedBack(String userId) {
        List<Object> dataList = feedbackDao.findReplyYesParentFeedBack(userId);
        Map<String, Integer> feedbackMap = new HashMap<String, Integer>();
        for (Object object : dataList) {
            Object[] array = (Object[])object;
            String feedbackId = (String) array[0];
            BigInteger replyNum = (BigInteger) array[1];
            feedbackMap.put(feedbackId,replyNum.intValue());
        }
        return feedbackMap;
    }

    @Override
    @Transactional
    public void updateFeedbackSeeReply(String userId, String feedbackId) {
        feedbackDao.updateFeedbackSeeReply(userId, feedbackId);
    }
}
