package com.voyageone.web2.admin.views.cart;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;
import com.voyageone.service.impl.com.cart.CartTrackingService;
import com.voyageone.service.model.com.ComMtTrackingInfoConfigModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.cart.CartTrackingFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/22
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Cart.Tracking.ROOT, method = RequestMethod.POST)
public class CartTrackingController extends AdminController {
	
	@Autowired
	private CartTrackingService cartTrackingService;
	
	@RequestMapping(AdminUrlConstants.Cart.Tracking.SEARCH_CART_TRACKING_BY_PAGE)
	public AjaxResponse searchCartTrackingByPage(@RequestBody CartTrackingFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索Cart物流信息
		PageModel<ComMtTrackingInfoConfigBean> cartTrackingPage = cartTrackingService.searchCartTrackingByPage(
				form.getOrderChannelId(), form.getCartId(), form.getTrackingStatus(), form.getLocation(),
				form.getPageNum(), form.getPageSize());
		
		return success(cartTrackingPage);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Tracking.ADD_CART_TRACKING)
	public AjaxResponse addCartTracking(@RequestBody CartTrackingFormBean form) {
		return addOrUpdateCartTracking(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Tracking.UPDATE_CART_TRACKING)
	public AjaxResponse updateCartTracking(@RequestBody CartTrackingFormBean form) {
		Preconditions.checkNotNull(form.getSeq());
		return addOrUpdateCartTracking(form, false);
	}
	
	private AjaxResponse addOrUpdateCartTracking(CartTrackingFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkNotNull(form.getCartId());
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getOrderChannelId()));
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getTrackingInfo()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getLocation()));
		Preconditions.checkNotNull(form.getActive());

		// 保存Cart物流信息
		ComMtTrackingInfoConfigModel model = new ComMtTrackingInfoConfigModel();
		BeanUtils.copyProperties(form, model);
		cartTrackingService.addOrUpdateCartTracking(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Tracking.DELETE_CART_TRACKING)
	public AjaxResponse deleteCartTracking(@RequestBody Integer[] cartTrackingIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(cartTrackingIds));
		// 删除Cart物流信息
		cartTrackingService.deleteCartTracking(Arrays.asList(cartTrackingIds), getUser().getUserName());

		return success(true);
	}

}
