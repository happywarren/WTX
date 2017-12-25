package com.lt.adapter.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

@Component
public class TokenInterceptor implements ITokenService{
	
	@Autowired
	private IUserApiService userApiService;
	
	@Override
	public boolean checkToken(String token) {
		try {
			userApiService.checkToken(token);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
//			throw new LTException(e.getMessage());
			return false;
		}
	}
}