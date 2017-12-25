package com.lt.util.utils;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.lt.util.utils.crypt.AES256;
import com.lt.util.utils.model.Token;


/**
 * TOKEN公共类
 * @author guodw
 *
 */
public class TokenTools {
	/**
	 * 判断Token是否过期
	 * @param token
	 * @return false  过期  true  没过期
	 */
	public static Token parseToken(String token){
		Token tokenVo = new Token();
		if( !StringTools.isNotEmpty(token) ){
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

			if( dateLen <= range ){
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
	 * @param tele
	 * @return
	 */
	public static String createToken(String userId){
		/* 要加密的字符串：14位随机数|用户ID|登陆时间|有效期分钟|14位随机数 */
		String source = StringTools.getRandom(8) + "|"+userId+"|"+CalendarTools.getNowDateTime()+"|10080|";
		int len = 64 - source.length();
		if(len > 0){
			source += StringTools.getRandom(len);
		}
		return AES256.getEnString(source);
	}
	
	public static void main(String[] args) {
		String str = createToken("16231");
		System.out.println(str);
		str = "bfd5cc437a299f63bc99ccb55368a98f4968c773f876a47d5cd738482ec367dee09361e7785df01de2cd4b4525222e4be8ebc3a1218cb11ec9c5baecad5d4969";
		Token token = parseToken(str);
		System.out.print(JSONObject.toJSON(token));
	}
}
