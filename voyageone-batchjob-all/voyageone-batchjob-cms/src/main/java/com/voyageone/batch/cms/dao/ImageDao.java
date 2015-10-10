package com.voyageone.batch.cms.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;

@Repository
public class ImageDao extends BaseDao {


	/**
	 * 取出要处理的图片url列表
	 *
	 * @param orderChannelId
	 * @return
	 */
	public List<String> getImageUrls(String orderChannelId) {
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

}