package com.lt.model.fund;

import java.io.Serializable;

/**
 * 资金服务：快钱鉴权记录
-----------------------------------------------------
快钱API：有两种鉴权方式
1. 独立鉴权：卡信息验证
2. 消费鉴权：通过用户用户快捷支付，设置报文
字段 savePciFlag
是否保存鉴权信息
0 不保存
1 保存
默认为0
-----------------------------------------------------
本系统采用第二种消费鉴权的方式，
1. 因为首次支付和再次支付的请求报文是不同的，
需要根据鉴权记录进行判别。
2. 如果没有鉴权记录，则是首次支付
如果已经存在鉴权记录，则是再次支付!
3. 鉴权记录的组合键：客户号（本系统的userId）+短卡号（本系统用户的银行卡，且为前六后四组合）
4.一个客户号下可以绑定多张银行卡。
 * @author thomas
 *
 */
public class FundPayAuthFlow implements Serializable{

	private static final long serialVersionUID = 346900144683445396L;
	private Integer id;
	/**客户号(本系统的userId)，保证唯一**/
	private String customerId;
	/**短卡号（本系统用户的银行卡，且为前六后四组合）**/
	private String storablePan;
	/**商户号(由快钱分配)**/
	private String merchantId;
	/**0:有效；1:无效**/
	private String flag;
	private String modifyTime;
	private String createTime;
	/**手机号码**/
	private String tel;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getStorablePan() {
		return storablePan;
	}
	public void setStorablePan(String storablePan) {
		this.storablePan = storablePan;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
}
