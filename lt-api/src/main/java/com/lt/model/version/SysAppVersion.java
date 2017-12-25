package com.lt.model.version;

import java.util.Date;

/**
 * app版本管理
 */
public class SysAppVersion implements java.io.Serializable {

    private Long id;
    /**
     * 渠道号
     */
    private String channel;

    /**
     * app类型 Android IOS
     *
     */
    private String platform;

    /**
     * 版本号
     */
    private String version;

    /**
     * app下载地址
     */
    private String url;

    /**
     * 更新内容
     */
    private String updateInfo;

    /**
     * 状态
     *  1 不需要更新
     *  2 需要更新
     *  3 强制更新
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    public SysAppVersion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
