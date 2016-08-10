package com.voyageone.web2.admin.views.channel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.web2.admin.bean.channel.ChannelFormBean;
import com.voyageone.web2.admin.views.AdminController;
import com.voyageone.web2.admin.views.AdminUrlConstants;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Channel.Self.ROOT, method = RequestMethod.POST)
public class ChannelController extends AdminController {
	
	@RequestMapping(AdminUrlConstants.Channel.Self.SEARCH_CHANNEL)
	public AjaxResponse searchChannel(@RequestBody ChannelFormBean form) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 检索渠道信息
		String channelList = "";
		result.put("channelList", channelList);
		
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.ADD_OR_UPDATE_CHANNEL)
	public AjaxResponse addOrUpdateChannel(@RequestBody ChannelFormBean form) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		// 验证参数
		Preconditions.checkNotNull(form.getCompanyId());
		Preconditions.checkNotNull(form.getChannelId());
		Preconditions.checkState(StringUtils.isNotBlank(form.getSecretKey()));
		Preconditions.checkState(StringUtils.isNotBlank(form.getSessionKey()));
		// 保存渠道信息

		result.put("message", "");
		
		return success(result);
	}

}
