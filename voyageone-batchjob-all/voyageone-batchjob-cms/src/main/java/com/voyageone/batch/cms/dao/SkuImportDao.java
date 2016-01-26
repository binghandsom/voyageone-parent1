package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.JmBtProductImportModel;
import com.voyageone.batch.cms.model.JmBtSkuImportModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SkuImportDao extends BaseDao {

	/**
	 * Sku数据追加
	 *
	 * @param jmBtSkuImportModel
	 * @return
	 */
	public boolean insertSkuImportInfo(JmBtSkuImportModel jmBtSkuImportModel) {
		boolean isSuccess = false;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "jm_bt_sku_import_insert", jmBtSkuImportModel);
		if (count == 1) {
			isSuccess = true;
		}

		return isSuccess;
	}

}