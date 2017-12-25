package com.lt.manager.service.impl.user;

import com.github.pagehelper.Page;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.manager.bean.user.InvestorFeeCfg;
import com.lt.manager.bean.user.ProductAccountMapper;
import com.lt.manager.dao.user.InvestorProductConfigManageDao;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.user.IInvestorFeeCfgManageService;
import com.lt.manager.service.user.IInvestorProductConfigManageService;
import com.lt.manager.bean.user.InvestorProductConfig;
import com.lt.model.product.Product;
import com.lt.util.LoggerTools;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 券商商品配置管理
 */
@Service
public class InvestorProductConfigManageServiceImpl implements IInvestorProductConfigManageService {

    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private InvestorProductConfigManageDao investorProductConfigManageDao;

    @Override
    @Transactional
    public boolean batchAddInvestorProductConfig(String creater, List<InvestorProductConfig> investorProductConfigList) {

        for (InvestorProductConfig investorProductConfig : investorProductConfigList) {
            String investorAccountId = investorProductConfig.getInvestorAccountId();
            String productCode = investorProductConfig.getProductCode();
            String counterFee = investorProductConfig.getCounterFee();
            if (StringTools.isEmpty(investorAccountId) || StringTools.isEmpty(productCode) || StringTools.isEmpty(counterFee)) {
                return false;
            }
            investorProductConfig.setCreater(creater);
            investorProductConfig.setCreateTime(new Date());
            investorProductConfigManageDao.insertInvestorProductConfig(investorProductConfig);
        }
        return true;
    }

    @Override
    public void editInvestorProductConfig(InvestorProductConfig investorProductConfig) {
        investorProductConfigManageDao.updateInvestorProductConfig(investorProductConfig);
    }

    @Override
    @Transactional
    public void deleteInvestorProductConfig(String ids) {
        for (String id : ids.split(",")){
            investorProductConfigManageDao.deleteInvestorProductConfig(Integer.parseInt(id));
        }
    }

    @Override
    public List<InvestorProductConfig> selectNoCheckProductList(InvestorProductConfig investorProductConfig) {
        return investorProductConfigManageDao.selectNoCheckProductList(investorProductConfig);
    }

    @Override
    public List<InvestorProductConfig> findInvestorProductConfigList(InvestorProductConfig investorProductConfig) {
        List<InvestorProductConfig> investorProductConfigList = investorProductConfigManageDao.selectInvestorProductConfigPage(investorProductConfig);
        return investorProductConfigList;
    }

    @Override
    public Page<InvestorProductConfig> findInvestorProductConfig(InvestorProductConfig param) {
        List<InvestorProductConfig> investorProductConfigList = investorProductConfigManageDao.selectInvestorProductConfigPage(param);
        Long count = investorProductConfigManageDao.selectInvestorProductConfigCount(param);
        Page<InvestorProductConfig> page = new Page<InvestorProductConfig>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.addAll(investorProductConfigList);
        page.setTotal(count);
        return page;
    }
}