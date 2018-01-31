package com.lt.quota.core.quota;

import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.quota.core.service.IQuotaCoreConfigService;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.otod.bean.quote.snapshot.ForexSnapshot;
import com.otod.test.QuoteClient;
import com.otod.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@Component
public class GtQuotaClient extends QuoteClient{

    @Autowired
    private ProductTimeCache productTimeCache;



    @Autowired
    private IQuotaCoreConfigService quotaCoreConfigService;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private SimpleDateFormat format = new SimpleDateFormat("yy");

    private CountDownLatch latch = new CountDownLatch(1);

    @PostConstruct
    public void init(){
        subscribe();

        System.out.println("size1="+productListOuter.size());
        System.out.println("start quota");
        GtQuotaClient quotaClient = new GtQuotaClient();
        quotaClient.start();

    }


    public void subscribe() {
        //每分钟获取最新的合约
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始获取合约信息！！");
                List<Map<String, Object>> outerList =  quotaCoreConfigService.productList(1);
                List<Map<String, Object>> innerList =  quotaCoreConfigService.productList(0);

                synchronized(productListOuter){
                    productListOuter.clear();
                    for(int i=0;i<outerList.size();i++){
                        productListOuter.put(outerList.get(i).get("commodityNo").toString(),outerList.get(i).get("contractNo").toString());
                    }
                }


                synchronized(productListInner){
                    productListInner.clear();
                    for(int i=0 ;i<innerList.size();i++){
                        productListInner.put(innerList.get(i).get("commodityNo").toString(),innerList.get(i).get("contractNo").toString());

                    }
                }

                System.out.println("size="+productListOuter.size());
                /*
                for(int i=0;i<productListOuter.size();i++){
                    String contractNo =  outerList.get(i).get("contractNo").toString();
                    String exchangeNo =  outerList.get(i).get("exchangeNo").toString();
                    String commodityNo = outerList.get(i).get("commodityNo").toString();
                    System.out.println("contractNo="+contractNo+",exchangeNo="+exchangeNo+",commodityNo="+commodityNo);
                }*/
            }
        },0,5, TimeUnit.HOURS);

    }

    public String getOuterContractNo(String commodityNo){
        synchronized (productListOuter){
            return productListOuter.get(commodityNo);
        }
    }

    public String getInnerContractNo(String commodityNo){
        synchronized(productListInner){
            return productListInner.get(commodityNo);
        }
    }

    @Override
    public void onMessage(ForexSnapshot snapshot) {
        Date date = new Date();
        String name = snapshot.cnName;
        if(name.startsWith("美原油")){
            //拿出美原油的的合约代码
            //String contract = productTimeCache.getContract("CL");
            //System.out.println("contract="+contract);
            //System.out.println(snapshot.ask1Volume+":"+snapshot.bid1Volume+":"+snapshot.volume+":"+snapshot.value+":"+snapshot.tValue);
            String contractNo =  getOuterContractNo("CL");
            if(StringTools.isNotEmpty(contractNo)){
                String contract =  format.format(date)+name.replaceAll("美原油","");
                if(contractNo.equals(contract)){
                    addQuotaBean(snapshot,1,"CL"+contractNo);
                }
            }
        }else if(name.startsWith("美黄金")){
            String contractNo =  getOuterContractNo("GC");
            if(StringTools.isNotEmpty(contractNo)){
                String contract =  format.format(date)+name.replaceAll("美黄金","");
                if(contractNo.equals(contract)){
                    System.out.println("contractNo="+contractNo);
                    addQuotaBean(snapshot,1,"GC"+contractNo);
                }
            }

        }else if(name.startsWith("美白银")){
            String contractNo =  productListOuter.get("SI");
            if(StringTools.isNotEmpty(contractNo)){
                String contract =  format.format(date)+name.replaceAll("美白银","");
                if(contractNo.equals(contract)){
                    addQuotaBean(snapshot,1,"SI"+contractNo);
                }
            }
        }

    }

    private void addQuotaBean(ForexSnapshot snapshot,int plate,String productName){

        //算涨幅
        double chageRate =  DoubleUtils.mul(DoubleUtils.div((snapshot.close-snapshot.pClose),snapshot.open),100);
        chageRate =  DoubleUtils.scaleFormatEnd(chageRate,2);
        QuotaBean quotaBean = new QuotaBean();
        quotaBean.setAskPrice1(String.valueOf(snapshot.ask1Price));
        quotaBean.setAskQty1(String.valueOf(snapshot.ask1Volume));
        quotaBean.setBidPrice1(String.valueOf(snapshot.bid1Price));
        quotaBean.setBidQty1(String.valueOf(snapshot.bid1Volume));
        quotaBean.setChangeRate(String.valueOf(chageRate));
        quotaBean.setChangeValue(String.valueOf(snapshot.close-snapshot.close));
        quotaBean.setHighPrice(String.valueOf(snapshot.high));
        quotaBean.setLastPrice(String.valueOf(snapshot.close));
        quotaBean.setLowPrice(String.valueOf(snapshot.low));
        quotaBean.setOpenPrice(String.valueOf(snapshot.open));
        quotaBean.setPositionQty(String.valueOf(snapshot.tVolume));
        quotaBean.setPreClosePrice(String.valueOf(snapshot.pClose));
        quotaBean.setProductName(productName);
        quotaBean.setSettlePrice(String.valueOf(snapshot.pClose));
        // quotaBean.setTimeStamp();
        //String time = snapshot.date+snapshot.time;
        String time = String.valueOf(snapshot.time);
        while(time.length()<6){
            time = "0"+time;
        }
        String datetime = String.valueOf(snapshot.date)+time;
        SimpleDateFormat formatdate = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date datex =  formatdate.parse(datetime);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            datetime =  format2.format(datex);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        quotaBean.setTimeStamp(datetime+".000");
        quotaBean.setPlate(plate);
        quotaBean.setTotalQty(0);
        CleanInstance.getInstance().setMarketDataQueue(quotaBean);
    }


}
