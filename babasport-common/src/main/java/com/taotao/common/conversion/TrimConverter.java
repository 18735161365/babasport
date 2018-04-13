package com.taotao.common.conversion;

import org.springframework.core.convert.converter.Converter;

/**
 * 去掉前后空格 如果本身就是空格 返回NUll
 * S:传递过来的数据类型
 * T：转换后的类型
 * @author lx
 *
 */
public class TrimConverter implements Converter<String, String>{

	@Override
	public String convert(String source) { 
		// TODO Auto-generated method stub
		try {
			if(null != source){
				source = source.trim();
				if(!"".equals(source)){
					return source;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
