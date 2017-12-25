package com.lt.tms.quota.ib.api;

import com.lt.tms.library.ib.client.*;
import com.lt.tms.operator.QuotaCache;
import com.lt.tms.quota.bean.QuotaDataBean;
import com.lt.tms.tcp.server.TcpClient;
import com.lt.tms.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 盈透回调
 */
@Component
public class IbQuotaWrapper implements EWrapper {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private EReaderSignal readerSignal;

    private EClientSocket clientSocket;

    //行情
    private BlockingQueue<QuotaDataBean> quotaDataQueue = new LinkedBlockingQueue<QuotaDataBean>();

    @Autowired
    private TcpClient tcpClient;

    public IbQuotaWrapper() {

    }

    @PostConstruct
    public void init() {
        readerSignal = new EJavaSignal();
        clientSocket = new EClientSocket(this, readerSignal);
    }

    public EClientSocket getClientSocket() {
        return clientSocket;
    }

    public EReaderSignal getReaderSignal() {
        return readerSignal;
    }

    public BlockingQueue<QuotaDataBean> getQuotaDataQueue() {
        return quotaDataQueue;
    }

    // 连接和服务器--------------------------------------------------------------------------------------------/
    // 接收服务器的当前系统时间
    @Override
    public void currentTime(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = new Date(time);
        logger.debug("currentTime...{} convert to {}", time, sf.format(date));
    }

    // 处理请求过程中发生异常
    @Override
    public void error(Exception e) {
        logger.error("处理请求过程中发生异常..." + e.getMessage());
    }

    // TWS给client发送错误信息
    @Override
    public void error(String str) {
        logger.error("TWS给client发送错误信息..." + str);
    }

    // 通讯出现错误或TWS给client发送消息
    @Override
    public void error(int id, int errorCode, String errorMsg) {
        logger.error("TWS给client发送消息 error id: {}, code: {}, msg: {}", id, errorCode, errorMsg);
        //IB和TWS之间的连接已经停止 1100
        //市场数据场连接已断开，但应依需要获重新连接 2108
        //不能连接TWS 502
        if (errorCode == 1100 || errorCode == 2108 || errorCode == 502) {
            clientSocket.eDisconnect();
        }
        //盈透行情异常处理
        tcpClient.sendErrorMessage(id, errorCode, errorMsg);
    }

    // TWS关闭socket连接, 或当TWS被关闭时调用
    @Override
    public void connectionClosed() {
        logger.info("TWS关闭socket连接, 或TWS已被关闭");
        clientSocket.eDisconnect();
    }

