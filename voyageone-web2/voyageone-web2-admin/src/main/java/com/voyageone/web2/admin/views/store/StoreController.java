package com.voyageone.web2.admin.views.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.voyageone.service.bean.com.CtStoreConfigBean;
import com.voyageone.service.bean.com.WmsMtStoreBean;
import com.voyageone.service.impl.com.store.StoreService;
import com.voyageone.service.model.com.CtStoreConfigKey;
import com.voyageone.service.model.com.CtStoreConfigModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.WmsMtStoreKey;
import com.voyageone.service.model.com.WmsMtStoreModel;
import com.voyageone.web2.admin.AdminController;
import com.voyageone.web2.admin.AdminUrlConstants;
import com.voyageone.web2.admin.bean.store.StoreFormBean;
import com.voyageone.web2.admin.bean.system.CommonConfigFormBean;
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

	//---------------------------------------------------------------------
	// 仓库信息
	//---------------------------------------------------------------------
	
	@SuppressWarnings("serial")
	@RequestMapping(AdminUrlConstants.Store.Self.GET_ALL_STORE)
	public AjaxResponse getAllStore(@RequestBody(required = false) String channelId) {
		List<WmsMtStoreBean> stores = storeService.getAllStore(channelId);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		stores.stream().forEach(item -> result.add(new HashMap<String, Object>() {{
			put("storeId", item.getStoreId());
			put("storeName", item.getStoreName());
			put("channelId", item.getOrderChannelId());
			put("channelName", item.getChannelName());
		}}));
		return success(result);
	}
	
	@SuppressWarnings("serial")
	@RequestMapping(AdminUrlConstants.Store.Self.GET_STORE_BY_CHANNEL_IDS)
	public AjaxResponse getStoreByChannelIds(@RequestBody List<String> channelIds) {
		List<WmsMtStoreBean> stores = storeService.getStoreByChannelIds(channelIds);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		stores.stream().forEach(item -> result.add(new HashMap<String, Object>() {{
			put("storeId", item.getStoreId());
			put("storeName", item.getStoreName());
			put("channelId", item.getOrderChannelId());
			put("channelName", item.getChannelName());
		}}));
		return success(result);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.SEARCH_STORE_BY_PAGE)
	public AjaxResponse searchStoreByPage(@RequestBody StoreFormBean form) {
		// 验证参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索仓库信息
		PageModel<WmsMtStoreBean> storePage = storeService.searchStoreByPage(form.getOrderChannelId(), form.getStoreName(),
				form.getIsSale(), form.getStoreType(), form.getActive(), form.getPageNum(), form.getPageSize());
		
		return success(storePage);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.ADD_STORE)
	public AjaxResponse addStore(@RequestBody StoreFormBean form) {
		return addOrUpdateStore(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.UPDATE_STORE)
	public AjaxResponse updateStore(@RequestBody StoreFormBean form) {
		Preconditions.checkNotNull(form.getStoreId());
		return addOrUpdateStore(form, false);
	}
	
	public AjaxResponse addOrUpdateStore(StoreFormBean form, boolean append) {
		// 验证参数
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
		//Preconditions.checkNotNull(form.getParentStoreId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getStoreType()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getStoreLocation()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getStoreKind()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getLabelType()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getIsSale()));
		Preconditions.checkNotNull(form.getAreaId());

		// 保存仓库信息
		WmsMtStoreModel model = new WmsMtStoreModel();
		BeanUtils.copyProperties(form, model);
		storeService.addOrUpdateStore(model, getUser().getUserName(), append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.DELETE_STORE)
	public AjaxResponse deleteStore(@RequestBody StoreFormBean[] forms) {
		Preconditions.checkArgument(ArrayUtils.isNotEmpty(forms), "没有可删除的仓库信息");
		List<WmsMtStoreKey> storeKeys = new ArrayList<WmsMtStoreKey>();
		for (StoreFormBean form : forms) {
			// 验证参数
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getOrderChannelId()));
			Preconditions.checkNotNull(form.getStoreId());
			WmsMtStoreKey storeKey = new WmsMtStoreKey();
			BeanUtils.copyProperties(form, storeKey);
			storeKeys.add(storeKey);
		}
		// 删除仓库信息
		storeService.deleteStore(storeKeys, getUser().getUserName());

		return success(true);
	}

	//---------------------------------------------------------------------
	// 仓库配置信息
	//---------------------------------------------------------------------
	
	@RequestMapping(AdminUrlConstants.Store.Self.SEARCH_STORE_CONFIG_BY_PAGE)
	public AjaxResponse searchStoreConfigByPage(@RequestBody CommonConfigFormBean form) {
		// 验证分页参数
		Preconditions.checkNotNull(form.getPageNum());
		Preconditions.checkNotNull(form.getPageSize());
		// 检索仓库配置信息
		PageModel<CtStoreConfigBean> storeConfigPage = storeService.searchStoreConfigByPage(form.getStoreId(),
				form.getCfgName(), form.getCfgVal(), form.getPageNum(), form.getPageSize());
		
		return success(storeConfigPage);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.ADD_STORE_CONFIG)
	public AjaxResponse addStoreConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateStoreConfig(form, true);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.UPDATE_STORE_CONFIG)
	public AjaxResponse updateStoreConfig(@RequestBody CommonConfigFormBean form) {
		return addOrUpdateStoreConfig(form, false);
	}
	
	public AjaxResponse addOrUpdateStoreConfig(CommonConfigFormBean form, boolean append) {
		// 验证配置类型参数
		Preconditions.checkNotNull(form.getStoreId());
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
		Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));

		// 保存仓库配置信息
		CtStoreConfigModel storeConfigModel = new CtStoreConfigModel();
		BeanUtils.copyProperties(form, storeConfigModel);
		storeService.addOrUpdateStoreConfig(storeConfigModel, append);
		
		return success(true);
	}
	
	@RequestMapping(AdminUrlConstants.Store.Self.DELETE_STORE_CONFIG)
	public AjaxResponse deleteStoreConfig(@RequestBody CommonConfigFormBean[] forms) {
		// 验证配置类型参数
		for (CommonConfigFormBean form : forms) {
			Preconditions.checkNotNull(form.getStoreId());
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgName()));
			Preconditions.checkArgument(StringUtils.isNotBlank(form.getCfgVal1()));
		}

		// 删除仓库配置信息
		List<CtStoreConfigKey> storeConfigKeys = new ArrayList<CtStoreConfigKey>();
		for (CommonConfigFormBean form : forms) {
			CtStoreConfigKey configKey = new CtStoreConfigKey();
			BeanUtils.copyProperties(form, configKey);
			storeConfigKeys.add(configKey);
		}
		storeService.deleteStoreConfig(storeConfigKeys);
		
		return success(true);
	}

}
