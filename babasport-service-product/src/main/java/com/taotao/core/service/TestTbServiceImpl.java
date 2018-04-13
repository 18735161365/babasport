package com.taotao.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.core.dao.TestTbDao;
import com.taotao.core.pojo.TestTb;
import com.taotao.core.service.TestTbService;

@Service("testTbService")
@Transactional
public class TestTbServiceImpl implements TestTbService {

	@Autowired
	private TestTbDao testTbDao;
	public void addTestTb(TestTb testTb){
		testTbDao.addTestTb(testTb);
//		throw new RuntimeException();
	}
}
