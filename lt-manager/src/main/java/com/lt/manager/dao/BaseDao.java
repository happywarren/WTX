package com.lt.manager.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 表操作基础dao
 *
 * @author lvx
 * @created 2017/7/19
 */
public interface BaseDao <T, PK>{

    Integer count(@Param("vo") T vo);

    List<T> query(@Param("vo") T vo);

    T load(@Param("id") PK id);

    void insert(@Param("vo") T vo);

    void update(@Param("vo") T vo);

    void delete(@Param("id") PK id);

    List<T> queryPage(@Param("vo") T vo);
}
