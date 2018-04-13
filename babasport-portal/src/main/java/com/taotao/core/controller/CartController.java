package com.taotao.core.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.netty.handler.codec.http.HttpServerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.json.JsonUtils;
import com.taotao.common.web.Constants;
import com.taotao.common.web.RequestUtils;
import com.taotao.core.pojo.BuyerCart;
import com.taotao.core.pojo.BuyerItem;
import com.taotao.core.pojo.order.Order;
import com.taotao.core.service.BuyerService;
import com.taotao.core.service.SessionProvider;

/**
 * 购物车 结算 订单
 * 
 * @author lx
 *
 */
@Controller
public class CartController {

	@Autowired
	private BuyerService buyerService;
	@Autowired
	private SessionProvider sessionProvider;
	
	// 加入购物车
	@RequestMapping(value = "/shopping/buyerCart")
	public String buyerCart(Long skuId, Integer amount, HttpServletRequest request, HttpServletResponse response) {
		BuyerCart buyerCart = null;
		// 1:获取Cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// 2:获取Cookie中的购物车
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					buyerCart = JsonUtils.jsonToObject(cookie.getValue(), BuyerCart.class);
				}
			}
		}
		// 3:没有 创建购物车
		if(null == buyerCart){
			buyerCart = new BuyerCart();
		}
		// 4:追加当前款
		if(null != skuId && null != amount){
			buyerCart.addItem(skuId, amount);
		}
		
		//判断用户是否登陆
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if(null != username){
			//登陆了
//			5:将购物车保存到Redis中  清空Cookie
			buyerService.insertBuyerCartToRedis(buyerCart, username);
			Cookie cookie = new Cookie(Constants.BUYER_CART,null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}else{
			//未登陆
			if(null != skuId && null != amount){
//		5:将购物车保存到Cookie中 并回写浏览器
				Cookie cookie = new Cookie(Constants.BUYER_CART,JsonUtils.objectToJson(buyerCart));
				//存活时间  -1 0 >0
				cookie.setMaxAge(60*60*24);
				//路径
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		
		
		
		
		// 6:重定向或转发
//		return "forward:/shopping/toCart";
		return "redirect:/shopping/toCart";
	}

	//去购物车结算
	@RequestMapping(value = "/shopping/toCart")
	public String toCart(HttpServletRequest request,HttpServletResponse response
			,Model model){
		BuyerCart buyerCart = null;
		// 1:获取Cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				// 2:获取Cookie中的购物车
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					buyerCart = JsonUtils.jsonToObject(cookie.getValue(), BuyerCart.class);
				}
			}
		}
		//判断用户是否登陆
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		if(null != username){
			//登陆了
//			3:有  将购物车添加到Redis中 清空Cookie
			if(null != buyerCart){
				buyerService.insertBuyerCartToRedis(buyerCart, username);
				Cookie cookie = new Cookie(Constants.BUYER_CART,null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
//			4:从Redis中取出来购物车
			buyerCart = buyerService.selectBuyerCartFromRedis(username);

		}
//		5:有    把购物车装满
		if(null != buyerCart){
			List<BuyerItem> items = buyerCart.getItems();
			for (BuyerItem item : items) {
				//通过SkuID查询一个Sku对象(颜色ID 查询颜色对象，商品ID查询商品对象）
				item.setSku(buyerService.selectSkuById(item.getSku().getId()));
			}
			
		}
//		6:回显购物车 
		model.addAttribute("buyerCart", buyerCart);
//		7:跳转购物车页面
		return "cart";
	}
	//去结算
	@RequestMapping(value = "/buyer/trueBuy")
	public String trueBuy(HttpServletRequest request,HttpServletResponse response,Model model){
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
//		2、	判断购物车中是否有商品 2）有商品  继续判断
		BuyerCart buyerCart = buyerService.selectBuyerCartFromRedis(username);
		if(null != buyerCart){
//		3、	判断购物车中商品是否有货 1）无货 刷新购物车页面进行无货提示 2）全有货  真过了进入订单提交页面
//		注意：购买数量大于库存数量视为无货
			//标记
			Boolean flag = false;
			
			List<BuyerItem> items = buyerCart.getItems();
			for (BuyerItem item : items) {
				//把购物车装满
				item.setSku(buyerService.selectSkuById(item.getSku().getId()));
				//判断是否有货
				if(item.getAmount() > item.getSku().getStock()){
					//无货
					item.setIsHave(false);
					flag = true;
				}
			}
			if(flag){
				//至少有一个商品是无货的
				model.addAttribute("buyerCart", buyerCart);
				return "cart";
			}
		}else{
			//无商品 刷新购物车页面进行提示
			return "redirect:/shopping/toCart";
		}
		//进入订单页面
		return "order";
	}
	
	//提交订单 
	@RequestMapping(value = "/buyer/submitOrder")
	public String submitOrder(Order order,HttpServletRequest request,HttpServletResponse response){
		String username = sessionProvider.getAttribute(RequestUtils.getCSESSIONID(request, response));
		//保存订单表
		//保存订单详情表
		buyerService.insertOrder(order, username);
		return "success";
	}
	//个人中心  
	//收货地址
	
	
}
