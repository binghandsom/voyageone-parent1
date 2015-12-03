package com.voyageone.core.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.MasterInfoBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jacky
 *
 */
@Repository("CoreMasterInfoDao")
public class MasterInfoDao extends BaseDao {

	/**
	 * 根据分类Id获得master信息
	 */
	public List<MasterInfoBean> getMasterInfoFromId(int id) {

		return selectList(Constants.DAO_NAME_SPACE_OMS + "oms_mt_value_getMasterInfoFromId", id);
	}
	
	/**
	 * 获得订单检索条件设定条件master信息
	 */
	public List<MasterInfoBean> getMasterInfoForOrderSearch() {

		return selectList(Constants.DAO_NAME_SPACE_OMS + "oms_mt_value_getMasterInfoForOrderSearch");
	}
}
