package com.taotao.core.service;

import java.util.List;

import com.taotao.core.pojo.ad.Ad;
import com.taotao.core.pojo.ad.Position;

public interface AdService {
	
	
	//通过父ID查询广告位置结果集
	public List<Position> selectPositionListByParentId(Long parentId);
	
	
	//通过广告位置Id 查询 此广告位置下所有广告结果集
	public List<Ad> selectAdListByPositionId(Long positionId);
	
	//通过广告位置Id 查询 此广告位置下所有广告结果集  从缓存中
	public String selectAdListByPositionIdFromRedis(Long positionId);

}
