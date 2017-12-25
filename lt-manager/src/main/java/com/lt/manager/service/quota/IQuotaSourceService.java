package com.lt.manager.service.quota;

import com.lt.manager.bean.quota.QuotaSourceVo;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IQuotaSourceService {

    List<QuotaSourceVo> listQuotaSource(String type);

    void insertQuotaSource(QuotaSourceVo quotaSourceVo);

    QuotaSourceVo getQuotaSource(Long id);

    void startQuotaSource(Long id);

    void endQuotaSource(Long id);

    void deleteQuotaSource(Long id);

}
