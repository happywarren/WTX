package com.lt.business.core.dao.version;

import com.lt.model.version.SysAppVersion;

import java.util.List;

public interface SysAppVersionDao {

    public List<SysAppVersion> getAppVersion(SysAppVersion sysAppVersion);

}
