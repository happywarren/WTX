package com.lt.adapter.adapter.user.func;

import java.util.*;

import com.lt.enums.trade.PlateEnum;
import com.lt.model.user.UserProductSelect;
import com.lt.vo.user.UserProductSelectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.UserProductSelectListVo;

@Service
public class GetAttentionProductListFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		String userId = String.valueOf(paraMap.get("userId"));

		//用户app-版本号
		String clientVersion = StringTools.formatStr(paraMap.get("clientVersion"), "");

		List<UserProductSelectListVo> list = null;

		try {
			list = userApiService.selectProductOptional(userId,clientVersion);
			//对内容进行排序
			LinkedList<UserProductSelectListVo> outerList= new LinkedList<UserProductSelectListVo>();
			LinkedList<UserProductSelectListVo> innerList = new LinkedList<UserProductSelectListVo>();
			for(UserProductSelectListVo userProductSelectListVo : list){
				if(userProductSelectListVo.getPlate() == PlateEnum.INNER_PLATE.getValue()){
					innerList.add(userProductSelectListVo);
				}else if(userProductSelectListVo.getPlate() == PlateEnum.OUTER_PLATE.getValue()){
					outerList.add(userProductSelectListVo);
				}
			}
			Collections.sort(outerList);
			Collections.sort(innerList);
			for(UserProductSelectListVo innerProduct : innerList){
				outerList.add(innerProduct);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,outerList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		return response;
	}
}