package com.taotao.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.core.pojo.product.Brand;
import com.taotao.core.service.product.BrandService;

import cn.itcast.common.page.Pagination;

/**
 * 品牌管理
 * 添加  课下
 * 修改  课上
 * 删除   全删除（课上） 单删除 课下
 * 查询（条件 + 分页） 课上
 * @author lx
 *
 */
@Controller
public class BrandController {

	
	@Autowired
	private BrandService brandService;
	
	//品牌管理之列表查询
	@RequestMapping(value = "/brand/list.do")
	public String list(Integer pageNo,String name,Integer isDisplay,Model model){
		
		//根据上面的二个条件及当前面查询分页对象
		Pagination pagination = brandService.findPaginationByQuery(pageNo, name, isDisplay);
		
		
		//根据上面的二个条件查询品牌结果集 
//		List<Brand> brands = brandService.selectBrandListByQuery(name, isDisplay);
		model.addAttribute("pagination", pagination);
		model.addAttribute("name", name);
		model.addAttribute("isDisplay", isDisplay);
		
		
		return "brand/list";
	}
	//去修改
	@RequestMapping(value = "/brand/toEdit.do")
	public String toEdit(Long id,Model model){
		//通过ID查询品牌对象
		Brand brand = brandService.findBrandById(id);
		model.addAttribute("brand", brand);
		return "brand/edit";
	}
	//修改提交
	@RequestMapping(value = "/brand/edit.do")
	public String edit(Brand brand){
		//修改完成
		brandService.updateBrandById(brand);
		return "redirect:/brand/list.do";
	}
	//删除
	@RequestMapping(value = "/brand/deletes.do")
	public String deletes(Long[] ids){
		//删除
		brandService.deletes(ids);
		return "forward:/brand/list.do";
	}
}
