package com.voyageone.oms.dao;

import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.modelbean.CartBean;

@Repository
public class CartDao extends BaseDao {
	
	/**
	 * 获得cart信息
	 * 
	 * @return
	 */
	public List<CartBean> getCartsListByType(String cartType) {
		List<CartBean> cartsList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "ct_cart_getCartsInfoByType", cartType);
		
		return cartsList;
	}

	/**
	 * 获得cart信息
	 *
	 * @return
	 */
	public CartBean getCartInfo(String cartId) {
		CartBean cartInfo = (CartBean) selectOne(Constants.DAO_NAME_SPACE_OMS + "ct_cart_getCartsInfo", cartId);

		return cartInfo;
	}
}
