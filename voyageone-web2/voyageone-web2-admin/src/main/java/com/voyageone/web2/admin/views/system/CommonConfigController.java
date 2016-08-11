package com.voyageone.web2.admin.views.system;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
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
	
	@RequestMapping(AdminUrlConstants.System.CommonConfig.SEARCH_CONFIG)
	public AjaxResponse searchConfig(CommonConfigFormBean form) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 验证参数
		Preconditions.checkNotNull(form.getConfigType());
		switch (form.getConfigType()) {
		default:
			break;
			
		}
		
		return success(result);
	}

}
