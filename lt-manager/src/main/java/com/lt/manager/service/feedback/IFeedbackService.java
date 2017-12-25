package com.lt.manager.service.feedback;

import com.github.pagehelper.Page;
import com.lt.manager.bean.feedback.FeedbackPage;
import com.lt.manager.bean.feedback.FeedbackParam;
import com.lt.manager.bean.feedback.FeedbackVo;

/**
 * 反馈意见
 */
public interface IFeedbackService {

    Page<FeedbackPage> getFeedbackListPage(FeedbackParam feedbackParam);

    Page<FeedbackPage> getDetailFeedbackPage(FeedbackParam feedbackParam);

    void updateFeedback(FeedbackVo feedbackVo);

}
