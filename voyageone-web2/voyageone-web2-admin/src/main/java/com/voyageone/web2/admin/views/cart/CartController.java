package com.voyageone.web2.admin.views.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.impl.admin.cart.CartService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Cart.Self.ROOT, method = RequestMethod.POST)
public class CartController extends AdminController {
	
	@Resource(name = "AdminCartService")
	private CartService cartService;
	
	@RequestMapping(AdminUrlConstants.Cart.Self.GET_ALL_CART)
	public AjaxResponse getAllCart() {
		return success(cartService.getAllCart());
	}
	
	@RequestMapping(AdminUrlConstants.Cart.Self.GET_CART_BY_IDS)
	public AjaxResponse getCartByIds(@RequestBody Map<String, String> params) {
		String cartIds = params.get("cartIds");
		Preconditions.checkArgument(StringUtils.isNotBlank(cartIds));
		
		// 取得CartId
		String[] cartIdArr = StringUtils.split(cartIds, ",");
		List<Integer> cartIdList = new ArrayList<Integer>();
		for (int i = 0; i < cartIdArr.length; i++) {
			cartIdList.add(Integer.valueOf(cartIdArr[i]));
		}
		
		return success(cartService.getCartByIds(cartIdList));
	}

}
