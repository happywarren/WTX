package com.lt.trade.defer.service.impl;

import com.lt.api.business.product.IProductApiService;
import com.lt.api.business.product.IProductTimeConfigApiService;
import com.lt.enums.trade.PlateEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.trade.defer.bean.CoinPositionCountBO;
import com.lt.trade.defer.bean.NextPeroidOrderInfo;
import com.lt.trade.defer.dao.DeferDao;
import com.lt.trade.defer.service.DeferService;
import com.lt.trade.order.service.ICoinPositionSumService;
import com.lt.trade.tradeserver.bean.DigitalCoinPosition;
import com.lt.util.utils.DoubleTools;
import com.lt.util.utils.DoubleUtils;
import com.lt.vo.defer.PeroidOrderHolidayVo;
import com.lt.vo.defer.ProNextTradePeriodVo;
import com.lt.vo.defer.ProductDeferTimeInfoVo;
import com.sun.tools.javac.resources.compiler;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**   
* 项目名称：lt-trade   
* 类名称：DeferServiceImpl   
* 类描述： 递延服务实现类  
* 创建人：yuanxin   
* 创建时间：2017年4月24日 下午3:42:36      
*/
@Service
public class DeferServiceImpl implements DeferService {
	
	private final static Logger logger = LoggerFactory.getLogger(DeferServiceImpl.class);
	@Autowired
	private DeferDao deferDao;
	@Autowired
	private IProductTimeConfigApiService productTimeConfigApiService;
	@Autowired
	private IProductApiService productApiService;
	@Autowired
	private ICoinPositionSumService coinPositionSumService;
	
	
	@Override
	public LinkedHashMap<String, Set<String>> getDeferTimeProduct() {
		List<ProductDeferTimeInfoVo> time_list = productTimeConfigApiService.queryAllProductDeferTime();
		if(CollectionUtils.isNotEmpty(time_list)){
			LinkedHashMap<String,Set<String>> map = new LinkedHashMap<String, Set<String>>();
			for(ProductDeferTimeInfoVo productDeferTimeInfo : time_list){
				if(map.containsKey(productDeferTimeInfo.getDeferBalanceTime())){
					Set<String> set = map.get(productDeferTimeInfo.getDeferBalanceTime());
					set.add(productDeferTimeInfo.getProductId());
				}else{
					Set<String> set = new HashSet<String>();
					set.add(productDeferTimeInfo.getProductId());
					map.put(productDeferTimeInfo.getDeferBalanceTime(), set);
				}
			}
			
			return map;
		}else{
			return null;
		}
	}

	@Override
	public Map<String, TreeSet<String>> getProductClearTime() {
		List<ProductDeferTimeInfoVo> time_list = productTimeConfigApiService.queryAllProductClearTime();
		if(CollectionUtils.isNotEmpty(time_list)){
			Map<String,TreeSet<String>> treeMap = new HashMap<String,TreeSet<String>>();
			for(ProductDeferTimeInfoVo prdDeferTime : time_list){
				if(treeMap.containsKey(prdDeferTime.getProductId())){
					TreeSet<String> time = treeMap.get(prdDeferTime.getProductId());
					time.add(prdDeferTime.getDeferBalanceTime());
				}else{
					TreeSet<String> time = new TreeSet<String>();
					time.add(prdDeferTime.getDeferBalanceTime());
					treeMap.put(prdDeferTime.getProductId(), time);
				}
			}
			
			return treeMap;
		}else{
			return null ;
		}
	}

	@Override
	public List<NextPeroidOrderInfo> findAllScoreOrderByCode(List<String> code) {
		return deferDao.findAllScoreOrdersByCode(code);
	}

	@Override
	public List<PeroidOrderHolidayVo> findCodeHoliday(List<String> code) {
		return productApiService.findAllCodeHoliday(code);
	}

	@Override
	public Map<Integer,ProNextTradePeriodVo> findCodeNextDayPeriod(List<String> code) {
		List<ProNextTradePeriodVo> peroidList = productApiService.qryNextDayTradeTime(code);
		Map<Integer,ProNextTradePeriodVo> map = new HashMap<Integer,ProNextTradePeriodVo>();
		if(CollectionUtils.isNotEmpty(peroidList)){
			Map<Integer,String> time = new HashMap<Integer,String>();
			Set<Integer> valueAble = new HashSet<Integer>();
			for(int i=0;i < peroidList.size();i++){
				ProNextTradePeriodVo tradePeriod = peroidList.get(i);
				Integer shortCode = tradePeriod.getProductId();
				if(shortCode == null){
					continue;
				}
				
				time.put(shortCode, tradePeriod.getSysSaleEndTime());
				if(Integer.parseInt(time.get(shortCode).split(":")[0]) >= 
						Integer.parseInt(tradePeriod.getSysSaleEndTime().split(":")[0])){
						valueAble.add(shortCode);
				}
			}
			
			for(Integer shortcode :valueAble){
				ProNextTradePeriodVo nextTradePeriod = new ProNextTradePeriodVo();
				nextTradePeriod.setNextDayTime(time.get(shortcode));
				map.put(shortcode,nextTradePeriod);
			}
		}
		return map;
	}

	@Override
	public double getOrderDeferredFee(Integer plate, Integer tradeDirection, String investorId,double deferInterest,String productCode) {
        double multiple = 1D;
        try {
        	DigitalCoinPosition digitalCoinPosition = coinPositionSumService.getAllBuySellPositionByInvestorId(productCode,investorId);
            logger.info("现金 券商: {} 多单: {} 空单: {} ", investorId, digitalCoinPosition.getBuyCount(), digitalCoinPosition.getSellCount());
            //差价合约
            if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue().intValue() == plate) {
                if (TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection) {
                    //看多
                    multiple = getBuyMultiple(digitalCoinPosition.getBuyCount(), digitalCoinPosition.getSellCount());
                } else if (TradeDirectionEnum.DIRECTION_DOWN.getValue() == tradeDirection) {
                    //看空
                    multiple = getSellMultiple(digitalCoinPosition.getBuyCount(), digitalCoinPosition.getSellCount());
                }
            }
        } catch (Exception e) {
        }
        logger.info("现金 券商: {} plate: {} tradeDirection: {} multiple: {} ", investorId, plate, tradeDirection, multiple);
        return DoubleUtils.mul(deferInterest, multiple);
	}
	
    private Double getBuyMultiple(Integer buyCount, Integer sellCount) {
        if (null == buyCount) {
            buyCount = 0;
        }
        if (null == sellCount) {
            sellCount = 0;
        }
        int sub = buyCount - sellCount;
        int sum = buyCount + sellCount;
        if (sum <= 0) {
            return 1D;
        }

        if (DoubleTools.div(sub, sum) > 0.9D) {
            return 3D;
        } else if (DoubleTools.div(sub, sum) > 0.7D) {
            return 2D;
        } else if (DoubleTools.div(sub, sum) > 0.5D) {
            return 1.5D;
        }

        return 1D;
    }

    private Double getSellMultiple(Integer buyCount, Integer sellCount) {
        if (null == buyCount) {
            buyCount = 0;
        }
        if (null == sellCount) {
            sellCount = 0;
        }

        int sub = sellCount - buyCount;
        int sum = buyCount + sellCount;

        if (sum <= 0) {
            return 1D;
        }
        if (DoubleTools.div(sub, sum) > 0.9D) {
            return 3D;
        } else if (DoubleTools.div(sub, sum) > 0.7D) {
            return 2D;
        } else if (DoubleTools.div(sub, sum) > 0.5D) {
            return 1.5D;
        }
        return 1D;
    }
}
