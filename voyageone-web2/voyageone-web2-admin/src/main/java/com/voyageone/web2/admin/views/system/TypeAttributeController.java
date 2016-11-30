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
import com.voyageone.service.bean.com.ComMtValueBean;
import com.voyageone.service.impl.com.system.TypeAttributeService;
import com.voyageone.service.model.com.ComMtValueModel;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.channel.ChannelAttributeFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/19
 */
@RestController
@RequestMapping(value = AdminUrlConstants.System.Attribute.ROOT, method = RequestMethod.POST)
public class TypeAttributeController extends AdminController {
	
	@Autowired
	private TypeAttributeService typeAttrService;
	
	@RequestMapping(AdminUrlConstants.System.Attribute.SEARCH_TYPE_ATTRIBUTE_BY_PAGE)
	public AjaxResponse searchTypeAttributeByPage(@RequestBody ChannelAttributeFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索类型属性信息
		PaginationResultBean<ComMtValueBean> typeAttrPage = typeAttrService.searchTypeAttributeByPage(form.getTypeId(),
				form.getLangId(), form.getName(), form.getValue(), form.getActive(),
				form.getPageNum(), form.getPageSize());
		
		return success(typeAttrPage);
	}
	
	@RequestMapping(AdminUrlConstants.System.Attribute.ADD_TYPE_ATTRIBUTE)
	public AjaxResponse addTypeAttribute(@RequestBody ChannelAttributeFormBean form) {
		return addOrUpdateTypeAttribute(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Attribute.UPDATE_TYPE_ATTRIBUTE)
	public AjaxResponse updateTypeAttribute(@RequestBody ChannelAttributeFormBean form) {
		Preconditions.checkNotNull(form.getId());
		return addOrUpdateTypeAttribute(form, false);
	}
	
	public AjaxResponse addOrUpdateTypeAttribute(ChannelAttributeFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkNotNull(form.getTypeId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getValue()));
		// 保存类型属性信息
		ComMtValueModel model = new ComMtValueModel();
		BeanUtils.copyProperties(form, model);
		typeAttrService.addOrUpdateTypeAttribute(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Attribute.DELETE_TYPE_ATTRIBUTE)
	public AjaxResponse deleteTypeAttribute(@RequestBody Integer[] typeAttrIds) {
		// 验证参数
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(typeAttrIds));
		// 删除类型属性信息
		typeAttrService.deleteTypeAttribute(Arrays.asList(typeAttrIds), getUser().getUserName());
		
		return success(true);
	}

}
