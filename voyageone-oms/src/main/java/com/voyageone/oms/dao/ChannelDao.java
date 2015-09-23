package com.voyageone.oms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.core.modelbean.MasterInfoBean;

import org.springframework.stereotype.Repository;

import com.voyageone.common.Constants;

import java.util.List;

@Repository("OmsChannelDao")
public class ChannelDao extends BaseDao {

	/**
	 * 根据property获得channel信息
	 * 
	 * @param id
	 * @return
	 */
	public int getSourceId(int id) {
		int source_id = 
				(int) selectOne(Constants.DAO_NAME_SPACE_OMS + "ct_property_channel_getsourceId", id);
		
		return source_id;
	}

	/**
	 * 根据property获得channel信息
	 *
	 * @param propertyId
	 * @return
	 */
	public List<MasterInfoBean> getShoppingCarts(String propertyId) {
		List<MasterInfoBean> shoppingCartList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "tm_channel_shop_getshoppingCarts", propertyId);

		return shoppingCartList;
	}
}
