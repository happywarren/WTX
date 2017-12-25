package com.lt.manager.dao.news;

import com.lt.model.news.NewsArticleBrand;
import com.lt.vo.news.NewsArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gdw
 */
public interface NewsArticleBrandDao {
    /**
     * 增
     * @param newsArticleBrand
     */
    void saveNewsArticleBrand(NewsArticleBrand newsArticleBrand);

    /**
     * 删
     * @param ids
     */
    void deleteNewsArticleBrandByNewsArticleId(List<String> ids );

    /**
     * 查
     * @param newsArticleId
     * @return 品牌名称集合
     */
    List<NewsArticleBrand> findBrandNameByNewsArticleId(String newsArticleId);

    /**
     * 删
     * @param brandId
     * @return
     */
    void deleteNewsArticleBrandByBrandId(String brandId);





}
