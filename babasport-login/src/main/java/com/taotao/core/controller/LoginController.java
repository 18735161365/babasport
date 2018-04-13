package com.taotao.core.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.web.RequestUtils;
import com.taotao.core.pojo.user.Buyer;
import com.taotao.core.service.BuyerService;
import com.taotao.core.service.SessionProvider;

/**
 * 单点登陆系统
 * @author lx
 *
 */
@Controller
public class LoginController {

	//去登陆页面
	@RequestMapping(value = "/login.aspx",method=RequestMethod.GET)
	public String login(){
		return "login";
	}
	@Autowired
	private BuyerService buyerService;
	@Autowired
	private SessionProvider sessionProvider;
	////提交登陆表单 
	@RequestMapping(value = "/login.aspx",method=RequestMethod.POST)
	public String login(String username,String password,String ReturnUrl,Model model
			,HttpServletRequest request,HttpServletResponse response){
		//1:验证码不能为空
		//2:验证码必须正确
		//3:验证码已失效
		//4:用户名不能为空
		if(null != username){
			//5:密码不能为空
			if(null != password){
				//6:此帐户不存在   
				Buyer buyer = buyerService.selectBuyerByUsername(username);
				if(null != buyer){
					//7:密码不正确
					if(buyer.getPassword().equals(encodePassword(password))){
						//8：保存用户到Session中
						sessionProvider.setAttribute(
								RequestUtils.getCSESSIONID(request, response),buyer.getUsername());
						//9:返回之前访问的页面
						return "redirect:" + ReturnUrl;
					}else{
						model.addAttribute("error", "密码不正确");
					}
					
				}else{
					model.addAttribute("error", "此帐户不存在");
				}
			}else{
				model.addAttribute("error", "密码不能为空");
			}
			
		}else{
			model.addAttribute("error", "用户名不能为空");
		}
		
		//有错误就去登陆页面
		return "login";
	}
	
	//加密
	public String encodePassword(String password){
		//加盐
//		password = "fqewbne635tggrewgnr5yhwegmi65yt656543";
		
		//MD5
		String algorithm = "MD5";
		char[] encodeHex = null;
		try {
			MessageDigest instance = MessageDigest.getInstance(algorithm);
			byte[] digest = instance.digest(password.getBytes());
			//十六进制
			encodeHex = Hex.encodeHex(digest);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(encodeHex);
	}
	public static void main(String[] args) {
		LoginController l = new LoginController();
		String p = l.encodePassword("123456");
		System.out.println(p);
	}
	//判断用户是否登陆
	@RequestMapping(value = "/isLogin.aspx")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback,HttpServletRequest request,HttpServletResponse response){
		Integer result = 0;
		//判断
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if(null != username){
			result = 1;
		}
		MappingJacksonValue mjv = new MappingJacksonValue(result);
		mjv.setJsonpFunction(callback);
		
		return mjv;
	}
	
}
