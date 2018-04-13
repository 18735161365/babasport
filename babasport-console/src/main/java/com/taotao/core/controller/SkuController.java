package com.taotao.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.core.pojo.product.Sku;
import com.taotao.core.service.product.SkuService;

/**
 * 库存管理
 * 列表查询
 * 修改
 * 保存
 * @author lx
 *
 */
@Controller
public class SkuController {

	@Autowired
	private SkuService skuService;
	//列表查询
	@RequestMapping(value = "/sku/list.do")
	public String list(Long productId,Model model){
		//通过商品ID查询库存结果集（每一个库存里都查询颜色名称）
		List<Sku> skus = skuService.findSkuListByProductId(productId);
		model.addAttribute("skus", skus);
		return "sku/list";
	}
	//异步  保存
	@RequestMapping(value = "/sku/addSku.do")
	@ResponseBody
	public String addSku(Sku sku){
		String message = null;
		try {
			skuService.updateSkuById(sku);
			message = "1";
		} catch (Exception e) {
			// TODO: handle exception
			message = "0";
		}
		return message;
	}
}
