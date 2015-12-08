package com.voyageone.oms.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.OutFormOrderdetailTracking;

@Repository
public class TrackingDao extends BaseDao {
	
	/**
	 * 获得订单Tracking信息
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailTracking> getOrderTrackingInfo(String orderNumber) {
//		synShip直接取 删除
//		 List<OutFormOrderdetailTracking> orderTrackingList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_tracking_getOrderTrackingInfo", orderNumber);
//		
//		return orderTrackingList;
		
		return null;
	}
}
