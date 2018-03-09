package com.lt.adapter.adapter.config.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetLiveUrlFunc extends BaseFunction {

    @Autowired
    private IUserApiService userApiService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        try{
            Map<String,String> map = new HashMap<String,String>();
            String liveUrl = userApiService.getLiveUrl();
            if(StringTools.isEmpty(liveUrl)){
                map.put("isOpen","0");
                map.put("liveUrl","--");
            }else{
                map.put("isOpen","1");
                map.put("liveUrl",liveUrl);
            }
            Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
            return response;
        }catch(Exception e){
            e.printStackTrace();
            throw new LTException(LTResponseCode.US00004);
        }
    }
}
