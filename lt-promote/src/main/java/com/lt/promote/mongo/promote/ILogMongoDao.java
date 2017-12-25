package com.lt.promote.mongo.promote;



import com.github.pagehelper.Page;
import com.lt.model.logs.LogInfo;


public interface ILogMongoDao {
	//日志保存
	public void addLog(LogInfo info);
	
	//日志查询
	public Page<LogInfo> findLogInfoByParam(int end,int start,String method,String ip,String deviceType);
}
