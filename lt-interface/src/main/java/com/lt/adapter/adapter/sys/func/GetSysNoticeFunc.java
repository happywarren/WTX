package com.lt.adapter.adapter.sys.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.sys.ISysApiService;
import com.lt.model.sys.SysNotice;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

@Service
public class GetSysNoticeFunc extends BaseFunction{

	@Autowired
	private ISysApiService sysApiServiceImpl;
	@Override
	public Response response(Map<String, Object> paraMap) {
		List<SysNotice> list = sysApiServiceImpl.getSysNoticeList();
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
	}
}
