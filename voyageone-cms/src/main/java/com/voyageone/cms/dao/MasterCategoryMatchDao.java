package com.voyageone.cms.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.cms.modelbean.CmsCategoryModel;
import com.voyageone.common.Constants;
@Repository
public class MasterCategoryMatchDao extends BaseDao {
	
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(MasterCategoryMatchDao.class);

	/**
	 * 获取cms类目.
	 * @param channelId
	 * @return
	 */
	public List<CmsCategoryModel> getCmsCategoryList(String channelId) {
		
		logger.info("查询cms类目开始...");
		
		List<CmsCategoryModel> models = selectList(Constants.DAO_NAME_SPACE_CMS + "get_cms_categories", channelId);
		
		logger.info("查询cms类目结束...");
		
		return models;
		
	}

	/**
	 * 获取类目属性已完成类目id.
	 * @param channelId
	 * @return
	 */
	public List<String> getPropMatchCompleteCategories(String channelId){

		logger.info("查询类目属性匹配已完成类目id开始...");

		List<String> categoryIds = selectList(Constants.DAO_NAME_SPACE_CMS + "get_cms_category_propMatchComplete", channelId);

		logger.info("查询类目属性匹配已完成类目id结束...");

		return categoryIds;
	}

}
