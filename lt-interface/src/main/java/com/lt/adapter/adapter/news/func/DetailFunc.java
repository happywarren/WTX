package com.lt.adapter.adapter.news.func;

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
* 项目名称：lt-interface   
* 类名称：DetailFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月3日 下午3:37:19      
*/
@Component
public class DetailFunc extends BaseFunction {
	
	@Autowired
	private INewsArticleApiService newsArticleApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		int newsArticleId = StringTools.formatInt(paraMap.get("newsArticleId"),-1);
		String brandCode = StringTools.formatStr(paraMap.get("brand"),"");
		if ( newsArticleId <= 0) {
			//新闻标识错误
			return LTResponseCode.getCode(LTResponseCode.NA00001);
		}
		
		//查询资讯详情
		NewsArticleVo data = newsArticleApiService.queryNewsArticleDetail(brandCode, newsArticleId);
		if (!StringTools.isNotEmpty(data)) {
			//未找到对应资源
			return LTResponseCode.getCode(LTResponseCode.NA00015);
		}

		return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
	}
}
