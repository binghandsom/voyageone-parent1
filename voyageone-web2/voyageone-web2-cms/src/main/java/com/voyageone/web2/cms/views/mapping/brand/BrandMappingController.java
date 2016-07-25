package com.voyageone.web2.cms.views.mapping.brand;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.BrandMappingBean;
import com.voyageone.web2.core.bean.UserSessionBean;

/**
 * Created by Wangtd on 7/25/16.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.MAPPING.BRAND.ROOT, method = RequestMethod.POST)
public class BrandMappingController extends CmsController {
	
	@Autowired
	private BrandMappingService brandMappingService;
	
	/**
	 * 初始化页面数据
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.INIT)
	public AjaxResponse init() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cartList", TypeChannels.getTypeListSkuCarts(getUser().getSelChannelId(),
				Constants.comMtTypeChannel.SKU_CARTS_53_A, getLang()));
		return success(result);
	}
	
	/**
	 * 搜索品牌匹配数据
	 */
	@RequestMapping(CmsUrlConstants.MAPPING.BRAND.SEARCH_BRANDS)
	public AjaxResponse searchBrands(@RequestBody BrandMappingBean brandMapping) {
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		// 添加查询参数
		Map<String, Object> result = new HashMap<String, Object>();
		UserSessionBean userSession = getUser();
		brandMapping.setChannelId(userSession.getSelChannel().getId());
		brandMapping.setLangId(getLang());
		// 设置返回结果
		result.put("brandList", brandMappingService.searchBrands(brandMapping));
		
		return success(result);
	}
	
	public AjaxResponse searchCustBrands(@RequestBody BrandMappingBean brandMapping) {
		// 检查参数
		Preconditions.checkNotNull(brandMapping);
		Preconditions.checkNotNull(brandMapping.getCartId());
		// 添加查询参数
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("custBrandList", brandMappingService.searchCustBrands(brandMapping));
		return success(result);
	}

}
