package com.taotao.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.web.Constants;
import com.taotao.common.web.RequestUtils;
import com.taotao.core.service.SessionProvider;

/**
 * 自定义拦截器处理类
 * @author lx
 *
 */
public class LoginInterceptor implements HandlerInterceptor{

	@Autowired
	private SessionProvider sessionProvider;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		//1、	判断用户是否登陆    
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if(null == username){
			//未登陆 跳转到登陆页面     跳转到首页  
			response.sendRedirect(Constants.LOGIN_URL + "/login.aspx?ReturnUrl=" + Constants.PORTAL_URL);
			return false;
		}
		//登陆了  放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
