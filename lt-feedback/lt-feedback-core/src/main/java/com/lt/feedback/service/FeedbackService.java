package com.lt.feedback.service;


import com.lt.feedback.model.Feedback;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface FeedbackService {

    /**
     * 提交意见反馈
     *
     * @param feedback
     */
    void saveFeedback(Feedback feedback);

    /**
     * 分页查询意见反馈
     *
     * @param userId      用户id
     * @param currentPage 当前页
     * @param pageSize    每页记录
     * @return
     */
    Page<Feedback> findFeedbackList(String userId, Integer currentPage, Integer pageSize);


    /**
     * 分页查询意见反馈
     *
     * @param userId      用户id
     * @param feedbackId  反馈意见id
     * @param currentPage 当前页
     * @param pageSize    每页记录
     * @return
     */
    Page<Feedback> findFeedbackList(String userId, String feedbackId, Integer currentPage, Integer pageSize);

    /**
     * 统计用户未查看回复
     *
     * @param userId 用户id
     * @return
     */
    Integer countReplyYesFeedback(String userId);

    /**
     * 统计顶层用户未查看回复
     *
     * @param userId 用户id
     * @return
     */
    Map<String,Integer> findReplyYesParentFeedBack(String userId);

    /**
     * 查看回复
     *
     * @param userId
     * @param feedbackId
     */
    void updateFeedbackSeeReply(String userId, String feedbackId);

}
