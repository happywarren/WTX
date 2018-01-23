package com.lt.quota.core.quota.ib;

import com.lt.quota.core.model.QuotaCoreConfigModel;
import com.lt.quota.core.service.IQuotaCoreConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class IbTcpClientMain {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IQuotaCoreConfigService quotaCoreConfigService;
    @Autowired
    private IbTcpClient ibTcpClient;

//    @PostConstruct
    public void init() {
        List<QuotaCoreConfigModel> dataList = quotaCoreConfigService.getQuotaCoreConfigModelByName("IB");
        if (null == dataList || dataList.isEmpty()) {
            logger.info("未加载IB配置文件");
            return;
        }

        for (QuotaCoreConfigModel quotaCoreConfigModel : dataList) {
            ibTcpClient.start(quotaCoreConfigModel.getHost(), quotaCoreConfigModel.getPort());
        }
    }
}
