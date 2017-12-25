package com.lt.adapter.adapter.user.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.api.user.IUserApiService;
import com.lt.model.user.UserServiceMapper;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * 用户是否可以使用 BTC
 *
 * @author yanzhenyu
 */
@Component
public class FindUserAccessBtcFunc extends BaseFunction {

    private final IUserApiService userApiService;

    @Autowired
    public FindUserAccessBtcFunc(IUserApiService userApiService) {
        this.userApiService = userApiService;
    }

    @Override
    public Response response(Map<String, Object> paraMap) {
        String userId = StringTools.formatStr(paraMap.get("userId"), "-1");
        if (Objects.equals(userId, "-1")) {
            throw new LTException("");
        }
        Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        response.setData(userApiService.findUserAccessBtc(userId));
        return response;
    }

}
