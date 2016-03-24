package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.model.JmBtSkuImportModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

@Repository
public class SkuImportDao extends BaseDao {

	/**
	 * Sku数据追加
	 *
	 * @param jmBtSkuImportModel
	 * @return
	 */
	public boolean insertSkuImportInfo(JmBtSkuImportModel jmBtSkuImportModel) {
		boolean isSuccess = true;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "jm_bt_sku_import_insert", jmBtSkuImportModel);
//		if (count == 1) {
//			isSuccess = true;
//		}

		return isSuccess;
	}

	/**
	 * 根据修改的SKU，更新ProductImport
	 * @param jmBtSkuImportModel
	 * @return
	 */
	public int updateProductImportInfoBySkuImport(JmBtSkuImportModel jmBtSkuImportModel){
		return updateTemplate.update("jm_bt_product_import_updateBySkuImport", jmBtSkuImportModel);
	}

}