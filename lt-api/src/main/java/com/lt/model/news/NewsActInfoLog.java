package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO 新闻活跃信息日志
 * @author XieZhibing
 * @date 2017年2月3日 下午3:30:21
 * @version <b>1.0.0</b>
 */
public class NewsActInfoLog implements Serializable{

	private Integer id;
	// 用户ID
	private String userId;
	// 用户昵称
	private String nick;
	// 日志类型 0收藏1点赞2分享
	private Integer logType;
	// 新闻ID
	private Integer newsArticleId;
	// 活跃信息来源IP
	private String ip;
	// 产生时间
	private Date createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public Integer getLogType() {
		return logType;
	}

	public Integer getNewsArticleId() {
		return newsArticleId;
	}

	public String getIp() {
		return ip;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	public void setNewsArticleId(Integer newsArticleId) {
		this.newsArticleId = newsArticleId;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
