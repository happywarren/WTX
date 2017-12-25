/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.vo.trade
 * FILE    NAME: AvgpriceVo.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.vo.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.enums.trade.TradeTypeEnum;

/**
 * TODO 均价
 * @author XieZhibing
 * @date 2016年12月12日 下午4:13:25
 * @version <b>1.0.0</b>
 */
public class AvgPriceVo implements Serializable{
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -8065453861258387039L;
	/** 数量 */
	private Integer count;
	/** 均价格 */
	private Double avgPrice;
	/**总价*/
	private Double priceTotal;
	/**最小价*/
	private Double minPrice;
	/**上部价*/
	private Double upPrice;
	/***下部价*/
	private Double downPrice;
	
	public AvgPriceVo(){}
	
	/**
	 * 构造
	 * @author XieZhibing
	 * @date 2016年12月12日 下午4:14:43
	 * @param count
	 * @param avgPrice
	 */
	public AvgPriceVo(Integer count, Double avgPrice, Integer matchVol, Double matchPrice,Integer tradeDirection,int digits) {
		count = (count == null) ? 0 : count;
		avgPrice = (avgPrice == null) ? 0.00: avgPrice;
		
		matchVol = (matchVol == null) ? 0 : matchVol;
		matchPrice = (matchPrice == null) ? 0.00 : matchPrice;
		
		if(count < 1) {
			count = matchVol;
			avgPrice = matchPrice;
		} else {
			avgPrice = (count * avgPrice + matchVol * matchPrice)/(count + matchVol);
			count = count + matchVol;
		}
		
		int digit = digits;//小数位数
		
		//空舍多入，保证亏损最小（对平台最有利）
		if(tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue()){//空
			//解决，如avgPrice=52.8 那么向下取整应该还是52.8，但是却为52.79的问题
			avgPrice = avgPrice + 0.0000001;
			avgPrice = new BigDecimal(avgPrice).divide(BigDecimal.ONE,digit,BigDecimal.ROUND_FLOOR).doubleValue();
		}else{//多
			avgPrice = avgPrice - 0.0000001;
			avgPrice = new BigDecimal(avgPrice).divide(BigDecimal.ONE,digit,BigDecimal.ROUND_CEILING).doubleValue();
		}
		
		this.count = count;
		this.avgPrice = avgPrice;
	}

	
	public AvgPriceVo(Double priceTotal,Double minPrice,Integer count,Integer matchVol,Double matchPrice,Integer tradeDirection,Integer tradeType,Double jumPrice) {
		Double avgPrice=null;//均价
		
		count = (count == null) ? 0 : count;
		priceTotal = (priceTotal == null) ? 0 : priceTotal;
		minPrice = (minPrice == null) ? 10000000000.0 : minPrice;
		
		priceTotal = priceTotal + matchPrice*matchVol;
		count = count + matchVol;
		
		minPrice = minPrice.compareTo(matchPrice)==-1 ?minPrice:matchPrice;//取最小价
		avgPrice = priceTotal/count;//均价
		//处理价格
		dealPrice(avgPrice, minPrice, jumPrice);
		//规则：买：空舍多入，卖：空入多舍
		if(TradeTypeEnum.BUY.getValue() == tradeType){//买
			if(TradeDirectionEnum.DIRECTION_DOWN.getValue() == tradeDirection){//空
				avgPrice = this.downPrice;
			}else if(TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection){//多
				avgPrice = this.upPrice;
			}
		}else if(TradeTypeEnum.SELL.getValue() == tradeType){//卖
			if(TradeDirectionEnum.DIRECTION_DOWN.getValue() == tradeDirection){//空
				avgPrice = this.upPrice;
			}else if(TradeDirectionEnum.DIRECTION_UP.getValue() == tradeDirection){//多
				avgPrice = this.downPrice;
			}
		}
		this.minPrice = minPrice;
		this.priceTotal = priceTotal;
		this.count = count;
		this.avgPrice = avgPrice;
	}
	
	
	/**
	 * 获取该平均价的上部价和下部价
	 * @param avgPrice
	 * @param minPrice
	 * @param jumPrice
	 */	
	public void dealPrice(Double avgPrice,Double minPrice,Double jumPrice){
		if(minPrice.compareTo(avgPrice) == 0){
			this.downPrice = minPrice;
			this.upPrice=minPrice;
			return;
		}
		
		this.downPrice = minPrice;
		minPrice = minPrice+jumPrice;
		this.upPrice=minPrice;
		if(minPrice.compareTo(avgPrice) == -1){//小于
			dealPrice(avgPrice, minPrice, jumPrice);
		}
		
	}
	/** 
	 * 获取 数量 
	 * @return count 
	 */
	public Integer getCount() {
		return count;
	}

	/** 
	 * 设置 数量 
	 * @param count 数量 
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/** 
	 * 获取 价格 
	 * @return avgPrice 
	 */
	public Double getAvgPrice() {
		return avgPrice;
	}

	/** 
	 * 设置 价格 
	 * @param avgPrice 价格 
	 */
	public void setAvgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
	}
	
	
	public Double getPriceTotal() {
		return priceTotal;
	}


	public void setPriceTotal(Double priceTotal) {
		this.priceTotal = priceTotal;
	}


	public Double getMinPrice() {
		return minPrice;
	}


	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}


	/**
	 * 获取输入的小数是几位小数
	 * @param jumpValue
	 * @return
	 */
	private int dealDigit(Double jumpValue){
		if(jumpValue >= 1){
			return 1;
		}
		String s = jumpValue + ""; 
		int position = s.length() - s.indexOf(".") -1; 
		return position;
	}
	
	
	
	public static void main(String[] args) {
		/*Double dd = 24781.0;
		//向下取整
		dd = dd + 0.000001;
		Double b1 = new BigDecimal(dd).divide(BigDecimal.ONE,0,BigDecimal.ROUND_FLOOR).doubleValue();
		//向上取整
		dd = dd - 0.000002;
		Double b2 = new BigDecimal(dd).divide(BigDecimal.ONE,0,BigDecimal.ROUND_CEILING).doubleValue();
		System.out.println(b1);
		System.out.println(b2);
		
		 
		 AvgPriceVo vv = new AvgPriceVo(1, 24781.0, 1, 24781.0, TradeDirectionEnum.DIRECTION_DOWN.getValue(),0);
		 System.out.println(vv.getAvgPrice());*/
		Double priceTotal=null;
		Integer count1=null;
		Double minPrice=null;
		
		
		Double[] price = new Double[]{49.56};
		Integer[] count = new Integer[]{1};
		for(int i =0; i < 1; i ++){
			AvgPriceVo vv = new AvgPriceVo(priceTotal,minPrice, count1, count[i], price[i], 1, 1,0.01);
			priceTotal = vv.getPriceTotal();
			count1 = vv.getCount();
			minPrice = vv.getMinPrice();
			System.out.println(vv.getAvgPrice());
		}
		
		
		//最小价要比较后传进来
		//AvgPriceVo vv = new AvgPriceVo(11.5, 11.0, 1, 1, 11.0, 2, 1, 0.5);
		//System.out.println(vv.getAvgPrice());
		/*AvgPriceVo vv = new AvgPriceVo();
		vv.dealPrice(16.0, 11.5, 0.5);
		System.out.println(vv.upPrice);
		System.out.println(vv.downPrice);*/
	}
}
