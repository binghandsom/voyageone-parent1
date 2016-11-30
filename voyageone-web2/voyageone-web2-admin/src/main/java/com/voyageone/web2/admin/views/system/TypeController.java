package com.voyageone.web2.admin.views.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.impl.com.system.TypeService;
import com.voyageone.service.model.com.ComMtTypeModel;
import com.voyageone.service.bean.com.PaginationResultBean;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
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
	
	@SuppressWarnings("serial")
	@RequestMapping(AdminUrlConstants.System.Type.GET_ALL_TYPE)
	public AjaxResponse getAllType() {
		List<ComMtTypeModel> types = typeService.getAllType();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		types.stream().forEach(item -> result.add(new HashMap<String, Object>() {{
			put("id", item.getId());
			put("name", item.getName());
			put("comment", item.getComment());
		}}));
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.System.Type.SEARCH_TYPE_BY_PAGE)
	public AjaxResponse searchTypeByPage(@RequestBody TypeFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索类型信息
		PaginationResultBean<ComMtTypeModel> typePage = typeService.searchTypeByPage(form.getId(), form.getName(),
				form.getComment(), form.getActive(), form.getPageNum(), form.getPageSize());
		
		return success(typePage);
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
		ComMtTypeModel TypeModel = typeService.addOrUpdateType(model, getUser().getUserName(), append);
		
		return success(TypeModel);
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
