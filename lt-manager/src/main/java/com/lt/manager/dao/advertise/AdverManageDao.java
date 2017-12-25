package com.lt.manager.dao.advertise;

import java.util.List;

import com.lt.manager.bean.advertise.AdvertisementPage;
import com.lt.manager.bean.advertise.AdvertisementVo;
import com.lt.model.advertise.Advertisement;

/**
 * 货币管理dao
 * @author yubei
 *
 */
public interface AdverManageDao {

	/**
	 * 新增广告
	 * @param param
	 */
	public void insertAdvertisement(Advertisement advertisement);
	
	/**
	 * 修改广告权重
	 * @param param
	 */
	public void updateAdvertisementWeight(Advertisement advertisement);
	
	/**
	 * 修改广告显示
	 * @param param
	 */
	public void updateAdvertisementShow(Advertisement advertisement);
	
	/**
	 * 修改广告
	 * @param param
	 */
	public void updateAdvertisement(Advertisement advertisement);
	
	/**
	 * 删除广告
	 * @param param
	 */
	public Integer deleteAdvertisement(String adverId);
	
	/**
	 * 根据条件查询广告图数据
	 * @param param
	 * @return
	 */
	public List<Advertisement> selectAdvertisementList(AdvertisementVo advertisementVo);
	
	/**
	 * 查询广告--单个
	 * @param param
	 * @return
	 */
	public Advertisement qryAdverMentById(String adverId);
	
	/**
	 * 获取该类型下最大权重
	 * @param adverType
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 上午11:21:08
	 */
	public Integer getHighWeight(String adverType);
	
	/**
	 * 查询广告图 -- 分页
	 * @param advertisementVo
	 * @return    
	 * @return:       List<AdvertisementPage>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 下午2:41:53
	 */
	public List<AdvertisementPage> qryAdvertisementPage(AdvertisementVo advertisementVo);
	
	/**
	 *  查询广告图 -- 数量
	 * @param advertisementVo
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 下午2:59:50
	 */
	public Integer qryAdvertisementCount(AdvertisementVo advertisementVo);
}
