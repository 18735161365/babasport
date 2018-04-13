package com.taotao.core.service.product;

import java.util.List;

import com.taotao.core.pojo.product.Brand;

import cn.itcast.common.page.Pagination;

public interface BrandService {
	
	
	//根据上面的二个条件查询品牌结果集 
	public List<Brand> selectBrandListByQuery(String name, Integer isDisplay); 
	
	//根据上面的二个条件及当前面查询分页对象
	public Pagination findPaginationByQuery(Integer pageNo,String name, Integer isDisplay);
	

	//通过ID查询品牌对象
	public Brand findBrandById(Long id);
	
	//修改完成
	public void updateBrandById(Brand brand);
	
	//删除
	public void deletes(Long[] ids);

}
