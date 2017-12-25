package com.lt.vo.news;

import java.io.Serializable;
import java.util.List;

import com.lt.model.news.NewsCmtReply;

/**
 * 
 * TODO 新闻评论回复数据
 * @author XieZhibing
 * @date 2017年2月3日 下午3:35:15
 * @version <b>1.0.0</b>
 */
public class NewsCmtReplyVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer cmtId;
	private List<NewsCmtReply> newsCmtReplyList;
	
	public NewsCmtReplyVo(Integer cmtId, List<NewsCmtReply> newsCmtReplyList) {
		super();
		this.cmtId = cmtId;
		this.newsCmtReplyList = newsCmtReplyList;
	}

	public Integer getCmtId() {
		return cmtId;
	}

	public List<NewsCmtReply> getNewsCmtReplyList() {
		return newsCmtReplyList;
	}

	public void setCmtId(Integer cmtId) {
		this.cmtId = cmtId;
	}

	public void setNewsCmtReplyList(List<NewsCmtReply> newsCmtReplyList) {
		this.newsCmtReplyList = newsCmtReplyList;
	}

}
