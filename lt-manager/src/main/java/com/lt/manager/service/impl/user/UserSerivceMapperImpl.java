package com.lt.manager.service.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lt.manager.bean.user.UserServiceMapper;
import com.lt.manager.dao.user.UserServiceMapperDao;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.user.IUserServiceMapper;
import com.lt.util.utils.StringTools;

/**
 * 银行卡管理实现类
 * @author licy
 *
 */
@Service
public class UserSerivceMapperImpl implements IUserServiceMapper {

	@Autowired
	private UserServiceMapperDao userServiceMapperDao;
	
    @Autowired
    private IProductManageService productManageService;
    
	@Override
	public List<UserServiceMapper> findUserServiceById(String userId) {
		
		return userServiceMapperDao.selectUserServiceMapperByUserId(userId);
	}
	@Override
	public List<String> findUserServiceByCode(String serviceCode){
		return userServiceMapperDao.findUserServiceByCode(serviceCode);
	}
	
	@Override
	@Transactional
	public void modifyUserServiceMapper(String userId, String codes,String all) {
		//删除该用户支持的服务
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("all", all);
		userServiceMapperDao.deleteUserService(map);
		//添加新服务
		if(!StringTools.isEmpty(codes)){//服务不为空
			String[] codeArray = codes.split(",");
			for(String code:codeArray){
				map.put("code", code);
				userServiceMapperDao.insertServiceMappper(map);
			}
		}
		userServiceMapperDao.updateUserInvestorId(userId);
		productManageService.initInvestorFeeConfig(userId);
	}
}
