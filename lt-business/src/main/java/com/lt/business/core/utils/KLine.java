package com.lt.business.core.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.KLineBean;

public class KLine {

	/**
	 * 本地内存 Map<商品,Map<类型,K线图对象>>
	 */
	public static Map<Integer, Map<String, KLineBean>> kLineMap = new ConcurrentHashMap<Integer, Map<String, KLineBean>>();

	public static final String RedisKey = "_K_";

	public static void addKLineBean(RedisQuotaObject obj,Integer type){
		Map<String, KLineBean> beanMap = kLineMap.get(type);
		if(beanMap == null || beanMap.isEmpty()){
			beanMap = new ConcurrentHashMap<String, KLineBean>();
			kLineMap.put(type, beanMap);
		}
		KLineBean bean = beanMap.get(obj.getProductName());
		if(!StringTools.isNotEmpty(bean)){
			bean = new KLineBean();
			bean.setClosePrice(obj.getLastPrice());
			bean.setHighPrice(obj.getLastPrice());
			bean.setLowPrice(obj.getLastPrice());
			bean.setOpenPrice(obj.getLastPrice());
			bean.setProductName(obj.getProductName());
			bean.setTimeStamp(obj.getTimeStamp());
			bean.setType(type);
			bean.setVolume(obj.getTotalQty());
			bean.setTotalQty(obj.getTotalQty());
		}else{
			double lastPrice = StringTools.formatDouble(obj.getLastPrice(),0.0);
			double highPrice = StringTools.formatDouble(bean.getHighPrice(),0.0);
			double lowPrice = StringTools.formatDouble(bean.getLowPrice(),0.0);
			int volume = StringTools.formatInt(bean.getTotalQty(),0);
			int totalQty = StringTools.formatInt(obj.getTotalQty(),0);
			if(lastPrice > highPrice){
				bean.setHighPrice(obj.getLastPrice());
			}
			if(lastPrice < lowPrice){
				bean.setLowPrice(obj.getLastPrice());
			}
			bean.setClosePrice(obj.getLastPrice());
			bean.setTimeStamp(obj.getTimeStamp());
			bean.setVolume(totalQty - volume);
		}
		beanMap.put(obj.getProductName(), bean);
	}

	/**
	 * 清空商品类型内存
	 * @param type
	 * @param poduct
	 */
	public static void clearProductKLineByType(int type,String poduct){
		if(kLineMap.get(type)==null ||kLineMap.get(type).isEmpty()){
			return;
		}
		kLineMap.get(type).remove(poduct);
	}

	/**
	 * 查询封装好的实体
	 * @param type
	 * @param poduct
	 */
	public static KLineBean getProductKLineByType(int type,String product){
		return kLineMap.get(type).get(product);
	}

	/**
	 * 比较大小 source > target,retrun true
	 * @param source
	 * @param target
	 * @return
	 */
	private static boolean compare(String source,String target){
		double s = StringTools.formatDouble(source,0.00);
		double t = StringTools.formatDouble(target,0.00);
		if(s > t){
			return true;
		}
		return false;

	}
}
