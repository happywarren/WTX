package com.lt.manager.dao.sys;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lt.manager.bean.sys.StaffDevice;

public interface StaffDeviceDao {

	public int findByUUID(@Param("uuid")String uuid,@Param("staffId")Integer staffId) ;

	public void save(StaffDevice userPermissions);
	
	public void deleteBystaffId(Integer staffId);
	
	public void deleteById(Integer id);
	
	public List<StaffDevice> findByParam(Map<String,Object> map);
	
	public int findByParamCount(Map<String,Object> map);

}
