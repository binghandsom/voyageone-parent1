package com.voyageone.web2.admin.views.openshop;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.common.configs.Properties;
import com.voyageone.service.bean.com.OpenShopBean;
import com.voyageone.service.impl.AdminProperty;
import com.voyageone.service.impl.com.openshop.OpenShopService;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.openshop.OpenShopFormBean;
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

	@RequestMapping(AdminUrlConstants.OpenShop.Self.HANDLE_CHANNEL_SERIES_SQL)
	public AjaxResponse handleChannelSeriesSql(@RequestBody OpenShopFormBean form) throws IOException {
		// 检查参数
		Preconditions.checkNotNull(form);
		Preconditions.checkNotNull(form.getChannel());
		// 处理开新店的SQL
		OpenShopBean bean = new OpenShopBean();
		BeanUtils.copyProperties(form, bean);
		openShopService.handleChannelSeriesSql(bean, getUser().getUserName());
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.OpenShop.Self.DOWNLOAD_OPEN_SHOP_SQL)
	public ResponseEntity<byte[]> downloadOpenShopSql(@RequestParam String fileName) {
		String sqlFile = FilenameUtils.concat(Properties.readValue(AdminProperty.Props.ADMIN_SQL_PATH), fileName);
		String downloadFileName = "open-shop-" + System.currentTimeMillis() + ".sql";
		return genResponseEntityFromFile(downloadFileName, sqlFile);
	}

}
