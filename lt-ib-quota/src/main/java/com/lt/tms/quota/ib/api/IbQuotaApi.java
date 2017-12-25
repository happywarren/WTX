package com.lt.tms.quota.ib.api;

import com.lt.tms.comm.json.FastJsonUtils;
import com.lt.tms.library.ib.client.Contract;
import com.lt.tms.library.ib.client.EClientSocket;
import com.lt.tms.library.ib.client.EReader;
import com.lt.tms.library.ib.client.EReaderSignal;
import com.lt.tms.quota.bean.QuotaDataBean;
import com.lt.tms.quota.ib.api.bean.ContractBean;
import com.lt.tms.quota.ib.api.config.IbQuotaConfig;
import com.lt.tms.tcp.server.TcpClient;
import com.lt.tms.tcp.server.config.NettyConfig;
import com.lt.tms.utils.FileUtils;
import com.lt.tms.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class IbQuotaApi {

    private Logger logger = LoggerFactory.getLogger(IbQuotaApi.class);

    private EClientSocket client;
    private EReaderSignal signal;

    //线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private IbQuotaConfig ibQuotaConfig;
    @Autowired
    private IbQuotaWrapper ibQuotaWrapper;
    @Autowired
    private TcpClient tcpClient;

    public IbQuotaApi() {

    }

    public void connect() {
        client = ibQuotaWrapper.getClientSocket();
        signal = ibQuotaWrapper.getReaderSignal();
        client.eConnect(ibQuotaConfig.getHost(), ibQuotaConfig.getPort(), ibQuotaConfig.getClientId());
        startupCallbackThread();
        startupMarketDataThread();
    }

    // 行情数据发送线程
    private void startupMarketDataThread() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        QuotaDataBean quotaDataBean = ibQuotaWrapper.getQuotaDataQueue().take();
                        tcpClient.sendQuotaInfo(quotaDataBean);
                        //存到本地文件
                        cacheFile(quotaDataBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("行情数据发送出错, 异常信息: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void cacheFile(QuotaDataBean quotaDataBean) {
        try {
            String message = FastJsonUtils.toJson(quotaDataBean);
            String fileDir = ibQuotaConfig.getFileDir();
            String productCode = quotaDataBean.getProductCode();
            FileUtils.creatDir(fileDir);
            String filePath = fileDir + File.separator + productCode + ".txt";
            FileUtils.writeFile(message, filePath, false);
        } catch (Exception e) {
        }
    }

    // 启动回调线程
    private void startupCallbackThread() {
        final EReader reader = new EReader(client, signal);
        reader.start();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (client.isConnected()) {
                    signal.waitForSignal();
                    try {
                        reader.processMsgs();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Exception: " + e.getMessage());
                    }
                }
            }
        });
    }

    // 订阅股票及期货市场行情
    public void subscribe(ContractBean contractBean) {
        Contract contract = contract(contractBean);
        if (!Utils.isNotEmpty(contract)) {
            return;
        }
        ///*"100,101,105,106,165,233,236,258"*/
        client.reqMktData(contractBean.getTickerId(), contract, "100,101,104,105,106,165,221,225,233,236,258,411,456", false, false, null);
        logger.info("订阅合约, tickerId: {}, symbol: {}, currency:{}, secType: {}, exchange: {}", contractBean.getTickerId(), contractBean.getSymbol(), contractBean.getCurrency(), contractBean.getSecType(), contractBean.getExchange());
    }

    //取消行情订阅
    public void cancel(Integer tickerId) {
        client.cancelMktData(tickerId);
        logger.info("取消订阅合约, tickerId: {}", tickerId);
    }

    private Contract contract(ContractBean contractBean) {
        String secType = contractBean.getSecType();
        Contract contract = new Contract();
        contract.symbol(contractBean.getSymbol());
        contract.secType(contractBean.getSecType());
        contract.currency(contractBean.getCurrency());
        contract.exchange(contractBean.getExchange());
        if (contractBean.getExchange().equals("SMART")) {
            contract.primaryExch("ISLAND");
        }
        contract.strike(0.0);
        if (secType.equals("FUT")) { //期货
            String lastTradeDateOrContractMonth = contractBean.getContractMonth() == null ? "" : contractBean.getContractMonth();
            contract.lastTradeDateOrContractMonth(lastTradeDateOrContractMonth);
        } else if (secType.equals("OPT")) { //期权
            String lastTradeDateOrContractMonth = contractBean.getContractMonth() == null ? "" : contractBean.getContractMonth();
            contract.lastTradeDateOrContractMonth(lastTradeDateOrContractMonth);
            contract.right(contractBean.getOptRight());
            contract.strike(contractBean.getOptStrike());
            contract.localSymbol(contractBean.getLocalSymbol());
        }
        return contract;
    }

    // 释放资源
    @PreDestroy
    public void release() {
        executorService.shutdown();
    }

    public boolean isConnected() {
        if (Utils.isNotEmpty(client)) {
            client.reqCurrentTime();
            return client.isConnected();
        }
        return false;
    }
}
