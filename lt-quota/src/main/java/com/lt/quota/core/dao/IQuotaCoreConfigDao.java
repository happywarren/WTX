package com.lt.quota.core.dao;

import com.lt.quota.core.model.QuotaCoreConfigModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IQuotaCoreConfigDao extends JpaRepository<QuotaCoreConfigModel, Long> {

    @Query(value = "select a.* from qtc_sys_config a where a.type = :type", nativeQuery = true)
    List<QuotaCoreConfigModel> getQuotaCoreConfigModelByName(@Param("type") String type);

}
