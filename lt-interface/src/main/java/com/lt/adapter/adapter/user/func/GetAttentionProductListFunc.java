package com.lt.adapter.adapter.user.func;

import java.util.*;

import com.lt.api.business.product.IProductApiService;
import com.lt.enums.trade.PlateEnum;
import com.lt.model.user.UserProductSelect;
import com.lt.vo.product.ProductQuotaVo;
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

	@Autowired
	private IProductApiService productApiService;

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

			//获取最新行情
			List<String> prdcodeList = new ArrayList<String>();
			for(int i=0;i<outerList.size();i++){
				prdcodeList.add(outerList.get(i).getProductCode());
			}
			List<ProductQuotaVo> quotaList = productApiService.findQuotaObjectByCodeList(prdcodeList);
			for(ProductQuotaVo quotaVo : quotaList){
				UserProductSelectListVo tmp = null;

				for(UserProductSelectListVo userProductSelectListVo : outerList){
					if(userProductSelectListVo.getProductCode().equals(quotaVo.getCode())){
						tmp = userProductSelectListVo;
						break;
					}
				}

				if(tmp != null){
					tmp.setBidPrice(quotaVo.getBidPrice());
					tmp.setAskPrice(quotaVo.getAskPrice());
					tmp.setLastPrice(quotaVo.getLastPrice());
					tmp.setPercentage(quotaVo.getPercentage());
					tmp.setChangeValue(quotaVo.getChangeValue());
				}

			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,outerList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		return response;
	}
}