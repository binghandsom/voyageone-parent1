package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ftp.FtpComponentFactory;
import com.voyageone.components.ftp.FtpConstants;
import com.voyageone.components.ftp.bean.FtpFileBean;
import com.voyageone.components.ftp.service.BaseFtpComponent;
import com.voyageone.task2.cms.dao.ImageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImagePostScene7Service {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ImageDao imageDao;

	@Autowired
	private IssueLog issueLog;

	@Autowired
	private TransactionRunner transactionRunner;

	private static Map<String, Integer> fileNotFoundUrlMap = new HashMap<>();

	private final static int retryTimes = 5;

	/**
	 * 取出要处理的图片url列表
	 */
	public List<Map<String, String>> getImageUrls(String orderChannelId) {
		return imageDao.getImageUrls(orderChannelId);
	}

	/**
	 * 根据图片url上传scene7图片文件
	 *
	 */
	public boolean getAndSendImage(String orderChannelId,
								   List<Map<String, String>> imageUrlList,
								   List<String> successImageUrlList,
								   List<Map<String, String>> urlErrorList,
								   int threadNo) {
		boolean isSuccess = true;

		if (imageUrlList != null && imageUrlList.size() > 0) {

			//FTP服务器保存目录设定
			String uploadPath = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.scene7_image_folder);

			// FtpBean初期化
			BaseFtpComponent ftpComponent = FtpComponentFactory.getFtpComponent(FtpConstants.FtpConnectEnum.SCENE7_FTP);

			String imageUrl;
			InputStream inputStream = null;

			try {
				//建立连接
				ftpComponent.openConnect();
				ftpComponent.enterLocalPassiveMode();
				for (Map<String, String> anImageUrlList : imageUrlList) {
					imageUrl = String.valueOf(anImageUrlList.get("image_url"));

					if (StringUtils.isNullOrBlank2(imageUrl)) {
						successImageUrlList.add(imageUrl);
						continue;
					}

					try {
						inputStream = HttpUtils.getInputStream(imageUrl);
					} catch (FileNotFoundException ex) {
						// 图片url错误
						logger.error(ex.getMessage(), ex);

						// url错误图片已经出现过，错误次数增加
						if (fileNotFoundUrlMap.containsKey(imageUrl)) {
							int errTimes = fileNotFoundUrlMap.get(imageUrl);
							// 错误次数到达重试次数
							if ((errTimes + 1) >= retryTimes) {
								// 记录url错误图片以便删除这张图片相关记录
								urlErrorList.add(anImageUrlList);

								// 记录次数结构体清空该图片
								fileNotFoundUrlMap.remove(imageUrl);

								// 次数++
							} else {
								fileNotFoundUrlMap.put(imageUrl, ++errTimes);
							}

							// 图片url错误第一次出现错误
						} else {
							fileNotFoundUrlMap.put(imageUrl, 1);
						}

						if (inputStream != null) {
							inputStream.close();
						}

						continue;
					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);

						if (inputStream != null) {
							inputStream.close();
						}

						continue;
					}

					// 没有出现url错误的图片清空之前可能没有成功的同一图片记录
					if (fileNotFoundUrlMap.containsKey(imageUrl)) {
						fileNotFoundUrlMap.remove(imageUrl);
					}

					int lastSlash = imageUrl.lastIndexOf("/");
					String fileName;
					if (ChannelConfigEnums.Channel.GILT.getId().equalsIgnoreCase(anImageUrlList.get("channel_id"))) {
						fileName = String.valueOf(anImageUrlList.get("image_name")) + ".jpg";
					} else {
						fileName = imageUrl.substring(lastSlash + 1);
					}
					if (fileName.contains("?")) {
						int qIndex = fileName.indexOf("?");
						fileName = fileName.substring(0, qIndex);
					}

					FtpFileBean ftpFileBean = new FtpFileBean(inputStream, uploadPath, fileName);
					ftpComponent.uploadFile(ftpFileBean);

					successImageUrlList.add(imageUrl);
					logger.info("thread-" + threadNo + ":" + imageUrl + "上传成功!");
				}

			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);

				isSuccess = false;

			} finally {
				//断开连接
				ftpComponent.closeConnect();
			}
		}

		return isSuccess;
	}

	/**
	 * 更新已上传图片发送标志
	 */
	public int updateImageSendFlag(String orderChannelId, List<String> successImageUrlList, String taskName) {
		return imageDao.updateImageSendFlag(orderChannelId, successImageUrlList, taskName);
	}

	/**
	 * 删除错误url图片
	 */
	public void deleteUrlErrorImage(String orderChannelId, List<Map<String, String>> urlErrorList) {
		for (Map<String, String> urlError : urlErrorList) {
			String productId = String.valueOf(urlError.get("product_id"));
			String imageTypeId = String.valueOf(urlError.get("image_type_id"));
			String imageId = String.valueOf(urlError.get("image_id"));
			String imageUrl = String.valueOf(urlError.get("image_url"));
			logger.info("IMAGE URL IS NOT EXIST! channelId:" + orderChannelId + " productId:" + productId + " imageTypeId:" + imageTypeId + " imageId:" + imageId + " imageUrl:" + imageUrl);

			// 单事务处理
			transactionRunner.runWithTran(() ->
				imageDao.deleteUrlErrorImage(orderChannelId, urlError)
			);
		}
	}



	public static void main(String[] args) throws IOException {
	}
}
