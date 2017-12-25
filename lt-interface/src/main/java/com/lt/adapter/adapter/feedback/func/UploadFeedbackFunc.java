package com.lt.adapter.adapter.feedback.func;

import com.alibaba.fastjson.JSON;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 意见反馈上传图片
 */
@Component
public class UploadFeedbackFunc extends BaseFunction {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response response(Map<String, Object> paraMap) {

        logger.info("Upload: {} ", JSON.toJSONString(paraMap));

        Map<String, String> imageMap = new HashMap<String, String>();
        for (String key : paraMap.keySet()) {
            if (key.contains("imageFile")) {
                imageMap.put(key, paraMap.get(key) + "");
            }
        }

        logger.info("Upload result: {} ", JSON.toJSONString(imageMap));

        return new Response(LTResponseCode.SUCCESS, "操作成功", imageMap);
    }
}
