package com.lt.manager.dao.user;


import com.lt.manager.bean.user.InvestorFeeCfg;
import com.lt.manager.bean.user.InvestorProductConfig;

import java.util.List;

/**
 * 券商商品配置管理dao
 *
 * @author licy
 */
public interface InvestorProductConfigManageDao {


    /**
     * 添加券商商品配置
     *
     * @param investorProductConfig
     */
    public void insertInvestorProductConfig(InvestorProductConfig investorProductConfig);

    /**
     * 编辑券商商品配置
     * @param investorProductConfig
     * @return
     */
    public void updateInvestorProductConfig(InvestorProductConfig investorProductConfig);

    /**
     * 查询
     * @param investorProductConfig
     * @return
     */
    public InvestorProductConfig selectInvestorProductConfig(InvestorProductConfig investorProductConfig);

    /**
     * 删除券商商品配置
     *
     * @param id
     */
    public void deleteInvestorProductConfig(Integer id);

    /**
     * 删除券商商品配置
     *
     * @param productCode
     */
    public void deleteInvestorProductConfigByProduct(String productCode);

    /**
     * 未配置商品列表
     * @param investorProductConfig
     * @return
     */
    public List<InvestorProductConfig> selectNoCheckProductList(InvestorProductConfig investorProductConfig);

    /***
     * 券商商品配置列表
     * @param investorProductConfig
     */
    public List<InvestorProductConfig> selectInvestorProductConfigPage(InvestorProductConfig investorProductConfig);

    public Long selectInvestorProductConfigCount(InvestorProductConfig investorProductConfig);
}
