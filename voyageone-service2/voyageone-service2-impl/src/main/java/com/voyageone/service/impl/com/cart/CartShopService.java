package com.voyageone.service.impl.com.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.TmChannelShopBean;
import com.voyageone.service.bean.com.TmChannelShopConfigBean;
import com.voyageone.service.dao.com.TmChannelShopConfigDao;
import com.voyageone.service.dao.com.TmChannelShopDao;
import com.voyageone.service.daoext.com.TmChannelShopDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmChannelShopConfigKey;
import com.voyageone.service.model.com.TmChannelShopConfigModel;
import com.voyageone.service.model.com.TmChannelShopKey;
import com.voyageone.service.model.com.TmChannelShopModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/19
 */
@Service
public class CartShopService extends BaseService {
	
	@Autowired
	private TmChannelShopDao cartShopDao;

	@Autowired
	private TmChannelShopDaoExt cartShopDaoExt;
	
	private TmChannelShopConfigDao cartShopConfigDao;

	public PageModel<TmChannelShopBean> searchCartShopByPage(String channelId, Integer cartId, String shopName,
			Integer pageNum, Integer pageSize) {
		PageModel<TmChannelShopBean> pageModel = new PageModel<TmChannelShopBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("cartId", cartId);
		params.put("shopName", shopName);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(cartShopDaoExt.selectCartShopCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道配置信息
		pageModel.setResult(cartShopDaoExt.selectCartShopByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateChannel(TmChannelShopModel model, String username, boolean append) {
		TmChannelShopModel cartShop = cartShopDao.select(model);
		boolean success = false;
		if (append) {
			// 添加Cart商店信息
			if (cartShop != null) {
				throw new BusinessException("添加的Cart商店信息已存在");
			}
			model.setCreater(username);
			model.setModifier(username);
			success = cartShopDao.insert(model) > 0;
		} else {
			// 更新Cart商店信息
			if (cartShop == null) {
				throw new BusinessException("更新的Cart商店信息不存在");
			}
			model.setModifier(username);
			success = cartShopDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存Cart商店信息失败");
		}
	}

	@VOTransactional
	public void deleteCartShop(List<TmChannelShopKey> shopKeys, String username) {
		for (TmChannelShopKey shopKey : shopKeys) {
			TmChannelShopModel shopModel = new TmChannelShopModel();
			shopModel.setOrderChannelId(shopKey.getOrderChannelId());
			shopModel.setCartId(shopKey.getCartId());
			shopModel.setActive(false);
			shopModel.setModifier(username);
			if (cartShopDao.update(shopModel) <= 0) {
				throw new BusinessException("删除Cart商店信息失败");
			}
		}
	}

	public PageModel<TmChannelShopConfigBean> searchCartShopConfigByPage(String channelId, String cartId,
			String cfgName, String cfgVal, Integer pageNum, Integer pageSize) {
		PageModel<TmChannelShopConfigBean> pageModel = new PageModel<TmChannelShopConfigBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("cartId", cartId);
		params.put("cfgName", cfgName);
		params.put("cfgVal", cfgVal);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(cartShopDaoExt.selectCartShopConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询Cart商店配置信息
		pageModel.setResult(cartShopDaoExt.selectCartShopConfigByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateCartShopConfig(TmChannelShopConfigModel model, boolean append) {
		TmChannelShopConfigModel shopConfig = cartShopConfigDao.select(model);
		boolean success = false;
		if (append) {
			// 添加Cart商店配置信息
			if (shopConfig != null) {
				throw new BusinessException("添加的Cart商店配置信息已存在");
			}
			success = cartShopConfigDao.insert(model) > 0;
		} else {
			// 更新Cart商店配置信息
			if (shopConfig == null) {
				throw new BusinessException("更新的Cart商店配置信息不存在");
			}
			success = cartShopConfigDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存Cart商店配置信息失败");
		}
	}

	public void deleteCartShopConfig(List<TmChannelShopConfigKey> shopConfigKeys) {
		for (TmChannelShopConfigKey shopConfigKey : shopConfigKeys) {
			if (cartShopConfigDao.delete(shopConfigKey) <= 0) {
				throw new BusinessException("删除Cart商店配置信息失败");
			}
		}
	}

}
