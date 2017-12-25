package com.lt.adapter.adapter.news.func;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.news.INewsArticleApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：NewsArticleCmtFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月3日 下午3:42:49      
*/
@Component
public class NewsArticleCmtFunc extends BaseFunction {
	
	@Autowired
	private INewsArticleApiService newsArticleApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
//		String newsArticleIdStr = paraMap.get("newsArticleId") == null ? "-1": paraMap.get("newsArticleId").toString();
//		String userIdStr = paraMap.get("userId") == null ? "1": paraMap.get("userId").toString();
//		String pageNoStr = paraMap.get("pageNo") == null ? "1": paraMap.get("pageNo").toString();
//		String pageSizeStr = paraMap.get("pageSize") == null ? "20": paraMap.get("pageSize").toString();
		
		Integer newsArticleId = StringTools.formatInt(paraMap.get("newsArticleId"), -1);
		String userId = String.valueOf(paraMap.get("userId"));
		int pageNo = StringTools.formatInt(paraMap.get("pageNo"), 1);
		int pageSize = StringTools.formatInt(paraMap.get("pageSize"), 20);
		if (newsArticleId <= 0) {
			//新闻标识错误
			return LTResponseCode.getCode(LTResponseCode.NA00001);
		}
		String data = newsArticleApiService.queryNewsArticleCmt(userId, newsArticleId
				, pageNo, pageSize);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
	}
}
