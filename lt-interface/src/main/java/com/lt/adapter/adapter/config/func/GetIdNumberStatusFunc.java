package com.lt.adapter.adapter.config.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.adapter.utils.ConfigUtils;
import com.lt.api.user.IUserApiService;
import com.lt.model.sys.QuotaHostBean;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetIdNumberStatusFunc extends BaseFunction {

    @Autowired
    private IUserApiService userApiService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        Map<String,String> map = new HashMap<String,String>();
        try{

            String isOpen = userApiService.isOpen();
            map.put("isOpen", isOpen);
            Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
            return response;
        }catch(Exception e){
            throw new LTException(LTResponseCode.US00004);
        }
    }
}
