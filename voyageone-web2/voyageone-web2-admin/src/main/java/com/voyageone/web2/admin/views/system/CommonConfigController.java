package com.voyageone.web2.admin.views.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.impl.admin.channel.ChannelService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelConfigModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.system.CommonConfigFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.System.CommonConfig.ROOT, method=RequestMethod.POST)
public class CommonConfigController extends AdminController {
	
	@Resource(name = "AdminChannelService")
	private ChannelService channelService;
	
	@RequestMapping(AdminUrlConstants.System.CommonConfig.SEARCH_CONFIG)
	public AjaxResponse searchConfig(@RequestBody CommonConfigFormBean form) {
		// 验证配置类型参数
		Preconditions.checkNotNull(form.getConfigType());
		// 验证分页参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());

		PageModel<?> result = null;
		switch (form.getConfigType()) {
		case Channel:
			result = channelService.searchChannelConfigByPage(form.getChannelId(), form.getCfgName(), form.getCfgVal(),
					form.getPageNum(), form.getPageSize());
			break;
		case ChannelCart:
			break;
		case Port:
			break;
		case Store:
			break;
		case Task:
			break;
		default:
			break;
		}
		
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.System.CommonConfig.ADD_OR_UPDATE_CONFIG)
	public AjaxResponse addOrUpdateConfig(@RequestBody CommonConfigFormBean form) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 验证配置类型参数
		Preconditions.checkNotNull(form.getConfigType());
		boolean success = false;
		switch (form.getConfigType()) {
		case Channel:
			TmOrderChannelConfigModel model = new TmOrderChannelConfigModel();
			success = channelService.addOrUpdateChannelConfig(model);
			break;
		case ChannelCart:
			break;
		case Port:
			break;
		case Store:
			break;
		case Task:
			break;
		default:
			break;
		}
		
		result.put("success", success);
		
		return success(result);
	}

}
