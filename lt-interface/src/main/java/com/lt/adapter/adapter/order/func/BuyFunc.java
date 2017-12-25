package com.lt.adapter.adapter.order.func;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.trade.IOrderScoreApiService;
import com.lt.api.user.IUserApiService;
import com.lt.constant.LTConstant;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.product.ProductMarketEnum;
import com.lt.enums.product.ProductTypeEnum;
import com.lt.enums.trade.BuyTriggerTypeEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 用户买
 *
 * @author jingwb
 */
@Service
public class BuyFunc extends BaseFunction {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final IOrderApiService orderApiService;
    private final IOrderScoreApiService orderScoreApiService;
    private final IProductApiService productApiService;
    private final IUserApiService userApiService;

    @Autowired
    public BuyFunc(IOrderApiService orderApiService, IOrderScoreApiService orderScoreApiService, IProductApiService productApiService, IUserApiService userApiService) {
        this.orderApiService = orderApiService;
        this.orderScoreApiService = orderScoreApiService;
        this.productApiService = productApiService;
        this.userApiService = userApiService;
    }

    @Override
    public Response response(Map<String, Object> paraMap) {
        //开始时间，用户提交时间
        long startTime = System.currentTimeMillis();

        //商品代码
        String productCode = (String) paraMap.get("productCode");

        //用户下单时间
        String orderTime = (String) paraMap.get("orderTime");

        //商品类型
        Integer productTypeId = Integer.valueOf(paraMap.get("productTypeId").toString());

        //手数
        Integer count = Integer.valueOf(paraMap.get("count").toString());

        //交易方向
        Integer tradeDirection = Integer.valueOf(paraMap.get("tradeType").toString());

        //递延状态
        Integer deferStatus = Integer.valueOf(paraMap.get("deferStatus").toString());

        //移动止损状态
        Integer trailStopLoss = Integer.valueOf(paraMap.get("trailStopLoss").toString());

        //资金类型
        Integer fundType = Integer.valueOf(paraMap.get("fundType").toString());

        //每手止损金额
        Double stopLoss = Double.valueOf(paraMap.get("stopLoss").toString());

        //每手止盈金额
        Double stopProfit = Double.valueOf(paraMap.get("stopProfit").toString());

        //用户买入价
        Double userBuyPrice = Double.valueOf(paraMap.get("userBuyPrice").toString());

        //用户id
        String userId = (String) paraMap.get("userId");

        //externalId,前端唯一id
        String externalId = (String) paraMap.get("externalId");

        //是否是迷你盘
        Integer mini = Integer.parseInt(paraMap.get("mini") == null? "1":paraMap.get("mini").toString());


        //记录下单方式：0:市价单 1:条件单 2:闪电单
        String purchaseOrderType = StringTools.formatStr(paraMap.get("purchaseOrderType"), "0");

        logger.info("---------------------userBuyPrice---------------------:{}", userBuyPrice);

        logger.info("==下单开始 productCode={},userId={}===", productCode, userId);
        //返回信息

        if (StringTools.isEmpty(userId)) {
            throw new LTException(LTResponseCode.US01105);
        }

        String investorId;
        String brandId = "";
        //校验券商ID
        UserBaseInfo info = userApiService.findUserByUserId(userId);
        if (info != null && StringTools.isNotEmpty(info.getInvestorAccountId())) {
            investorId = info.getInvestorAccountId();
        } else {
            logger.info("investorId使用默认值:{}", LTConstant.DEFAULT_INVESTOR_ID);
            //使用默认券商ID
            investorId = LTConstant.DEFAULT_INVESTOR_ID + "";
        }

        //校验品牌ID
        if (info != null && StringTools.isNotEmpty(info.getBrandId())) {
            brandId = info.getBrandId();
        }

        //商品类型
        logger.info("校验商品类型ID:{}", productTypeId);
        if (productTypeId == null) {
            //商品类型未找到
            throw new LTException(LTResponseCode.PR00004);
        }

        //从redis中获取商品信息
        ProductVo productVo = productApiService.getProductInfo(productCode);
        logger.info("从redis中获取商品信息，product:{}", JSONObject.toJSONString(productVo));
        if (productVo == null) {
            //商品未找到
            throw new LTException(LTResponseCode.PR00002);
        }

        /*
          差价合约相关判断
         */
        if (ProductTypeEnum.CONTRACT.getCode().equals(productVo.getProductTypeCode())) {
            if (!StringTools.isEmpty(productVo.getMaxSingleOpenPosition())) {
                if (count > productVo.getMaxSingleOpenPosition()) {
                    //下单手数不能大于最大单次开仓量
                    throw new LTException("单次下单不得超过"+productVo.getMaxSingleOpenPosition()+"手");
                }
            }
        }

        //商品市场状态判断
        Integer marketStatus = productVo.getMarketStatus();
        logger.info("校验商品市场状态状态:{}", marketStatus);
        if (ProductMarketEnum.ONLY_OPEN.getValue() != marketStatus
                && ProductMarketEnum.START_TRADING.getValue() != marketStatus) {
            //商品市场状态判断
            throw new LTException(LTResponseCode.PR00005);
        }

        //商品上下架状态判断
        Integer status = productVo.getStatus();
        logger.info("校验商品上架状态:{}", status);
        if (ProductMarketEnum.PRODUCT_STATUS_UP.getValue() != status) {
            throw new LTException(LTResponseCode.PR00008);
        }

        //买入手数
        logger.info("校验买入手数:{}", count);
        if (count <= 0) {
            throw new LTException(LTResponseCode.TD00001);
        }

        //交易方向: 0 空; 1 多
        logger.info("校验交易方向:{}", tradeDirection);
        if (tradeDirection == null) {
            throw new LTException(LTResponseCode.TD00002);
        }
        if (TradeDirectionEnum.DIRECTION_DOWN.getValue() != tradeDirection && TradeDirectionEnum.DIRECTION_UP.getValue() != tradeDirection) {
            throw new LTException(LTResponseCode.TD00002);
        }

        //资金类型: 0 现金; 1 积分
        logger.info("校验资金类型:{}", fundType);
        if (FundTypeEnum.CASH.getValue() != fundType && FundTypeEnum.SCORE.getValue() != fundType) {
            throw new LTException(LTResponseCode.TD00003);
        }

        //订单买入参数
        OrderVo orderVo = new OrderVo(productCode, productVo.getProductName(),
                productTypeId, userId, investorId, count,
                tradeDirection, deferStatus, trailStopLoss, fundType, stopLoss,
                stopProfit, userBuyPrice, new Date(startTime), BuyTriggerTypeEnum.CUSTOMER.getValue(),
                Integer.valueOf(purchaseOrderType));
        orderVo.setProductVo(productVo);
        orderVo.setUserBaseInfo(info);
        orderVo.setExternalId(externalId);
        orderVo.setOrderTime(orderTime);
        orderVo.setBrandId(brandId);
        orderVo.setMini(mini);
        //调用订单服务接口买入
        logger.info("调用订单服务开仓接口");
        logger.info("订单分发, 资金类型: 0 现金, 1 积分, fundType:{}", fundType);
        if (FundTypeEnum.CASH.getValue() == fundType) {
            orderApiService.buy(orderVo);
        } else if (FundTypeEnum.SCORE.getValue() == fundType) {
            orderScoreApiService.buy(orderVo);
        } else {
            throw new LTException(LTResponseCode.TD00003);
        }

        return LTResponseCode.getCode(LTResponseCode.SUCCESS);
    }

}
