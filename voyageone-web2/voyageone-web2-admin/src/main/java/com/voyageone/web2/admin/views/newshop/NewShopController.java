package com.voyageone.web2.admin.views.newshop;

import java.io.File;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.NewShopBean;
import com.voyageone.service.impl.com.newshop.NewShopService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.newshop.NewShopFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/26
 */
@RestController
@RequestMapping(value = AdminUrlConstants.NewShop.Self.ROOT, method = RequestMethod.POST)
public class NewShopController extends AdminController {
	
	@Autowired
	private NewShopService newShopService;
	
	@RequestMapping(AdminUrlConstants.NewShop.Self.GET_CHANNEL_SERIES)
	public AjaxResponse getChannelSeries(@RequestBody String channelId) {
		return success(newShopService.getChannelSeries(channelId));
	}

	@RequestMapping(AdminUrlConstants.NewShop.Self.SAVE_CHANNEL_SERIES)
	public AjaxResponse saveChannelSeries(@RequestBody NewShopFormBean form) throws Exception {
		// 检查参数
		Preconditions.checkNotNull(form);
		Preconditions.checkNotNull(form.getChannel());
		// 处理开新店的SQL
		NewShopBean bean = new NewShopBean();
		BeanUtils.copyProperties(form, bean);
		newShopService.saveChannelSeries(form.getId(), bean, getUser().getUserName());
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.NewShop.Self.DOWNLOAD_NEW_SHOP_SQL)
	public ResponseEntity<byte[]> downloadNewShopSql(@RequestParam Long newShopId) throws Exception {
		File sqlFile = newShopService.downloadNewShopSql(newShopId, getUser().getUserName());
		String downloadFileName = "new-shop-" + System.currentTimeMillis() + ".sql";
		return genResponseEntityFromFile(downloadFileName, sqlFile.getPath());
	}
	
	@RequestMapping(AdminUrlConstants.NewShop.Self.SEARCH_NEW_SHOP_BY_PAGE)
	public AjaxResponse searchNewShopByPage(@RequestBody NewShopFormBean form) {
		return success(true);
	}	
	
	@RequestMapping(AdminUrlConstants.NewShop.Self.DELETE_NEW_SHOP)
	public AjaxResponse deleteNewShop(@RequestBody Long newShopId) {
		Preconditions.checkNotNull(newShopId);
		newShopService.deleteNewShop(newShopId);
		return success(true);
	}

}
