package com.lt.api.version;

import com.lt.model.version.SysAppVersion;
import com.lt.util.utils.model.Response;

/**
 * 项目名称：lt-api
 * 类名称：SysVersionApiService
 * 类描述：系统版本处理类
 * 创建人：yuanxin
 * 创建时间：2017年3月21日 下午3:27:50
 */
public interface SysVersionApiService {

    /**
     * 获取系统安卓版本
     *
     * @return
     * @throws
     * @return: Response
     * @author yuanxin
     * @Date 2017年3月21日 下午3:31:03
     */
    public Response getSysVersion();

    /**
     * 获取系统IOS版本
     *
     * @return
     * @throws
     * @return: Response
     * @author yuanxin
     * @Date 2017年3月21日 下午3:31:42
     */
    public Response getSysVersionIOS();


    /**
     * 版本检测
     *
     * @param sysAppVersion
     *
     * @return
     */
    public SysAppVersion checkVersion(SysAppVersion sysAppVersion);
}
