package com.taotao.core.service.product;

import java.util.List;

import com.taotao.core.pojo.product.Sku;

public interface SkuService {
	
	
	//通过商品ID查询库存结果集（每一个库存里都查询颜色名称）
	public List<Sku> findSkuListByProductId(Long productId);
	
	//修改
	public void updateSkuById(Sku sku);

}
