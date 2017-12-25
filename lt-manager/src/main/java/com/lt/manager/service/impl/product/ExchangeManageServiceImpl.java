package com.lt.manager.service.impl.product;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.manager.dao.product.ExchangeManageDao;
import com.lt.manager.dao.product.ProductManageDao;
import com.lt.manager.service.product.IExchangeManageService;
import com.lt.model.product.ExchangeInfo;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**
 * 交易所管理实现类
 * @author jingwb
 *
 */
@Service
public class ExchangeManageServiceImpl implements IExchangeManageService{

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private ExchangeManageDao exchangeManageDao;
	@Autowired
	private ProductManageDao productManageDao;
	
	@Override
	public void addExchangeInfo(ExchangeParamVO param) throws Exception {
		logger.info("添加交易所信息param={}",param);
		exchangeManageDao.insertExchangeInfo(param);	
	}

	@Override
	public void editExchangeInfo(ExchangeParamVO param) throws Exception {
		exchangeManageDao.updateExchangeInfo(param);
	}

	@Override
	public void removeExchangeInfo(ExchangeParamVO param) throws Exception {
		
		//判断该交易所是否已被商品占用，如果被占用则不允许删除	
		Map<String,String> map = new HashMap<String,String>();
		map.put("exchangeIds", param.getIds());
		Integer count = productManageDao.selectProCountByParam(map);
		if(count > 0){
			throw new LTException(LTResponseCode.PR00013);
		}
		
		exchangeManageDao.deleteExchangeInfo(param);
	}

	@Override
	public Page<ExchangeInfo> queryExchangeInfoPage(ExchangeParamVO param)
			throws Exception {
		Page<ExchangeInfo> page = new Page<ExchangeInfo>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.addAll(exchangeManageDao.selectExchangeInfoPage(param));
		page.setTotal(exchangeManageDao.selectExchangeInfoCount(param));
		return page;
	}

	@Override
	public List<ExchangeInfo> queryExchangeInfo(ExchangeParamVO param)
			throws Exception {
		param.setLimit(9999);
		return exchangeManageDao.selectExchangeInfoPage(param);
	}

	@Override
	public Integer queryExchangeInfoCount(ExchangeParamVO param)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
