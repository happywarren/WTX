package com.lt.trade.settle.service.impl.func;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.model.settle.SettleTmpBean;
import com.lt.trade.settle.dao.SettleDao;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-trade   
* 类名称：BaseSettleFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月16日 下午1:53:45      
*/
@Component
public class BaseCommonSettle {
	
	@Autowired
	private SettleDao settleDao;

	public void insertSettle(List<SettleTmpBean> innerBean) throws LTException {
		settleDao.insertSettleTmp(innerBean);
	}
	

}
