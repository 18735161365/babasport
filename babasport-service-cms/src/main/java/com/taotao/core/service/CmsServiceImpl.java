package com.taotao.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.core.dao.product.ColorDao;
import com.taotao.core.dao.product.ProductDao;
import com.taotao.core.dao.product.SkuDao;
import com.taotao.core.pojo.product.Product;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.pojo.product.SkuQuery;
import com.taotao.core.service.CmsService;

/**
 * 商品详情页面数据加载
 * @author lx
 *
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	
	//通过商品ID查询商品对象
	public Product selectProductById(Long id){
		return productDao.selectByPrimaryKey(id);
	}
	//通过商品Id查询库存结果集（每一个库存里只有颜色Id   通过颜色ID再查询颜色对象）
	public List<Sku> selectSkuListByProductId(Long id){
		//商品ID
		//库存大于0
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id).andStockGreaterThan(0);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
}
