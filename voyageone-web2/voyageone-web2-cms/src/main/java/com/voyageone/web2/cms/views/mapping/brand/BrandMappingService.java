package com.voyageone.web2.cms.views.mapping.brand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.service.bean.cms.CmsBtBrandBean;
import com.voyageone.service.bean.cms.CmsBtBrandMappingBean;
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
	 * 检索品牌匹配数据
	 */
	public List<CmsBtBrandMappingBean> searchBrandsByPage(BrandMappingBean brandMapping) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mappingState", brandMapping.getMappingState());
		params.put("brandName", brandMapping.getBrandName());
		params.put("offset", brandMapping.getOffset());
		params.put("size", brandMapping.getSize());
		// 查询品牌匹配关系
		return brandBtMappingService.searchBrandsByPage(brandMapping.getChannelId(), brandMapping.getCartId(),
				brandMapping.getLangId(), params);
	}

	/**
	 * 检索品牌数量
	 */
	public Long searchBrandsCount(BrandMappingBean brandMapping) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mappingState", brandMapping.getMappingState());
		params.put("brandName", brandMapping.getBrandName());
		// 查询品牌匹配关系
		return brandBtMappingService.searchBrandsCount(brandMapping.getChannelId(), brandMapping.getCartId(),
				brandMapping.getLangId(), params);
	}

	/**
	 * 检索商户品牌数据
	 */
	public List<CmsBtBrandBean> searchCustBrands(BrandMappingBean brandMapping) {
		List<CmsBtBrandBean> brandList = null;
		if (CartEnums.Cart.JM.getId().equals(String.valueOf(brandMapping.getCartId()))) {
			// 聚美的品牌数据
			brandList = brandBtMappingService.searchJmBrands(brandMapping.getLangId(), brandMapping.getBrandName());
		} else {
			// 非聚美的品牌数据
			brandList = brandBtMappingService.searchMasterBrands(brandMapping.getChannelId(), brandMapping.getCartId(),
					brandMapping.getBrandName());
		}
		
		return brandList;
	}
	
	/**
	 * 检索品牌数量 
	 */
	public List<CmsBtBrandMappingBean> searchMatchedBrands(BrandMappingBean brandMapping) {
		return brandBtMappingService.searchMatchedBrands(brandMapping.getChannelId(), brandMapping.getCartId(),
				brandMapping.getLangId(), brandMapping.getBrandId());
	}

}
