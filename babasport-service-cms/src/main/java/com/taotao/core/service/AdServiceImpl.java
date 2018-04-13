package com.taotao.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taotao.common.json.JsonUtils;
import com.taotao.core.dao.ad.AdDao;
import com.taotao.core.dao.ad.PositionDao;
import com.taotao.core.pojo.ad.Ad;
import com.taotao.core.pojo.ad.AdQuery;
import com.taotao.core.pojo.ad.Position;
import com.taotao.core.pojo.ad.PositionQuery;
import com.taotao.core.service.AdService;

import redis.clients.jedis.Jedis;

/**
 * 广告管理
 * 广告位置管理
 * @author lx
 *
 */
@Service("adService")
@Transactional
public class AdServiceImpl implements AdService {

	@Autowired
	private PositionDao positionDao;
	@Autowired
	private AdDao adDao;
	@Autowired
	private Jedis jedis;
	
	//通过父ID查询广告位置结果集
	public List<Position> selectPositionListByParentId(Long parentId){
		PositionQuery positionQuery = new PositionQuery();
		positionQuery.createCriteria().andParentIdEqualTo(parentId);
		return positionDao.selectByExample(positionQuery);
	}
	//通过广告位置Id 查询 此广告位置下所有广告结果集
	public List<Ad> selectAdListByPositionId(Long positionId){
		AdQuery adQuery = new AdQuery();
		adQuery.createCriteria().andPositionIdEqualTo(positionId);
		return adDao.selectByExample(adQuery);
	}
	//通过广告位置Id 查询 此广告位置下所有广告结果集  从缓存中
	public String selectAdListByPositionIdFromRedis(Long positionId){
		//从缓存中查询
		String ads = jedis.get("ads");
		if(null == ads){
			//从数据库查询
			AdQuery adQuery = new AdQuery();
			adQuery.createCriteria().andPositionIdEqualTo(positionId);
			ads = JsonUtils.objectToJson(adDao.selectByExample(adQuery));
			//保存缓存一份
			jedis.set("ads", ads);
			//存活时间
			jedis.expire("ads", 60*60*24*2);
		}
		return ads;
	}
}
