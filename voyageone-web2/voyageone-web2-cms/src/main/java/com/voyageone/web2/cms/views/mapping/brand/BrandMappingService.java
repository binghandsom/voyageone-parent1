package com.voyageone.web2.cms.views.mapping.brand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.service.bean.cms.BrandBtMappingBean;
import com.voyageone.service.impl.cms.BrandBtMappingService;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.bean.BrandMappingBean;

/**
 * Created by Wangtd on 7/25/16.
 */
@Service
public class BrandMappingService extends BaseAppService {
	
	@Autowired
	private BrandBtMappingService brandBtMappingService;

	/**
	 * 搜索品牌匹配数据
	 */
	public List<BrandBtMappingBean> searchBrands(BrandMappingBean brandMapping) {
		Map<String, Object> otherParams = new HashMap<String, Object>();
		otherParams.put("mappingState", brandMapping.getMappingState());
		otherParams.put("brandName", brandMapping.getBrandName());
		// 查询品牌匹配关系
		return brandBtMappingService.searchBrands(brandMapping.getChannelId(), brandMapping.getCartId(),
				brandMapping.getLangId(), otherParams);
	}

	public Object searchCustBrands(BrandMappingBean brandMapping) {
		// TODO Auto-generated method stub
		return null;
	}

}
