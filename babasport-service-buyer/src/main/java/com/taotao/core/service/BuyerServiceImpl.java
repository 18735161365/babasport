package com.taotao.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.core.dao.order.DetailDao;
import com.taotao.core.dao.order.OrderDao;
import com.taotao.core.dao.product.ColorDao;
import com.taotao.core.dao.product.ProductDao;
import com.taotao.core.dao.product.SkuDao;
import com.taotao.core.dao.user.BuyerDao;
import com.taotao.core.pojo.BuyerCart;
import com.taotao.core.pojo.BuyerItem;
import com.taotao.core.pojo.order.Detail;
import com.taotao.core.pojo.order.Order;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.pojo.user.Buyer;
import com.taotao.core.service.BuyerService;

import redis.clients.jedis.Jedis;

/**
 * 用户管理
 * 订单管理
 * 购物车管理
 * @author lx
 *
 */
@Service("buyerService")
@Transactional
public class BuyerServiceImpl implements BuyerService {

	@Autowired
	private BuyerDao buyerDao;
	@Autowired
	private Jedis jedis;
	
//	通过用户名查询用户对象（如果在 就存在  如果不在就不存在）
	public Buyer selectBuyerByUsername(String username){
		String id = jedis.get(username);
		Buyer buyer = null;
		if(null != id){
			buyer = buyerDao.selectByPrimaryKey(Long.parseLong(id));
		}
		
		return buyer;
	}
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	@Autowired
	private ProductDao productDao;
	//通过SkuID查询一个Sku对象(颜色ID 查询颜色对象，商品ID查询商品对象）
	public Sku selectSkuById(Long id){
		Sku sku = skuDao.selectByPrimaryKey(id);
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
		return sku;
	}
//	将购物车保存到Redis中
	public void insertBuyerCartToRedis(BuyerCart buyerCart,String username){
		
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem item : items) {
			jedis.hincrBy("buyerCart:" + username, 
					String.valueOf(item.getSku().getId()), item.getAmount());
		}
		
	}
//	从Redis中取出来购物车
	public BuyerCart selectBuyerCartFromRedis(String username){
		
		BuyerCart buyerCart = null;
		
		Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
		if(null != hgetAll && hgetAll.size() > 0){
			buyerCart = new BuyerCart();
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			for (Entry<String, String> entry : entrySet) {
				buyerCart.addItem(Long.parseLong(entry.getKey()),
						Integer.parseInt(entry.getValue()));
				
			}
		}
		return buyerCart;
	}
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private DetailDao detailDao;
	//保存订单表
	//保存订单详情表
	public void insertOrder(Order order,String username){
//		订单表
//		ID、订单编号   由Redis生成
		Long id = jedis.incr("oId");
		order.setId(id);
		
		BuyerCart buyerCart = selectBuyerCartFromRedis(username);
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem item : items) {
			item.setSku(selectSkuById(item.getSku().getId()));
		}
//		运费       由购物车提供
		order.setDeliverFee(buyerCart.getFee());
//		总价
		order.setTotalPrice(buyerCart.getTotalPrice());
//		订单价
		order.setOrderPrice(buyerCart.getProductPrice());
		
//		支付方式    由页面传递
//		支付要求    0现金 1 POS
//		留言       
//		送货方式   略  
//		电话确认   略
		
//		支付状态   :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败
		if(order.getPaymentWay() == 1){
			order.setIsPaiy(0);
		}else{
			order.setIsPaiy(1);
		}
//		订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
		order.setOrderState(0);
//		时间：     由程序提供
		order.setCreateDate(new Date());
//		用户ID   由程序提供
		String buyerId = jedis.get(username);
		order.setBuyerId(Long.parseLong(buyerId));
		//保存订单表
		orderDao.insertSelective(order);
		//保存订单详情表
		for (BuyerItem item : items) {
			Detail detail = new Detail();
//			订单详情表
//			Id   自增长
//			订单ID
			detail.setOrderId(order.getId());
//			商品ID（编号）   由购物车中购物项
			detail.setProductId(item.getSku().getProductId());
//			商品名称
			detail.setProductName(item.getSku().getProduct().getName());
//			颜色名称
			detail.setColor(item.getSku().getColor().getName());
//			尺码名称
			detail.setSize(item.getSku().getSize());
//			价格
			detail.setPrice(item.getSku().getPrice());
//			数量、    快照
			detail.setAmount(item.getAmount());
			detailDao.insertSelective(detail);
			
		}
		//清空购物车
		jedis.del("buyerCart:" + username);
		//指定删除某个商品
//		jedis.hdel("buyerCart:" + username, "6652","6654");
		
	}
}
