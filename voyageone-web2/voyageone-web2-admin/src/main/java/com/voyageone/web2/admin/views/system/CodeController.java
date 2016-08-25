package com.voyageone.web2.admin.views.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.impl.com.system.CodeService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmCodeKey;
import com.voyageone.service.model.com.TmCodeModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.system.CodeFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.System.Code.ROOT, method = RequestMethod.POST)
public class CodeController extends AdminController {
	
	@Autowired
	private CodeService codeService;
	
	@RequestMapping(AdminUrlConstants.System.Code.SEARCH_CODE_BY_PAGE)
	public AjaxResponse searchCodeByPage(@RequestBody CodeFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索Code信息
		PageModel<TmCodeModel> codePage = codeService.searchCodeByPage(form.getCode(), form.getName(), form.getDes(),
				form.getActive(), form.getPageNum(), form.getPageSize());
		
		return success(codePage);
	}
	
	@RequestMapping(AdminUrlConstants.System.Code.ADD_CODE)
	public AjaxResponse addCode(@RequestBody CodeFormBean form) {
		return addOrUpdateCode(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Code.UPDATE_CODE)
	public AjaxResponse updateCode(@RequestBody CodeFormBean form) {
		return addOrUpdateCode(form, false);
	}
	
	private AjaxResponse addOrUpdateCode(CodeFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getId()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCode()));

		// 保存Code信息
		TmCodeModel model = new TmCodeModel();
		BeanUtils.copyProperties(form, model);
		codeService.addOrUpdateCode(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.System.Code.DELETE_CODE)
	public AjaxResponse deleteCode(@RequestBody CodeFormBean[] forms) {
		// 验证参数
		List<TmCodeKey> codeKeys = new ArrayList<TmCodeKey>();
		for (CodeFormBean form : forms) {
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getId()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCode()));
			TmCodeKey codeKey = new TmCodeKey();
			BeanUtils.copyProperties(form, codeKey);
			codeKeys.add(codeKey);
		}
		// 删除Code信息
		codeService.deleteCode(codeKeys, getUser().getUserName());

		return success(true);
	}

}
