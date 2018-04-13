package com.taotao.core.service.product;

import org.springframework.stereotype.Service;

import com.taotao.common.fdfs.FastDFSUtils;
import com.taotao.core.service.product.UploadService;

/**
 * 上传图片
 * @author lx
 *
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

	//上传图片
	public String uploadPic(byte[] pic ,String name,long size) throws Exception{
		return FastDFSUtils.uploadPic(pic, name, size);
	}
}
