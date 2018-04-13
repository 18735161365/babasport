package com.taotao.core.service;

import com.taotao.core.pojo.BuyerCart;
import com.taotao.core.pojo.order.Order;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.pojo.user.Buyer;

public interface BuyerService {
	
//	通过用户名查询用户对象（如果在 就存在  如果不在就不存在）
	public Buyer selectBuyerByUsername(String username);
	
	//通过SkuID查询一个Sku对象(颜色ID 查询颜色对象，商品ID查询商品对象）
	public Sku selectSkuById(Long id);
	
//	将购物车保存到Redis中
	public void insertBuyerCartToRedis(BuyerCart buyerCart,String username);
	
//	将Redis中购物车取出来
	public BuyerCart selectBuyerCartFromRedis(String username);
	
	//保存订单表
	//保存订单详情表
	public void insertOrder(Order order,String username);

}
