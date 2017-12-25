package com.lt.manager.dao.feedback;

import com.lt.manager.bean.feedback.FeedbackPage;
import com.lt.manager.bean.feedback.FeedbackParam;
import com.lt.manager.bean.feedback.FeedbackVo;

import java.util.List;

/**
 * 反馈意见
 */
public interface FeedbackDao {

    List<FeedbackPage> getFeedbackListPage(FeedbackParam feedbackParam);

    FeedbackVo getFeedback(String feedbackId);

    Integer getFeedbackListCount(FeedbackParam feedbackParam);

    List<FeedbackPage> getDetailFeedbackPage(FeedbackParam feedbackParam);

    Integer getDetailFeedbackCount(FeedbackParam feedbackParam);

    void updateFeedback(FeedbackVo feedbackVo);

    void updateFeedbackRead(FeedbackVo feedbackVo);
}
