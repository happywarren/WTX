package com.lt.api.advertise;

import java.util.List;

import com.lt.model.advertise.Advertisement;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-api   
* 类名称：AdvertiseApiService   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月5日 上午9:55:14      
*/
public interface AdvertiseApiService {
	
	/**
	 * 获取广告图内容
	 * @param type
	 * @return
	 * @throws LTException    
	 * @return:       List<Advertisement>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月5日 上午9:58:33
	 */
	public List<Advertisement> getAdvertisementByType(Integer type) throws LTException;
	
	/**
	 * 通过广告图id查询后面内容
	 * @param advertiseId
	 * @return
	 * @throws LTException    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月5日 上午9:59:30
	 */
	public String getAdvertistContent(String advertiseId) throws LTException;
}
