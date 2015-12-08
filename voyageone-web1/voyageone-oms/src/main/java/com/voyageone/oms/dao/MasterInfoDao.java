/**
 * 
 */
package com.voyageone.oms.dao;

import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.core.modelbean.MasterInfoBean;

/**
 * @author jacky
 *
 */
@Repository
public class MasterInfoDao extends BaseDao {

	/**
	 * 根据分类Id获得master信息
	 * 
	 * @param id
	 * @return
	 */
	public List<MasterInfoBean> getMasterInfoFromId(int id) {
		List<MasterInfoBean> masterInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_mt_value_getMasterInfoFromId", id);
		
		return masterInfoList;
	}
	
	/**
	 * 获得订单检索条件设定条件master信息
	 * 
	 * @return
	 */
	public List<MasterInfoBean> getMasterInfoForOrderSearch() {
		List<MasterInfoBean> masterInfoList = 
				(List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_mt_value_getMasterInfoForOrderSearch");
		
		return masterInfoList;
	}
}
