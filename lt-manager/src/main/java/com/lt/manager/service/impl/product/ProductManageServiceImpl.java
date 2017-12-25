package com.lt.manager.service.impl.product;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.lt.api.trade.IOrderScoreApiService;
import com.lt.enums.product.ProductTypeEnum;
import com.lt.manager.bean.product.DigitalCoinConfigVO;
import com.lt.manager.bean.product.ProductRiskConfigVO;
import com.lt.manager.dao.product.*;
import com.lt.manager.service.product.IProductRiskConfigManageService;

import com.lt.util.utils.jms.MessageQueueProducer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.trade.IOrderApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.product.ProductMarketEnum;
import com.lt.manager.bean.enums.product.ProductEnum;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.bean.product.ProductParamVO.ProductCode;
import com.lt.manager.bean.user.InvestorFeeCfg;
import com.lt.manager.bean.user.ProductAccountMapper;
import com.lt.manager.dao.user.InvestorFeeCfgManageDao;
import com.lt.manager.dao.user.ProductAccountMapperDao;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.user.IUserServiceMapper;
import com.lt.model.product.Product;
import com.lt.model.product.ProductQuotaConfig;
import com.lt.model.product.ProductTagInfo;
import com.lt.model.product.ProductTagMapper;
import com.lt.model.product.ProductTimeConfig;
import com.lt.model.product.ProductTradeConfig;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.ProductVo;

/**
 * 商品管理service实现类
 *
 * @author jingwb
 */
@Service
public class ProductManageServiceImpl implements IProductManageService {
    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IUserServiceMapper userServiceMapper;
    @Autowired
    private InvestorFeeCfgManageDao investorFeeCfgManageDao;
    @Autowired
    private ProductManageDao productManageDao;
    @Autowired
    private ProductTradeConfigManageDao productTradeConfigManageDao;
    @Autowired
    private ProductTimeConfigManageDao productTimeConfigManageDao;
    @Autowired
    private ProductQuotaConfigManageDao productQuotaConfigManageDao;
    @Autowired
    private ProductTagManageDao productTagManageDao;
    @Autowired
    private IOrderApiService orderApiService;
    @Autowired
    private IOrderScoreApiService orderScoreApiService;
    @Autowired
    private IProductApiService productApiService;
    @Autowired
    private IProductRiskConfigManageService productRiskConfigManageService;
    @Autowired
    private ProductAccountMapperDao productAccountMapperDao;
    @Autowired
    private ProductRiskConfigManageDao productRiskConfigManageDao;
    @Autowired
    private DigitalCoinConfigManageDao digitalCoinConfigManageDao;
    @Resource
    private MessageQueueProducer spreadModifyMsgProducer;


