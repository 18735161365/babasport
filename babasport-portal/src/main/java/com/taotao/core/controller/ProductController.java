package com.taotao.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.json.JsonUtils;
import com.taotao.core.pojo.ad.Ad;
import com.taotao.core.pojo.product.Brand;
import com.taotao.core.pojo.product.Color;
import com.taotao.core.pojo.product.Product;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.service.AdService;
import com.taotao.core.service.CmsService;
import com.taotao.core.service.SearchService;

import cn.itcast.common.page.Pagination;

/**
 * 首页
 * 搜索页面
 * 商品详情页面
 * @author lx
 *
 */
@Controller
public class ProductController {

	
	@Autowired
	private AdService adService;
	//进入首页
	@RequestMapping(value = "/")
	public String index(Model model){
		//查询大广告位置下的所有广告结果集  89外键
		model.addAttribute("ads", adService.selectAdListByPositionIdFromRedis(89L));
		return "index";
	}
	
	@Autowired
	private SearchService searchService;
	//搜索
	@RequestMapping(value = "/Search")
	public String search(String keyword,Integer pageNo,String price,Long brandId,Model model) throws Exception{
		//查询品牌结果集  （从缓存中查询）
		List<Brand> brands = searchService.findBrandListFromRedis();
		model.addAttribute("brands", brands);
		
		//已选条件
		Map<String,String> map = new HashMap<>();
		if(null != price){
			if(price.contains("-")){
				map.put("价格", price);
			}else{
				map.put("价格", price + "以上");
			}
		}
		//品牌
		if(null != brandId){
			for (Brand brand : brands) {
				if(brandId.equals(brand.getId())){
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		model.addAttribute("map", map);
		
		//通过关键词 ，品牌ID 价格区间， 及当前页查询分页对象
		Pagination pagination = searchService.selectPaginationByQuery(keyword, pageNo
				,price,brandId);
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		model.addAttribute("price", price);
		model.addAttribute("brandId", brandId);
		
		return "search";
	}
	
	@Autowired
	private CmsService cmsService;
	//去商品 详情页面
	@RequestMapping(value = "/product/detail")
	public String detail(Long id,Model model){
		//通过商品ID查询商品对象
		Product product = cmsService.selectProductById(id);
		//通过商品Id查询库存结果集（每一个库存里只有颜色Id   通过颜色ID再查询颜色对象）
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		//去重颜色
		Set<Color> colors = new HashSet<>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		
		model.addAttribute("product", product);
		model.addAttribute("skus", skus);
		model.addAttribute("colors", colors);
		return "product";
	}
}
