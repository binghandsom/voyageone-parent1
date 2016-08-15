package com.voyageone.web2.admin.views.channel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.admin.TmSmsConfigBean;
import com.voyageone.service.impl.admin.channel.SmsConfigService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmSmsConfigModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.SmsConfigFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
public class SmsConfigController extends AdminController {
	
	@Autowired
	private SmsConfigService smsConfigService;
	
	@RequestMapping(AdminUrlConstants.Channel.Sms.SEARCH_SMS_CONFIG_BY_PAGE)
	public AjaxResponse searchSmsConfigByPage(@RequestBody SmsConfigFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索短信配置信息
		PageModel<TmSmsConfigBean> smsConfigPage = smsConfigService.searchSmsConfigByPage(form.getChannelId(),
				form.getSmsType(), form.getContent(), form.getSmsCode(), form.getPageNum(), form.getPageSize());
		
		return success(smsConfigPage);
	}
	
	
	
	public AjaxResponse addOrUpdateSmsConfig(@RequestBody SmsConfigFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getChannelId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getSmsType()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getSmsCode1()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getContent()));
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		try {
			// 设置渠道信息
			TmSmsConfigModel model = new TmSmsConfigModel();
			BeanUtils.copyProperties(form, model);
			model.setOrderChannelId(form.getChannelId());
			// 保存渠道信息
			smsConfigService.addOrUpdateSmsConfig(model, getUser().getUserName(), append);
			result.put(SUCCESS, true);
		} catch (BusinessException e) {
			result.put(MESSAGE, e.getMessage());
		}
		
		return success(result);
	}
	

}
