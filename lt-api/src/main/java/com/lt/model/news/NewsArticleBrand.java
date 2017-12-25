package com.lt.model.news;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guodawang on 2017/11/1.
 * @author
 */
@SuppressWarnings("serial")
public class NewsArticleBrand implements Serializable{

    private Integer id;
    /**
     * 品牌ID
     */
    private String brandId;
    /**
     * 发布人
     */
    private String creater;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 关联策略新闻ID
     */
    private String newsArticleId;
    /**
     * 创建时间
     */
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getNewsArticleId() {
        return newsArticleId;
    }

    public void setNewsArticleId(String newsArticleId) {
        this.newsArticleId = newsArticleId;
    }
}

