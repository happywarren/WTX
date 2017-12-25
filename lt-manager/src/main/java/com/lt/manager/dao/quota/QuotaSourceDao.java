package com.lt.manager.dao.quota;


import com.lt.manager.bean.quota.QuotaSourceVo;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuotaSourceDao {

    List<QuotaSourceVo> listQuotaSource(@Param("type") String type);

    QuotaSourceVo getQuotaSource(QuotaSourceVo quotaSourceVo);

    void insertQuotaSource(QuotaSourceVo quotaSourceVo);

    QuotaSourceVo getQuotaSourceById(@Param("id") Long id);

    void startQuotaSource(@Param("id") Long id);

    void endQuotaSource(@Param("id") Long id);

    void deleteQuotaSource(@Param("id") Long id);
}
