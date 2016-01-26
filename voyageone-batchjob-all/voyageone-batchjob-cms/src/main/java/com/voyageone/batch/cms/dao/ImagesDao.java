package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.JmBtDealImportModel;
import com.voyageone.batch.cms.model.JmBtImagesModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ImagesDao extends BaseDao {

	/**
	 * Images数据追加
	 *
	 * @param jmBtImagesModel
	 * @return
	 */
	public boolean insertImagesInfo(JmBtImagesModel jmBtImagesModel) {
		boolean isSuccess = false;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "jm_bt_images_insert", jmBtImagesModel);
		if (count == 1) {
			isSuccess = true;
		}

		return isSuccess;
	}

}