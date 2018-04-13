package com.taotao.core.service;

import java.util.List;

import com.taotao.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface SearchService {
	
	
	//通过关键词及当前页查询分页对象
	public Pagination selectPaginationByQuery(String keyword,Integer pageNo
			,String price,Long brandId) throws Exception;
	
	//查询品牌结果集  （从缓存中查询）
	public List<Brand> findBrandListFromRedis();
	
	//保存商品信息到索引库
	public void insertProductToSolr(Long id);

}
