package com.lt.business.core.service.version;

import com.lt.business.core.dao.version.SysAppVersionDao;
import com.lt.model.version.SysAppVersion;
import com.lt.util.utils.StringTools;
import com.lt.vo.news.SystemConfigVO;
import org.springframework.beans.factory.annotation.Autowired;

import com.lt.api.version.SysVersionApiService;
import com.lt.business.core.dao.version.SysVersionDao;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

import java.util.List;

/**
 * 项目名称：lt-business
 * 类名称：SysVersionServiceImpl
 * 类描述：系统版本实现类
 * 创建人：yuanxin
 * 创建时间：2017年3月21日 下午3:28:36
 */
public class SysVersionServiceImpl implements SysVersionApiService {

    @Autowired
    private SysVersionDao sysVersionDao;

    @Autowired
    private SysAppVersionDao sysAppVersionDao;

    @Override
    public Response getSysVersion() {
        // TODO Auto-generated method stub
        SystemConfigVO configVO = sysVersionDao.getVersion();
        Response response = null;
        if (configVO == null) {
            response = new Response(LTResponseCode.ER400, "查找数据失败");
        } else {
            response = new Response(LTResponseCode.SUCCESS, "查询成功", configVO);
        }
        return response;
    }

    @Override
    public Response getSysVersionIOS() {
        SystemConfigVO configVO = sysVersionDao.getVersionIOS();
        Response response = null;
        if (configVO == null) {
            response = new Response(LTResponseCode.ER400, "查找数据失败");
        } else {
            response = new Response(LTResponseCode.SUCCESS, "查询成功", configVO);
        }
        return response;
    }


    @Override
    public SysAppVersion checkVersion(SysAppVersion param) {
        List<SysAppVersion> sysAppVersionList = sysAppVersionDao.getAppVersion(param);
        int status = 0;
        int inputVersion = StringTools.formatInt(param.getVersion().replace(".", ""), 0);
        for (SysAppVersion sysAppVersion : sysAppVersionList) {
            int version = StringTools.formatInt(sysAppVersion.getVersion().replace(".", ""), 0);
            if (inputVersion < version && sysAppVersion.getStatus() == 3) {
                status = 3;
                break;
            }
        }

        SysAppVersion sub = null;
        if (StringTools.isNotEmpty(sysAppVersionList)) {
            sub = sysAppVersionList.get(sysAppVersionList.size() - 1);
        }

        SysAppVersion sysAppVersion = new SysAppVersion();
        if (!StringTools.isNotEmpty(sub)){
            sysAppVersion.setStatus(1);
            return sysAppVersion;
        }
        int version = StringTools.formatInt(sub.getVersion().replace(".", ""), 0);
        if (version <= inputVersion){
            sysAppVersion.setStatus(1);
            return sysAppVersion;
        }

        sysAppVersion.setStatus(sub.getStatus());
        if (status == 3){
            sysAppVersion.setStatus(3);
        }
        sysAppVersion.setVersion(sub.getVersion());
        sysAppVersion.setUrl(sub.getUrl());
        sysAppVersion.setUpdateInfo(sub.getUpdateInfo());
        return sysAppVersion;
    }


}
