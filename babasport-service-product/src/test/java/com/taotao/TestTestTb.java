package com.taotao;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.taotao.core.dao.TestTbDao;
import com.taotao.core.pojo.TestTb;
import com.taotao.core.service.TestTbService;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestTestTb {

	@Autowired
//	private TestTbDao testTbDao;
	private TestTbService testTbService;
	
	@Test
	public void testAdd() throws Exception {
		TestTb testTb = new TestTb();
		testTb.setName("成龙");
		testTb.setBirthday(new Date());
		testTbService.addTestTb(testTb);
		
	}
}
