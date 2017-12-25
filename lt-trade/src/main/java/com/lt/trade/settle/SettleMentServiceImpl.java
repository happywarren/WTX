package com.lt.trade.settle;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.api.settle.SettleMentApiService;
import com.lt.model.settle.SettleTypeInfo;
import com.lt.trade.settle.dao.SettleDao;
import com.lt.trade.settle.service.SettleMentAdapter;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-trade   
* 类名称：SettleMentServiceImpl   
* 类描述：  结算实现类
* 创建人：yuanxin   
* 创建时间：2017年3月15日 上午10:06:19      
*/
@Component
public class SettleMentServiceImpl implements SettleMentApiService {
	
	@Autowired
	private SettleDao settleDao;
	
	private final static String adapter = "Adapter";
	
	/**
	 * 返回结算配置
	 * @param id
	 * @return    
	 * @return:       SettleTypeInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月16日 下午3:47:50
	 */
	public SettleTypeInfo getSettleCfgInfo(Integer id){
		return settleDao.querySettleCfgById(id);
	}

	@Override
	public Response execute(Map<String,Object> paraMap) throws LTException{
//		String balanceType = paraMap.get("service").toString();
//		SettleTypeInfo settleTypeInfo = getSettleCfgInfo(Integer.parseInt(balanceType));
//		SettleMentAdapter settleAdapter = (SettleMentAdapter) SpringUtils.getBean(settleTypeInfo.getSettleValue()+adapter);
//		settleAdapter.excute(settleTypeInfo, paraMap.get("object"));
		return new Response(LTResponseCode.SUCCESS, "结算成功");
	}

}
