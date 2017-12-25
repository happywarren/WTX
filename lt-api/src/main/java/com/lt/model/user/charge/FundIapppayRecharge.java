package com.lt.model.user.charge;

import java.util.Date;

public class FundIapppayRecharge {
    private Integer id;

    private String userId;

    private String appid;

    private Integer waresid;

    private String waresname;

    private String cporderid;

    private Double price;

    private String currency;

    private String appuserid;

    private String cpprivateinfo;

    private String notifyurl;

    private String sign;

    private String signtype;

    private String bizCode;

    private Date createStamp;

    private Date updateStamp;

    private String reqTransdata;

    private String resTransdata;

    private String resultId;

    private String resultDesc;

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
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public Integer getWaresid() {
        return waresid;
    }

    public void setWaresid(Integer waresid) {
        this.waresid = waresid;
    }

    public String getWaresname() {
        return waresname;
    }

    public void setWaresname(String waresname) {
        this.waresname = waresname == null ? null : waresname.trim();
    }

    public String getCporderid() {
        return cporderid;
    }

    public void setCporderid(String cporderid) {
        this.cporderid = cporderid == null ? null : cporderid.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getAppuserid() {
        return appuserid;
    }

    public void setAppuserid(String appuserid) {
        this.appuserid = appuserid == null ? null : appuserid.trim();
    }

    public String getCpprivateinfo() {
        return cpprivateinfo;
    }

    public void setCpprivateinfo(String cpprivateinfo) {
        this.cpprivateinfo = cpprivateinfo == null ? null : cpprivateinfo.trim();
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl == null ? null : notifyurl.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public String getSigntype() {
        return signtype;
    }

    public void setSigntype(String signtype) {
        this.signtype = signtype == null ? null : signtype.trim();
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode == null ? null : bizCode.trim();
    }

    public Date getCreateStamp() {
        return createStamp;
    }

    public void setCreateStamp(Date createStamp) {
        this.createStamp = createStamp;
    }

    public Date getUpdateStamp() {
        return updateStamp;
    }

    public void setUpdateStamp(Date updateStamp) {
        this.updateStamp = updateStamp;
    }

    public String getReqTransdata() {
        return reqTransdata;
    }

    public void setReqTransdata(String reqTransdata) {
        this.reqTransdata = reqTransdata == null ? null : reqTransdata.trim();
    }

    public String getResTransdata() {
        return resTransdata;
    }

    public void setResTransdata(String resTransdata) {
        this.resTransdata = resTransdata == null ? null : resTransdata.trim();
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId == null ? null : resultId.trim();
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc == null ? null : resultDesc.trim();
    }
    
    
}