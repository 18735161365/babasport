package com.taotao.core.service;

import java.util.List;

import com.taotao.core.pojo.product.Product;
import com.taotao.core.pojo.product.Sku;

public interface CmsService {

	// 通过商品ID查询商品对象
	public Product selectProductById(Long id);

	// 通过商品Id查询库存结果集（每一个库存里只有颜色Id 通过颜色ID再查询颜色对象）
	public List<Sku> selectSkuListByProductId(Long id);

}
