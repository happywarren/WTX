package com.lt.user.core.dao.sqldb;


import com.lt.model.sys.SysConfig;

import java.util.List;

public interface ISysConfigDao {

    public List<SysConfig> selectSysConfigs();

}
