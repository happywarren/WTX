package com.lt.manager.service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.BaseBean;

import java.util.List;

/**
 * 描述: 表操作基础服务
 *
 * @author lvx
 * @created 2017/7/19
 */
public interface BaseService<T extends BaseBean, PK>{

    Page<T> queryPage(T vo);

    Integer count(T vo);

    List<T> query(T vo);

    T load(PK id);

    void insert(T vo);

    void update(T vo);

    void delete(PK id);
}
