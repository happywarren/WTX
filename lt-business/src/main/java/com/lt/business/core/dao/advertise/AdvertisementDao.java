package com.lt.business.core.dao.advertise;

import java.util.List;
import com.lt.model.advertise.Advertisement;


public interface AdvertisementDao {

	/**
	 * 根据类型查找广告图并根据权重和修改时间排序
	 * @param adverType
	 * @return    
	 * @return:       List<AdvertistMent>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月5日 上午10:31:48
	 */
	public List<Advertisement> qryAdvertisementByWeightUptDate(Integer adverType) ;
	
	/**
	 * 根据广告图id查询广告图内容
	 * @param advertiseId
	 * @return    
	 * @return:       Advertisement    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月5日 上午10:35:59
	 */
	public Advertisement qryAdvertisementById(String advertiseId);

}