    @Override
    public void connectAck() {
        if (clientSocket.isAsyncEConnect()) {
            clientSocket.startAPI();
        }
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttr tickAttr) {
        String fieldDesc = TickType.getField(field);
        logger.debug("tick price. ticker Id: {}, field: {}, desc: {}, price: {}, canAutoExecute: {}", tickerId, field, fieldDesc, price, tickAttr.canAutoExecute());
        if (price <= 0) {
            return;
        }
        QuotaDataBean quotaData = QuotaCache.getInstance().getQuotaDataBean(tickerId);
        long timestamp = new Date().getTime();
        quotaData.setTimestamp(timestamp);
        switch (field) {
            case 1:
                // 买价
                quotaData.setBidPrice1(Double.toString(price));
                break;
            case 2:
                // 卖价
                quotaData.setAskPrice1(Double.toString(price));
                break;
            case 4:
                // 最新价
                quotaData.setLastPrice(Double.toString(price));
                BigDecimal lastPrice = new BigDecimal(quotaData.getLastPrice());

                BigDecimal preSettlePrice = new BigDecimal(quotaData.getPreSettlePrice() == null ? "0" : quotaData.getPreSettlePrice());
                BigDecimal changePrice = lastPrice.subtract(preSettlePrice);
                quotaData.setChangePrice(Double.toString(changePrice.doubleValue()));
                if (preSettlePrice.doubleValue() > 0.000001) {
                    BigDecimal changeValue = new BigDecimal(Double.toString(100.0 * changePrice.doubleValue()));
                    quotaData.setChangeRate(Double.toString(changeValue.divide(preSettlePrice, 6, BigDecimal.ROUND_HALF_UP).doubleValue()));
                } else {
                    quotaData.setChangeRate("0.0");
                }
                break;
            case 6:
                // 最高价
                quotaData.setHighPrice(Double.toString(price));
                break;
            case 7:
                // 最低价
                quotaData.setLowPrice(Double.toString(price));
                break;
            case 9:
                // 收盘价 close
                quotaData.setPreClosePrice(Double.toString(price));
                quotaData.setPreSettlePrice(Double.toString(price));
                break;
            case 14:
                // 开盘价 open
                quotaData.setOpenPrice(Double.toString(price));
                break;
            case 15:
                quotaData.setLow13Week(Double.toString(price));    //13周最低价
                break;
            case 16:
                quotaData.setHigh13Week(Double.toString(price));    //13周最高价
                break;
            case 17:
                quotaData.setLow26Week(Double.toString(price));    //26周最低价
                break;
            case 18:
                quotaData.setHigh26Week(Double.toString(price));    //26周最高价
                break;
            case 19:
                quotaData.setLow52Week(Double.toString(price));    //52周最低价
                break;
            case 20:
                quotaData.setHigh52Week(Double.toString(price));    //52周最高价
                break;
            default:
                return;
        }
        try {
            QuotaCache.getInstance().setQuotaDataMap(tickerId, quotaData);
            quotaDataQueue.put(quotaData);
        } catch (Exception e) {
            logger.error("添加价格信息出错, 异常信息: " + e.getMessage());
        }
    }

    // 当市场数据变动时调用该方法, 尺寸立即更新, 没有延迟
    @Override
    public void tickSize(int tickerId, int field, int size) {
        String fieldDesc = TickType.getField(field);
        logger.debug("tick size. ticker Id: {}, field: {},fieldDesc: {} size: {}", tickerId, field, fieldDesc, size);
        if (size <= 0) {
            return;
        }
        QuotaDataBean quotaData = QuotaCache.getInstance().getQuotaDataBean(tickerId);
        long timestamp = new Date().getTime();
        quotaData.setTimestamp(timestamp);
        switch (field) {
            case 0:
                // 买量
                quotaData.setBidVolume1(Integer.toString(size));
                break;
            case 3:
                // 卖量
                quotaData.setAskVolume1(Integer.toString(size));
                break;
            case 8:
                // 交易量
                quotaData.setTradeVolume(Integer.toString(size));
                break;
            default:
                return;
        }
        try {
            QuotaCache.getInstance().setQuotaDataMap(tickerId, quotaData);
            quotaDataQueue.put(quotaData);
        } catch (Exception e) {
            logger.error("添加寸头信息出错, 异常信息: " + e.getMessage());
        }
    }

