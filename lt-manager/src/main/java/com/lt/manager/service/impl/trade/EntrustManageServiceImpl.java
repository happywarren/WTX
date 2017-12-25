package com.lt.manager.service.impl.trade;

import java.util.Date;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundTradeApiService;
import com.lt.api.trade.IOrderApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.DeferStatusEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.manager.bean.trade.OrderParamVO;
import com.lt.manager.dao.trade.OrderCashEntrustChildInfoDao;
import com.lt.manager.dao.trade.OrderCashEntrustInfoDao;
import com.lt.manager.dao.trade.OrderCashInfoDao;
import com.lt.manager.dao.trade.OrderCashSuccessInfoDao;
import com.lt.manager.service.trade.IEntrustManageService;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.trade.OrderCashSuccessInfo;
import com.lt.util.LoggerTools;
import com.lt.util.TradeUtil;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.vo.fund.FundOrderVo;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.AvgPriceVo;

/**
 * 现金订单交易实现类
 * @author jingwb
 *
 */
@Service
public class EntrustManageServiceImpl implements IEntrustManageService{

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private OrderCashEntrustChildInfoDao orderCashEntrustInfoDao;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	

	@Override
	public Page<OrderParamVO> queryEntrustTradeOrderPage(OrderParamVO param)
			throws Exception {
		Page<OrderParamVO> page = new Page<OrderParamVO>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.setTotal(orderCashEntrustInfoDao.selectEntrustTradeOrderCount(param));
		page.addAll(orderCashEntrustInfoDao.selectEntrustTradeOrderPage(param));
		return page;
	}

	@Override
	public OrderParamVO getCashEntrustOrderInfo(Integer id) throws Exception {
		return orderCashEntrustInfoDao.selectCashEntrustOrderInfo(id);
	}

	@Override
	public OrderCashEntrustInfo queryEntrustInfo(OrderParamVO param)
			throws Exception {
		OrderCashEntrustInfo info = orderCashEntrustInfoDao.selectEntrustInfoOne(param);
		
		Double quotaPrice = TradeUtil.getQuotaPrice(info.getProductCode(), info.getTradeType(), redisTemplate);
		if(info != null){
			info.setQuotaPrice(quotaPrice);
		}
		
		return info;
	}

	

}
