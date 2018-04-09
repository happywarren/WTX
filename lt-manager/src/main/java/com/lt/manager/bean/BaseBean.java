package com.lt.manager.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BaseBean implements Serializable {
    private String id;//主键
    private String ids;//id串1,2,3
    private Integer rows = 25;// 默认条数
    private Integer page = 1;// 默认页数
    private String sort;// 排序字段
    private String order;// 排序方式 desc asc
    private String flag;//附加标志字段

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if (rows != null && rows != 0) {
            this.rows = rows;
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page != null && page != 0) {
            this.page = page;
        }
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = "".equals(sort) ? null : sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = "".equals(order) ? null : order;
    }

    public Integer getOffset() {
        return (page - 1) * rows;
    }

    public Integer getLimit() {
        return rows;
    }

    public void setLimit(Integer limit) {
        this.rows = limit;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
