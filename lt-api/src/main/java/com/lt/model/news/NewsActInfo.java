package com.lt.model.news;

import java.io.Serializable;

/**
 * 
 * TODO 描述:新闻文章活跃信息
 * @author XieZhibing
 * @date 2017年2月3日 下午3:30:05
 * @version <b>1.0.0</b>
 */
public class NewsActInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 目标ID
	private Integer targetId;
	// 类型 0表示新闻或文章，默认0
	private Integer targetType;
	// 初始化阅读量
	private Integer initReadCount;
	// 初始化收藏量
	private Integer initCollectCount;
	// 初始化点赞量
	private Integer initLikeCount;
	// 实际阅读量
	private Integer realReadCount;
	// 实际收藏量
	private Integer realCollectCount;
	// 实际点赞量
	private Integer realLikeCount;
	// 评论量
	private Integer realCommentCount;
	// 回复量
	private Integer realReplyCount;
	// 初始化分享量
	private Integer initShareCount;
	// 实际分享量
	private Integer realShareCount;

	public NewsActInfo(){
		
	}
	
	public NewsActInfo(Integer zero){
		this.initCollectCount = zero;
		this.initLikeCount = zero;
		this.initReadCount = zero;
		this.initShareCount = zero;
		this.realCollectCount = zero;
		this.realCommentCount =zero;
		this.realLikeCount = zero;
		this.realReadCount = zero;
		this.realReplyCount = zero;
		this.realShareCount = zero;
	}
	// 非数据库字段，是否更新过
	// 用来标记是否需要将缓存信息同步到数据库
	private boolean isChanged = false;

	public Integer getInitReadCount() {
		return initReadCount;
	}

	public Integer getInitCollectCount() {
		return initCollectCount;
	}

	public Integer getInitLikeCount() {
		return initLikeCount;
	}

	public Integer getRealReadCount() {
		return realReadCount;
	}

	public Integer getRealCollectCount() {
		return realCollectCount;
	}

	public Integer getRealLikeCount() {
		return realLikeCount;
	}

	public Integer getRealCommentCount() {
		return realCommentCount;
	}

	public Integer getRealReplyCount() {
		return realReplyCount;
	}

	public Integer getInitShareCount() {
		return initShareCount;
	}

	public Integer getRealShareCount() {
		return realShareCount;
	}

	public void setInitReadCount(Integer initReadCount) {
		this.initReadCount = initReadCount;
	}

	public void setInitCollectCount(Integer initCollectCount) {
		this.initCollectCount = initCollectCount;
	}

	public void setInitLikeCount(Integer initLikeCount) {
		this.initLikeCount = initLikeCount;
	}

	public void setRealReadCount(Integer realReadCount) {
		this.realReadCount = realReadCount;
	}

	public void setRealCollectCount(Integer realCollectCount) {
		this.realCollectCount = realCollectCount;
	}

	public void setRealLikeCount(Integer realLikeCount) {
		this.realLikeCount = realLikeCount;
	}

	public void setRealCommentCount(Integer realCommentCount) {
		this.realCommentCount = realCommentCount;
	}

	public void setRealReplyCount(Integer realReplyCount) {
		this.realReplyCount = realReplyCount;
	}

	public void setInitShareCount(Integer initShareCount) {
		this.initShareCount = initShareCount;
	}

	public void setRealShareCount(Integer realShareCount) {
		this.realShareCount = realShareCount;
	}

	public Integer getTargetId() {
		return targetId;
	}

	public Integer getTargetType() {
		return targetType;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}
}
