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
import com.voyageone.service.impl.com.system.TypeService;
import com.voyageone.service.model.com.ComMtTypeModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.CarrierConfigFormBean;
import com.voyageone.web2.admin.bean.system.TypeFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
@RestController
@RequestMapping(value = AdminUrlConstants.System.Type.ROOT, method = RequestMethod.POST)
public class TypeController extends AdminController {
	
	@Autowired
	private TypeService typeService;
	
	@RequestMapping(AdminUrlConstants.System.Type.GET_ALL_TYPE)
	public AjaxResponse getAllType() {
		return success(typeService.getAllType());
	}
	
	@RequestMapping(AdminUrlConstants.System.Type.SEARCH_TYPE_BY_PAGE)
	public AjaxResponse searchTypeByPage(@RequestBody TypeFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索类型信息
		PageModel<ComMtTypeModel> carrierConfigPage = typeService.searchTypeByPage(form.getId(), form.getName(),
				form.getComment(), form.getPageNum(), form.getPageSize());
		
		return success(carrierConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.System.Type.ADD_TYPE)
	public AjaxResponse addType(@RequestBody TypeFormBean form) {
		return addOrUpdateType(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Type.UPDATE_TYPE)
	public AjaxResponse updateType(@RequestBody TypeFormBean form) {
		Preconditions.checkNotNull(form.getId());
		return addOrUpdateType(form, false);
	}
	
	public AjaxResponse addOrUpdateType(TypeFormBean form, boolean append) {
		// 保存类型信息
		ComMtTypeModel model = new ComMtTypeModel();
		BeanUtils.copyProperties(form, model);
		typeService.addOrUpdateType(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Type.DELETE_TYPE)
	public AjaxResponse deleteType(@RequestBody Integer[] typeIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(typeIds));
		// 删除类型信息
		typeService.deleteType(Arrays.asList(typeIds), getUser().getUserName());
		
		return success(true);
	}

}
