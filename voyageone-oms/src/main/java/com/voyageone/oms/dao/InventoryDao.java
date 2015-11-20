package com.voyageone.oms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.oms.formbean.OutFormSearchRate;
import com.voyageone.oms.modelbean.RateBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class InventoryDao extends BaseDao {

	/**
	 * 逻辑库存取得
	 *
	 * @return
	 */
	public int getLogicQuantity(String orderChannelId, String sku) {
		int ret = 0;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("orderChannelId", orderChannelId);
		paraIn.put("sku", sku);

		Integer quantity = (Integer) selectOne(Constants.DAO_NAME_SPACE_OMS + "wms_bt_inventory_center_logic_getQuantity", paraIn);
		if (quantity != null) {
			ret = quantity;
		}

		return ret;
	}

}
