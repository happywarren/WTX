package com.lt.adapter.adapter.news.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.news.INewsArticleApiService;
import com.lt.api.user.IUserApiService;
import com.lt.model.news.NewsCmtReplyParam;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.TokenTools;
import com.lt.util.utils.model.Response;
import com.lt.util.utils.model.Token;

/**   
* 项目名称：lt-interface   
* 类名称：AddNewsArticleCmtFunc   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年3月3日 下午3:45:50      
*/
@Component
public class AddNewsArticleCmtFunc extends BaseFunction {
	
	@Autowired
	private IUserApiService userService;
	
	@Autowired
	private INewsArticleApiService newsArticleApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		String userId = String.valueOf(paraMap.get("userId"));
		Integer newsId = StringTools.formatInt(paraMap.get("newsId"), -1);
		Integer cmtId = StringTools.formatInt(paraMap.get("cmtId"), -1);
		Integer replyId = StringTools.formatInt(paraMap.get("replyId"), -1);
		String cmtComent = StringTools.formatStr(paraMap.get("cmtContent"), "");
		String userNick = StringTools.formatStr(paraMap.get("userNick"), "");
		String replyContent = StringTools.formatStr(paraMap.get("replyContent"), "");
		
		NewsCmtReplyParam newsCmtReplyParam = new NewsCmtReplyParam();
		
		newsCmtReplyParam.setUserId(userId);
		newsCmtReplyParam.setReplyUserId(userId);
		newsCmtReplyParam.setNewsId(newsId);
		newsCmtReplyParam.setCmtId(cmtId);
		newsCmtReplyParam.setReplyId(replyId);
		newsCmtReplyParam.setCmtContent(cmtComent);
		newsCmtReplyParam.setUserNick(userNick);
		newsCmtReplyParam.setReplyContent(replyContent);
		
		//查询用户昵称
		UserBaseInfo userinfo = userService.findUserByUserId(userId);
		if(StringTools.isNotEmpty(userinfo)){
			newsCmtReplyParam.setUserNick(userinfo.getNickName());
		}
		Response response = newsArticleApiService.addNewsCmtReply(newsCmtReplyParam);
		return response;
	}
}
