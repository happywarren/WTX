package com.lt.trade.settle.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.lt.model.settle.SettleBean;
import com.lt.model.settle.SettleTmpBean;
import com.lt.model.settle.SettleTypeInfo;
import com.lt.trade.settle.dao.SettleDao;
import com.lt.util.error.LTException;
import com.lt.util.utils.SpringUtils;

/**   
* 项目名称：lt-trade   
* 类名称：SettleResolverUtil   
* 类描述： 结算组件解析类 
* 创建人：yuanxin   
* 创建时间：2017年3月16日 上午9:05:22      
*/
@Service
public class SettleResolverUtil {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static String funcValue = "SettleFunc";
	
	/**
	 * 转换处理类
	 * @param settleBeanList    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月16日 下午3:36:17
	 */
	public void resolver(List<SettleBean> settleBeanList){
		if(CollectionUtils.isNotEmpty(settleBeanList)){
			for(SettleBean settleBean : settleBeanList){
				logger.info("进入配置方法,传参为：{}",JSONObject.toJSONString(settleBeanList));
				SettleMentTypeFunc func = (SettleMentTypeFunc) SpringUtils.getBean(settleBean.getFunc()+funcValue);
				logger.info("func是否为null:{}",func == null );
				func.settle(settleBean.getAmt(), settleBean.getSettleInnerBean());
			}
		}
	}
	
}
