package com.taotao.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.core.dao.product.ColorDao;
import com.taotao.core.dao.product.SkuDao;
import com.taotao.core.pojo.product.Color;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.pojo.product.SkuQuery;
import com.taotao.core.service.product.SkuService;

/**
 * 库存管理
 * @author lx
 *
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {
	
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private ColorDao colorDao;
	
	//通过商品ID查询库存结果集（每一个库存里都查询颜色名称）
	public List<Sku> findSkuListByProductId(Long productId){
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(productId);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		for (Sku sku : skus) {
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}
	//修改
	public void updateSkuById(Sku sku){
		skuDao.updateByPrimaryKeySelective(sku);
	}
}
