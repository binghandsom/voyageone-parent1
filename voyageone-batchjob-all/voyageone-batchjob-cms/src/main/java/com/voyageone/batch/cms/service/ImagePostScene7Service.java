package com.voyageone.batch.cms.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.cms.dao.ImageDao;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.configs.beans.FtpBean;
import com.voyageone.common.util.FtpUtil;
import com.voyageone.common.util.HttpUtils;

@Service
public class ImagePostScene7Service {
	
	private static Log logger = LogFactory.getLog(ImagePostScene7Service.class);
	
	public static Map<String, String> CHANNEL_IMAGE_FOLD = new HashMap<String, String>();
	
	@Autowired
	private ImageDao imageDao;
	
	@Autowired
	IssueLog issueLog;
	
	static {
		CHANNEL_IMAGE_FOLD.put("010", "/Jewelry");
	}
	
	/**
	 * 取出要处理的图片url列表
	 * 
	 * @return
	 */
	public List<String> getImageUrls(String orderChannelId) {
		return imageDao.getImageUrls(orderChannelId);
	}
	
	/**
	 * 根据图片url上传scene7图片文件
	 * 
	 * @return
	 */
	public boolean getAndSendImage(String orderChannelId, List<String> imageUrlList, List<String> successImageUrlList, int threadNo) throws Exception {
		boolean isSuccess = true;
		
		if (imageUrlList != null && imageUrlList.size() > 0) {
			
			InputStream inputStream = null;
			
			FtpBean ftpBean = new FtpBean();
			// ftp连接port
			ftpBean.setPort("21");
			// ftp连接url
			ftpBean.setUrl("s7ftp2.scene7.com");
			// ftp连接usernmae
			ftpBean.setUsername("maintenance-40voyageone-2eco8553");
			// ftp连接password
			ftpBean.setPassword("VoyageOne#");
			ftpBean.setFile_coding("utf-8");
			
			//FTP服务器保存目录设定
			ftpBean.setUpload_path(CHANNEL_IMAGE_FOLD.get(orderChannelId));
			
			FtpUtil ftpUtil = new FtpUtil();
			FTPClient ftpClient = new FTPClient();
			
			try {
				//建立连接
				ftpClient = ftpUtil.linkFtp(ftpBean);
				if (ftpClient != null) {
					
					boolean change = ftpClient.changeWorkingDirectory(ftpBean.getUpload_path());
					if (change) {
						ftpClient.enterLocalPassiveMode();
						ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
						ftpClient.setConnectTimeout(120000);
						
						for (int i = 0; i < imageUrlList.size(); i++) {
							String imageUrl = imageUrlList.get(i);
							
							inputStream = HttpUtils.getInputStream(imageUrl, null);
							
							int lastSlash = imageUrl.lastIndexOf("/");
							String fileName = imageUrl.substring(lastSlash + 1);
							
							boolean result = ftpClient.storeFile(fileName, inputStream);

							if (result) {
								successImageUrlList.add(imageUrl);
								
								logger.info("thread-" + threadNo + ":" + imageUrl + "上传成功!");
								
							} else {
								isSuccess = false;
								
								break;
							}
							
							if (inputStream != null) {
								inputStream.close();
							}
						}
					}
				}
					
			} catch (Exception ex) {
				System.out.println(ex);
				isSuccess = false;
				
			} finally {
				//断开连接
				if (ftpClient != null) {
					ftpUtil.disconnectFtp(ftpClient);
				}
				
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
		
		return isSuccess;
	}
	
	/**
	 * 更新已上传图片发送标志
	 * 
	 * @param orderChannelId
	 * @param successImageUrlList
	 * @param taskName
	 * @return
	 */
	public int updateImageSendFlag(String orderChannelId, List<String> successImageUrlList, String taskName) {
		return imageDao.updateImageSendFlag(orderChannelId, successImageUrlList, taskName);
	}
	
	public static void main(String[] args) {
		String imageUrl = "http://max1.jewelrystatic.com/media/catalog/product/a/t/atv722az_main.jpg";
		int lastSlash = imageUrl.lastIndexOf("/");
		String fileName = imageUrl.substring(lastSlash + 1);
		System.out.println(fileName);
	}
}
