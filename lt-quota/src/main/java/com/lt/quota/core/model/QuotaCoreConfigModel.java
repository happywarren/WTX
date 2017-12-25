package com.lt.quota.core.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "qtc_sys_config")
public class QuotaCoreConfigModel implements Serializable {

    private static final long serialVersionUID = -7348903013835702184L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * 名称
     */
    @NotNull
    @Column(name = "name", length = 45)
    private String name;

    @NotNull
    @Column(name = "type", length = 45)
    private String type;

    /**
     * ip
     */
    @NotNull
    @Column(name = "host", length = 45)
    private String host;

    /**
     * 端口
     */
    @NotNull
    @Column(name = "port", length = 10)
    private Integer port;

    /**
     * 请求URL
     */
    @NotNull
    @Column(name = "req_url", length = 200)
    private String reqUrl;

    /**
     * 状态
     * <p>
     * 0 已停止
     * <p>
     * 1 已启动
     */
    @NotNull
    @Column(name = "status", length = 1)
    private Integer status;
    /**
     * 创建时间
     */
    @NotNull
    @Column(name = "create_time")
    private Date createTime;


    public QuotaCoreConfigModel() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
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
