import com.lt.feedback.Application;
import com.lt.feedback.api.FeedbackRpcApi;
import com.lt.feedback.dto.FeedbackDetailDTO;
import com.lt.feedback.dto.FeedbackListDTO;
import com.lt.feedback.dto.FeedbackSubmitDTO;
import com.lt.feedback.dto.Pagination;
import com.lt.feedback.utils.FastJsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SubjectTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeedbackRpcApi feedbackRpcApi;

    @Test
    public void test() {
        System.out.println("aaaa");
    }


    public void submitFeedbackTest() {
        for (int i = 0; i < 20; i++) {
            FeedbackSubmitDTO feedbackDTO = new FeedbackSubmitDTO();
            feedbackDTO.setUserId("20170728102942329LzPgijCO");
            feedbackDTO.setContent("测试反馈:" + i);
            feedbackRpcApi.submitFeedback(feedbackDTO);
        }
    }

    @Test
    public void submitParentFeedbackTest() {
        FeedbackSubmitDTO feedbackSubmitDTO = new FeedbackSubmitDTO();
        feedbackSubmitDTO.setUserId("20170728102942329LzPgijCO");
        feedbackSubmitDTO.setParentId("1015046631403630013");
        feedbackSubmitDTO.setImageUrl("htt://www.baidu.com");
        feedbackSubmitDTO.setContent("测试反馈parent");
        feedbackRpcApi.submitFeedback(feedbackSubmitDTO);
    }

    public void countReplyYesFeedbackTest() {
        int count = feedbackRpcApi.countReplyYesFeedback("20170728102942329LzPgijCO");
        System.out.println("countReplyYesFeedback count:"+count);
    }

    @Test
    public void feedbackListTest(){
        Pagination<FeedbackListDTO> pagination = feedbackRpcApi.feedbackList("20170720190915624R2fa8glM",1,5);
        logger.info("feedbackList: {}", FastJsonUtils.toJson(pagination));
    }

    public void detailFeedbackTest(){
        Pagination<FeedbackDetailDTO> pagination = feedbackRpcApi.detailFeedback("20170728102942329LzPgijCO","1015046631403630013",1,20);
        logger.info("feedbackList: {}", FastJsonUtils.toJson(pagination));
    }
}
