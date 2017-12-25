package com.lt.manager.service.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ExchangeParamVO;
import com.lt.manager.dao.product.ExchangeHolidayManageDao;
import com.lt.manager.service.product.IExchangeHolidayManageService;
import com.lt.model.product.ExchangeHoliday;


/**
 * 交易所假期实现类
 * @author jingwb
 *
 */
@Service
public class ExchangeHolidayManageServiceImpl implements IExchangeHolidayManageService{

	@Autowired
	private ExchangeHolidayManageDao exchangeHolidayManageDao;
	
	@Override
	public void addExchangeHoliday(ExchangeParamVO param) throws Exception {
		
		List<ExchangeParamVO> params = new ArrayList<ExchangeParamVO>();
		String[] ids = param.getIds().split(",");//交易所id串1,2,3,4
		
		for(String exId:ids){
			ExchangeParamVO vo = new ExchangeParamVO();
			vo.setBeginTime(param.getBeginTime());
			vo.setEndTime(param.getEndTime());
			vo.setRemark(param.getRemark());
			vo.setCreateUser(param.getCreateUser());
			vo.setExchangeId(Integer.valueOf(exId));
			params.add(vo);
		}
		exchangeHolidayManageDao.insertExchangeHolidays(params);
	}

	@Override
	public void editExchangeHoliday(ExchangeParamVO param) throws Exception {
		exchangeHolidayManageDao.updateExchangeHoliday(param);
	}

	@Override
	public void removeExchangeHoliday(ExchangeParamVO param)
			throws Exception {
		exchangeHolidayManageDao.deleteExchangeHoliday(param);
	}

	@Override
	public Page<ExchangeHoliday> queryExchangeHolidayPage(ExchangeParamVO param)
			throws Exception {
		Page<ExchangeHoliday> page = new Page<ExchangeHoliday>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.setTotal(exchangeHolidayManageDao.selectExchangeHolidayCount(param));
		page.addAll(exchangeHolidayManageDao.selectExchangeHolidayPage(param));
		return page;
	}

}
