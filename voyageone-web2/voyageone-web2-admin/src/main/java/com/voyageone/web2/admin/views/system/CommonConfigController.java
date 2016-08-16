package com.voyageone.web2.admin.views.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.admin.channel.ChannelService;
import com.voyageone.service.impl.admin.store.StoreService;
import com.voyageone.service.model.admin.CtStoreConfigKey;
import com.voyageone.service.model.admin.CtStoreConfigModel;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelConfigKey;
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
	
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(AdminUrlConstants.System.CommonConfig.SEARCH_CONFIG_BY_PAGE)
	public AjaxResponse searchConfigByPage(@RequestBody CommonConfigFormBean form) {
		// 验证配置类型参数
		Preconditions.checkNotNull(form.getConfigType());
		// 验证分页参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());

		PageModel<?> result = null;
		switch (form.getConfigType()) {
		case Channel:
			result = channelService.searchChannelConfigByPage(form.getOrderChannelId(), form.getCfgName(),
					form.getCfgVal(), form.getPageNum(), form.getPageSize());
			break;
		case ChannelCart:
			break;
		case Port:
			break;
		case Store:
			result = storeService.searchStoreConfigByPage(form.getStoreId(), form.getCfgName(), form.getCfgVal(),
					form.getPageNum(), form.getPageSize());
			break;
		case Task:
			break;
		default:
			break;
		}
		
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.System.CommonConfig.ADD_CONFIG)
	public AjaxResponse addConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.System.CommonConfig.UPDATE_CONFIG)
	public AjaxResponse updateConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateConfig(form, false);
	}
	
	public AjaxResponse addOrUpdateConfig(@RequestBody CommonConfigFormBean form, boolean append) {
		// 验证配置类型参数
		Preconditions.checkNotNull(form.getConfigType());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		try {
			String username = getUser().getUserName();
			switch (form.getConfigType()) {
			// 按类型保存配置信息
			case Channel:
				Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
				TmOrderChannelConfigModel channelConfigModel = new TmOrderChannelConfigModel();
				BeanUtils.copyProperties(form, channelConfigModel);
				channelService.addOrUpdateChannelConfig(channelConfigModel, username, append);
				break;
			case ChannelCart:
				break;
			case Port:
				break;
			case Store:
				CtStoreConfigModel storeConfigModel = new CtStoreConfigModel();
				BeanUtils.copyProperties(form, storeConfigModel);
				storeService.addOrUpdateStoreConfig(storeConfigModel, username, append);
				break;
			case Task:
				break;
			default:
				break;
			}
			result.put(SUCCESS, true);
		} catch (BusinessException e) {
			result.put(MESSAGE, e.getMessage());
		}
		
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.System.CommonConfig.DELETE_CONFIG)
	public AjaxResponse deleteConfig(@RequestBody CommonConfigFormBean[] forms) {
		// 验证配置类型参数
		for (CommonConfigFormBean form : forms) {
			Preconditions.checkNotNull(form.getConfigType());
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		try {
			switch (forms[0].getConfigType()) {
			// 按类型删除配置信息
			case Channel:
				List<TmOrderChannelConfigKey> channelConfigKeys = new ArrayList<TmOrderChannelConfigKey>();
				for (CommonConfigFormBean form : forms) {
					Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
					TmOrderChannelConfigKey configKey = new TmOrderChannelConfigKey();
					BeanUtils.copyProperties(form, configKey);
					configKey.setOrderChannelId(form.getOrderChannelId());
					channelConfigKeys.add(configKey);
				}
				// 删除渠道配置信息
				channelService.deleteChannelConfig(channelConfigKeys);
				break;
			case ChannelCart:
				break;
			case Port:
				break;
			case Store:
				List<CtStoreConfigKey> storeConfigKeys = new ArrayList<CtStoreConfigKey>();
				for (CommonConfigFormBean form : forms) {
					Preconditions.checkNotNull(form.getStoreId());
					CtStoreConfigKey configKey = new CtStoreConfigKey();
					BeanUtils.copyProperties(form, configKey);
					storeConfigKeys.add(configKey);
				}
				// 删除仓库配置信息
				storeService.deleteStoreConfig(storeConfigKeys);
				break;
			case Task:
				break;
			default:
				break;
			}
			result.put(SUCCESS, true);
		} catch (BusinessException e) {
			result.put(MESSAGE, e.getMessage());
		}
		
		return success(result);
	}

}
