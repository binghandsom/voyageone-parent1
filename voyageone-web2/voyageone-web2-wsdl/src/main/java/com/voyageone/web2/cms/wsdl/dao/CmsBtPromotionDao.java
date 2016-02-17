/**
 * (c) Copyright Voyageone Corp 2016
 */

package com.voyageone.web2.cms.wsdl.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;

/**
 * @description promotion 数据持久层
 * @author gbb
 */
@Repository
public class CmsBtPromotionDao extends BaseDao {

	/**
	 * 条件查询
	 * 
	 * @param condtionParams
	 * @return
	 */
	public List<CmsBtPromotionModel> findByCondition(Map<?, ?> condtionParams) {
		return selectList("select_By_Condtion", condtionParams);
	}

	/**
	 * 根据id查询
	 * 
	 * @param condtionParams
	 * @return
	 */
	public CmsBtPromotionModel findById(Map<?, ?> condtionParams) {
		return selectOne("select_By_Id", condtionParams);
	}

	/**
	 * 修改
	 * 
	 * @param cmsBtPromotionModel
	 * @return
	 */
	public int update(CmsBtPromotionModel cmsBtPromotionModel) {
		return updateTemplate.update("update", cmsBtPromotionModel);
	}

	/**
	 * 插入
	 * 
	 * @param cmsBtPromotionModel
	 * @return
	 */
	public int insert(CmsBtPromotionModel cmsBtPromotionModel) {
		return updateTemplate.insert("insert", cmsBtPromotionModel);
	}

	/**
	 * 删除(逻辑删除，修改is_active=0)
	 * 
	 * @param condtionParams
	 * @return
	 */
	public int deleteById(Map<?, ?> condtionParams) {
		return updateTemplate.update("delete_By_Id", condtionParams);
	}

}
