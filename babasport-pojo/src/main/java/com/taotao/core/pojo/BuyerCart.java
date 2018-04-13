package com.taotao.core.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.core.pojo.product.Sku;

/**
 * 购物车
 * @author lx
 *
 */
public class BuyerCart implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//商品结果集
	private List<BuyerItem> items = new ArrayList<BuyerItem>();
	//追加商品
	public void addItem(Long skuId,Integer amount){
		Sku sku = new Sku();
		sku.setId(skuId);
		BuyerItem item = new BuyerItem();
		item.setSku(sku);
		item.setAmount(amount);
		
		if(items.contains(item)){
			//包含
			for (BuyerItem it : items) {
				if(it.equals(item)){
					it.setAmount(item.getAmount() + it.getAmount());
				}
			}
		}else{
			items.add(item);
		}
		
	}
	

	public List<BuyerItem> getItems() {
		return items;
	}

	public void setItems(List<BuyerItem> items) {
		this.items = items;
	}
	
	//统计或小计
	//商品件数
	@JsonIgnore
	public Integer getProductAmount(){
		Integer result = 0;
		for (BuyerItem item : items) {
			result += item.getAmount();
		}
		return result;
	}
	//商品金额
	@JsonIgnore
	public Float getProductPrice(){
		Float result = 0f;
		for (BuyerItem item : items) {
			result += item.getAmount()*item.getSku().getPrice();
		}
		return result;
	}
	//运费
	@JsonIgnore
	public Float getFee(){
		Float result = 0f;
		if(getProductPrice() < 88){
			result = 6f;
		}
		return result;
	}
	//总价格
	@JsonIgnore
	public Float getTotalPrice(){
		return getProductPrice() + getFee();
	}
	
    
}
