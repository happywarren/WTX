package com.lt.vo.product;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * 商品时间配置实体，用于redis
 * @author jingwb
 *
 */
public class TimeConfigVO implements Serializable{

	private static final long serialVersionUID = 6803340096419454047L;

	/**
	 * 用于存储交易时间配置信息
	 */
	private List<TradeAndQuotaTime> tradeTimeList = new ArrayList<TradeAndQuotaTime>();
	
	/**
	 * 用于存储行情时间配置信息
	 */
	private List<TradeAndQuotaTime> quotaTimeList = new ArrayList<TradeAndQuotaTime>();
	
	/**
	 * 用于存储系统清仓时间配置信息
	 */
	private List<SysSaleTime> sysSaleTimeList = new ArrayList<SysSaleTime>();
	
	/**
	 * 构造方法
	 */
	public TimeConfigVO(){
		
	}
	
	/**
	 * 添加交易时间
	 * @param beginTime
	 * @param endTime
	 */
    public void addTradeTimeList(String beginTime,String endTime,String time) {
        tradeTimeList.add(new TradeAndQuotaTime(beginTime, endTime,time));
    }
    
    /**
     * 添加行情时间
     * @param beginTime
     * @param endTime
     */
    public void addQuotaTimeList(String beginTime,String endTime,String time) {
    	quotaTimeList.add(new TradeAndQuotaTime(beginTime, endTime,time));
    }
    
    /**
     * 添加清仓时间
     * @param beginTime
     * @param endTime
     * @param time
     */
    public void addSysSaleTimeList(String beginTime,String endTime,String time){
    	sysSaleTimeList.add(new SysSaleTime(beginTime,endTime,time));
    }
    
	public List<TradeAndQuotaTime> getTradeTimeList() {
		return tradeTimeList;
	}

	public List<TradeAndQuotaTime> getQuotaTimeList() {
		return quotaTimeList;
	}

	public List<SysSaleTime> getSysSaleTimeList() {
		return sysSaleTimeList;
	}

	public void setTradeTimeList(List<TradeAndQuotaTime> tradeTimeList) {
		this.tradeTimeList = tradeTimeList;
	}

	public void setQuotaTimeList(List<TradeAndQuotaTime> quotaTimeList) {
		this.quotaTimeList = quotaTimeList;
	}

	public void setSysSaleTimeList(List<SysSaleTime> sysSaleTimeList) {
		this.sysSaleTimeList = sysSaleTimeList;
	}


	/**
	 * 交易和行情时间内部类
	 * @author jingwb
	 *
	 */
	public static class TradeAndQuotaTime implements Serializable {
		/**
		 * TODO（用一句话描述这个变量表示什么）
		 */
		private static final long serialVersionUID = -8862137025545750241L;
		/**
		 * 开始时间，格式：00:00:00
		 */
		private String beginTime;
		/**
		 * 结束时间，格式:00:00:00
		 */
		private String endTime;
		/**
		 * 具体时间 格式：00:00:00
		 */
		private String time;
		
		private TradeAndQuotaTime(){
			
		}
		private TradeAndQuotaTime(String beginTime,String endTime,String time){
			this.beginTime = beginTime;
			this.endTime = endTime;
			this.time = time;
		}


		public String getBeginTime() {
			return beginTime;
		}


		public void setBeginTime(String beginTime) {
			this.beginTime = beginTime;
		}


		public String getEndTime() {
			return endTime;
		}


		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		
	}
	
	/**
	 * 系统清仓时间内部类
	 * @author jingwb
	 *
	 */
	public static class SysSaleTime implements Serializable {
		/**
		 * TODO（用一句话描述这个变量表示什么）
		 */
		private static final long serialVersionUID = -7538233775620408418L;
		/**
		 * 开始时间，格式：00:00:00
		 */
		private String beginTime;
		/**
		 * 结束时间，格式:00:00:00
		 */
		private String endTime;
		/**
		 * 时间点，格式：00:00:00
		 */
		private String time;
		
		private SysSaleTime(){
			
		}
		private SysSaleTime(String beginTime,String endTime,String time){
			this.beginTime = beginTime;
			this.endTime = endTime;
			this.time = time;
		}

		public String getBeginTime() {
			return beginTime;
		}

		public void setBeginTime(String beginTime) {
			this.beginTime = beginTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}
		
	}
	
	public static void main(String[] args) {
		long l = 1488229140000l;
		System.out.println(new Date(l));
	}
	
}
