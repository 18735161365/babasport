package com.taotao.common.web;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 生成令牌
 * @author lx
 *
 */
public class RequestUtils {

	
	//获取令牌
	public static String getCSESSIONID(HttpServletRequest request,HttpServletResponse response){
		//1:获取Cookie
		Cookie[] cookies = request.getCookies();
		if(null != cookies && cookies.length >0){
			//2:遍历Cookie  判断是否存在令牌
			for (Cookie cookie : cookies) {
				if(Constants.CSESSIONID.equals(cookie.getName())){
					//3:有  直接返回
					return cookie.getValue();
				}
				
			}
		}
		//4:没有  创建新的令牌 并保存在Cookie 并写回浏览器
		String csessionid = UUID.randomUUID().toString().replaceAll("-", "");
		Cookie cookie = new Cookie(Constants.CSESSIONID,csessionid);
		//存活时间   关闭浏览器销毁 -1     立即销毁0   到时间再销毁>0 
		cookie.setMaxAge(-1);
		//路径
		cookie.setPath("/");
		response.addCookie(cookie);
		//5:返回新的令牌
		return csessionid;
		
	}
}
