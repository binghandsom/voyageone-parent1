/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.service.dao.cms;

import com.voyageone.service.dao.ServiceBaseDao;
import com.voyageone.service.model.cms.CmsBtPromotionModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * promotion 数据持久层
 * @author gbb
 */
@Repository
public class CmsBtPromotionDao extends ServiceBaseDao {

	/**
	 * 条件查询
	 *
	 * @param condtionParams condtionParams
	 * @return List<CmsBtPromotionModel>
	 */
	public List<CmsBtPromotionModel> findByCondition(Map<?, ?> condtionParams) {
		return selectList("select_cms_bt_promotion_by_condtion", condtionParams);
	}

	/**
	 * 根据id查询
	 *
	 * @param condtionParams condtion Params
	 * @return CmsBtPromotionModel
	 */
	public CmsBtPromotionModel findById(Map<?, ?> condtionParams) {
		return selectOne("select_cms_bt_promotion_by_id", condtionParams);
	}

	/**
	 * 修改
	 *
	 * @param cmsBtPromotionModel CmsBtPromotionModel
	 * @return int
	 */
	public int update(CmsBtPromotionModel cmsBtPromotionModel) {
		return update("update_cms_bt_promotion", cmsBtPromotionModel);
	}

	/**
	 * 插入
	 *
	 * @param cmsBtPromotionModel CmsBtPromotionModel
	 * @return int
	 */
	public int insert(CmsBtPromotionModel cmsBtPromotionModel) {
		return insert("insert_cms_bt_promotion", cmsBtPromotionModel);
	}

	/**
	 * 删除(逻辑删除，修改is_active=0)
	 *
	 * @param condtionParams condtion Params
	 * @return int
	 */
	public int deleteById(Map<?, ?> condtionParams) {
		return update("delete_cms_bt_promotion_by_id", condtionParams);
	}

}