    //刷新交易
    private Boolean isLoadTrade_ = true;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addProduct(ProductParamVO param) throws Exception {
        logger.info("param.getJsonTradeConfig() = {}", param.getJsonTradeConfig());
        //解析商品配置实体
        ProductTradeConfig tradeConfig = JSONObject.parseObject(param.getJsonTradeConfig(), ProductTradeConfig.class);
        //解析交易时间配置集（夏令时）
        List<ProductTimeConfig> timeConfigList = JSONObject.parseArray(param.getJsonTimeConfigList(), ProductTimeConfig.class);
        //解析交易时间配置集（冬令时）
        List<ProductTimeConfig> timeConfigWinterList = JSONObject.parseArray(param.getJsonTimeConfigWinterList(), ProductTimeConfig.class);
        //解析交易时间配置集
        List<ProductRiskConfigVO> riskConfigVOList = JSONObject.parseArray(param.getJsonRiskConfigList(), ProductRiskConfigVO.class);
        //解析行情配置
        ProductQuotaConfig quotaConfig = JSONObject.parseObject(param.getJsonQuotaConfig(), ProductQuotaConfig.class);

        if(timeConfigList == null || timeConfigList.size() == 0){
            throw new LTException(LTResponseCode.PR00001);
        }

        if(timeConfigWinterList == null || timeConfigWinterList.size() == 0){
            throw new LTException(LTResponseCode.PR00001);
        }
        //如果市场状态不为开市，则将is_operate置为0
        if (!Objects.equals(param.getMarketStatus(), ProductEnum.MARKET_STATUS_OPEN.getValue())) {
            param.setIsOperate(0);//不允许操作
        } else {
            param.setIsOperate(1);//允许操作
        }

        //判断添加的商品是否已存在
        ProductParamVO pc = new ProductParamVO();
        pc.setCode(param.getCode());
        Integer count = productManageDao.selectProCountByCode(pc);
        if (count > 0) {
            throw new LTException(LTResponseCode.PRJ0002);
        }
        //修改原有商品排序加1
        productManageDao.updateProductSort();
        //添加商品
        productManageDao.inserProduct(param);

        dealTimeConfig(param, timeConfigList);
        dealTimeConfig(param, timeConfigWinterList);

        //将行情浮动限制转为小数
        if (StringTools.isNotEmpty(tradeConfig.getFloatLimit())) {
            tradeConfig.setFloatLimit(DoubleUtils.div(tradeConfig.getFloatLimit(), 100.0));
        }

        //添加差价合约数字货币配置
        if (ProductTypeEnum.CONTRACT.getCode().equals(param.getProductTypeCode())) {
            DigitalCoinConfigVO digitalCoinConfigVO = new DigitalCoinConfigVO();
            digitalCoinConfigVO.setProductCode(param.getCode());
            digitalCoinConfigVO.setContractSize(param.getContractSize());
            digitalCoinConfigVO.setMaxLever(param.getMaxLever());
            digitalCoinConfigVO.setMaxPositionPerAccount(param.getMaxPositionPerAccount());
            digitalCoinConfigVO.setMaxSingleOpenPosition(param.getMaxSingleOpenPosition());
            digitalCoinConfigVO.setSpread(param.getSpread());
            digitalCoinConfigVO.setBrandPosition(param.getBrandPosition());
            digitalCoinConfigManageDao.insertCoinConfig(digitalCoinConfigVO);
            //加入点差变化商品列表，在redis加载完成后推送java行情服务器
            param.addSpreadModProductList(param.getCode());
        }

        //添加商品配置
        tradeConfig.setProductId(Integer.valueOf(param.getId()));

        productTradeConfigManageDao.insertProductTradeConfig(tradeConfig);

        //添加交易时间
        if (StringTools.isNotEmpty(timeConfigList)) {
            productTimeConfigManageDao.insertProTimeCfgs(timeConfigList);

        }
        //添加交易时间
        if (StringTools.isNotEmpty(timeConfigWinterList)) {
            productTimeConfigManageDao.insertProTimeCfgsWinter(timeConfigWinterList);

        }
        //添加风险杠杆配置
        if (StringTools.isNotEmpty(riskConfigVOList)) {

            for (ProductRiskConfigVO vo : riskConfigVOList) {
                vo.setProductId(Integer.valueOf(param.getId()));
            }
            productRiskConfigManageDao.insertBatch(riskConfigVOList);
        }
        if (StringTools.isNotEmpty(quotaConfig)) {
            //添加行情配置
            quotaConfig.setProductId(Integer.valueOf(param.getId()));
            productQuotaConfigManageDao.insertProQuotaConfig(quotaConfig);
        }

        //添加商品与标签中间表信息
        if (StringTools.isNotEmpty(param.getTagIds())) {
            String[] tagIds = param.getTagIds().split(",");

            //批量添加商品与标签中间信息
            List<ProductTagMapper> ptmList = new ArrayList<ProductTagMapper>();
            for (String tagId : tagIds) {
                ProductTagMapper ptm = new ProductTagMapper();
                ptm.setProductId(Integer.valueOf(param.getId()));
                ptm.setTagId(Integer.valueOf(tagId));
                ptmList.add(ptm);
            }
            productTagManageDao.insertProTagMappers(ptmList);
        }

        //插入券商和商品的关联
        List<ProductAccountMapper> pamlist = productManageDao.selectProAccMapperList(param.getCode());
        for (ProductAccountMapper m : pamlist) {
            m.setProductId(Integer.valueOf(param.getId()));
        }

        if (pamlist != null && pamlist.size() != 0) {
            productAccountMapperDao.insertProAccMappers(pamlist);
        }

        //执行初始化平仓商品、券商数据
        init();

    }

    /**
     * 执行初始化平仓商品、券商数据
     */
    private void init() {
        //TODO 这里执行初始化数据
        List<String> list = userServiceMapper.findUserServiceByCode("1002");
        List<ProductTradeConfig> tradeConfig = productTradeConfigManageDao.selectProTradeCfgs(null);
        for (ProductTradeConfig productTradeConfig : tradeConfig) {
            InvestorFeeCfg cfg = getDetails(productTradeConfig);
            for (String userId : list) {
                cfg.setAccountId(userId);
                InvestorFeeCfg info = new InvestorFeeCfg();
                info.setAccountId(userId);
                info.setProductId(productTradeConfig.getProductId());
                int i = investorFeeCfgManageDao.selectInvestorFeeCfgCount(info);
                if (i < 1) {
                    //取品种上一个合约的配置信息
                    Product lastProduct = productManageDao.getLastProduct(productTradeConfig.getProductId());
                    if (StringTools.isNotEmpty(lastProduct)) {
                        logger.info("获取商品 {} 上一个合约 {} ", productTradeConfig.getProductId(), lastProduct.getProductCode());
                        InvestorFeeCfg investorFeeCfg = new InvestorFeeCfg();
                        investorFeeCfg.setProductId(lastProduct.getId());
                        investorFeeCfg.setAccountId(userId);
                        List<InvestorFeeCfg> investorFeeCfgList = investorFeeCfgManageDao.selectInvestorFeeCfgPage(investorFeeCfg);
                        if (StringTools.isNotEmpty(investorFeeCfgList)) {
                            cfg = investorFeeCfgList.get(0);
                            cfg.setIsModel(1);
                            cfg.setProductId(productTradeConfig.getProductId());
                            cfg.setCreateDate(new Date());
                            cfg.setModifyDate(new Date());
                        }
                    }
                    if (cfg.getPlatformCounterfee() == null || cfg.getPlatformCounterfee().equals("")) {
                        cfg.setPlatformCounterfee(0.0);
                    }
                    investorFeeCfgManageDao.insertInvestorFeeCfg(cfg);
                }
            }
        }
    }

