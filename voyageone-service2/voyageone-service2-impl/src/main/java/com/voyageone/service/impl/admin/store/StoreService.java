package com.voyageone.service.impl.admin.store;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.admin.WmsMtStoreBean;
import com.voyageone.service.dao.admin.WmsMtStoreDao;
import com.voyageone.service.daoext.admin.WmsMtStoreDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.WmsMtStoreKey;
import com.voyageone.service.model.admin.WmsMtStoreModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/12
 */
@Service
public class StoreService extends BaseService {
	
	@Autowired
	private WmsMtStoreDao storeDao;
	
	@Autowired
	private WmsMtStoreDaoExt storeDaoExt;

	public List<WmsMtStoreModel> getAllStore() {
		return storeDao.selectList(Collections.emptyMap());
	}
	
	public List<WmsMtStoreBean> searchStore(String channelId, String storeName, String isSale,
			String storeType) {
		return searchStoreByPage(channelId, storeName, isSale, storeType, null, null).getResult();
	}

	public PageModel<WmsMtStoreBean> searchStoreByPage(String channelId, String storeName, String isSale,
			String storeType, Integer pageNum, Integer pageSize) {
		PageModel<WmsMtStoreBean> pageModel = new PageModel<WmsMtStoreBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("storeName", storeName);
		params.put("isSale", isSale);
		params.put("storeType", storeType);
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(storeDaoExt.selectStoreCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询仓库信息
		pageModel.setResult(storeDaoExt.selectStoreByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateStore(WmsMtStoreModel model, boolean append) {
		// 查询仓库信息
		WmsMtStoreKey storeKey = new WmsMtStoreKey();
		storeKey.setOrderChannelId(model.getOrderChannelId());
		storeKey.setStoreId(model.getStoreId());
		WmsMtStoreModel store = storeDao.select(storeKey);
		
		boolean success = false;
		// 保存仓库信息
		if (append) {
			// 添加仓库信息
			if (store != null) {
				throw new BusinessException("添加的仓库信息已存在");
			}
			success = storeDao.insert(model) > 0;
		} else {
			// 更新仓库信息
			if (store == null) {
				throw new BusinessException("更新的仓库信息不存在");
			}
			success = storeDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存仓库信息失败");
		}
	}

}
