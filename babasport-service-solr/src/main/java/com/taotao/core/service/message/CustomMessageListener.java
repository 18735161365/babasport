package com.taotao.core.service.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.core.service.SearchService;
/**
 * 自定义消息处理类
 * @author lx
 *
 */
public class CustomMessageListener implements MessageListener{

	
	@Autowired
	private SearchService searchService;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ActiveMQTextMessage atm = (ActiveMQTextMessage)message;
		try {
			String id = atm.getText();
			System.out.println("Solr:" + id);
			searchService.insertProductToSolr(Long.parseLong(id));
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
