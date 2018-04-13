package com.taotao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.taotao.core.dao.TestTbDao;
import com.taotao.core.dao.product.ProductDao;
import com.taotao.core.pojo.TestTb;
import com.taotao.core.pojo.product.Product;
import com.taotao.core.pojo.product.ProductQuery;
import com.taotao.core.service.TestTbService;

/**
 * 测试
 * @author lx
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestProduct {

	@Autowired
	private ProductDao productDao;
	
	@Test
	public void testAdd() throws Exception {
		
	/*	
		Product p = productDao.selectByPrimaryKey(2L);
		System.out.println(p);*/
		String name = "大";
		ProductQuery productQuery = new ProductQuery();
		productQuery.createCriteria().andBrandIdEqualTo(4L).andNameLike("%" + name + "%");
		
		List<Product> products = productDao.selectByExample(productQuery);
		for (Product product : products) {
			System.out.println(product);
		}
	}
}
