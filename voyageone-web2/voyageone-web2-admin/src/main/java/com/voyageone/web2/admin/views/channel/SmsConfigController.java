package com.voyageone.web2.admin.views.channel;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.TmSmsConfigBean;
import com.voyageone.service.impl.com.channel.SmsConfigService;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.service.model.com.TmSmsConfigModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.SmsConfigFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Channel.Sms.ROOT, method = RequestMethod.POST)
public class SmsConfigController extends AdminController {
	
	@Autowired
	private SmsConfigService smsConfigService;
	
	@RequestMapping(AdminUrlConstants.Channel.Sms.SEARCH_SMS_CONFIG_BY_PAGE)
	public AjaxResponse searchSmsConfigByPage(@RequestBody SmsConfigFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索短信配置信息
		PaginationResultBean<TmSmsConfigBean> smsConfigPage = smsConfigService.searchSmsConfigByPage(form.getOrderChannelId(),
				form.getSmsType(), form.getContent(), form.getSmsCode(), form.getActive(), 
				form.getPageNum(), form.getPageSize());
		
		return success(smsConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Sms.ADD_SMS_CONFIG)
	public AjaxResponse addSmsConfig(@RequestBody SmsConfigFormBean form) {
		return addOrUpdateSmsConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Sms.UPDATE_SMS_CONFIG)
	public AjaxResponse updateSmsConfig(@RequestBody SmsConfigFormBean form) {
		Preconditions.checkNotNull(form.getSeq());
		return addOrUpdateSmsConfig(form, false);
	}
	
	public AjaxResponse addOrUpdateSmsConfig(SmsConfigFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getSmsType()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getSmsCode1()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getContent()));
		
		// 设置渠道信息
		TmSmsConfigModel model = new TmSmsConfigModel();
		BeanUtils.copyProperties(form, model);
		// 保存渠道信息
		smsConfigService.addOrUpdateSmsConfig(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Channel.Sms.DELETE_SMS_CONFIG)
	public AjaxResponse deleteSmsConfig(@RequestBody Integer[] seqIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(seqIds));
		// 删除短信配置信息
		smsConfigService.deleteSmsConfig(Arrays.asList(seqIds), getUser().getUserName());
		
		return success(true);
	}

}
