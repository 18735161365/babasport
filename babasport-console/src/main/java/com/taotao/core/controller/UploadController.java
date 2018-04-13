package com.taotao.core.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.taotao.common.web.Constants;
import com.taotao.core.service.product.UploadService;

/**
 * 上传图片
 * 品牌
 * 商品
 * 
 * @author lx
 *
 */
@Controller
public class UploadController {

	
	@Autowired
	private UploadService uploadService;
	//上传品牌图片
	@RequestMapping(value = "/upload/uploadPic.do")
	public void uploadPic(MultipartFile pic,HttpServletRequest request
			,HttpServletResponse response) throws Exception{
		//保存在分布式文件系统中
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		
		JSONObject jo = new JSONObject();
		jo.put("path", Constants.IMG_URL + path);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
		
	}
	//上传商品图片   批量
	@RequestMapping(value = "/upload/uploadPics.do")
	@ResponseBody
	public List<String> uploadPics(@RequestParam(required=false) MultipartFile[] pics) throws  Exception{
		List<String> urls = new ArrayList<>();
		for (MultipartFile pic : pics) {
			//保存在分布式文件系统中
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			urls.add(Constants.IMG_URL + path);
		}
		return urls;
	}
	//上传富文本编辑器的图片
	@RequestMapping(value = "/upload/uploadFck.do")
	public void uploadFck(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//无敌版接收
		MultipartRequest mr = (MultipartRequest)request;
		//取出的都是图片
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			//保存在分布式文件系统中
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			JSONObject jo = new JSONObject();
			jo.put("url", Constants.IMG_URL + path);
			jo.put("error", 0);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(jo.toString());
		}
	}
}
