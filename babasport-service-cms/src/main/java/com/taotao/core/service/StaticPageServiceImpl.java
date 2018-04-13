package com.taotao.core.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.core.service.StaticPageService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化处理
 * @author lx
 *
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

	
	private Configuration conf;
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	//静态化主程序
	public void index(Map<String,Object> root,Long id){
		
		//输出全路径
		String path = getPath("/html/product/" + id + ".html");
		
		File f = new File(path);
		File parentFile = f.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		
		Writer out = null;
		try { 
			//2：加载模板  读
			Template template = conf.getTemplate("product.html");
			//输出  写
			out = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
			template.process(root, out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(null != out){
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	//获取全路径
	public String getPath(String path){
		return servletContext.getRealPath(path);
	}
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
		
	}
}
