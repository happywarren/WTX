package com.lt.model.user.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息中心日志实体
 * @author jingwb
 *
 */
public class NotificationLog implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**用户id*/
	private String userId;
	/**标题*/
	private String title;
	/**内容*/
	private String content;
	/**创建时间*/
	private Date createDate;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
