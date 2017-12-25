package com.lt.user.core.dao.sqldb;

import com.lt.model.brand.BrandInfo;
import com.lt.model.user.Channel;
import org.springframework.data.repository.query.Param;

public interface IBrandInfoDao {

    BrandInfo getBrandInfoByCode(@Param("code") String code);
}
