package com.taotao.core.dao.product;

import java.util.List;

import com.taotao.core.pojo.product.Brand;
import com.taotao.core.pojo.product.BrandQuery;

public interface BrandDao {
	
	//根据上面的二个条件查询品牌结果集    所有结果集   改成多功能的  所有结果集 还能查询分页结果集
	public List<Brand> selectBrandListByQuery(BrandQuery brandQuery); 
	
	//总条数
	public Integer countBrandByQuery(BrandQuery brandQuery);
	
	//通过ID查询品牌对象
	public Brand findBrandById(Long id);
	
	//修改完成
	public void updateBrandById(Brand brand);
	//删除
	public void deletes(Long[] ids);
	
	

}