    @Override
    public void initInvestorFeeConfig(String userId) {
        //TODO 这里执行初始化数据
        List<ProductTradeConfig> tradeConfig = productTradeConfigManageDao.selectProTradeCfgs(null);
        for (ProductTradeConfig productTradeConfig : tradeConfig) {
            InvestorFeeCfg cfg = getDetails(productTradeConfig);
            cfg.setAccountId(userId);
            InvestorFeeCfg info = new InvestorFeeCfg();
            info.setAccountId(userId);
            info.setProductId(productTradeConfig.getProductId());
            int i = investorFeeCfgManageDao.selectInvestorFeeCfgCount(info);
            if (i < 1) {
                //取品种上一个合约的配置信息
                Product lastProduct = productManageDao.getLastProduct(productTradeConfig.getProductId());
                if (StringTools.isNotEmpty(lastProduct)) {
                    logger.info("获取商品 {} 上一个合约 {} ", productTradeConfig.getProductId(), lastProduct.getProductCode());
                    InvestorFeeCfg investorFeeCfg = new InvestorFeeCfg();
                    investorFeeCfg.setProductId(lastProduct.getId());
                    investorFeeCfg.setAccountId(userId);
                    List<InvestorFeeCfg> investorFeeCfgList = investorFeeCfgManageDao.selectInvestorFeeCfgPage(investorFeeCfg);
                    if (StringTools.isNotEmpty(investorFeeCfgList)) {
                        cfg = investorFeeCfgList.get(0);
                        cfg.setIsModel(1);
                        cfg.setProductId(productTradeConfig.getProductId());
                        cfg.setCreateDate(new Date());
                        cfg.setModifyDate(new Date());
                    }
                }
                if (cfg.getPlatformCounterfee() == null || cfg.getPlatformCounterfee().equals("")) {
                    cfg.setPlatformCounterfee(0.0);
                }
                investorFeeCfgManageDao.insertInvestorFeeCfg(cfg);
            }
        }
    }

    private InvestorFeeCfg getDetails(ProductTradeConfig productTradeConfig) {
        InvestorFeeCfg cfg = new InvestorFeeCfg();
//		cfg.setBounsType(productTradeConfig);//分红方式
        cfg.setCreateDate(new Date());
        cfg.setCreater("9999");
//		cfg.setCounterFeeType(counterFeeType);//手续费收取方式
        cfg.setInvestorCounterfee(productTradeConfig.getMinCounterFee());
        cfg.setDefaultCount(productTradeConfig.getDefaultCount());//默认手数
        cfg.setDefaultStopLoss(productTradeConfig.getDefaultStopLoss());//默认止损
        cfg.setDefaultStopProfit(productTradeConfig.getDefaultStopProfit());//默认止盈
        cfg.setDeferFee(productTradeConfig.getMinDeferFee());//默认递延费为最小平台递延费
        cfg.setDeferFund(productTradeConfig.getMinDeferFund());//默认递延保证金为最小平台递延保证金
//		cfg.setEndTime(endTime);
//		cfg.setInvestorBouns(investorBouns);//券商分红
        cfg.setIsModel(1);//是否支持模版
        cfg.setIsSupportDefer(0);//是否支持递延
        cfg.setModifyDate(new Date());
        cfg.setModifyId(9999);//修改人
        cfg.setMultipleRange(productTradeConfig.getMultipleRange());//手数区间
//		cfg.setPlatformBouns(platformBouns);//平台方分红
        //`platform_bouns_target`  '平台分红方向  1.向投资方收取分红  2.向交易方收取  3.双方都收取   4 双方都不收取',
//		cfg.setPlatformBounsTarget(platformBounsTarget);
//		cfg.setPlatformCounterfee(platformCounterfee);//'平台方手续费',
//		cfg.setPlatformCounterFeeTarget(platformCounterFeeTarget);//平台手续费目标收取方 1.向交易方收取 2.向平台方收取
        cfg.setProductId(productTradeConfig.getProductId());
        cfg.setStopLossRange(productTradeConfig.getStopLossRange());//
        cfg.setStopProfitRange(productTradeConfig.getStopProfitRange());
        cfg.setSurcharge(productTradeConfig.getMinSurcharge());//保证金参数
        return cfg;
    }

