package com.voyageone.batch.cms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.transaction.TransactionStatus;

@Repository
public class ImageDao extends BaseDao {


	/**
	 * 取出要处理的图片url列表
	 *
	 * @param orderChannelId
	 * @return
	 */
	public List<Map<String, String>> getImageUrls(String orderChannelId) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_work_product_image_url_getImageUrls", orderChannelId);
	}
	
	/**
	 * 更新已上传图片发送标志
	 *
	 * @param orderChannelId
	 * @param imageUrlList
	 * @param taskName
	 * @return
	 */
	public int updateImageSendFlag(String orderChannelId, List<String> imageUrlList, String taskName) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orderChannelId", orderChannelId);
		paramMap.put("imageUrlList", imageUrlList);
		paramMap.put("taskName", taskName);
		return updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_work_product_image_url_updateImageSendFlag", paramMap);
	}

	/**
	 * 删除url错误的图片
	 *
	 * @param orderChannelId
	 * @param urlError
	 * @return
	 */
	public void deleteUrlErrorImage(String orderChannelId, Map<String, String> urlError) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orderChannelId", orderChannelId);
		paramMap.put("productId", String.valueOf(urlError.get("product_id")));
		paramMap.put("imageTypeId", String.valueOf(urlError.get("image_type_id")));
		paramMap.put("imageId", String.valueOf(urlError.get("image_id")));
		paramMap.put("imageUrl", String.valueOf(urlError.get("image_url")));

		updateTemplate.delete(Constants.DAO_NAME_SPACE_CMS + "cms_work_product_image_url_deleteUrlErrorImage", paramMap);
		updateTemplate.delete(Constants.DAO_NAME_SPACE_CMS + "cms_bt_product_image_deleteUrlErrorImage", paramMap);
		updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_bt_product_deleteUrlErrorImage", paramMap);
	}

}