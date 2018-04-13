package com.taotao.core.service.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.core.pojo.product.Color;
import com.taotao.core.pojo.product.Product;
import com.taotao.core.pojo.product.Sku;
import com.taotao.core.service.CmsService;
import com.taotao.core.service.StaticPageService;
/**
 * 自定义消息处理类
 * @author lx
 *
 */
public class CustomMessageListener implements MessageListener{

	@Autowired
	private CmsService cmsService;
	@Autowired
	private StaticPageService staticPageService;
	
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ActiveMQTextMessage atm = (ActiveMQTextMessage)message;
		try {
			String id = atm.getText();
			System.out.println("CMS:" + id);
			//静态化
			Map<String,Object> root = new HashMap<>();
			//数据
			//通过商品ID查询商品对象
			Product p = cmsService.selectProductById(Long.parseLong(id));
			//通过商品Id查询库存结果集（每一个库存里只有颜色Id   通过颜色ID再查询颜色对象）
			List<Sku> skus = cmsService.selectSkuListByProductId(Long.parseLong(id));
			//去重颜色
			Set<Color> colors = new HashSet<>();
			for (Sku sku : skus) {
				colors.add(sku.getColor());
			}
			root.put("product", p);
			root.put("skus", skus);
			root.put("colors", colors);
			
			staticPageService.index(root, Long.parseLong(id));
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
