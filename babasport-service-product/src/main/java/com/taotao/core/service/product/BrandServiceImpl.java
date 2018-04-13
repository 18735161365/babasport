package com.taotao.core.service.product;

import java.util.List;

import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.core.dao.product.BrandDao;
import com.taotao.core.pojo.product.Brand;
import com.taotao.core.pojo.product.BrandQuery;
import com.taotao.core.service.product.BrandService;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;
/**
 * 品牌管理
 * @author lx
 *
 */
@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {

	
	@Autowired
	private BrandDao brandDao;
	@Override
	public List<Brand> selectBrandListByQuery(String name, Integer isDisplay) {
		// TODO Auto-generated method stub
		BrandQuery brandQuery = new BrandQuery();
		//判断
		if(null != name){
			brandQuery.setName(name);
		}
		if(null != isDisplay){
			brandQuery.setIsDisplay(isDisplay);
		}else{
			//默认查询是的
			brandQuery.setIsDisplay(1);
		}
		return brandDao.selectBrandListByQuery(brandQuery);
	}
	
	//根据上面的二个条件及当前面查询分页对象
	public Pagination findPaginationByQuery(Integer pageNo,String name, Integer isDisplay){
		BrandQuery brandQuery = new BrandQuery();
		
		StringBuilder params = new StringBuilder();
		
		
		//判断
		if(null != name){
			brandQuery.setName(name);
			params.append("name=").append(name);
		}
		if(null != isDisplay){
			brandQuery.setIsDisplay(isDisplay);
			params.append("&isDisplay=").append(isDisplay);
		}else{
			//默认查询是的
			brandQuery.setIsDisplay(1);
			params.append("&isDisplay=").append(1);
		}
		//当前页
		brandQuery.setPageNo(Pagination.cpn(pageNo));
//		//每页数
		brandQuery.setPageSize(5);
		
		//创建分页对象
		Pagination pagination = new Pagination(
				brandQuery.getPageNo(),
				brandQuery.getPageSize(),
				brandDao.countBrandByQuery(brandQuery),
				brandDao.selectBrandListByQuery(brandQuery)
				);
		//分页在页面上的展示
		String url = "/brand/list.do";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}

	@Override
	public Brand findBrandById(Long id) {
		// TODO Auto-generated method stub
		return brandDao.findBrandById(id);
	}
	@Autowired
	private Jedis jedis;
	
	
	//进入切面    通过数据源 直接连接Mysql数据库     begin transation    此处开始  Mysql  
	//修改完成
	public void updateBrandById(Brand brand){
		//修改Mysql
		brandDao.updateBrandById(brand); 
		//保存缓存
		jedis.hset("brand",String.valueOf(brand.getId()), brand.getName());
		//进入切面    通过数据源 直接连接Mysql数据库     rollback
	}
	//进入切面    通过数据源 直接连接Mysql数据库   commit
	//删除
	public void deletes(Long[] ids){
		brandDao.deletes(ids);
	}
}
