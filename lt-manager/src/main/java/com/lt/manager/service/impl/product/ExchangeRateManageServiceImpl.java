package com.lt.manager.service.impl.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.manager.dao.product.ExchangeRateManageDao;
import com.lt.manager.dao.product.ProductManageDao;
import com.lt.manager.service.product.IExchangeRateManageService;
import com.lt.model.product.ExchangeRate;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**
 * 货币汇率管理service实现类
 * @author jingwb
 *
 */
@Service
public class ExchangeRateManageServiceImpl implements IExchangeRateManageService{

	@Autowired
	private ExchangeRateManageDao exchangeRateManageDao;
	@Autowired
	private ProductManageDao productManageDao;
	
	@Override
	public void addExchangeRate(ExchangeParamVO param) throws Exception {
		exchangeRateManageDao.insertExchangeRate(param);
	}

	@Override
	public void editExchangeRate(ExchangeParamVO param) throws Exception {
		exchangeRateManageDao.updateExchangeRate(param);
	}

	@Override
	public void removeExchangeRate(ExchangeParamVO param) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		map.put("rateIds", param.getIds());
		Integer count = productManageDao.selectProCountByParam(map);
		if(count > 0){
			throw new LTException(LTResponseCode.PR00013);
		}
		exchangeRateManageDao.deleteExchangeRate(param);
	}

	@Override
	public Page<ExchangeRate> queryExchangeRatePage(ExchangeParamVO param)
			throws Exception {
		Page<ExchangeRate> page = new Page<ExchangeRate>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.setTotal(exchangeRateManageDao.selectExchangeRateCount(param));
		page.addAll(exchangeRateManageDao.selectExchangeRatePage(param));
		return page;
	}

	@Override
	public List<ExchangeRate> queryExchangeRate(ExchangeParamVO param)
			throws Exception {
		param.setLimit(9999);
		return exchangeRateManageDao.selectExchangeRatePage(param);
	}

}
