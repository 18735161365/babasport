package com.taotao;

import static org.junit.Assert.*;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestSolrJ {

	
	@Autowired
	private SolrServer solrServer;
	@Test
	public void testAdd() throws Exception {
//		String baseURL = "http://192.168.200.128:8080/solr";
//		SolrServer solrServer = new HttpSolrServer(baseURL);
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", 3);
		doc.setField("name", "范冰冰");
		//添加
		solrServer.add(doc, 1000);
	}
	//集群版
	@Autowired
	private CloudSolrServer cloudSolrServer;
	@Test
	public void testeSolrCloud() throws Exception {
//		String zkHost = "192.168.200.128:2181,192.168.200.128:2182,192.168.200.128:2183";
//		CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHost);
		//设置总数据名称
//		cloudSolrServer.setDefaultCollection("collection1");
		
		
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", 6);
		doc.setField("name", "黄忠");
		//添加
		cloudSolrServer.add(doc, 1000);
		
		
	}
}
