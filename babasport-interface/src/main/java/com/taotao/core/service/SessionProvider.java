package com.taotao.core.service;

public interface SessionProvider {
	
	
	/**
	 * Key : 32位长度的字符串
	 * @param key
	 * @param value
	 */
	//保存用户名到Redis
	public void setAttribute(String key,String value);
	//获取用户名从Redis
	public String getAttribute(String key);

}
