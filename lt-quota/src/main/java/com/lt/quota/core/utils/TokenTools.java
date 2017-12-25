package com.lt.quota.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.model.Token;
import com.lt.quota.core.utils.aes.AES256;

import java.util.Date;


/**
 * TOKEN公共类
 *
 * @author guodw
 */
public class TokenTools {
    /**
     * 判断Token是否过期
     *
     * @param token
     * @return false 没过期   true  过期
     */
    public static Token parseToken(String token) {
        Token tokenVo = new Token();
        if (!Utils.isNotEmpty(token)) {
            tokenVo.setIsdeline(true);
            tokenVo.setUserSecret(null);
            tokenVo.setToken(null);
            tokenVo.setUserId(null);
            return tokenVo;
        }
        try {
            /* 解密当前Token，获取token的过期时间  */
            String decrypt = AES256.getDeString(token);
            String result = AES256.getDecode(decrypt);
            String[] resultToken = result.split("\\|");
            String userId = resultToken[1];
            String lastLoginDate = resultToken[2];
            Long dateLen = Long.parseLong(resultToken[3]);
            Long range = CalendarTools.getMiniteBetween(
                    CalendarTools.parseDateTime(lastLoginDate,
                            "yyyy-MM-dd HH:mm:ss"), new Date());

            if (dateLen <= range) {
                tokenVo.setIsdeline(true);
                tokenVo.setUserSecret(null);
                tokenVo.setToken(null);
                tokenVo.setUserId(userId);

                return tokenVo;
            }

            tokenVo.setIsdeline(false);
            tokenVo.setUserSecret(null);
            tokenVo.setToken(token);
            tokenVo.setUserId(userId);

            return tokenVo;

        } catch (Exception e) {
            e.printStackTrace();
            tokenVo.setIsdeline(true);
            tokenVo.setUserSecret(null);
            tokenVo.setToken(null);
            return tokenVo;
        }
    }

    /**
     * 通过用户的ID+Token产生时间+Token过期时间长度(分钟)加密后产生一个
     *
     * @param tele
     * @return
     */
    public static String createToken(String userId) {
        /* 要加密的字符串：14位随机数|用户ID|登陆时间|有效期分钟|14位随机数 */
        String source = Utils.getRandom(8) + "|" + userId + "|" + CalendarTools.getNowDateTime() + "|10080|";
        int len = 64 - source.length();
        if (len > 0) {
            source += Utils.getRandom(len);
        }
        return AES256.getEnString(source);
    }

    public static void main(String[] args) {
        String str = createToken("13218");
        System.out.println(str);
        str = "d93cff10e384bcd95263e2dee7a6bcd97bd2ef1d66f747b0ecc0b7ccc4ea1d20d6db8a32f15010d5557953d58bf13eadbac0c60737ed02bad8e8577db8d1663e";
        Token token = parseToken(str);
        System.out.print(JSONObject.toJSON(token));
    }
}
