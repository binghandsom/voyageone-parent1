package com.voyageone.web2.admin.views.system;

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
import com.voyageone.service.impl.com.system.PortConfigService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmPortConfigModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.system.CommonConfigFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.System.Port.ROOT, method = RequestMethod.POST)
public class PortConfigController extends AdminController {
	
	@Autowired
	private PortConfigService portConfigService;
	
	@RequestMapping(AdminUrlConstants.System.Port.SEARCH_PORT_CONFIG_BY_PAGE)
	public AjaxResponse searchPortConfigByPage(@RequestBody CommonConfigFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索港口信息
		PageModel<TmPortConfigModel> portConfigPage = portConfigService.searchPortConfigByPage(form.getPort(),
				form.getCfgName(), form.getCfgVal(), form.getPageNum(), form.getPageSize());
		
		return success(portConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.System.Port.ADD_PORT_CONFIG)
	public AjaxResponse addPortConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdatePortConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Port.UPDATE_PORT_CONFIG)
	public AjaxResponse updatePortConfig(@RequestBody CommonConfigFormBean form) {
		Preconditions.checkNotNull(form.getSeq());
		return addOrUpdatePortConfig(form, false);
	}
	
	private AjaxResponse addOrUpdatePortConfig(CommonConfigFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getPort()));
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getCfgName()));
		Preconditions.checkArgument(StringUtils.isNoneBlank(form.getCfgVal1()));

		// 保存港口信息
		TmPortConfigModel model = new TmPortConfigModel();
		BeanUtils.copyProperties(form, model);
		portConfigService.addOrUpdatePortConfig(model, append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Port.DELETE_PORT_CONFIG)
	public AjaxResponse deletePortConfig(@RequestBody Integer[] portIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(portIds));
		// 删除港口信息
		portConfigService.deletePortConfig(Arrays.asList(portIds));

		return success(true);
	}

}
