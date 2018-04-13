package com.taotao.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.core.pojo.ad.Ad;
import com.taotao.core.pojo.ad.Position;
import com.taotao.core.service.AdService;

/**
 * 广告位置管理
 * 广告管理
 * @author lx
 *
 */
@Controller
public class AdController {

	
	@Autowired
	private AdService adService;
	//第一种： 初始化加载   url : "/position/tree.do"   默认入参： String root = source   查询父ID为0的广告位置结果集     
	//第二种： 点击事件       url : "/position/tree.do"   默认入参： String root = (点击元素的ID)  查询父ID为（点击元素的ID）的广告位置结果集
	//返回值 ：自动回调  回调的数据固定格式 （JSON的固定格式）
//	广告位置结果集
	@RequestMapping(value = "/position/tree.do")
	public String tree(String root,Model model){
		List<Position> positions = null;
		//判断
		if("source".equals(root)){
			// 初始化加载
			positions = adService.selectPositionListByParentId(0L);
		}else{
			//点击事件
			positions = adService.selectPositionListByParentId(Long.parseLong(root));
		}
		model.addAttribute("list", positions);
		
		return "position/tree";
		
	}
	//广告位置子位置的列表查询
	@RequestMapping(value = "/position/list.do")
	public String list(Long root,Model model){
		//通过Root查询  父ID为root的广告位置结果集
		List<Position> positions = adService.selectPositionListByParentId(root);
		model.addAttribute("positions", positions);
		
		return "position/list";
	}
	//通过广告位置Id 查询 此广告位置下所有广告结果集
	@RequestMapping(value = "/ad/list.do")
	public String adList(Long root,String name, Model model){
		//通过广告位置Id 查询 此广告位置下所有广告结果集
		List<Ad> ads = adService.selectAdListByPositionId(root);
		model.addAttribute("ads", ads);
		model.addAttribute("name", name);
		return "ad/list";
	}
}