    /**
     * 同步更新券商方法
     *
     * @param productId
     */
    private void editInvestorFeeCfg(String productId) {
        logger.info("进入更新券商方法 productId ={}", productId);
        List<String> list = userServiceMapper.findUserServiceByCode("1002");
        ProductParamVO param = new ProductParamVO();
        param.setId(productId);
        List<ProductTradeConfig> tradeConfig = productTradeConfigManageDao.selectProTradeCfgs(param);
        logger.info("tradeConfig = {}", JSONObject.toJSONString(tradeConfig));
        for (ProductTradeConfig productTradeConfig : tradeConfig) {
            InvestorFeeCfg info = new InvestorFeeCfg();
            info.setProductId(productTradeConfig.getProductId());
            for (String userId : list) {
                info.setAccountId(userId);
                List<InvestorFeeCfg> listCfg = investorFeeCfgManageDao.selectInvestorFeeCfgPage(info);
                logger.info("listCfg = {}", JSONObject.toJSONString(listCfg));
                if (StringTools.isNotEmpty(listCfg)) {
                    for (InvestorFeeCfg investorFeeCfg : listCfg) {
                        investorFeeCfg.setInvestorCounterfee(null);
                        investorFeeCfg.setMultipleRange(productTradeConfig.getMultipleRange());
                        investorFeeCfg.setDefaultCount(productTradeConfig.getDefaultCount());//默认手数
                        investorFeeCfg.setDefaultStopLoss(productTradeConfig.getDefaultStopLoss());//默认止损
                        investorFeeCfg.setDefaultStopProfit(productTradeConfig.getDefaultStopProfit());//默认止盈
                        investorFeeCfg.setStopLossRange(productTradeConfig.getStopLossRange());//
                        investorFeeCfg.setStopProfitRange(productTradeConfig.getStopProfitRange());
                        investorFeeCfg.setDeferFee(null);//默认递延费为最小平台递延费
                        investorFeeCfg.setDeferFund(null);//默认递延保证金为最小平台递延保证金
                        investorFeeCfg.setModifyDate(new Date());
                        investorFeeCfg.setModifyId(9999);//修改人
                        investorFeeCfg.setSurcharge(null);//保证金参数
                        investorFeeCfgManageDao.updateInvestorFeeCfg(investorFeeCfg);
                        logger.info("productTradeConfig = {}", JSONObject.toJSONString(productTradeConfig));
                        logger.info("investorFeeCfg = {}", JSONObject.toJSONString(investorFeeCfg));

                    }
                } else {
                    InvestorFeeCfg cfg = getDetails(productTradeConfig);
                    cfg.setAccountId(userId);
                    investorFeeCfgManageDao.insertInvestorFeeCfg(cfg);
                }
            }
        }
        logger.info("结束更新券商方法");
    }

    /**
     * 同步删除券商配置
     *
     * @param
     */
    private void deleleInvestorFeeCfg(ProductParamVO param) {
        productTradeConfigManageDao.deleteProTradeCfgs(param);
    }

