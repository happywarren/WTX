package com.lt.quota.core.quota;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mcsong
 * @create 2017-11-01 17:41
 */
@Component
public class WarningTimer {

    private static final String fileDir = "/data/server/warning/";

    private static final String fileName = "warning.txt";

    @Scheduled(cron = "0 0/1 * * * ?")
    private void checkSource() {
        FileUtils.creatDir(fileDir);
        String fileFullPath = fileDir + fileName;
        long nowTimestamp = System.currentTimeMillis();
        String now = DateUtils.formatDate();
        Map<String, Long> sourceTimeMap = WarningUtil.getSourceTimeMap();
        for (Map.Entry<String, Long> entry : sourceTimeMap.entrySet()) {
            String source = entry.getKey();
            Long timestamp = entry.getValue();
            //5分钟没有行情
            if (nowTimestamp - timestamp > 300000) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("时间 ").append(now).append(" 服务器 ").append(IConstants.SERVER_IP)
                        .append("数字货币行情源 ").append(source).append(" 5分钟无行情");
                FileUtils.writeFile(stringBuilder.toString(), fileFullPath, false);
            }
        }
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    private void checkQuota() {
        FileUtils.creatDir(fileDir);
        String fileFullPath = fileDir + fileName;
        long nowTimestamp = System.currentTimeMillis();
        String now = DateUtils.formatDate();
        Map<String, Long> quotaTimeMap = WarningUtil.getQuotaTimeMap();
        for (Map.Entry<String, Long> entry : quotaTimeMap.entrySet()) {
            String product = entry.getKey();
            Long timestamp = entry.getValue();
            //5分钟没有行情
            if (nowTimestamp - timestamp > 300000) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("时间 ").append(now).append(" 服务器 ").append(IConstants.SERVER_IP)
                        .append("数字货币 ").append(product).append(" 5分钟无行情");
                FileUtils.writeFile(stringBuilder.toString(), fileFullPath, false);
            }
        }
    }
}
