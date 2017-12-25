package com.lt.tms.quota.bean;


public class ErrorBean {

    public final static String QUOTA = "quota";

    public final static String TRADE = "trade";

    /**
     * quota
     *
     * trade
     */
    private String errorType;

    private String errorCode;

    private String errorMessage;

    public ErrorBean() {

    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
