package com.voyageone.web2.admin.views.channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.TmOrderChannelBean;
import com.voyageone.service.bean.com.TmOrderChannelConfigBean;
import com.voyageone.service.impl.com.channel.ChannelService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmOrderChannelConfigKey;
import com.voyageone.service.model.com.TmOrderChannelConfigModel;
import com.voyageone.service.model.com.TmOrderChannelModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.ChannelFormBean;
import com.voyageone.web2.admin.bean.system.CommonConfigFormBean;
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
	
	//---------------------------------------------------------------------
	// 渠道信息
	//---------------------------------------------------------------------
	
	@SuppressWarnings("serial")
	@RequestMapping(AdminUrlConstants.Channel.Self.GET_ALL_CHANNEL)
	public AjaxResponse getAllChannel() {
		List<TmOrderChannelModel> channels = channelService.getAllChannel();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		channels.stream().forEach(item -> result.add(new HashMap<String, Object>(){{
			put("name", item.getName());
			put("orderChannelId", item.getOrderChannelId());
		}}));
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.SEARCH_CHANNEL_BY_PAGE)
	public AjaxResponse searchChannelByPage(@RequestBody ChannelFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索渠道信息
		PageModel<TmOrderChannelBean> channelPage = channelService.searchChannelByPage(form.getOrderChannelId(),
				form.getChannelName(), form.getIsUsjoi(), form.getPageNum(), form.getPageSize());
		
		return success(channelPage);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.ADD_CHANNEL)
	public AjaxResponse addChannel(@RequestBody ChannelFormBean form) {
		return addOrUpdateChannel(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.UPDATE_CHANNEL)
	public AjaxResponse updateChannel(@RequestBody ChannelFormBean form) {
		return addOrUpdateChannel(form, false);
	}
	
	public AjaxResponse addOrUpdateChannel(ChannelFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkNotNull(form.getCompanyId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getScrectKey()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getSessionKey()));

		// 保存渠道信息
		TmOrderChannelModel model = new TmOrderChannelModel();
		BeanUtils.copyProperties(form, model);
		channelService.addOrUpdateChannel(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.DELETE_CHANNEL)
	public AjaxResponse deleteChannel(@RequestBody String[] channelIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(channelIds));
		// 删除渠道信息
		channelService.deleteChannel(Arrays.asList(channelIds), getUser().getUserName());
		
		return success(true);
	}

	//---------------------------------------------------------------------
	// 渠道配置信息
	//---------------------------------------------------------------------
	
	@RequestMapping(AdminUrlConstants.Channel.Self.SEARCH_CHANNEL_CONFIG_BY_PAGE)
	public AjaxResponse searchChannelConfigByPage(@RequestBody CommonConfigFormBean form) {
		// 验证分页参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索渠道配置信息
		PageModel<TmOrderChannelConfigBean> channelConfigPage = channelService.searchChannelConfigByPage(
				form.getOrderChannelId(), form.getCfgName(), form.getCfgVal(), form.getPageNum(), form.getPageSize());
		
		return success(channelConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.ADD_CHANNEL_CONFIG)
	public AjaxResponse addChannelConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateChannelConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.UPDATE_CHANNEL_CONFIG)
	public AjaxResponse updateChannelConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateChannelConfig(form, false);
	}
	
	public AjaxResponse addOrUpdateChannelConfig(CommonConfigFormBean form, boolean append) {
		// 验证配置类型参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
		
		// 保存渠道配置信息
		TmOrderChannelConfigModel channelConfigModel = new TmOrderChannelConfigModel();
		BeanUtils.copyProperties(form, channelConfigModel);
		channelService.addOrUpdateChannelConfig(channelConfigModel, append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.DELETE_CHANNEL_CONFIG)
	public AjaxResponse deleteChannelConfig(@RequestBody CommonConfigFormBean[] forms) {
		// 验证配置类型参数
		for (CommonConfigFormBean form : forms) {
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
		}

		List<TmOrderChannelConfigKey> channelConfigKeys = new ArrayList<TmOrderChannelConfigKey>();
		for (CommonConfigFormBean form : forms) {
			TmOrderChannelConfigKey configKey = new TmOrderChannelConfigKey();
			BeanUtils.copyProperties(form, configKey);
			configKey.setOrderChannelId(form.getOrderChannelId());
			channelConfigKeys.add(configKey);
		}
		// 删除渠道配置信息
		channelService.deleteChannelConfig(channelConfigKeys);
		
		return success(true);
	}
	
	//---------------------------------------------------------------------
	// 唯一的随机数
	//---------------------------------------------------------------------
	
	@RequestMapping(AdminUrlConstants.Channel.Self.GENERATE_SECRET_KEY)
	public AjaxResponse generateSecretKey() {
		return success(UUID.randomUUID().toString().replace("-", "").toUpperCase());
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.GENERATE_SESSION_KEY)
	public AjaxResponse generateSessionKey() {
		return success(UUID.randomUUID().toString());
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Self.GET_ALL_COMPANY)
	public AjaxResponse getAllCompany() {
		return success(channelService.getAllCompany());
	}

}
