package com.lt.quota.core.quota;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mcsong
 * @create 2017-11-01 17:41
 */
public class WarningUtil {

    private static final String fileDir = "/data/server/warning/";

    private static final String fileName = "warning.txt";

    private static final Map<String, Long> SOURCE_TIME_MAP = new ConcurrentHashMap<>();

    private static final Map<String, Long> QUOTA_TIME_MAP = new ConcurrentHashMap<>();

    public static void warn(String host, Integer port) {

        FileUtils.creatDir(fileDir);

        String now = DateUtils.formatDate();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("时间 ").append(now).append(" 行情服务器 ").append(IConstants.SERVER_IP)
                .append("连接 ").append(host).append(":").append(port).append(" 失败");

        String fileFullPath = fileDir + fileName;

        FileUtils.writeFile(stringBuilder.toString(), fileFullPath, false);
    }


    public static void warn(String source) {
        long timestamp = System.currentTimeMillis();
        SOURCE_TIME_MAP.put(source, timestamp);
    }

    public static void warnQuota(String product) {
        long timestamp = System.currentTimeMillis();
        QUOTA_TIME_MAP.put(product, timestamp);
    }

    public static void warnQuotaPrice(String content) {
        FileUtils.creatDir(fileDir);
        String fileFullPath = fileDir + fileName;
        FileUtils.writeFile(content, fileFullPath, false);
    }

    public static Map<String, Long> getSourceTimeMap(){
        return SOURCE_TIME_MAP;
    }

    public static Map<String, Long> getQuotaTimeMap(){
        return QUOTA_TIME_MAP;
    }

    public static void main(String[] args) {
        warn("192.168.0.123", 8080);
    }
}
