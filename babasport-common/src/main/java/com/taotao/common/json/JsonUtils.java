package com.taotao.common.json;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 对象与Json字符串之间互转
 * @author lx
 *
 */
public class JsonUtils {
	
	private static ObjectMapper om = new ObjectMapper();
	static{
		om.setSerializationInclusion(Include.NON_NULL);
	}
	//对象转Json字符串
	public static String objectToJson(Object obj){
		try {
			return  om.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//Json格式字符串转成对象 
	public static <T> T jsonToObject(String json,Class<T> javaType){
		T t = null;
		try {
			t = om.readValue(json, javaType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
	
}
