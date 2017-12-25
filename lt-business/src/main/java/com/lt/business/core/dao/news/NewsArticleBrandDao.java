package com.lt.business.core.dao.news;

import com.lt.vo.news.NewsArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gdw
 */
public interface NewsArticleBrandDao {
    List<NewsArticleVo> getNewsByBrandIdSection(@Param("brandId") String brandId, @Param("section") Integer section, @Param("index") Integer index,
                                                @Param("size") Integer size, @Param("topId") Integer topId);

    NewsArticleVo getNewsArticleVoById(@Param("newsId") Integer newsId, @Param("brandId") String brandId);

    NewsArticleVo getLastTopNews(@Param("brandId") String brandId);

    List<NewsArticleVo> getNewsBySection(@Param("brandId") String brandId,@Param("section") Integer section, @Param("index") Integer index,
                                         @Param("size") Integer size, @Param("topId") Integer topId);
}
