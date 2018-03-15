package com.lt.quota.core.quota.rf;

import com.lt.quota.core.model.QuotaCoreConfigModel;
import com.lt.quota.core.service.IQuotaCoreConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class RfTcpClientMain {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IQuotaCoreConfigService quotaCoreConfigService;
    @Autowired
    private RfTcpClient rfTcpClient;

    @PostConstruct
    public void init() {
        /*
        List<QuotaCoreConfigModel> dataList = quotaCoreConfigService.getQuotaCoreConfigModelByName("RF");
        if (null == dataList || dataList.isEmpty()) {
            logger.info("未加载RF配置文件");
            return;
        }

        //dataList = new ArrayList<QuotaCoreConfigModel>()
        for (QuotaCoreConfigModel quotaCoreConfigModel : dataList) {
            rfTcpClient.start(quotaCoreConfigModel.getHost(), quotaCoreConfigModel.getPort());
        }*/
        rfTcpClient.start("106.14.153.130", 9100);
    }
}
