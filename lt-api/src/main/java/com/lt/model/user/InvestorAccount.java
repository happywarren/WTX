package com.lt.model.user;

import java.io.Serializable;

/**
 * 描述:券商基本信息对象
 *
 * @author lvx
 * @created 2017/10/16
 */
public class InvestorAccount implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5002565418466339323L;
    /**
     *  ID
     */
    private Integer id;
    /**
     *  券商ID
     */
    private String userId;
    /**
     *  账户名称
     */
    private String accountName;
    /**
     *  证券账号编码
     */
    private String securityCode;
    /**
     *  服务器IP地址
     */
    private String serverIp;
    /**
     *  服务器端口号
     */
    private String serverPort;
    /**
     *  密码
     */
    private String passwd;

    /**
     *  权重
     */
    private Integer weight ;

    /**
     *  在线状态 1 在线 0 不在线
     */
    private Integer onlineStatus ;

    /**
     *  备注
     */
    private String remark ;

    /**
     *  账户类型: 1 外盘账户 0内盘账户 2 差价合约
     */
    private Integer plateType;

    /**
     *  是否默认
     */
    private Integer idDefault ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getPlateType() {
        return plateType;
    }

    public void setPlateType(Integer plateType) {
        this.plateType = plateType;
    }

    public Integer getIdDefault() {
        return idDefault;
    }

    public void setIdDefault(Integer idDefault) {
        this.idDefault = idDefault;
    }
}
