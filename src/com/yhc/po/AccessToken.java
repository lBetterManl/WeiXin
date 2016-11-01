package com.yhc.po;

/**
 * @author YHC
 * @email yhc8023tm@foxmail.com
 * @version 2016年10月29日 上午11:54:54
 */
public class AccessToken {
	private String token;
	private int expiresIn;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
}
