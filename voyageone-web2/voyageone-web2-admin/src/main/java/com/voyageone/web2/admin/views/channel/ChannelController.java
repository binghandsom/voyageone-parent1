package com.voyageone.web2.admin.views.channel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.impl.admin.channel.ChannelService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelModel;
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
	
	@Resource(name = "AdminChannelService")
	private ChannelService channelService;
	
	@RequestMapping(AdminUrlConstants.Channel.Self.SEARCH_CHANNEL)
	public AjaxResponse searchChannel(@RequestBody ChannelFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());

		// 检索渠道信息
		PageModel<TmOrderChannelModel> channelPage = channelService.searchChannel(form.getChannelId(), form.getName(),
				form.getFullName(), form.getIsUsjoi(), form.getPageNum(), form.getPageSize());
		
		return success(channelPage);
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
