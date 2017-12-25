package com.lt.user.charge.dao.sqldb;

import com.lt.model.user.charge.DinpayExtraInfo;

public interface DinpayExtraInfoDao {
	
    int deleteByPrimaryKey(Integer id);

    int insert(DinpayExtraInfo record);

    int insertSelective(DinpayExtraInfo record);

    DinpayExtraInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DinpayExtraInfo record);

    int updateByPrimaryKey(DinpayExtraInfo record);
    
    int updateByOrderNo(DinpayExtraInfo record);
    
}