package com.taotao.common.fdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * //分布式文件系统Java接口
 * @author lx
 *
 */
public class FastDFSUtils {

	//上传
	public static String uploadPic(byte[] pic,String name,long size) throws Exception{
		
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		//配置文件
		ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
		//创建Tracker客户端
		TrackerClient trackerClient = new TrackerClient();
		//连接tracker (地址：保存Storage的地址）
		TrackerServer trackerServer = trackerClient.getConnection();
		
		StorageServer storageServer = null;
		//创建Storage客户端
		StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
		//上传图片
		String ext = FilenameUtils.getExtension(name);
		
		NameValuePair[] meta_list = new NameValuePair[3];
		meta_list[0] = new NameValuePair("filename",name);
		meta_list[1] = new NameValuePair("fileext",ext);
		meta_list[2] = new NameValuePair("filesize",String.valueOf(size));
		
//			group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
		String path = storageClient1.upload_file1(pic, ext, meta_list);
		return path;
	}
}
