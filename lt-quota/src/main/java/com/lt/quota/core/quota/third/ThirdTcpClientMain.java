package com.lt.quota.core.quota.third;

import com.lt.quota.core.service.IQuotaCoreConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ThirdTcpClientMain {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    @Autowired
    private IQuotaCoreConfigService quotaCoreConfigService;

    @Autowired
    private ThirdTcpClient thirdTcpClient;

    @PostConstruct
    public static  void init(){

        //logger.info("开始连接第三方行情");
        ThirdTcpClient.getInstance().start("121.41.58.181",22536);

    }

    public void subscribe() {
        //每分钟获取最新的合约
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始获取合约信息！！");
                List<Map<String, Object>> outerList =  quotaCoreConfigService.productList(1);
                List<Map<String, Object>> innerList =  quotaCoreConfigService.productList(0);

                synchronized(ThirdSanpShot.productListOuter){
                    ThirdSanpShot.productListOuter.clear();
                    for(int i=0;i<outerList.size();i++){
                        ThirdSanpShot.productListOuter.put(outerList.get(i).get("commodityNo").toString(),outerList.get(i).get("contractNo").toString());
                    }
                }


                synchronized(ThirdSanpShot.productListInner){
                    ThirdSanpShot.productListInner.clear();
                    for(int i=0 ;i<innerList.size();i++){
                        ThirdSanpShot.productListInner.put(innerList.get(i).get("commodityNo").toString(),innerList.get(i).get("contractNo").toString());
                    }
                }

            }
        },0,1, TimeUnit.HOURS);

    }


    public static  void main(String [] args){
        init();
    }
}
