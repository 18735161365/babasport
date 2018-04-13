package com.taotao.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.core.dao.product.BrandDao;
import com.taotao.core.dao.product.ProductDao;
import com.taotao.core.dao.product.SkuDao;
import com.taotao.core.pojo.product.Brand;
import com.taotao.core.pojo.product.BrandQuery;
import com.taotao.core.pojo.product.Product;
import com.taotao.core.pojo.product.ProductQuery;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.pojo.product.SkuQuery;
import com.taotao.core.service.SearchService;

import cn.itcast.common.page.Pagination;
import redis.clients.jedis.Jedis;

/**
 * 搜索服务
 * @author lx
 *
 */
@Service("searchService")
public class SearchServiceImpl implements SearchService {

	
	@Autowired
	private SolrServer solrServer;
	
	//通过关键词及当前页查询分页对象
	public Pagination selectPaginationByQuery(String keyword,Integer pageNo
			,String price,Long brandId) throws Exception{
		//创建商品条件对象  
		ProductQuery productQuery = new ProductQuery();
		//当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		//每页数
		productQuery.setPageSize(8);
		
		StringBuilder params = new StringBuilder();
		
		//Solr服务器条件对象
		SolrQuery solrQuery = new SolrQuery();
		//关键词
		if(null != keyword){
			solrQuery.setQuery(keyword);
			params.append("keyword=").append(keyword);
		}else{
			solrQuery.setQuery("2016");
			params.append("keyword=").append(2016);
		}
		//过滤条件 
		//品牌ID
		if(null != brandId){
			solrQuery.addFilterQuery("brandId:" + brandId);
			params.append("&brandId=").append(brandId);
		}
		//价格区间  0-99  1600
		if(null != price){
			String[] p = price.split("-");
			if(p.length == 2){
				solrQuery.addFilterQuery("price:["+p[0]+" TO "+p[1]+"]");
			}else{
				solrQuery.addFilterQuery("price:["+p[0]+" TO *]");
			}
			params.append("&price=").append(price);
		}
		
		//高亮
		//1:开启高亮开关
		solrQuery.setHighlight(true);
		//2:设置需要高亮的域
		solrQuery.addHighlightField("name_ik");
		//3:高亮的域的前缀
		solrQuery.setHighlightSimplePre("<font color='red'>");
		//4:后缀
		solrQuery.setHighlightSimplePost("</font>");
		
		
		//排序  默认 价格 底到高
		solrQuery.setSort("price", ORDER.asc);
		//分页
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		//指定查询的域 field list
		solrQuery.set("fl", "id,name_ik,price,url");
		//设置默认要查询的域  default field
		solrQuery.set("df", "name_ik");
		
		//执行查询
		QueryResponse response = solrServer.query(solrQuery);
		
		//取出高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		//1:Map K:商品ID  V：map
		//2:Map K:需要高亮的域名 : V:list
		//3:List 支持多值   本次使用一个值  list.get(0)
		
		//结果集
		SolrDocumentList docs = response.getResults();
		//创建商品结果集
		List<Product> products = new ArrayList<>();
		for (SolrDocument doc : docs) {
			Product product = new Product();
			//商品ID
			String id = (String) doc.get("id");
			product.setId(Long.parseLong(id));
			//商品名称
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
//			String name = (String) doc.get("name_ik");
			product.setName(list.get(0));
			//商品价格
			product.setPrice((Float) doc.get("price"));
			//商品的图片
			String url = (String) doc.get("url");
			product.setImgUrl(url);
			
			products.add(product);
		}
		
		//总条数
		long numFound = docs.getNumFound();
		//创建分页对象
		Pagination pagination = new Pagination(
				productQuery.getPageNo(),
				productQuery.getPageSize(),
				(int) numFound,
				products
				);
		//分页在页面上展示（显示）
		String url = "/Search";
		pagination.pageView(url, params.toString());
		
		return pagination;
	}
	@Autowired
	private Jedis jedis;
	@Autowired
	private BrandDao brandDao;
	//查询品牌结果集  （从缓存中查询）
	public List<Brand> findBrandListFromRedis(){
		
		Map<String, String> hgetAll = jedis.hgetAll("brand");
		
		List<Brand> brands = null;
		
		if(null != hgetAll && hgetAll.size() > 0){
			brands = new ArrayList<>();
			//从缓存中查询
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			for (Entry<String, String> entry : entrySet) {
				Brand brand = new Brand();
				brand.setId(Long.parseLong(entry.getKey()));
				brand.setName(entry.getValue());
				brands.add(brand);	
			}
		}else{
			//从Mysql数据库中查询
			BrandQuery brandQuery = new BrandQuery();
			brandQuery.setIsDisplay(1);
			brands = brandDao.selectBrandListByQuery(brandQuery);
			//保存到缓存中为了下次查询时 就可以查询缓存了
			Map<String,String> hash = new HashMap<>();
			for (Brand brand : brands) {
				hash.put(String.valueOf(brand.getId()), brand.getName());
			}
			//保存一次缓存
			jedis.hmset("brand", hash);
		}
		return brands;
	}
	@Autowired
	private ProductDao productDao;
	@Autowired
	private SkuDao skuDao;
	//保存商品信息到索引库
	public void insertProductToSolr(Long id){
		//TODO 保存商品信息到索引库
		SolrInputDocument doc = new SolrInputDocument();
		//商品ID
		doc.setField("id", id);
		//商品名称
		Product p = productDao.selectByPrimaryKey(id);
		doc.setField("name_ik", p.getName());
		//商品价格  select price from bbs_sku where product_id = 442  order by price asc limit 0,1
		SkuQuery skuQuery = new SkuQuery();
		//where 条件部分
		skuQuery.createCriteria().andProductIdEqualTo(id);
		//排序
		skuQuery.setOrderByClause("price asc");
		//分页
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		//指定查询的字段
		skuQuery.setFields("price");
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		doc.setField("price", skus.get(0).getPrice());
		//商品图片
		doc.setField("url", p.getImgUrl());
		//品牌ID
		doc.setField("brandId", p.getBrandId());
		try {
			solrServer.add(doc, 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