    /**
     * 处理交易时间配置信息
     *
     * @param param
     * @param timeConfigList
     */
    private void dealTimeConfig(ProductParamVO param, List<ProductTimeConfig> timeConfigList) {
        if (!StringTools.isNotEmpty(timeConfigList)) {
            return;
        }
        String[] timeIds = {StringTools.getUUID(), StringTools.getUUID(), StringTools.getUUID(), StringTools.getUUID()};
        int i = 0;
        for (ProductTimeConfig productTimeConfig : timeConfigList) {
            productTimeConfig.setProductId(Integer.valueOf(param.getId()));
            productTimeConfig.setTimeId(timeIds[i]);
            if (i == 3) {
                productTimeConfig.setNextTimeId(timeIds[0]);
            } else {
                productTimeConfig.setNextTimeId(timeIds[i + 1]);
            }
            if (i == 0) {
                productTimeConfig.setPrevTimeId(timeIds[3]);
            } else {
                productTimeConfig.setPrevTimeId(timeIds[i - 1]);
            }
            i++;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void editProduct(ProductParamVO param) throws Exception {
        //解析商品配置实体
        ProductTradeConfig tradeConfig = JSONObject.parseObject(param.getJsonTradeConfig(), ProductTradeConfig.class);
        //解析交易时间配置集
        List<ProductTimeConfig> timeConfigList = JSONObject.parseArray(param.getJsonTimeConfigList(), ProductTimeConfig.class);
        //解析交易时间配置集（冬令时）
        List<ProductTimeConfig> timeConfigWinterList = JSONObject.parseArray(param.getJsonTimeConfigWinterList(), ProductTimeConfig.class);
        //解析风险杠杆配置集
        List<ProductRiskConfigVO> riskConfigVOList = JSONObject.parseArray(param.getJsonRiskConfigList(), ProductRiskConfigVO.class);
        //解析行情配置
        ProductQuotaConfig quotaConfig = (ProductQuotaConfig) JSONObject.parseObject(param.getJsonQuotaConfig(), ProductQuotaConfig.class);

        if (timeConfigList == null || timeConfigList.size() == 0) {
            throw new LTException(LTResponseCode.PR00001);
        }

        if (timeConfigWinterList == null || timeConfigWinterList.size() == 0) {
            throw new LTException(LTResponseCode.PR00001);
        }
        //判断添加的商品是否已存在
        ProductParamVO pc = new ProductParamVO();
        pc.setCode(param.getCode());
        pc.setId(param.getIds().split(",")[0]);
        //判断商品是否在售，如果在售不允许编辑商品code
        pc.setCode(null);
        Product pro = productManageDao.selectProductOne(pc);
        logger.info("========pro={}==========", JSONObject.toJSONString(pro));
        if (!pro.getProductCode().equals(param.getCode()) && pro.getStatus() == ProductMarketEnum.PRODUCT_STATUS_UP.getValue()) {//上架状态
            logger.info("========error={}========", LTResponseCode.PRJ0004);
            throw new LTException(LTResponseCode.PRJ0004);
        }

        //修改了市场状态
        if (pro.getMarketStatus() != param.getMarketStatus()) {
            if (param.getMarketStatus() != ProductEnum.MARKET_STATUS_OPEN.getValue()) {
                param.setIsOperate(0);//不允许操作
                param.setShutReason("运营强制闭市");
            } else {
                param.setIsOperate(1);//允许操作
                param.setShutReason("运营强制开市");
            }
        }

        //修改商品基本信息
        if ("--".equals(param.getExpirationTime())) {
            param.setExpirationTime(null);
        }
        productManageDao.updateProducts(param);

        //修改差价合约数字货币配置
        if (ProductTypeEnum.CONTRACT.getCode().equals(param.getProductTypeCode())) {
            if (pro.getProductCode().equals(param.getCode())) {
                DigitalCoinConfigVO digitalCoinConfigVO = new DigitalCoinConfigVO();
                digitalCoinConfigVO.setProductCode(param.getCode());
                digitalCoinConfigVO.setContractSize(param.getContractSize());
                digitalCoinConfigVO.setMaxLever(param.getMaxLever());
                digitalCoinConfigVO.setMaxPositionPerAccount(param.getMaxPositionPerAccount());
                digitalCoinConfigVO.setMaxSingleOpenPosition(param.getMaxSingleOpenPosition());
                digitalCoinConfigVO.setSpread(param.getSpread());
                digitalCoinConfigVO.setBrandPosition(param.getBrandPosition());
                digitalCoinConfigManageDao.updateCoinConfig(digitalCoinConfigVO);
            } else {
                DigitalCoinConfigVO digitalCoinConfigVO = new DigitalCoinConfigVO();
                digitalCoinConfigVO.setProductCode(pro.getProductCode());
                digitalCoinConfigManageDao.deleteCoinConfig(digitalCoinConfigVO);

                digitalCoinConfigVO.setProductCode(param.getCode());
                digitalCoinConfigVO.setContractSize(param.getContractSize());
                digitalCoinConfigVO.setMaxLever(param.getMaxLever());
                digitalCoinConfigVO.setMaxPositionPerAccount(param.getMaxPositionPerAccount());
                digitalCoinConfigVO.setMaxSingleOpenPosition(param.getMaxSingleOpenPosition());
                digitalCoinConfigVO.setSpread(param.getSpread());
                digitalCoinConfigVO.setBrandPosition(param.getBrandPosition());
                digitalCoinConfigManageDao.insertCoinConfig(digitalCoinConfigVO);
            }

            //如果点差有变化，加入点差变化商品列表，在redis加载完成后推送java行情服务器
            if (pro.getSpread() != null && !pro.getSpread().equals(param.getSpread())) {
                param.addSpreadModProductList(param.getCode());
            }
        }

        if (StringTools.isNotEmpty(tradeConfig)) {
            //修改商品配置信息
            tradeConfig.setIds(param.getIds());
            //将行情浮动限制转为小数
            if (StringTools.isNotEmpty(tradeConfig.getFloatLimit())) {
                tradeConfig.setFloatLimit(DoubleUtils.div(tradeConfig.getFloatLimit(), 100.0));
            }
            productTradeConfigManageDao.updateProductTradeConfigs(tradeConfig);

            //如果商品关闭递延，那么修改券商的递延状态为关闭
            if (tradeConfig.getIsDefer() != null && tradeConfig.getIsDefer() == 1) {//关闭
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ids", param.getIds());
                map.put("deferStatus", 0);
                investorFeeCfgManageDao.updateDeferSattusByProducts(map);
            }
        }
        //修改行情配置信息
        if (StringTools.isNotEmpty(quotaConfig)) {
            quotaConfig.setIds(param.getIds());
            productQuotaConfigManageDao.updateProQuotaConfigs(quotaConfig);
        }
        //删除交易时间配置信息
        productTimeConfigManageDao.deleteProTimeCfgs(param);
        //删除交易时间配置信息（冬令时）
        productTimeConfigManageDao.deleteProTimeCfgsWinter(param);

        //删除风险杠杆配置信息
        productRiskConfigManageService.deleteBatch(param.getIds());
        //添加交易时间配置信息
        String[] ids = param.getIds().split(",");//商品id串1,2,3
        for (String proId : ids) {
            param.setId(proId);
            dealTimeConfig(param, timeConfigList);
            if (timeConfigList != null && timeConfigList.size() > 0) {
                productTimeConfigManageDao.insertProTimeCfgs(timeConfigList);
            }

            dealTimeConfig(param, timeConfigWinterList);
            if (timeConfigWinterList != null && timeConfigWinterList.size() > 0) {
                productTimeConfigManageDao.insertProTimeCfgsWinter(timeConfigWinterList);
            }


            if (StringTools.isNotEmpty(riskConfigVOList)) {
                productRiskConfigManageService.insertBatch(riskConfigVOList, Integer.valueOf(proId));
            }

        }

        //如果晃过来的tagIds为空串，那么认为清空标签中间信息
        //if("".equals(param.getTagIds())){
        //删除商品与标签中间信息
        productTagManageDao.deleteProTagMappers(param);
        //}
        //添加商品与标签中间信息
        if (StringTools.isNotEmpty(param.getTagIds())) {
            String[] tagIds = param.getTagIds().split(",");
            for (String proId : ids) {

                //批量添加商品与标签中间信息
                List<ProductTagMapper> ptmList = new ArrayList<ProductTagMapper>();
                for (String tagId : tagIds) {
                    ProductTagMapper ptm = new ProductTagMapper();
                    ptm.setProductId(Integer.valueOf(proId));
                    ptm.setTagId(Integer.valueOf(tagId));
                    ptmList.add(ptm);
                }
                productTagManageDao.insertProTagMappers(ptmList);
            }
        }
        if (StringTools.isNotEmpty(ids)) {
            for (String proId : ids) {
                //执行初始化平仓商品、券商数据
                editInvestorFeeCfg(proId);
            }
        }
    }

    @Override
    @Transactional
    public void removeProducts(ProductParamVO param) throws Exception {
        String productCodes = "";
        //判断商品是否在售，如果在售不允许删除商品
        String[] idArray = param.getIds().split(",");
        for (String id : idArray) {
            param.setId(id);
            Product pro = productManageDao.selectProductOne(param);
            if (pro.getStatus() == ProductMarketEnum.PRODUCT_STATUS_UP.getValue()) {//上架状态
                throw new LTException(LTResponseCode.PRJ0005);
            }

            if (StringTools.isEmpty(productCodes)) {
                productCodes = "'" + pro.getProductCode() + "'";
            } else {
                productCodes += (",'" + pro.getProductCode() + "'");
            }
        }

        param.setId(null);
        //删除商品信息
        productManageDao.deleteProduct(param);
        //查询删除后的剩余商品，将排序重排
        List<Product> pros = productManageDao.selectProducts(param);
        if (pros != null && pros.size() > 0) {
            int i = 1;
            for (Product pro : pros) {
                pro.setSortNum(i);
                i++;
            }
            //更新商品排序
            productManageDao.updateProSortForList(pros);
        }

        //删除差价合约货币配置
        digitalCoinConfigManageDao.deleteCoinConfigBatch(productCodes);
        //删除商品费用配置信息
        productTradeConfigManageDao.deleteProTradeCfgs(param);
        //删除商品交易时间配置信息
        productTimeConfigManageDao.deleteProTimeCfgs(param);
        //删除风险杠杆配置信息
        productRiskConfigManageService.deleteBatch(param.getIds());
        //删除商品行情配置信息
        productQuotaConfigManageDao.deleteProQuotaCfgs(param);
        //删除商品与标签中间表信息
        productTagManageDao.deleteProTagMappers(param);
        //执行初始化平仓商品、券商数据
        deleleInvestorFeeCfg(param);
    }

    @Override
    public Page<Product> queryProductPage(ProductParamVO param)
            throws Exception {
        Page<Product> page = new Page<Product>();
        page.setPageNum(param.getPage());
        page.setPageSize(param.getRows());
        page.addAll(productManageDao.selectProductPage(param));
        page.setTotal(productManageDao.selectProductCount(param));
        return page;
    }

    @Override
    public Integer queryProductCount(ProductParamVO param) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Object> queryProductDetail(ProductParamVO param) throws Exception {
        Map<String, Object> rm = new HashMap<String, Object>();

        //查询商品信息map
        param.setId(param.getIds().split(",")[0]);
        ProductVo pv = productManageDao.selectProductMap(param);
        if (pv == null) {
            throw new LTException(LTResponseCode.PR00002);
        }
        String pvs = JSONObject.toJSONString(pv);
        rm = JSONObject.parseObject(pvs, Map.class);
        //查询商品代码集合
        List<String> codes = productManageDao.selectProductCodeS(param);
        rm.put("proCodes", codes);

        //查询商品交易配置信息
        ProductTradeConfig tradeConfig = productTradeConfigManageDao.selectProTradeCfg(param);
        rm.put("jsonTradeConfig", tradeConfig);
        //查询商品行情配置信息
        ProductQuotaConfig quotaConfig = productQuotaConfigManageDao.selectProQuotaCfg(param);
        rm.put("jsonQuotaConfig", quotaConfig);
        //查询商品时间配置信息
        List<ProductTimeConfig> timeConfigs = productTimeConfigManageDao.selectProTimeCfgs(param);
        rm.put("jsonTimeConfigList", timeConfigs);
        //查询商品时间配置信息
        List<ProductTimeConfig> timeConfigsWinter = productTimeConfigManageDao.selectProTimeCfgsWinter(param);
        rm.put("jsonTimeConfigWinterList", timeConfigsWinter);
        //查询商品风险杠杆配置
        ProductRiskConfigVO query = new ProductRiskConfigVO();
        query.setProductId(Integer.valueOf(param.getId()));
        List<ProductRiskConfigVO> riskConfigVOList = productRiskConfigManageService.query(query);
        rm.put("jsonRiskConfigList", riskConfigVOList);

        //查询标签信息
        List<ProductTagInfo> tagInfos = productTagManageDao.selectProductTagListByProId(Integer.valueOf(param.getId()));

        rm.put("tagInfos", tagInfos);
        return rm;
    }

    @Override
    public boolean editProductInfo(ProductParamVO param) throws Exception {
        boolean b = false;//是否点击了上架操作
        //如果商品的状态改为下架，要判断该商品当前是否有订单未完成（订单的状态小于3），如果有，不允许下架
        if (param.getStatus() == ProductMarketEnum.PRODUCT_STATUS_DOWN.getValue()) {//下架
            //查询商品合约号
            List<String> list = productManageDao.selectProductCodeS(param);
            Integer count = productManageDao.selectOrderCount(list);
            Integer scount = productManageDao.selectScoreOrderCount(list);
            if ((count + scount) > 0) {
                throw new LTException(LTResponseCode.PRJ0006);
            }
        }else if (param.getStatus() == ProductMarketEnum.PRODUCT_STATUS_UP.getValue()) {//上架
            b=true;
        }


        productManageDao.updateProducts(param);
        //执行初始化平仓商品、券商数据
        init();

        return b;
    }

    @Override
    public void editProductSort(ProductParamVO param) throws Exception {
        Map<String, Object> p = new HashMap<>();
        Product pro = productManageDao.selectProductOne(param);
        if (param.getSortNum() == pro.getSortNum()) {
            throw new LTException(LTResponseCode.PR00003);
        }

        p.put("oldSort", pro.getSortNum());
        p.put("newSort", param.getSortNum());
        if (param.getSortNum() < pro.getSortNum()) {
            productManageDao.updateProSortForGTL(p);
        } else {
            productManageDao.updateProSortForLTG(p);
        }
        param.setIds(param.getId().toString());
        productManageDao.updateProducts(param);
        //执行初始化平仓商品、券商数据
        init();
    }

    @Override
    public void loadProAndTimeToRedis() throws Exception {
        logger.info("====================从数据库中加载商品放到缓存中=============");
        final Map<String, String> oldCm = new HashMap<String, String>();//存储老商品map，key:商品id_内外盘，value:商品code
        final Map<String, String> newCm = new HashMap<String, String>();//存储新商品map，key:商品id_内外盘，value:商品code

        //从redis获取旧的商品信息
        BoundHashOperations<String, String, ProductVo> redisHash = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
        Set<String> set = redisHash.keys();//老的商品code集合
        for (String code : set) {
            oldCm.put(redisHash.get(code).getId() + "_" + redisHash.get(code).getPlate(), code);
        }

        //删除旧的商品redis
        redisTemplate.delete(RedisUtil.PRODUCT_INFO);
        //重新建立商品redis
        redisHash = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
        //获取数据库中所有商品
        List<ProductVo> list = productManageDao.selectProductVos();
        for (ProductVo pro : list) {
            redisHash.put(pro.getProductCode(), pro);

            newCm.put(pro.getId() + "_" + pro.getPlate(), pro.getProductCode());

        }
        //加载时间配置信息到redis
        productApiService.loadProTimeCfgToRedis();
        logger.info("----------添加时间配置到缓存---成功");

        //通知交易添加或修改处理
        dealTradeForAlter(oldCm, newCm);
        //通知交易删除处理
        dealTradeForDel(oldCm, newCm);
        if (isLoadTrade_) {
            //只刷新交易
            logger.info("==========刷新交易==========");
            orderApiService.manageProducts(null, null, null);
            orderScoreApiService.manageProducts(null, null, null);

        }

        logger.info("==============从数据库中加载商品放到缓存中--结束==============");
    }

    /**
     * 通知交易添加或修改处理
     *
     * @param oldCm
     * @param newCm
     */
    private void dealTradeForAlter(Map<String, String> oldCm,
                                   Map<String, String> newCm) {
        logger.info("=========通知交易添加或修改处理=========");
        Set<String> newIdPlate = newCm.keySet();
        for (String nidPlate : newIdPlate) {
            String oldCode = oldCm.get(nidPlate);//老的商品代码
            Integer plate = Integer.valueOf(nidPlate.split("_")[1]);
            if (oldCode == null) {//说明该遍历商品是新增的
                orderApiService.manageProducts(plate, newCm.get(nidPlate), null);
                orderScoreApiService.manageProducts(plate, newCm.get(nidPlate), null);
                logger.info("=========manageProducts===新增商品={},执行成功====", newCm.get(nidPlate));


                isLoadTrade_ = false;
            } else if (!oldCode.equals(newCm.get(nidPlate))) {//新的code和就的code不等，说明做了修改

                orderApiService.manageProducts(plate, newCm.get(nidPlate), oldCode);
                orderScoreApiService.manageProducts(plate, newCm.get(nidPlate), oldCode);
                logger.info("=========manageProducts===将原商品={}，修改为={},执行成功====", oldCode, newCm.get(nidPlate));


                isLoadTrade_ = false;
            }
        }
        logger.info("==========通知交易添加或修改处理=====结束====");
    }

    /**
     * 通知交易删除处理
     *
     * @param oldCm
     * @param newCm
     */
    private void dealTradeForDel(Map<String, String> oldCm,
                                 Map<String, String> newCm) {
        logger.info("============通知交易删除处理==========");
        Set<String> oldIdPlate = oldCm.keySet();
        for (String oidPlate : oldIdPlate) {
            String newCode = newCm.get(oidPlate);//获取新加载的商品code
            if (newCode == null) {//说明对应老的code做了删除
                Integer plate = Integer.valueOf(oidPlate.split("_")[1]);
                orderApiService.manageProducts(plate, null, oldCm.get(oidPlate));
                orderScoreApiService.manageProducts(plate, null, oldCm.get(oidPlate));
                logger.info("=========manageInnerProducts===删除商品={},执行成功====", oldCm.get(oidPlate));


                isLoadTrade_ = false;
            }
        }
        logger.info("============通知交易删除处理=======结束===");
    }

    @Override
    @Transactional
    public void topProducts(ProductParamVO param) throws Exception {
        String[] ids = param.getIds().split(",");
        for (int i = 0; i < ids.length; i++) {
            param.setId(ids[i]);
            param.setSortNum(i + 1);
            editProductSort(param);
        }
    }

    @Override
    public List<Product> getProShortCode(Product product) throws Exception {
        return productManageDao.selectProShortCode(product);
    }

    @Override
    public void loadProductToRedis() throws Exception {
        //从redis获取旧的商品信息
        BoundHashOperations<String, String, ProductVo> redisHash = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
        //删除旧的商品redis
        redisTemplate.delete(RedisUtil.PRODUCT_INFO);
        //重新建立商品redis
        redisHash = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
        //获取数据库中所有商品
        List<ProductVo> list = productManageDao.selectProductVos();
        for (ProductVo pro : list) {
            redisHash.put(pro.getProductCode(), pro);
        }
    }

    @Override
    public String selectRate(String string) {
        return productManageDao.selectRateByCurrency(string);
    }

    @Override
    public Product getProductByProductId(Integer productId) {
        ProductParamVO productParamVO = new ProductParamVO();
        productParamVO.setId(productId + "");
        return productManageDao.selectProductOne(productParamVO);
    }

    @Override
    @Transactional
    public boolean editOrAddProduct(ProductParamVO param) throws Exception {
        //判断是否有新增合约
        boolean b=false;
        //商品合约和交割日list
        String jsonProductCodeList = param.getJsonProductCodeList();
        List<ProductCode> productCodeList = JSONObject.parseArray(jsonProductCodeList, ProductCode.class);
        if (productCodeList.size() == 0) {
            throw new LTException(LTResponseCode.PR00001);
        }
        logger.info("==========productCodeList={}=============", JSONObject.toJSONString(productCodeList));

        for (ProductCode productCode : productCodeList) {
            param.setCode(productCode.getProductCode());//商品code
            param.setExpirationBeginTime(productCode.getExpirationBeginTime());//合约开始时间
            param.setExpirationTime(productCode.getExpirationEndTime());//合约到期时间
            //判断商品是新增还是编辑
            if (StringTools.isEmpty(productCode.getId())) {//新增
                this.addProduct(param);
                b=true;
            } else {//编辑
                param.setIds(productCode.getId().toString());
                this.editProduct(param);
            }
        }

        return b;
    }

    @Override
    public void reloadProdClearTime(String... proudctCodes) {
        try {
            String errorStr = orderApiService.updateRiskOrderClearTime(proudctCodes);
            orderScoreApiService.updateRiskOrderClearTime(proudctCodes);
            if (StringTools.isNotEmpty(errorStr)) {
                throw new LTException(errorStr);
            }
        } finally {
            BoundValueOperations<String, String> redis = redisTemplate.boundValueOps(RedisUtil.PRODUCT_CLEAR_TIME_FLAG);
            redis.set("Y");
            redis.expire(61, TimeUnit.SECONDS);
        }

    }

    @Override
    public void updateTimeSummer() {
        productManageDao.updateTimeSummer();
    }

    @Override
    public void updateTimeWinter() {
        productManageDao.updateTimeWinter();
    }

    @Override
    public List<ProductVo> queryProductList(ProductParamVO param)
            throws Exception {
        return productManageDao.selectProductList(param);
    }

    @Override
    public void pushSpreadModifyMsg(List<String> productList) {
        if (StringTools.isNotEmpty(productList)) {
            for (String productCode : productList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cmd", "spread");
                jsonObject.put("productCode", productCode);
                spreadModifyMsgProducer.sendMessage(jsonObject);
            }
        }
    }
}