    // 当市场数据改变时调用该方法, 值被立即更新, 没有延迟
    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        String typeDesc = TickType.getField(tickType);
        logger.debug("tick generic. ticker Id: {}, type: {}, desc: {}, value: {}", tickerId, tickType, typeDesc, value);
    }

    // 当市场数据改变时调用该方法, 值被立即更新, 没有延迟
    @Override
    public void tickString(int tickerId, int tickType, String value) {
        String typeDesc = TickType.getField(tickType);
        logger.debug("tick string. ticker Id: {}, type: {}, desc: {}, value: {}", tickerId, tickType, typeDesc, value);
        if (Utils.isEmpty(value)) {
            return;
        }
        QuotaDataBean quotaData = QuotaCache.getInstance().getQuotaDataBean(tickerId);
        long timestamp = new Date().getTime();
        quotaData.setTimestamp(timestamp);
        switch (tickType) {
            case 47:
                //VOL10DAVG 两周均量
                //MKTCAP 市值
                //TTMDIVSHR 股息
                //TTMEPSXCLX 每股盈利
                Map<String, String> valueMap = parseTickString47(value);
                if (Utils.isNotEmpty(valueMap)) {
                    if (valueMap.containsKey("VOL10DAVG")) {
                        quotaData.setAvgVolume2Week(valueMap.get("VOL10DAVG"));
                    }
                    if (valueMap.containsKey("MKTCAP")) {
                        quotaData.setMarketCap(valueMap.get("MKTCAP"));
                    }
                    if (valueMap.containsKey("TTMDIVSHR")) {
                        quotaData.setDividend(valueMap.get("TTMDIVSHR"));
                    }
                    if (valueMap.containsKey("TTMEPSXCLX")) {
                        quotaData.setEps(valueMap.get("TTMEPSXCLX"));
                    }
                }
                break;
            default:
                return;
        }
        try {
            QuotaCache.getInstance().setQuotaDataMap(tickerId, quotaData);
            quotaDataQueue.put(quotaData);
        } catch (Exception e) {
            logger.error("添加tickString信息出错, 异常信息: " + e.getMessage());
        }
    }

    private Map<String, String> parseTickString47(String value) {
        if (Utils.isEmpty(value)) {
            return null;
        }
        try {
            String[] valueArray = value.split(";");
            Map<String, String> valueMap = new HashMap<String, String>();
            for (String subValue : valueArray) {
                if (Utils.isEmpty(subValue)) {
                    continue;
                }
                String[] subArray = subValue.split("=");
                if (subArray.length != 2) {
                    continue;
                }
                valueMap.put(subArray[0], subArray[1]);
            }
            return valueMap;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints,
                        String formattedBasisPoints, double impliedFuture, int holdDays,
                        String futureLastTradeDate, double dividendImpact,
                        double dividendsToLastTradeDate) {
        logger.debug("TickEFP. " + tickerId + ", Type: " + tickType + ", BasisPoints: " + basisPoints + ", FormattedBasisPoints: " +
                formattedBasisPoints + ", ImpliedFuture: " + impliedFuture + ", HoldDays: " + holdDays + ", FutureLastTradeDate: " + futureLastTradeDate +
                ", DividendImpact: " + dividendImpact + ", DividendsToLastTradeDate: " + dividendsToLastTradeDate);
    }

    // 当期权市场或期权底层变化时调用该方法
    @Override
    public void tickOptionComputation(int tickerId, int field,
                                      double impliedVol, double delta, double optPrice,
                                      double pvDividend, double gamma, double vega, double theta,
                                      double undPrice) {
        logger.debug("TickOptionComputation. TickerId: " + tickerId + ", field: " + field + ", ImpliedVolatility: " + impliedVol + ", Delta: " + delta
                + ", OptionPrice: " + optPrice + ", pvDividend: " + pvDividend + ", Gamma: " + gamma + ", Vega: " + vega + ", Theta: " + theta + ", UnderlyingPrice: " + undPrice);
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        logger.info("TickSnapshotEnd: " + reqId);
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        logger.debug("MarketDataType. [" + reqId + "], Type: [" + marketDataType + "]\n");
    }

    // 定单---------------------------------------------------------------------------------------------------/
    @Override
    public void orderStatus(int orderId, String status, double filled,
                            double remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld) {

        logger.debug("OrderStatus. Id: " + orderId + ", Status: " + status + ", Filled" + filled + ", Remaining: " + remaining
                + ", AvgFillPrice: " + avgFillPrice + ", PermId: " + permId + ", ParentId: " + parentId + ", LastFillPrice: " + lastFillPrice +
                ", ClientId: " + clientId + ", WhyHeld: " + whyHeld);
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        logger.debug("OpenOrder. ID: " + orderId + ", " + contract.symbol() + ", " + contract.secType() + " @ " + contract.exchange() + ": " +
                order.action() + ", " + order.orderType() + " " + order.totalQuantity() + ", " + orderState.status());
    }

    @Override
    public void openOrderEnd() {
        logger.debug("OpenOrderEnd");
    }

    @Override
    public void nextValidId(int orderId) {
        logger.info("Next Valid Id: {}", orderId);
    }

    // 账户和投资组合------------------------------------------------------------------------------------------/
    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
    }

    @Override
    public void updatePortfolio(Contract contract, double position,
                                double marketPrice, double marketValue, double averageCost,
                                double unrealizedPNL, double realizedPNL, String accountName) {
    }

    @Override
    public void updateAccountTime(String timeStamp) {

    }

    @Override
    public void accountDownloadEnd(String accountName) {
    }

    // 合约详细-----------------------------------------------------------------------------------------------/
    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {

    }

    @Override
    public void contractDetailsEnd(int reqId) {

    }

    // 执行---------------------------------------------------------------------------------------------------/
    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
    }

    @Override
    public void execDetailsEnd(int reqId) {

    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
    }

    // 市场深度-----------------------------------------------------------------------------------------------/
    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
    }

    // 新闻公告-----------------------------------------------------------------------------------------------/
    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
    }

    // 金融顾问-----------------------------------------------------------------------------------------------/
    @Override
    public void managedAccounts(String accountsList) {

    }

    @Override
    public void receiveFA(int faDataType, String xml) {

    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
    }

    @Override
    public void accountSummaryEnd(int reqId) {

    }

    @Override
    public void position(String account, Contract contract, double pos, double avgCost) {
    }

    @Override
    public void positionEnd() {
        logger.info("PositionEnd \n");
    }

    // 历史数据-----------------------------------------------------------------------------------------------/
    @Override
    public void historicalData(int reqId, String date, double open,
                               double high, double low, double close, int volume, int count,
                               double WAP, boolean hasGaps) {
        //LogUtils.history(reqId, date, open, high, low, close, volume, count, WAP, hasGaps);
    }

    // 市场扫描仪---------------------------------------------------------------------------------------------/
    @Override
    public void scannerParameters(String xml) {
    }

    @Override
    public void scannerData(int reqId, int rank,
                            ContractDetails contractDetails, String distance, String benchmark,
                            String projection, String legsStr) {
    }

    @Override
    public void scannerDataEnd(int reqId) {
        logger.info("ScannerDataEnd. " + reqId);
    }

    // 实时柱-------------------------------------------------------------------------------------------------/
    @Override
    public void realtimeBar(int reqId, long time, double open, double high,
                            double low, double close, long volume, double wap, int count) {
        logger.debug("RealTimeBars. " + reqId + " - Time: " + time + ", Open: " + open + ", High: " + high + ", Low: " + low + ", Close: " + close + ", Volume: " + volume + ", Count: " + count + ", WAP: " + wap);
    }

    // 基本面数据----------------------------------------------------------------------------------------------/
    @Override
    public void fundamentalData(int reqId, String data) {

    }

    // 其他---------------------------------------------------------------------------------------------------/
    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {

    }

    @Override
    public void verifyMessageAPI(String apiData) {

    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {

    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
    }

    @Override
    public void displayGroupList(int reqId, String groups) {
    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode,
                              Contract contract, double pos, double avgCost) {
    }

    @Override
    public void positionMultiEnd(int reqId) {

    }

    @Override
    public void accountUpdateMulti(int reqId, String account, String modelCode,
                                   String key, String value, String currency) {
    }

    @Override
    public void accountUpdateMultiEnd(int reqId) {
    }

    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange,
                                                    int underlyingConId, String tradingClass, String multiplier,
                                                    Set<String> expirations, Set<Double> strikes) {
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {

    }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {

    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {

    }

    @Override
    public void symbolSamples(int i, ContractDescription[] contractDescriptions) {

    }

    @Override
    public void historicalDataEnd(int i, String s, String s1) {

    }

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {

    }

    @Override
    public void tickNews(int i, long l, String s, String s1, String s2, String s3) {

    }

    @Override
    public void smartComponents(int i, Map<Integer, Map.Entry<String, Character>> map) {

    }

    @Override
    public void tickReqParams(int i, double v, String s, int i1) {

    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {

    }

    @Override
    public void newsArticle(int i, int i1, String s) {

    }

    @Override
    public void historicalNews(int i, String s, String s1, String s2, String s3) {

    }

    @Override
    public void historicalNewsEnd(int i, boolean b) {

    }

    @Override
    public void headTimestamp(int i, String s) {

    }

    @Override
    public void histogramData(int i, List<Map.Entry<Double, Long>> list) {

    }
}
