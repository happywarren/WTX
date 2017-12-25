package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 公告
 * @author XieZhibing
 * @date 2017年2月4日 下午3:25:40
 * @version <b>1.0.0</b>
 */
public class NewsNotice implements Serializable {

	/**
	 * Description: 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	private Integer id;
	
	/**
	 * 标题
	 */
	private String title;
	
	
	/**
	 * 中图
	 */
	private String middleBanner;
	
	
	/**
	 * 类型(1:新闻，2:公告)
	 */
	private Integer type;
	
	/**
	 * 来源
	 */
	private String source;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 添加人ID
	 */
	private Integer createStaffId;
	
	/**
	 * 添加人姓名
	 */
	private String createStaffName;
	
	/**
	 * 添加时间
	 */
	private Date createDate;
	
	/**
	 * 修改人ID
	 */
	private Integer modifyStaffId;
	
	/**
	 * 修改人姓名
	 */
	private String modifyStaffName;
	
	/**
	 * 修改日期
	 */
	private Date modifyDate;
	
	/**
	 * 状态 0 创建 ，1 发布
	 */
	private Integer status; 
	
	/**
	 * 访问地址
	 */
	private String url;
	
	/**
	 * 权重登记
	 */
	private Integer level;
	
	/**
	 * 是否推送
	 */
	private Integer isPush;
	
	/**
	 * 文本还是h5
	 */
	private Integer isTxt;

	public NewsNotice() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMiddleBanner() {
		return middleBanner;
	}

	public void setMiddleBanner(String middleBanner) {
		this.middleBanner = middleBanner;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCreateStaffId() {
		return createStaffId;
	}

	public void setCreateStaffId(Integer createStaffId) {
		this.createStaffId = createStaffId;
	}

	public String getCreateStaffName() {
		return createStaffName;
	}

	public void setCreateStaffName(String createStaffName) {
		this.createStaffName = createStaffName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getModifyStaffId() {
		return modifyStaffId;
	}

	public void setModifyStaffId(Integer modifyStaffId) {
		this.modifyStaffId = modifyStaffId;
	}

	public String getModifyStaffName() {
		return modifyStaffName;
	}

	public void setModifyStaffName(String modifyStaffName) {
		this.modifyStaffName = modifyStaffName;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIsPush() {
		return isPush;
	}

	public void setIsPush(Integer isPush) {
		this.isPush = isPush;
	}

	public Integer getIsTxt() {
		return isTxt;
	}

	public void setIsTxt(Integer isTxt) {
		this.isTxt = isTxt;
	}
	
}
