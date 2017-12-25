package com.lt.manager.service.impl.feedback;

import com.github.pagehelper.Page;
import com.lt.manager.bean.feedback.FeedbackPage;
import com.lt.manager.bean.feedback.FeedbackParam;
import com.lt.manager.bean.feedback.FeedbackVo;
import com.lt.manager.dao.feedback.FeedbackDao;
import com.lt.manager.service.feedback.IFeedbackService;
import com.lt.util.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private FeedbackDao feedbackDao;

    @Override
    public Page<FeedbackPage> getFeedbackListPage(FeedbackParam feedbackParam) {
        Page<FeedbackPage> page = new Page<>();
        page.setPageNum(feedbackParam.getPage());
        page.setPageSize(feedbackParam.getRows());
        List<FeedbackPage> brandPages = feedbackDao.getFeedbackListPage(feedbackParam);
        for (FeedbackPage feedbackPage : brandPages) {
            feedbackPage.setReplyStatus(status(feedbackPage.getStatus(), feedbackPage.getReplyStatus()));
        }
        page.addAll(brandPages);
        page.setTotal(feedbackDao.getFeedbackListCount(feedbackParam));
        return page;
    }

    @Override
    public Page<FeedbackPage> getDetailFeedbackPage(FeedbackParam feedbackParam) {
        String feedbackId = feedbackParam.getFeedbackId();

        FeedbackVo parent = feedbackDao.getFeedback(feedbackParam.getFeedbackId());
        if (StringTools.isNotEmpty(parent) && StringTools.isNotEmpty(parent.getParentId())) {
            feedbackParam.setFeedbackId(parent.getParentId());
        }
        Page<FeedbackPage> page = new Page<>();
        page.setPageNum(feedbackParam.getPage());
        page.setPageSize(feedbackParam.getRows());
        List<FeedbackPage> brandPages = feedbackDao.getDetailFeedbackPage(feedbackParam);
        for (FeedbackPage feedbackPage : brandPages) {
            feedbackPage.setReplyStatus(status(feedbackPage.getStatus(), feedbackPage.getReplyStatus()));
        }
        page.addAll(brandPages);
        page.setTotal(feedbackDao.getDetailFeedbackCount(feedbackParam));

        FeedbackVo feedbackVo = new FeedbackVo();
        feedbackVo.setFeedbackId(feedbackId);
        feedbackDao.updateFeedbackRead(feedbackVo);

        return page;
    }

    @Override
    public void updateFeedback(FeedbackVo feedbackVo) {
        feedbackVo.setReplyTime(new Date());
        feedbackVo.setReplyStatus(1);
        feedbackDao.updateFeedback(feedbackVo);
    }

    private int status(int status, int replyStatus) {
        if (status == 1 && replyStatus == 0) {
            return 0;
        } else if (status == 0 && replyStatus == 0) {
            return 1;
        } else if (status == 0 && replyStatus == 1) {
            return 2;
        } else if (status == 0 && replyStatus == 2) {
            return 3;
        }
        return 0;
    }
}
