package com.lt.manager.service.promote;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.promote.PromoteParamVo;
import com.lt.model.product.Product;
import com.lt.model.promote.PromoterFeeConfig;
import com.lt.model.promote.PromoterLevel;
import com.lt.util.utils.model.Response;


/**
 * 推广管理接口
 * @author jingwb
 *
 */
public interface IPromoteManageService {

	/**
	 * 新建级别
	 * @param level
	 * @param shortCodes
	 * @return
	 * @throws Exception
	 */
	public Response addPromoterLevel(String level) throws Exception;
	
	/**
	 * 添加品种
	 * @param level
	 * @param promoterFeeConfig
	 * @return
	 * @throws Exception
	 */
	public void addShortCodes(String level,String promoterFeeConfig) throws Exception;
	
	/**
	 * 修改佣金设置
	 * @param id
	 * @throws Exception
	 */
	public void modifyPromoterConfig(PromoterFeeConfig promoterFeeConfig) throws Exception;
	
	/**
	 * 删除推广等级
	 * @param id
	 * @throws Exception
	 */
	public Response removePromoterLevel(String id) throws Exception;
	
	public void removePromoterFeeConfig(PromoterFeeConfig promoterFeeConfig) throws Exception;
	
	/**
	 * 查询推广等级
	 * @return
	 * @throws Exception
	 */
	public List<PromoterLevel> getPromoterLevel() throws Exception;
	
	/**
	 * 查询推配置信息
	 * @param levelId
	 * @return
	 * @throws Exception
	 */
	public List<PromoterFeeConfig> getPromoterFeeConfig(String levelId) throws Exception;
	
	/**
	 * 查询推配置信息--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<PromoterFeeConfig> queryPromoterFeeConfigPage(PromoteParamVo param) throws Exception;
	
	/**
	 * 查询推广员列表--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<PromoteParamVo> queryPromoterPage(PromoteParamVo param) throws Exception;
	
	/**
	 * 查询推广员数据列表--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<PromoteParamVo> queryPromoterDataPage(PromoteParamVo param) throws Exception;
	
	/**
	 * 查询下线列表数据--分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<PromoteParamVo> queryBrancherPage(PromoteParamVo param) throws Exception;
	
	/**
	 * 设置推广员等级
	 * @param userId
	 * @return
	 */
	public Response setPromoterLevel(String userId,String level) throws Exception;
	
	/**
	 * 取消成为推广员
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Response cancelPromoter(String userId) throws Exception;
	
	/**
	 * 修改推广员关联关系
	 * @param userId
	 * @param level
	 * @return
	 * @throws Exception
	 */
	public Response modifyPromoterMapper(String userId,String ssy) throws Exception;
	
	/***
	 * 查询推广员信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Response queryPromoterInfo(String userId) throws Exception;
	
	/**
	 * 恢复上级关系
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Response renewPromoterMapper(String userId) throws Exception;
	
	public List<Product> getProShortCode(String level)  throws Exception;
	
	public Integer getProShortCodeCount(String level)  throws Exception;
}
