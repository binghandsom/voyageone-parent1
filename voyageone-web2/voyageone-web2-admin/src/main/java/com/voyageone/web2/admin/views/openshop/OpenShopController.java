package com.voyageone.web2.admin.views.openshop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.voyageone.service.impl.com.openshop.OpenShopService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/26
 */
@RestController
@RequestMapping(value = AdminUrlConstants.OpenShop.Self.ROOT, method = RequestMethod.POST)
public class OpenShopController extends AdminController {
	
	@Autowired
	private OpenShopService openShopService;
	
	@RequestMapping(AdminUrlConstants.OpenShop.Self.GET_CHANNEL_SERIES)
	public AjaxResponse getChannelSeries(@RequestBody String channelId) {
		return success(openShopService.getChannelSeries(channelId));
	}
	
	@RequestMapping(AdminUrlConstants.OpenShop.Self.GET_NEW_CHANNEL_SERIES)
	public AjaxResponse getNewChannelSeries(@RequestBody Map<String, Object> params) {
		return success(true);
	}

}
