package com.lt.quota.core.appServer.bean.request;

/**
 * 设备登出
 */
public class LoginOutRequest extends BaseRequest {
    private String recordIP;
    private String recordImei;
    private String recordLoginMode;
    private String recordCarrierOperator;
    private String recordAccessMode;
    private String recordDevice;
    private String recordVersion;

    public String getRecordIP() {
        return recordIP;
    }

    public void setRecordIP(String recordIP) {
        this.recordIP = recordIP;
    }

    public String getRecordImei() {
        return recordImei;
    }

    public void setRecordImei(String recordImei) {
        this.recordImei = recordImei;
    }

    public String getRecordLoginMode() {
        return recordLoginMode;
    }

    public void setRecordLoginMode(String recordLoginMode) {
        this.recordLoginMode = recordLoginMode;
    }

    public String getRecordCarrierOperator() {
        return recordCarrierOperator;
    }

    public void setRecordCarrierOperator(String recordCarrierOperator) {
        this.recordCarrierOperator = recordCarrierOperator;
    }

    public String getRecordAccessMode() {
        return recordAccessMode;
    }

    public void setRecordAccessMode(String recordAccessMode) {
        this.recordAccessMode = recordAccessMode;
    }

    public String getRecordDevice() {
        return recordDevice;
    }

    public void setRecordDevice(String recordDevice) {
        this.recordDevice = recordDevice;
    }

    public String getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(String recordVersion) {
        this.recordVersion = recordVersion;
    }
}
