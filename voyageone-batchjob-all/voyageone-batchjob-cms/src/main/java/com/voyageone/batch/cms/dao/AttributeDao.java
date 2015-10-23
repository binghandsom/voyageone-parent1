package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AttributeDao extends BaseDao {

	/**
	 * 获得要翻译的属性数据
	 *
	 * @return
	 */
	public List<Map<String, String>> getAttributeValue() {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_mt_product_attribute_getAttributeValue");
	}
	
	/**
	 * 更新已上传图片发送标志
	 *
	 * @param paramMap
	 * @return
	 */
	public boolean updateAttributeValueCn(Map<String, String> paramMap) {
		boolean isSuccess = false;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "cms_mt_product_attribute_updateAttributeValueCn", paramMap);
		if (count == 1) {
			isSuccess = true;
		}

		return isSuccess;
	}

}