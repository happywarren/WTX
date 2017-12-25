package com.lt.manager.bean.user;

import java.io.Serializable;

/**   
* 项目名称：lt-manager   
* 类名称：BankChannelVo   
* 类描述：   用户银行下渠道显示
* 创建人：yuanxin   
* 创建时间：2017年6月16日 下午6:06:57      
*/
public class BankChannelVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 银行名称*/
	private String bankName ;
	/** 银行图片*/
	private String bankPic;
	/**以下字段已无用，已改成map自动映射*/
	/** 银生宝金额 单日 命名方便前台解析*/
	private Double daily1010102 = 0.0;
	/** 快钱金额 单日*/
	private Double daily1010103 = 0.0;
	/** 支付宝金额 单日*/
	private Double daily1010101= 0.0;
	/** 快钱金额 单日*/
	private Double daily1010105= 0.0;
	/** 银生宝金额 单笔*/
	private Double single1010102= 0.0;
	/** 快钱 单笔*/
	private Double single1010103 = 0.0;
	/** 支付宝 单笔*/
	private Double single1010101= 0.0;
	/** 快钱 单笔*/
	private Double single1010105= 0.0;

	private Double single1010107 = 0.0;
	private Double daily1010107 = 0.0;
	
	private Double single1010108= 0.0;
	private Double daily1010108= 0.0;
	
	private Double single1010109= 0.0;
	private Double daily1010109= 0.0;
	
	private Double single1010110= 0.0;
	private Double daily1010110= 0.0;
	
	private Double single1010111= 0.0;
	private Double daily1010111= 0.0;
	
	private Double single1010112 = 0.0;
	private Double daily1010112= 0.0;
	
	private Double single1010113 = 0.0;
	private Double daily1010113= 0.0;

	private Double single1010114 = 0.0;
	private Double daily1010114= 0.0;
	
	private Double single1010115 = 0.0;
	private Double daily1010115= 0.0;

	private Double single1010116 = 0.0;
	private Double daily1010116= 0.0;

	public BankChannelVo(){
		
	}
	
	public BankChannelVo(String bankName,String bankPic){
		this.bankName = bankName;
		this.bankPic = bankPic ;
	}
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the bankPic
	 */
	public String getBankPic() {
		return bankPic;
	}
	/**
	 * @param bankPic the bankPic to set
	 */
	public void setBankPic(String bankPic) {
		this.bankPic = bankPic;
	}

	/**
	 * @return the daily1010102
	 */
	public Double getDaily1010102() {
		return daily1010102;
	}

	/**
	 * @param daily1010102 the daily1010102 to set
	 */
	public void setDaily1010102(Double daily1010102) {
		this.daily1010102 = daily1010102;
	}

	/**
	 * @return the daily1010103
	 */
	public Double getDaily1010103() {
		return daily1010103;
	}

	/**
	 * @param daily1010103 the daily1010103 to set
	 */
	public void setDaily1010103(Double daily1010103) {
		this.daily1010103 = daily1010103;
	}

	/**
	 * @return the daily1010101
	 */
	public Double getDaily1010101() {
		return daily1010101;
	}

	/**
	 * @param daily1010101 the daily1010101 to set
	 */
	public void setDaily1010101(Double daily1010101) {
		this.daily1010101 = daily1010101;
	}

	/**
	 * @return the single1010102
	 */
	public Double getSingle1010102() {
		return single1010102;
	}

	/**
	 * @param single1010102 the single1010102 to set
	 */
	public void setSingle1010102(Double single1010102) {
		this.single1010102 = single1010102;
	}

	/**
	 * @return the single1010103
	 */
	public Double getSingle1010103() {
		return single1010103;
	}

	/**
	 * @param single1010103 the single1010103 to set
	 */
	public void setSingle1010103(Double single1010103) {
		this.single1010103 = single1010103;
	}

	/**
	 * @return the single1010101
	 */
	public Double getSingle1010101() {
		return single1010101;
	}

	/**
	 * @param single1010101 the single1010101 to set
	 */
	public void setSingle1010101(Double single1010101) {
		this.single1010101 = single1010101;
	}

	public Double getDaily1010105() {
		return daily1010105;
	}

	public void setDaily1010105(Double daily1010105) {
		this.daily1010105 = daily1010105;
	}

	public Double getSingle1010105() {
		return single1010105;
	}

	public void setSingle1010105(Double single1010105) {
		this.single1010105 = single1010105;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getSingle1010109() {
		return single1010109;
	}

	public void setSingle1010109(Double single1010109) {
		this.single1010109 = single1010109;
	}

	public Double getDaily1010109() {
		return daily1010109;
	}

	public void setDaily1010109(Double daily1010109) {
		this.daily1010109 = daily1010109;
	}

	public Double getSingle1010110() {
		return single1010110;
	}

	public void setSingle1010110(Double single1010110) {
		this.single1010110 = single1010110;
	}

	public Double getDaily1010110() {
		return daily1010110;
	}

	public void setDaily1010110(Double daily1010110) {
		this.daily1010110 = daily1010110;
	}

	public Double getSingle1010111() {
		return single1010111;
	}

	public void setSingle1010111(Double single1010111) {
		this.single1010111 = single1010111;
	}

	public Double getDaily1010111() {
		return daily1010111;
	}

	public void setDaily1010111(Double daily1010111) {
		this.daily1010111 = daily1010111;
	}

	public Double getSingle1010112() {
		return single1010112;
	}

	public void setSingle1010112(Double single1010112) {
		this.single1010112 = single1010112;
	}

	public Double getDaily1010112() {
		return daily1010112;
	}

	public void setDaily1010112(Double daily1010112) {
		this.daily1010112 = daily1010112;
	}

	public Double getSingle1010107() {
		return single1010107;
	}

	public void setSingle1010107(Double single1010107) {
		this.single1010107 = single1010107;
	}

	public Double getDaily1010107() {
		return daily1010107;
	}

	public void setDaily1010107(Double daily1010107) {
		this.daily1010107 = daily1010107;
	}

	public Double getSingle1010108() {
		return single1010108;
	}

	public void setSingle1010108(Double single1010108) {
		this.single1010108 = single1010108;
	}

	public Double getDaily1010108() {
		return daily1010108;
	}

	public void setDaily1010108(Double daily1010108) {
		this.daily1010108 = daily1010108;
	}

	public Double getSingle1010113() {
		return single1010113;
	}

	public void setSingle1010113(Double single1010113) {
		this.single1010113 = single1010113;
	}

	public Double getDaily1010113() {
		return daily1010113;
	}

	public void setDaily1010113(Double daily1010113) {
		this.daily1010113 = daily1010113;
	}

	public Double getSingle1010114() {
		return single1010114;
	}

	public void setSingle1010114(Double single1010114) {
		this.single1010114 = single1010114;
	}

	public Double getDaily1010114() {
		return daily1010114;
	}

	public void setDaily1010114(Double daily1010114) {
		this.daily1010114 = daily1010114;
	}

	public Double getSingle1010115() {
		return single1010115;
	}

	public void setSingle1010115(Double single1010115) {
		this.single1010115 = single1010115;
	}

	public Double getDaily1010115() {
		return daily1010115;
	}

	public void setDaily1010115(Double daily1010115) {
		this.daily1010115 = daily1010115;
	}

	public Double getSingle1010116() {
		return single1010116;
	}

	public void setSingle1010116(Double single1010116) {
		this.single1010116 = single1010116;
	}

	public Double getDaily1010116() {
		return daily1010116;
	}

	public void setDaily1010116(Double daily1010116) {
		this.daily1010116 = daily1010116;
	}
}
