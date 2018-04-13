package com.taotao.core.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.core.dao.product.ColorDao;
import com.taotao.core.pojo.product.Color;
import com.taotao.core.pojo.product.ColorQuery;
import com.taotao.core.service.product.ColorService;

/**
 * 颜色管理
 * @author lx
 *
 */
@Service("colorService")
public class ColorServiceImpl implements ColorService {

	
	@Autowired
	private ColorDao colorDao;
	//查询颜色结果集
	public List<Color> findColorList(){
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		return colorDao.selectByExample(colorQuery);
	}
}
