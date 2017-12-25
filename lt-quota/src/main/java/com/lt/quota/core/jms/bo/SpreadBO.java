package com.lt.quota.core.jms.bo;

/**
 * @author mcsong
 * @create 2017-10-19 13:42
 */
public class SpreadBO implements java.io.Serializable {

    private String cmd;

    private String productCode;

    public SpreadBO() {

    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
