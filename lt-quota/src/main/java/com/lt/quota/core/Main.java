package com.lt.quota.core;

import com.lt.quota.core.utils.FileUtils;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        sql();
    }

    private static void sql() {
        Map<Integer, String> productMap = new HashMap<>();
        productMap.put(206, "BTC");
        productMap.put(207, "BTCUSDmini");
        productMap.put(208, "ETH");

        List<String> userList = FileUtils.readFile("C:\\work\\user.txt");
        for (String userId : userList) {
            for (Map.Entry<Integer, String> entry : productMap.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO `user_product_select` (`user_id`, `product_id`, `create_date`, `product_short_code`) VALUES ('");
                sb.append(userId).append("',").append(entry.getKey()).append(",now(),'").append(entry.getValue()).append("');");

                FileUtils.writeLine("C:\\work\\sql_2.txt", sb.toString());
            }
        }
    }

}
