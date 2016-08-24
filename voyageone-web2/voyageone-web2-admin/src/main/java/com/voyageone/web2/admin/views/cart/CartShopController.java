package com.voyageone.web2.admin.views.cart;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.TmChannelShopBean;
import com.voyageone.service.bean.com.TmChannelShopConfigBean;
import com.voyageone.service.impl.com.cart.CartShopService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmChannelShopConfigKey;
import com.voyageone.service.model.com.TmChannelShopConfigModel;
import com.voyageone.service.model.com.TmChannelShopKey;
import com.voyageone.service.model.com.TmChannelShopModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.cart.CartShopFormBean;
import com.voyageone.web2.admin.bean.system.CommonConfigFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Cart.Shop.ROOT, method = RequestMethod.POST)
public class CartShopController extends AdminController {
	
	@Autowired
	private CartShopService cartShopService;
	
	//---------------------------------------------------------------------
	// Cart商店信息
	//---------------------------------------------------------------------
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.SEARCH_CART_SHOP_BY_PAGE)
	public AjaxResponse searchCartShopByPage(@RequestBody CartShopFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索Cart商店信息
		PageModel<TmChannelShopBean> cartShopPage = cartShopService.searchCartShopByPage(form.getOrderChannelId(),
				form.getCartId(), form.getShopName(), form.getActive(), form.getPageNum(), form.getPageSize());
		
		return success(cartShopPage);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.ADD_CART_SHOP)
	public AjaxResponse addCartShop(@RequestBody CartShopFormBean form) {
		return addOrUpdateCartShop(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.UPDATE_CART_SHOP)
	public AjaxResponse updateCartShop(@RequestBody CartShopFormBean form) {
		return addOrUpdateCartShop(form, false);
	}
	
	public AjaxResponse addOrUpdateCartShop(CartShopFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkNotNull(form.getCartId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));

		// 保存Cart商店信息
		TmChannelShopModel model = new TmChannelShopModel();
		BeanUtils.copyProperties(form, model);
		cartShopService.addOrUpdateChannel(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.DELETE_CART_SHOP)
	public AjaxResponse deleteCartShop(@RequestBody CartShopFormBean[] forms) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(forms), "没有可删除的Cart商店信息");
		List<TmChannelShopKey> shopKeys = new ArrayList<TmChannelShopKey>();
		for (CartShopFormBean form : forms) {
			Preconditions.checkNotNull(form.getCartId());
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
			TmChannelShopKey shopKey = new TmChannelShopKey();
			BeanUtils.copyProperties(form, shopKey);
			shopKeys.add(shopKey);
		}
		// 删除Cart商店信息
		cartShopService.deleteCartShop(shopKeys, getUser().getUserName());
		
		return success(true);
	}
	
	//---------------------------------------------------------------------
	// Cart商店配置信息
	//---------------------------------------------------------------------
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.SEARCH_CART_SHOP_CONFIG_BY_PAGE)
	public AjaxResponse searchCartShopConfigByPage(@RequestBody CommonConfigFormBean form) {
		// 验证分页参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索Cart商店配置信息
		PageModel<TmChannelShopConfigBean> channelConfigPage = cartShopService.searchCartShopConfigByPage(
				form.getOrderChannelId(), form.getCartId(), form.getCfgName(), form.getCfgVal(), form.getPageNum(),
				form.getPageSize());
		
		return success(channelConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.ADD_CART_SHOP_CONFIG)
	public AjaxResponse addCartShopConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateCartShopConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.UPDATE_CART_SHOP_CONFIG)
	public AjaxResponse updateCartShopConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateCartShopConfig(form, false);
	}
	
	public AjaxResponse addOrUpdateCartShopConfig(CommonConfigFormBean form, boolean append) {
		// 验证Cart商店配置参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCartId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
		
		// 保存Cart商店配置信息
		TmChannelShopConfigModel model = new TmChannelShopConfigModel();
		BeanUtils.copyProperties(form, model);
		cartShopService.addOrUpdateCartShopConfig(model, append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Shop.DELETE_CART_SHOP_CONFIG)
	public AjaxResponse deleteCartShopConfig(@RequestBody CommonConfigFormBean[] forms) {
		// 验证Cart商店配置参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(forms), "没有可删除的Cart商店配置信息");
		for (CommonConfigFormBean form : forms) {
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
		}

		List<TmChannelShopConfigKey> shopConfigKeys = new ArrayList<TmChannelShopConfigKey>();
		for (CommonConfigFormBean form : forms) {
			TmChannelShopConfigKey configKey = new TmChannelShopConfigKey();
			BeanUtils.copyProperties(form, configKey);
			configKey.setOrderChannelId(form.getOrderChannelId());
			shopConfigKeys.add(configKey);
		}
		// 删除Cart商店配置信息
		cartShopService.deleteCartShopConfig(shopConfigKeys);
		
		return success(true);
	}

}
