package com.lt.manager.controller.feedback;

import com.github.pagehelper.Page;
import com.lt.manager.bean.feedback.FeedbackPage;
import com.lt.manager.bean.feedback.FeedbackParam;
import com.lt.manager.bean.feedback.FeedbackVo;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.feedback.IFeedbackService;
import com.lt.util.StaffUtil;
import com.lt.util.annotation.LimitLess;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 反馈意见管理
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFeedbackService feedbackService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public String list(HttpServletRequest request, FeedbackParam feedbackParam) {
        Response response = null;
        try {
            Page<FeedbackPage> page = feedbackService.getFeedbackListPage(feedbackParam);
            return JqueryEasyUIData.init(page);
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            return response.toJsonString();
        }
    }

    @RequestMapping(value = "/detail")
    @ResponseBody
    public String detail(HttpServletRequest request, FeedbackParam feedbackParam) {
        Response response = null;
        try {
            String feedbackId = feedbackParam.getFeedbackId();
            if (StringTools.isEmpty(feedbackId)) {
                throw new LTException(LTResponseCode.MA00004);
            }
            Page<FeedbackPage> page = feedbackService.getDetailFeedbackPage(feedbackParam);
            return JqueryEasyUIData.init(page);
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            return response.toJsonString();
        }
    }

    @RequestMapping(value = "/reply")
    @ResponseBody
    public String reply(HttpServletRequest request, FeedbackVo feedbackVo) {
        Response response = null;
        try {
            String feedbackId = feedbackVo.getFeedbackId();
            String replyContent = feedbackVo.getReplyContent();
            if (StringTools.isEmpty(feedbackId)) {
                throw new LTException(LTResponseCode.MA00004);
            }
            if (StringTools.isEmpty(replyContent)) {
                throw new LTException(LTResponseCode.MA00004);
            }
            feedbackVo.setReplyUser("system");
            Staff staff = StaffUtil.getStaff(request);
            if (StringTools.isNotEmpty(staff)){
                feedbackVo.setReplyUser(staff.getLoginName());
            }
            feedbackService.updateFeedback(feedbackVo);
            response = new Response(LTResponseCode.SUCCESS, "回复成功");
            return response.toJsonString();
        } catch (LTException e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            return response.toJsonString();
        }
    }
}
