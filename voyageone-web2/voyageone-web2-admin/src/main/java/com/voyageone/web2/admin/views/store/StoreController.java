package com.voyageone.web2.admin.views.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.admin.WmsMtStoreBean;
import com.voyageone.service.impl.admin.store.StoreService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.WmsMtStoreModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.store.StoreFormBean;
import com.voyageone.web2.base.ajax.AjaxResponse;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/9
 */
@RestController
@RequestMapping(value = AdminUrlConstants.Store.Self.ROOT, method = RequestMethod.POST)
public class StoreController extends AdminController {
	
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(AdminUrlConstants.Store.Self.SEARCH_STORE_BY_PAGE)
	public AjaxResponse searchStoreByPage(@RequestBody StoreFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索仓库信息
		PageModel<WmsMtStoreBean> storePage = storeService.searchStoreByPage(form.getChannelId(), form.getStoreName(),
				form.getIsSale(), form.getStoreType(), form.getPageNum(), form.getPageSize());
		
		return success(storePage);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.ADD_OR_UPDATE_STORE)
	public AjaxResponse addOrUpdateStore(@RequestBody StoreFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getAppend());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getChannelId()));
		Preconditions.checkNotNull(form.getParentStoreId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getStoreType()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getStoreLocation()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getStoreKind()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getLabelType()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getIsSale()));
		Preconditions.checkNotNull(form.getAreaId());

		// 保存仓库信息
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(SUCCESS, false);
		try {
			WmsMtStoreModel model = new WmsMtStoreModel();
			BeanUtils.copyProperties(form, model);
			storeService.addOrUpdateStore(model, form.getAppend());
			result.put(SUCCESS, true);
		} catch (BusinessException e) {
			result.put(MESSAGE, e.getMessage());
		}
		
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.GET_ALL_STORE)
	public AjaxResponse getAllStore() {
		return success(storeService.getAllStore());
	}

}
