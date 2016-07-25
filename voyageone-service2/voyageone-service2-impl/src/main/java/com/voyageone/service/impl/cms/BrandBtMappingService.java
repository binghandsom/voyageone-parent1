package com.voyageone.service.impl.cms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.service.bean.cms.BrandBtMappingBean;
import com.voyageone.service.daoext.cms.CmsBtBrandMappingDaoExt;
import com.voyageone.service.impl.BaseService;

/**
 * Created by Wangtd on 7/25/16.
 */
@Service
public class BrandBtMappingService extends BaseService {
	
	@Autowired
	private CmsBtBrandMappingDaoExt cmsBtBrandMappingDaoExt;
	
	public List<BrandBtMappingBean> searchBrands(String channelId, int cartId, String langId,
			Map<String, Object> others) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 必要参数设置
		params.put("channelId", channelId);
		params.put("cardId", cartId);
		params.put("langId", langId);
		params.put("typeId", 41); // TODO 品牌类型ID为41，常量在哪里定义？
		// 区分聚美品牌与其他品牌
		if (CartEnums.Cart.JM.getId().equals(String.valueOf(cartId))) {
			params.put("isJMPlatform", true);
		} else {
			params.put("isJMPlatform", false);
		}
		// 添加其他非必要参数
		if (MapUtils.isNotEmpty(others)) {
			params.putAll(others);
		}
		
		return cmsBtBrandMappingDaoExt.searchBrands(params);
	}

}
