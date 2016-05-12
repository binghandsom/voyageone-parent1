package com.voyageone.task2.cms.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.service.FtpService;
import com.voyageone.task2.cms.dao.ImageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.util.HttpUtils;

@Service
public class ImagePostScene7Service {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ImageDao imageDao;

	@Autowired
	IssueLog issueLog;

	@Autowired
	private TransactionRunner transactionRunner;

	@Autowired
	private FtpService ftpService;

	// Scene7FTP设置
	private static final String S7FTP_CONFIG = "S7FTP_CONFIG";

	private static Map<String, Integer> fileNotFoundUrlMap = new HashMap<String, Integer>();

	private static int retryTimes = 5;

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
	public boolean getAndSendImage(String orderChannelId, List<Map<String, String>> imageUrlList, List<String> successImageUrlList,
								   List<Map<String, String>> urlErrorList, int threadNo) throws Exception {
		boolean isSuccess = true;

		if (imageUrlList != null && imageUrlList.size() > 0) {

			InputStream inputStream = null;

			String imageUrl;

			try {

						for (Map<String, String> anImageUrlList : imageUrlList) {
							imageUrl = String.valueOf(anImageUrlList.get("image_url"));

							if (StringUtils.isNullOrBlank2(imageUrl)) {
								successImageUrlList.add(imageUrl);

								continue;
							}

							try {
//								if (imageUrl.startsWith("https")) {
//									inputStream = HttpUtils.getHttpsInputStream(imageUrl, null);
//								} else {
								inputStream = HttpUtils.getInputStream(imageUrl);
//								}

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

							boolean result = ftpService.storeFile(
									Codes.getCodeName(S7FTP_CONFIG, "Url"),
									Codes.getCodeName(S7FTP_CONFIG, "Port"),
									Codes.getCodeName(S7FTP_CONFIG, "UserName"),
									Codes.getCodeName(S7FTP_CONFIG, "Password"),
									fileName,
									ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.scene7_image_folder),
									inputStream,
									Codes.getCodeName(S7FTP_CONFIG, "FileCoding"),
									120000);

							if (result) {
								successImageUrlList.add(imageUrl);

								logger.info("thread-" + threadNo + ":" + imageUrl + "上传成功!");

							} else {
								isSuccess = false;

								continue;
							}

							if (inputStream != null) {
								inputStream.close();
							}
						}
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				issueLog.log(ex, ErrorType.BatchJob, SubSystem.CMS);

				isSuccess = false;

			} finally {

				if (inputStream != null) {
					inputStream.close();
				}
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
			transactionRunner.runWithTran(() -> {
				imageDao.deleteUrlErrorImage(orderChannelId, urlError);
			});
		}
	}



	public static void main(String[] args) throws IOException {
	}
}
