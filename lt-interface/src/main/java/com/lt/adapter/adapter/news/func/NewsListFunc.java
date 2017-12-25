package com.lt.adapter.adapter.news.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.news.INewsArticleApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.news.NewsArticleVo;

/**
 * 项目名称：lt-interface 类名称：NewsListFunc 类描述： 创建人：yuanxin 创建时间：2017年3月3日 下午1:58:01
 */
@Component
public class NewsListFunc extends BaseFunction {

	@Autowired
	private INewsArticleApiService newsArticleApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		int pageNoStr = StringTools.formatInt(paraMap.get("pageNo"), 1);
		int pageSizeStr = StringTools.formatInt(paraMap.get("pageSize"), 20);
		String brandCode = StringTools.formatStr(paraMap.get("brand"), "") ;
		List<NewsArticleVo> data = newsArticleApiService.queryNewsList( brandCode,pageNoStr, pageSizeStr);

		return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
	}
}
