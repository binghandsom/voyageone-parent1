package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.JmBtDealImportModel;
import com.voyageone.batch.cms.model.JmBtSkuImportModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DealImportDao extends BaseDao {

	/**
	 * Deal数据追加
	 *
	 * @param jmBtDealImportModel
	 * @return
	 */
	public boolean insertDealImportInfo(JmBtDealImportModel jmBtDealImportModel) {
		boolean isSuccess = true;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "jm_bt_deal_import_insert", jmBtDealImportModel);
//		if (count == 1) {
//			isSuccess = true;
//		}

		return isSuccess;
	}

	/**
	 * 更新JmBtDealImportModel
	 * @param jmBtDealImportModel
	 * @return
	 */
	public int updateDealImportInfo(JmBtDealImportModel jmBtDealImportModel){
		int ret = updateTemplate.update("jm_bt_deal_import_update", jmBtDealImportModel);
		return ret;
	}

	/**
	 * 更新JmBtDealImportModel（专场货架更新）
	 * @param jmBtDealImportModel
	 * @return
	 */
	public int updateDealImportInfoForSpecialActivity(JmBtDealImportModel jmBtDealImportModel){
		int ret = updateTemplate.update("jm_bt_deal_import_updateForSpecialActivity", jmBtDealImportModel);
		return ret;
	}

	/**
	 * 根据修改的Deal，更新ProductImport
	 * @param jmBtDealImportModel
	 * @return
	 */
	public int updateProductImportInfoByDealImport(JmBtDealImportModel jmBtDealImportModel){
		return updateTemplate.update("jm_bt_product_import_updateByDealImport", jmBtDealImportModel);
	}
}