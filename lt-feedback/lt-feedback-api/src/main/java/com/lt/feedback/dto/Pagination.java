package com.lt.feedback.dto;


import java.util.ArrayList;
import java.util.List;

public class Pagination<T> implements java.io.Serializable{

    private Integer totalSize = 0; // 总记录数
    private Integer pageSize = 10; // 每页显示记录数
    private Integer totalPage = 1; // 总页数
    private Integer currentPage = 1; // 当前页
    private Boolean firstPage = false;        //是否为第一页
    private Boolean lastPage = false;         //是否为最后一页
    private List<T> dataList = new ArrayList<T>(); //对象记录结果集

    public Pagination(){

    }

    public Pagination(Integer totalSize, Integer currentPage) {
        init(totalSize, currentPage, pageSize);
    }

    public Pagination(Integer currentPage, Integer totalSize, Integer pageSize) {
        init(totalSize, currentPage, pageSize);
    }

    private void init(Integer totalSize, Integer currentPage, Integer pageSize) {
        //设置基本参数
        this.totalSize = totalSize;
        this.pageSize = pageSize;
        this.totalPage = (this.totalSize - 1) / this.pageSize + 1;

        //根据输入可能错误的当前号码进行自动纠正
        if (currentPage < 1) {
            this.currentPage = 1;
        } else if (currentPage > this.totalPage) {
            this.currentPage = this.totalPage;
        } else {
            this.currentPage = currentPage;
        }
        //以及页面边界的判定
        judgePageBoudary();
    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        firstPage = currentPage.intValue() == 1;
        lastPage = currentPage.equals(totalPage);
    }


    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * 得到当前页的内容
     *
     * @return {List}
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 得到记录总数
     *
     * @return {Integer}
     */
    public Integer getTotalSize() {
        return totalSize;
    }

    /**
     * 得到每页显示多少条记录
     *
     * @return {Integer}
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 得到页面总数
     *
     * @return {Integer}
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     * 得到当前页号
     *
     * @return {Integer}
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    public Boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        this.firstPage = firstPage;
    }

    public Boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }
}
