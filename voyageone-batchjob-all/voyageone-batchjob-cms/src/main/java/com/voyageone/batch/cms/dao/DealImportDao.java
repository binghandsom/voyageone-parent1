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
		boolean isSuccess = false;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "jm_bt_deal_import_insert", jmBtDealImportModel);
		if (count == 1) {
			isSuccess = true;
		}

		return isSuccess;
	}

}