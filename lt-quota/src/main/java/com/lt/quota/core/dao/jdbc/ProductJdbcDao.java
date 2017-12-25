package com.lt.quota.core.dao.jdbc;

import com.lt.quota.core.dao.IProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductJdbcDao implements IProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getPreProduct(Integer plate) {
        String sql = "select p.id,p.short_code as shortCode,p.product_code as productCode,p.quota_exchange_code as exchangeCode from product p,exchange_info e where p.exchange_id = e.id and e.plate = ? and p.expiration_begin_time > NOW() AND p.expiration_begin_time < ADDDATE(NOW(),INTERVAL 5 DAY) and p.product_type_id = 1";
        return jdbcTemplate.queryForList(sql, new Object[]{plate});
    }

    @Override
    public List<Map<String, Object>> getCurrentProduct(Integer plate) {
        String sql = "select p.id as id,p.short_code as shortCode,p.product_code as productCode,p.quota_exchange_code as exchangeCode from product p ,exchange_info e where p.exchange_id = e.id and e.plate = ? and  p.expiration_begin_time <= NOW() and p.expiration_time >= NOW() and p.product_type_id = 1";
        return jdbcTemplate.queryForList(sql, new Object[]{plate});
    }

    @Override
    public List<Map<String, Object>> getExpireProduct(Integer plate) {
        String sql = "select p.id,p.short_code as shortCode,p.product_code as productCode,p.quota_exchange_code as exchangeCode from product p ,exchange_info e where p.exchange_id = e.id and e.plate = ? and expiration_time < NOW() and expiration_time > SUBDATE(NOW(),INTERVAL 1 DAY) and product_type_id = 1";
        return jdbcTemplate.queryForList(sql, new Object[]{plate});
    }

    @Override
    public List<Map<String, Object>> getProductList(Long productTypeId) {
        String sql = "select product_code from product where product_type_id = ? ";
        return jdbcTemplate.queryForList(sql, new Object[]{productTypeId});
    }

    @Override
    public List<Map<String, Object>> getCoinProductList() {
        String sql = "select p.product_code as productCode from product p ,product_type pt where p.product_type_id = pt.id and pt.`code` = '10004' and p.expiration_begin_time <= ADDDATE(NOW(),INTERVAL 2 DAY) and p.expiration_time >= NOW()";
        return jdbcTemplate.queryForList(sql);
    }
}
