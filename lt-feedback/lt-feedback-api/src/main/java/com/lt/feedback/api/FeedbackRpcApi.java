package com.lt.feedback.api;


import com.lt.feedback.dto.FeedbackDetailDTO;
import com.lt.feedback.dto.FeedbackListDTO;
import com.lt.feedback.dto.FeedbackSubmitDTO;
import com.lt.feedback.dto.Pagination;

public interface FeedbackRpcApi {

    /**
     * 提交反馈意见
     *
     * @param feedbackSubmitDTO
     */
    void submitFeedback(FeedbackSubmitDTO feedbackSubmitDTO);

    /**
     * 分页查询意见反馈
     *
     * @param userId
     * @param currentPage 当前页
     * @param pageSize    每页记录
     * @return
     */
    Pagination<FeedbackListDTO> feedbackList(String userId, Integer currentPage, Integer pageSize);

    /**
     * 统计回复未查看
     *
     * @param userId
     * @return
     */
    Integer countReplyYesFeedback(String userId);

    /**
     * 反馈意见详情
     *
     * @param userId     用户id
     * @param feedbackId 反馈意见id
     * @return
     */
    Pagination<FeedbackDetailDTO> detailFeedback(String userId, String feedbackId, Integer currentPage, Integer pageSize);
}
