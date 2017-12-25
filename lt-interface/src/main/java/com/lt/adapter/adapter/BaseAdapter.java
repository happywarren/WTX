package com.lt.adapter.adapter;

import java.util.Map;

import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 作者: 邓玉明
 * 时间: 2017/2/24 上午11:32
 * email:cndym@163.com
 */
public class BaseAdapter implements IAdapter {

	public final String FUNC = "Func";
	public final String LOGFUNC = "logFunc";
	
    public Response funcErr() {
        return LTResponseCode.getCode(LTResponseCode.ER400);
    }

    @Override
    public Response execute(Map<String, Object> paraMap) {
    	//funcLog(paraMap);
        String func = StringTools.formatStr(paraMap.get("func"), "");
        IFunction function = (IFunction) SpringUtils.getBean(func + FUNC);
        if (null == function) {
            return funcErr();
        }
        return function.response(paraMap);
    }
    
    public void funcLog(Map<String, Object> paraMap){
    	IFunction function = (IFunction) SpringUtils.getBean(LOGFUNC);
    	function.response(paraMap);
    }
}
