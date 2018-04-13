package com.taotao.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.core.pojo.product.Brand;
import com.taotao.core.pojo.product.Color;
import com.taotao.core.pojo.product.Product;
import com.taotao.core.service.product.BrandService;
import com.taotao.core.service.product.ColorService;
import com.taotao.core.service.product.ProductService;

import cn.itcast.common.page.Pagination;

/**
 * 商品管理
 * 商品列表查询
 * 商品添加
 * 
 * 修改
 * 删除
 * 
 * @author lx
 *
 */
@Controller
public class ProductController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private ProductService productService;
	
	//商品列表查询
	@RequestMapping(value = "/product/list.do")
	public String list(Integer pageNo,String name,Long brandId,Boolean isShow,Model model){
		//品牌结果集   
		List<Brand> brands = brandService.selectBrandListByQuery(null, 1);
		//通过上面条件  查询分页对象（商品结果集）
		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
		model.addAttribute("pagination", pagination);
		model.addAttribute("brands", brands);
		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		model.addAttribute("isShow", isShow);
		
		return "product/list";
	}
	
	@Autowired
	private ColorService colorService;
	//去商品添加页面
	@RequestMapping(value = "/product/toAdd.do")
	public String toAdd(Model model){
		//品牌结果集   
		List<Brand> brands = brandService.selectBrandListByQuery(null, 1);
		//颜色结果集 
		List<Color> colors = colorService.findColorList();
		
		model.addAttribute("brands", brands);
		model.addAttribute("colors", colors);
		
		return "product/add";
	}
	//商品提交保存
	@RequestMapping(value = "/product/add.do")
	public String add(Product product){
		//保存商品表    返回商品ID
		//保存库存表   商品ID外键
		productService.insertProduct(product);
		
		return "redirect:/product/list.do";
	}
	//上架
	@RequestMapping(value = "/product/isShow.do")
	public String isShow(Long[] ids){
		//上架（批量）
		productService.isShow(ids);
		return "forward:/product/list.do";
	}
}
