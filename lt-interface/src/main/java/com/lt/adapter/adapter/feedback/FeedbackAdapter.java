package com.lt.adapter.adapter.feedback;

import com.lt.adapter.adapter.BaseAdapter;
import com.lt.adapter.adapter.IFunction;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 作者: 邓玉明 时间: 2017/2/24 下午2:30 email:cndym@163.com
 */
@Service
public class FeedbackAdapter extends BaseAdapter {

    public final String FUNC = "FeedbackFunc";

    @Override
    public Response execute(Map<String, Object> paraMap) {
        funcLog(paraMap);
        String func = StringTools.formatStr(paraMap.get("func"), "");
        IFunction function = (IFunction) SpringUtils.getBean(func + FUNC);
        if (null == function) {
            return funcErr();
        }
        return function.response(paraMap);
    }

}
