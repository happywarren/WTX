package com.lt.manager.service.impl;

import com.github.pagehelper.Page;
import com.lt.manager.bean.BaseBean;
import com.lt.manager.dao.BaseDao;
import com.lt.manager.service.BaseService;

import java.util.List;

/**
 * 描述: 表操作基础服务实现类
 *
 * @author lvx
 * @created 2017/7/19
 */
public abstract class BaseServiceImpl <T extends BaseBean, PK> implements BaseService<T, PK> {

    protected abstract BaseDao<T, PK> getDao();

    @Override
    public T load(PK id) {
        return getDao().load(id);
    }

    @Override
    public Integer count(T vo) {
        return getDao().count(vo);
    }

    @Override
    public void insert(T vo) {
        getDao().insert(vo);
    }

    @Override
    public void update(T vo) {
        getDao().update(vo);
    }

    @Override
    public List<T> query(T vo) {
        return getDao().query(vo);
    }

    @Override
    public void delete(PK id) {
        getDao().delete(id);
   }

    @Override
    public Page<T> queryPage(T vo) {

        Page<T> tPage = new Page<>();
        tPage.setPageNum(vo.getPage());
        tPage.setPageSize(vo.getRows());

        List<T> list = getDao().queryPage(vo);

        tPage.addAll(list);
        tPage.setTotal(getDao().count(vo));
        return tPage;
    }
}
