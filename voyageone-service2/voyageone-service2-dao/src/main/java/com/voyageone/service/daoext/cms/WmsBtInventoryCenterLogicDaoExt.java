package com.voyageone.service.daoext.cms;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.voyageone.service.dao.ServiceBaseDao;

/**
 * @author Wangtd
 * @since 2.0.0 2016/10/28
 */
@Repository
public class WmsBtInventoryCenterLogicDaoExt extends ServiceBaseDao {
	
	public List<Map<String, Object>> selectSkuInventoryList(String channelId, String code) {
		return selectList("select_sku_inventory", parameters("channelId", channelId, "code", code));
	}

}
