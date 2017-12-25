package com.lt.business.core.dao.brand;

import com.github.pagehelper.Page;
import com.lt.model.news.*;
import com.lt.vo.news.NewsArticleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BrandDao {



	String findBrandIdByBrandCode(String brandCode);

}
