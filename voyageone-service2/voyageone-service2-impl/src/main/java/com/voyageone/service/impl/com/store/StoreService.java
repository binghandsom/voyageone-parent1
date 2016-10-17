package com.voyageone.service.impl.com.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.CtStoreConfigBean;
import com.voyageone.service.bean.com.WmsMtStoreBean;
import com.voyageone.service.dao.com.CtStoreConfigDao;
import com.voyageone.service.dao.com.WmsMtStoreDao;
import com.voyageone.service.daoext.com.WmsMtStoreDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.CtStoreConfigKey;
import com.voyageone.service.model.com.CtStoreConfigModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.WmsMtStoreKey;
import com.voyageone.service.model.com.WmsMtStoreModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/12
 */
@Service
public class StoreService extends BaseService {
	
	@Autowired
	private WmsMtStoreDao storeDao;
	
	@Autowired
	private CtStoreConfigDao storeConfigDao;
	
	@Autowired
	private WmsMtStoreDaoExt storeDaoExt;

	public List<WmsMtStoreBean> getAllStore(String channelId) {
		return storeDaoExt.selecAllStore(channelId);
	}

	public List<WmsMtStoreBean> getStoreByChannelIds(List<String> channelIds) {
		return storeDaoExt.getStoreByChannelIds(channelIds);
	}
	
	public List<WmsMtStoreBean> searchStoreAndConfigByChannelId(String channelId) {
		List<WmsMtStoreBean> stores = searchStore(channelId, null, null, null, null);
		if (CollectionUtils.isNotEmpty(stores)) {
			stores.forEach(store -> {
				store.setStoreConfig(searchStoreConfigByPage(store.getStoreId(), null, null, null, null).getResult());
			});
		}
		
		return stores;
	}
	
	public List<WmsMtStoreBean> searchStore(String channelId, String storeName, String isSale,
			String storeType, Boolean active) {
		return searchStoreByPage(channelId, storeName, isSale, storeType, active, null, null).getResult();
	}

	public PageModel<WmsMtStoreBean> searchStoreByPage(String channelId, String storeName, String isSale,
			String storeType, Boolean active, Integer pageNum, Integer pageSize) {
		PageModel<WmsMtStoreBean> pageModel = new PageModel<WmsMtStoreBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("storeName", storeName);
		params.put("isSale", isSale);
		params.put("storeType", storeType);
		params.put("active", active);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(storeDaoExt.selectStoreCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询仓库信息
		pageModel.setResult(storeDaoExt.selectStoreByPage(params));
		
		return pageModel;
	}

	@VOTransactional
	public void addOrUpdateStore(WmsMtStoreModel model, String username, boolean append) {
		// 查询仓库信息
		WmsMtStoreModel store = storeDao.select(model);

		// 保存仓库信息
		boolean success = false;
		if (append) {
			// 添加仓库信息
			if (store != null) {
				throw new BusinessException("添加的仓库信息已存在");
			}
			if (model.getParentStoreId() == null) {
				model.setParentStoreId(0);
			}
			model.setCreater(username);
			model.setModifier(username);
			success = storeDao.insert(model) > 0;
			// 更新主仓库名
			if (model.getParentStoreId() == 0 && success) {
				model.setParentStoreId(model.getStoreId().intValue());
				success = storeDao.update(model) > 0;
			}
		} else {
			// 更新仓库信息
			if (store == null) {
				throw new BusinessException("更新的仓库信息不存在");
			}
			model.setModifier(username);
			if (model.getParentStoreId() == null) {
				model.setParentStoreId(model.getStoreId().intValue());
			}
			success = storeDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存仓库信息失败");
		}
	}

	@VOTransactional
	public void deleteStore(List<WmsMtStoreKey> storeKeys, String username) {
		for (WmsMtStoreKey storeKey : storeKeys) {
			// 设置更新参数
			WmsMtStoreModel model = new WmsMtStoreModel();
			model.setOrderChannelId(storeKey.getOrderChannelId());
			model.setStoreId(storeKey.getStoreId());
			model.setActive(false);
			model.setModifier(username);
			// 软删除仓库信息
			if (storeDao.update(model) <= 0) {
				throw new BusinessException("删除仓库信息失败");
			}
		}
	}

	public PageModel<CtStoreConfigBean> searchStoreConfigByPage(Long storeId, String cfgName, String cfgVal,
			Integer pageNum, Integer pageSize) {
		PageModel<CtStoreConfigBean> pageModel = new PageModel<CtStoreConfigBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("storeId", storeId);
		params.put("cfgName", cfgName);
		params.put("cfgVal", cfgVal);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(storeDaoExt.selectStoreConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询仓库配置信息
		pageModel.setResult(storeDaoExt.selectStoreConfigByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateStoreConfig(CtStoreConfigModel model, boolean append) {
		// 查询仓库配置信息
		CtStoreConfigKey configKey = new CtStoreConfigKey();
		configKey.setStoreId(model.getStoreId());
		configKey.setCfgName(model.getCfgName());
		configKey.setCfgVal1(model.getCfgVal1());
		CtStoreConfigModel storeConfig = storeConfigDao.select(configKey);
		
		boolean success = false;
		// 保存仓库配置信息
		if (append) {
			// 添加仓库配置信息
			if (storeConfig != null) {
				throw new BusinessException("添加的仓库配置已存在");
			}
			success = storeConfigDao.insert(model) > 0;
		} else {
			// 更新仓库配置信息
			if (storeConfig == null) {
				throw new BusinessException("更新的仓库配置不存在");
			}
			success = storeConfigDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存仓库配置失败");
		}
	}

	public void deleteStoreConfig(List<CtStoreConfigKey> configKeys) {
		for (CtStoreConfigKey configKey : configKeys) {
			if (storeConfigDao.delete(configKey) <= 0) {
				throw new BusinessException("删除仓库配置失败");
			}
		}
	}

}
