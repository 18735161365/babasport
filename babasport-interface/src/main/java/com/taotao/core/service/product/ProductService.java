package com.taotao.core.service.product;

import com.taotao.core.pojo.product.Product;

import cn.itcast.common.page.Pagination;

public interface ProductService {
	
	//通过上面条件  查询分页对象（商品结果集）
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Long brandId,Boolean isShow);
	
	//保存商品表    返回商品ID
	//保存库存表   商品ID外键
	public void insertProduct(Product product);
	
	
	//上架（批量）
	public void isShow(Long[] ids);

}
