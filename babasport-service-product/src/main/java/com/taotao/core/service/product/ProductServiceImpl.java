package com.taotao.core.service.product;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.core.dao.product.ProductDao;
import com.taotao.core.dao.product.SkuDao;
import com.taotao.core.pojo.product.Color;
import com.taotao.core.pojo.product.Product;
import com.taotao.core.pojo.product.ProductQuery;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.pojo.product.SkuQuery;
import com.taotao.core.pojo.product.ProductQuery.Criteria;
import com.taotao.core.service.CmsService;
import com.taotao.core.service.StaticPageService;
import com.taotao.core.service.product.ProductService;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

/**
 * 商品管理
 * @author lx
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	
	//通过上面条件  查询分页对象（商品结果集）
	public Pagination selectPaginationByQuery(Integer pageNo,String name,Long brandId,Boolean isShow){
		//创建商品条件对象
		ProductQuery productQuery = new ProductQuery();
		
		StringBuilder params = new StringBuilder();
		
		Criteria createCriteria = productQuery.createCriteria();//Shift+Alt L 
		//判断
		if(null != name){
			createCriteria.andNameLike("%" + name +"%");
			params.append("name=").append(name);
		}
		if(null != brandId){
			createCriteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if(null != isShow){
			createCriteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		}else{
			//默认查询下架商品
			createCriteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}
		//当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数
		productQuery.setPageSize(8);
		//排序
		productQuery.setOrderByClause("id desc");
		
		//创建分页对象
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				productDao.countByExample(productQuery),
				productDao.selectByExample(productQuery)
				);
		
		//分页在页面上展示
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}
	@Autowired
	private SkuDao skuDao;
	@Autowired
	private Jedis jedis;
	//保存商品表    返回商品ID
	//保存库存表   商品ID外键
	public void insertProduct(Product product){
		//商品ID
		Long id = jedis.incr("pno");
		product.setId(id);
		//默认是下架
		product.setIsShow(false);
		//默认是不删除
		product.setIsDel(true);
		//时间
		product.setCreateTime(new Date());
		//保存
		productDao.insertSelective(product);
		
		//保存库存表
		//遍历颜色
		for(String colorId : product.getColors().split(",")){
			//遍历尺码
			for(String size : product.getSizes().split(",")){
				Sku sku = new Sku();
				//商品ID
				sku.setProductId(product.getId());
				//颜色ID
				sku.setColorId(Long.parseLong(colorId));
				//尺码
				sku.setSize(size);
				//价格
				sku.setPrice(0f);
				//库存
				sku.setStock(0);
				//运费
				sku.setDeliveFee(10f);
				//上限
				sku.setUpperLimit(100);
				//时间
				sku.setCreateTime(new Date());
				
				skuDao.insertSelective(sku);
			}
		}
	}
	@Autowired
	private JmsTemplate jmsTemplate;
	//上架（批量）
	public void isShow(Long[] ids) {
		Product product = new Product();
		//下架改上架
		product.setIsShow(true);
		for (final Long id : ids) {
			product.setId(id);
			//更新上架状态
			productDao.updateByPrimaryKeySelective(product);
			//发消息  商品ID
			jmsTemplate.send(new MessageCreator(){
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createTextMessage(String.valueOf(id));
				}
			});
		}
		
		
	}
}
