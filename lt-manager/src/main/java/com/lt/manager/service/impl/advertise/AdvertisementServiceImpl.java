package com.lt.manager.service.impl.advertise;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.advertise.AdvertiseCommentTypeEnum;
import com.lt.manager.bean.advertise.AdvertisementPage;
import com.lt.manager.bean.advertise.AdvertisementVo;
import com.lt.manager.bean.fund.FundFlowVO;
import com.lt.manager.dao.advertise.AdverManageDao;
import com.lt.manager.service.advertise.AdvertisementService;
import com.lt.model.advertise.Advertisement;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;

/**   
* 项目名称：lt-manager   
* 类名称：AdvertisementServiceImpl   
* 类描述：   活动广告业务实现类 
* 创建人：yuanxin   
* 创建时间：2017年7月4日 上午9:24:25      
*/
@Service
public class AdvertisementServiceImpl implements AdvertisementService {
	
	@Autowired
	private AdverManageDao adverManageDao ;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void insertAdvertisement(Advertisement advertisement) throws LTException {
		
		String adverId = DateTools.formatDate(new Date(), DateTools.LOG_DATE_TIME);
		advertisement.setAdverId(adverId);
		
		adverManageDao.insertAdvertisement(advertisement);
	}

	@Override
	public void topAdvertiseMent(String adverId,String adverType,String updateUserId) throws LTException {
		
		Integer highWeight = adverManageDao.getHighWeight(adverType);
		Integer weight = 1 ;
		
		if(highWeight != null){
			weight = highWeight ;
		}
		
		if(weight != 1){
			Advertisement advertisement = new Advertisement();
			advertisement.setWeight(weight);
			advertisement.setAdverId(adverId);
			advertisement.setUpdateUserId(updateUserId);
			
			adverManageDao.updateAdvertisementWeight(advertisement);
		}
		
	}

	@Override
	public Advertisement getAdvertisementById(String adverId) throws LTException {
		return adverManageDao.qryAdverMentById(adverId);
	}

	@Override
	public void updateAdvertisement(Advertisement advertisement) throws LTException {
		
		Integer contentType = advertisement.getContentType();
		
		// 内容类型为 0 时，没有内容
		if(contentType == 0){
			advertisement.setH5Url("");
			advertisement.setContent("");
		}else if(contentType == 1){
			advertisement.setContent("");
		}else{
			advertisement.setH5Url("");
		}
		
		adverManageDao.updateAdvertisement(advertisement);
	}

	@Override
	public void delAdvertisement(String adverIds) throws LTException {
		
		if(!adverIds.equals("")){
			if(adverIds.contains(",")){
				String[] adverIdList = adverIds.split(",");
				for(String adverId : adverIdList){
					adverManageDao.deleteAdvertisement(adverId);
				}
			}else{
				adverManageDao.deleteAdvertisement(adverIds);
			}
		}
	}
	
	@Override
	public void changeShowStatusAdver(String adverId, Integer status,String updateUserId) throws LTException {
		Advertisement advertisement = new Advertisement();
		advertisement.setAdverId(adverId);
		advertisement.setIsShow(status);
		advertisement.setUpdateUserId(updateUserId);
		adverManageDao.updateAdvertisementShow(advertisement);
	}

	@Override
	public Page<AdvertisementPage> qryAdvertistMentPage(AdvertisementVo advertistmentVo) throws LTException {
		Page<AdvertisementPage> page = new Page<AdvertisementPage>();
		page.setPageNum(advertistmentVo.getPage());
		page.setPageSize(advertistmentVo.getRows());
		page.addAll(adverManageDao.qryAdvertisementPage(advertistmentVo));
		page.setTotal(adverManageDao.qryAdvertisementCount(advertistmentVo));
		return page;
	}

	@Override
	public String createAdvertiseQR(String adverId) throws LTException {
		Advertisement advertisement = adverManageDao.qryAdverMentById(adverId);
		
		if(advertisement != null ){
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			if(advertisement.getIsShowContent() == 0){
				return "" ;
			}else{
				logger.info("advertisement:{},flag:{}", JSONObject.toJSONString(advertisement),advertisement.getContentType() == AdvertiseCommentTypeEnum.H5_URL.getValue());
				if(advertisement.getContentType() == AdvertiseCommentTypeEnum.H5_URL.getValue()){
					return advertisement.getH5Url() == null ? "": advertisement.getH5Url();
				}else{
					return advertisement.getContent() == null ? "" : sysCfgRedis.get("current_domain")+"/advertise?advertiseId="+advertisement.getAdverId();
				}
			}
		}
		return "";
	}
	
	
	
}
