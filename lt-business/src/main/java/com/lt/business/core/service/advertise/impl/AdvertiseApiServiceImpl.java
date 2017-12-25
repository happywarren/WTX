package com.lt.business.core.service.advertise.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.advertise.AdvertiseApiService;
import com.lt.business.core.dao.advertise.AdvertisementDao;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.advertise.AdvertiseCommentTypeEnum;
import com.lt.enums.advertise.AdvertiseMentTypeEnum;
import com.lt.model.advertise.Advertisement;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

/**   
* 项目名称：lt-business   
* 类名称：AdvertiseApiServiceImpl   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月5日 上午10:04:33      
*/
@Service
public class AdvertiseApiServiceImpl implements AdvertiseApiService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AdvertisementDao advertisementDao;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public List<Advertisement> getAdvertisementByType(Integer type) throws LTException {
		List<Advertisement> advertiseList = advertisementDao.qryAdvertisementByWeightUptDate(type);
		
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		String currentDomain = sysCfgRedis.get("current_domain");
		if(CollectionUtils.isNotEmpty(advertiseList)){
			for(Advertisement advertisement : advertiseList){
				
				if(advertisement.getIsShowContent() == 0){
					advertisement.setContent("");
					advertisement.setH5Url("");
					continue ;
				}
				
				if(advertisement.getContentType() == AdvertiseCommentTypeEnum.H5_URL.getValue()){
					advertisement.setContent("");
				}else if(advertisement.getContentType() == AdvertiseCommentTypeEnum.CONTENT.getValue()){
					advertisement.setH5Url(currentDomain+"/advertise?advertiseId="+advertisement.getAdverId());
					advertisement.setContent("");
				}
			}
			
		}
		
		return advertiseList;
	}

	@Override
	public String getAdvertistContent(String advertiseId) throws LTException {
		Advertisement advertisement = advertisementDao.qryAdvertisementById(advertiseId);
		
		if(advertisement == null){
			throw new LTException(LTResponseCode.ADY0003);
		}
		
		if(advertisement.getIsShowContent() == 0){
			return "" ;
		}
		
		return advertisement.getContent();
	}

}
