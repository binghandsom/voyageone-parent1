package com.voyageone.oms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.oms.modelbean.CartBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class CainiaoDao extends BaseDao {
	
	/**
	 * 获得物流信息
	 * 
	 * @return
	 */
	public String getLogisticsId(String sourceOrderId) {
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("sourceOrderId", sourceOrderId);

		String ret = (String) selectOne(Constants.DAO_NAME_SPACE_OMS + "tt_cainiao_order_getLogisticsId", paraIn);
		
		return ret;
	}


}
