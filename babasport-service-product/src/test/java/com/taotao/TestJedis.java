package com.taotao;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestJedis {

	@Autowired
	private Jedis jedis;
	@Test
	public void testAdd() throws Exception {
//		Jedis jedis = new Jedis("192.168.200.128",6379);
//		jedis.set("ww", "22");
		jedis.incr("ww");
		jedis.incrBy("ww", 3);
		String age = jedis.get("ww");
		System.out.println(age);
	}
	//JedisCluster集群
	@Autowired
	private JedisCluster jc;
	@Test
	public void testjedisCluster() throws Exception {
	/*	Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.200.128", 6379));
		nodes.add(new HostAndPort("192.168.200.128", 6380));
		nodes.add(new HostAndPort("192.168.200.128", 6381));
		nodes.add(new HostAndPort("192.168.200.128", 6382));
		nodes.add(new HostAndPort("192.168.200.128", 6383));
		nodes.add(new HostAndPort("192.168.200.128", 6384));
		JedisCluster jc = new JedisCluster(nodes);*/
		
		jc.incr("ww");
		jc.incrBy("ww", 3);
		String age = jc.get("ww");
		System.out.println(age);
		
		
	}
}
