package com.lt.tms.trade.ib.api;

import com.lt.tms.library.ib.client.*;
import com.lt.tms.trade.ib.api.bean.IbTradeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 盈透回调
 */
@Component
public class IbTradeWrapper implements EWrapper {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private EReaderSignal readerSignal;
    private EClientSocket clientSocket;

    private volatile int baseOrderId = -1;

    private BlockingQueue<IbTradeBean> orderDataQueue = new LinkedBlockingQueue<IbTradeBean>();

    public IbTradeWrapper() {

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

    public int getNextOrderId() {
        return ++baseOrderId;
    }

    public BlockingQueue<IbTradeBean> getOrderDataQueue() {
        return orderDataQueue;
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
        logger.error("TWS给client发送消息...error id: {}, code: {}, msg: {}", id, errorCode, errorMsg);
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
    }

    // 当市场数据变动时调用该方法, 尺寸立即更新, 没有延迟
    @Override
    public void tickSize(int tickerId, int field, int size) {
    }

    // 当市场数据改变时调用该方法, 值被立即更新, 没有延迟
    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
    }

    // 当市场数据改变时调用该方法, 值被立即更新, 没有延迟
    @Override
    public void tickString(int tickerId, int tickType, String value) {
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints,
                        String formattedBasisPoints, double impliedFuture, int holdDays,
                        String futureLastTradeDate, double dividendImpact,
                        double dividendsToLastTradeDate) {
    }

    // 当期权市场或期权底层变化时调用该方法
    @Override
    public void tickOptionComputation(int tickerId, int field,
                                      double impliedVol, double delta, double optPrice,
                                      double pvDividend, double gamma, double vega, double theta,
                                      double undPrice) {
    }

    @Override
    public void tickSnapshotEnd(int reqId) {
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
    }

    // 定单---------------------------------------------------------------------------------------------------/
    @Override
    public void orderStatus(int orderId, String status, double filled,
                            double remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld) {

        logger.info("OrderStatus. Id: " + orderId + ", Status: " + status + ", Filled" + filled + ", Remaining: " + remaining
                + ", AvgFillPrice: " + avgFillPrice + ", PermId: " + permId + ", ParentId: " + parentId + ", LastFillPrice: " + lastFillPrice +
                ", ClientId: " + clientId + ", WhyHeld: " + whyHeld);

        switch (status) {
            case "PendingSubmit":
                // 已经报单, 但还未接到来自定单目的地接收的确认信息
                break;
            case "PreSubmitted":
                // 模拟定单类型已经被IB系统接收, 但还未被选中
                break;
            case "Submitted":
                // 定单已经被定单目的地接受, 并处于工作中
                break;
            case "Filled":
                // 定单已被全部执行
                break;
            case "Cancelled":
                // 定单的剩余部分已被IB系统确认取消了, 这也会在IB或目的地拒绝你的定单时发生
                break;
            case "Inactive":
                // 表示定单已被系统接收(模拟定单)或交易所接收(本地定单), 但由于系统/交易所或其它原因, 目前定单处于非工作状态
                break;
            case "PendingCancel":
                // 已经发送了取消定单的请求, 但还未收到来自定单目的地的取消确认
                break;
            default:
                break;
        }
        orderStatus(orderId, status, filled, remaining, avgFillPrice, lastFillPrice, permId + "");
    }

    private void orderStatus(int orderId, String status, double successNumber, double overNumber, double avgPrice, double lastPrice, String sysOrderId) {
        IbTradeBean ibTradeBean = new IbTradeBean();
        ibTradeBean.setOrderId(orderId);
        ibTradeBean.setStatus(status);
        ibTradeBean.setSuccessNumber(successNumber);
        ibTradeBean.setOverNumber(overNumber);
        ibTradeBean.setAvgPrice(avgPrice);
        ibTradeBean.setLastPrice(lastPrice);
        ibTradeBean.setSysOrderId(sysOrderId);
        try {
            orderDataQueue.put(ibTradeBean);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        logger.info("OpenOrder. ID: " + orderId + ", " + contract.symbol() + ", " + contract.secType() + " @ " + contract.exchange() + ": " +
                order.action() + ", " + order.orderType() + " " + order.totalQuantity() + ", " + orderState.status());
    }

    @Override
    public void openOrderEnd() {
        logger.debug("OpenOrderEnd");
    }

    @Override
    public void nextValidId(int orderId) {
        logger.info("Next Valid Id: {}", orderId);
        baseOrderId = orderId;
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
