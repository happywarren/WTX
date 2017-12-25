package com.lt.user.charge.service;

import java.util.Map;

import com.lt.api.fund.IFundAccountApiService;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-user   
* 类名称：UserChargeFunc   
* 类描述： 用户充值功能处理类 
* 创建人：yuanxin   
* 创建时间：2017年6月29日 下午3:22:38      
*/
public interface UserChargeFunc {
	
	/**
	 * 封装报文
	 * @param object
	 * @return
	 * @throws LTException    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月29日 下午3:28:19
	 */
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException;
	
	/**
	 * 封装返回参数
	 * @param urlCode 请求返回码
	 * @param packet 封装报文
	 * @param BaseChargeBean
	 * @return
	 * @throws LTException    
	 * @return:       Map<String,Object>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月29日 下午4:14:17
	 */
	public Map<String,Object> returnParam(String urlCode,String packet,BaseChargeBean baseCharge) throws LTException;
	
	/**
	 * 发送http请求
	 * @param object
	 * @return
	 * @throws LTException    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月29日 下午4:14:53
	 */
	public String requestUrl(String packet,BaseChargeBean baseCharge) throws LTException;
	
	/**
	 * 插入数据库
	 * @param object
	 * @return
	 * @throws LTException    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月29日 下午4:16:45
	 */
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException;
	
	/**
	 * 执行入口
	 * @param map
	 * @return
	 * @throws LTException    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月29日 下午5:21:32
	 */
	public Map<String,Object> excute(Map<String,Object> map,IFundAccountApiService fundAccountServiceImpl) throws LTException;
}
