package com.taotao.core.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.core.service.SessionProvider;

import redis.clients.jedis.Jedis;

/**
 * 远程Session
 * 
 * @author lx
 *
 */
public class SessionProviderImpl implements SessionProvider {

	@Autowired
	private Jedis jedis;
	
	//分钟 
	private Integer exp = 30;
	public void setExp(Integer exp) {
		this.exp = exp;
	}

	/**
	 * Key : 32位长度的字符串
	 * @param key
	 * @param value
	 */
	//保存用户名到Redis
	public void setAttribute(String key,String value){
		jedis.set(key + ":USER_SESSION", value);
		//时间
		jedis.expire(key + ":USER_SESSION", 60*exp);
		
	}
	//获取用户名从Redis
	public String getAttribute(String key){
		String value = jedis.get(key + ":USER_SESSION");
		if(null != value){
			//时间
			jedis.expire(key + ":USER_SESSION", 60*exp);
		}
		return value;
	}
	
}
