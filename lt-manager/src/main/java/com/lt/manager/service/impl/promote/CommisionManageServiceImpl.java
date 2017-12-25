package com.lt.manager.service.impl.promote;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.promote.CommisionParamVo;
import com.lt.manager.dao.promote.CommisionManageDao;
import com.lt.manager.service.promote.ICommisionManageService;
import com.lt.model.promote.CommisionOptcode;

/**
 * 佣金管理实现类
 * @author jingwb
 *
 */
@Service
public class CommisionManageServiceImpl implements ICommisionManageService{

	@Autowired
	private CommisionManageDao commisionManageDao;
	
	@Override
	public Page<CommisionParamVo> queryCommisionIoPage(CommisionParamVo param) {
		Page<CommisionParamVo> page = new Page<CommisionParamVo>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.setTotal(commisionManageDao.selectCommisionIoCount(param));
		page.addAll(commisionManageDao.selectCommisionIoPage(param));
		return page;
	}

	@Override
	public Page<CommisionParamVo> queryCommisionFlowPage(CommisionParamVo param) {
		Page<CommisionParamVo> page = new Page<CommisionParamVo>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.setTotal(commisionManageDao.selectCommisionFlowCount(param));
		page.addAll(commisionManageDao.selectCommisionFlowPage(param));
		return page;
	}

	@Override
	public Map<String, Object> getCommisionFlowData(CommisionParamVo param) {
		return commisionManageDao.selectCommisionFlowData(param);
	}

	@Override
	public List<CommisionOptcode> getFirstOptcode() {
		return commisionManageDao.getCommisionOptcodes(new CommisionOptcode());
	}

	@Override
	public List<CommisionOptcode> getSecondOptcode(CommisionOptcode code) {
		return commisionManageDao.getCommisionOptcodes(code);
	}

}
