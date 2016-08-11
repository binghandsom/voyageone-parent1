package com.voyageone.web2.admin.views.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.admin.channel.ChannelService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.ChannelFormBean;
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
	public AjaxResponse searchChannelByPage(@RequestBody ChannelFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());

		// 检索渠道信息
		PageModel<TmOrderChannelModel> channelPage = channelService.searchChannelByPage(form.getChannelId(),
				form.getChannelName(), form.getIsUsjoi(), form.getPageNum(), form.getPageSize());
		
		return success(channelPage);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.ADD_OR_UPDATE_CHANNEL)
	public AjaxResponse addOrUpdateChannel(@RequestBody ChannelFormBean form) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		// 验证参数
		Preconditions.checkNotNull(form.getCompanyId());
		Preconditions.checkNotNull(form.getChannelId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getSecretKey()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getSessionKey()));
		
		// 设置渠道信息
		TmOrderChannelModel model = new TmOrderChannelModel();
		BeanUtils.copyProperties(model, form);
		model.setOrderChannelId(form.getChannelId());
		String username = getUser().getUserName();
		if (StringUtils.isBlank(form.getChannelId())) {
			model.setCreater(username);
			model.setModifier(username);
		} else {
			model.setModifier(username);
		}

		// 保存渠道信息
		try {
			result.put("success", channelService.addOrUpdateChannel(model));
			result.put("success", true);
		} catch (BusinessException e) {
			result.put("message", e.getMessage());
		}
		
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.GET_ALL_COMPANY)
	public AjaxResponse getAllCompany() {
		return success(channelService.getAllCompany());
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.GET_ALL_CHANNEL)
	public AjaxResponse getAllChannel() {
		return success(channelService.getAllChannel());
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.GENERATE_SECRET_KEY)
	public AjaxResponse generateSecretKey() {
		return success(UUID.randomUUID().toString().replace("-", "").toUpperCase());
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.GENERATE_SESSION_KEY)
	public AjaxResponse generateSessionKey() {
		return success(UUID.randomUUID().toString());
	}

}
