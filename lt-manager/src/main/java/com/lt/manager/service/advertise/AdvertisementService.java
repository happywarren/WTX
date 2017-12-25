package com.lt.manager.service.advertise;

import com.github.pagehelper.Page;
import com.lt.manager.bean.advertise.AdvertisementPage;
import com.lt.manager.bean.advertise.AdvertisementVo;
import com.lt.model.advertise.Advertisement;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-manager   
* 类名称：AdvertisementService   
* 类描述： 活动广告业务类  
* 创建人：yuanxin   
* 创建时间：2017年7月4日 上午9:23:44      
*/
public interface AdvertisementService {
	
	/**
	 * 插入广告图（插入制定优先级，优先级不重复）
	 * @param advertisement
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 上午9:33:46
	 */
	public void insertAdvertisement(Advertisement advertisement) throws LTException;
	
	/**
	 * 将单个广告图权重设为最高值
	 * @param adverId 广告图id
	 * @param adverType 广告图类型
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 上午11:18:45
	 */
	public void topAdvertiseMent(String adverId,String adverType,String updateUserId) throws LTException;
	
	/**
	 * 根据广告图id查询广告图对象内容
	 * @param adverId
	 * @return
	 * @throws LTException    
	 * @return:       Advertisement    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 上午11:39:20
	 */
	public Advertisement getAdvertisementById(String adverId) throws LTException;
	
	/**
	 * 修改广告图对象内容
	 * @param advertisement
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 上午11:48:20
	 */
	public void updateAdvertisement(Advertisement advertisement) throws LTException;
	
	/**
	 * 根据广告图id删除内容
	 * @param adverId
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 下午1:56:24
	 */
	public void delAdvertisement(String adverId) throws LTException;
	
	/**
	 * 转换广告图的显示状态
	 * @param adverId
	 * @param status
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 下午2:11:07
	 */
	public void changeShowStatusAdver(String adverId,Integer status,String updateUserId) throws LTException;
	
	/**
	 * 查询广告图分页数据
	 * @param advertistmentVo
	 * @return
	 * @throws LTException    
	 * @return:       Page<AdvertisementVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月4日 下午2:22:36
	 */
	public Page<AdvertisementPage> qryAdvertistMentPage(AdvertisementVo advertistmentVo) throws LTException;
	
	/**
	 * 生成二维码 
	 * @param adverId
	 * @return
	 * @throws LTException    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月6日 上午9:27:09
	 */
	public String createAdvertiseQR(String adverId) throws LTException;
	
}
