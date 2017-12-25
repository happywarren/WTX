package com.lt.adapter.adapter.news.func;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.news.INewsArticleApiService;
import com.lt.model.news.NewsNotice;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
/**
 * 
 * @author jingwb
 *
 */
@Service
public class GetNoticeListFunc extends BaseFunction {

	@Autowired
	private INewsArticleApiService newsArticleApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		int pageNoStr = StringTools.formatInt(paraMap.get("pageNo"), 1);
		int pageSizeStr = StringTools.formatInt(paraMap.get("pageSize"), 10);
		
		List<NewsNotice> data = newsArticleApiService.findNewsNoticeList(pageNoStr, pageSizeStr, 1);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
	}
}
