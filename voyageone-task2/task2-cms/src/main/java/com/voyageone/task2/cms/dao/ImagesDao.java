package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.task2.cms.model.JmBtImagesModel;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ImagesDao extends BaseDao {

	/**
	 * Images数据追加
	 *
	 * @param jmBtImagesModel
	 * @return
	 */
	public boolean insertImagesInfo(JmBtImagesModel jmBtImagesModel) {
		boolean isSuccess = true;

		int count = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "jm_bt_images_insert", jmBtImagesModel);
//		if (count == 1) {
//			isSuccess = true;
//		}

		return isSuccess;
	}

	/**
	 * 上传图片状态取得
	 *
	 * @param jmBtImagesModel
	 * @return
	 */
	public List<JmBtImagesModel>  getImagesBySynFlg(JmBtImagesModel jmBtImagesModel) {
		return selectList(Constants.DAO_NAME_SPACE_CMS + "jm_bt_images_selectBySynFlg", jmBtImagesModel);
	}

	/**
	 * 根据修改的Image，更新ProductImport
	 * @param dealId
	 * @return
	 */
	public int updateProductImportInfoByImage(String orderChannelID, String dealId, String modifier){
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("channelId", orderChannelID);
		paraIn.put("dealId", dealId);
		paraIn.put("modifier", modifier);

		return updateTemplate.update("jm_bt_product_import_updateByImage", paraIn);
	}

}