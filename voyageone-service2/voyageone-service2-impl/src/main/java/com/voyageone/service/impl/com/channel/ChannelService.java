package com.voyageone.service.impl.com.channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.com.TmOrderChannelBean;
import com.voyageone.service.bean.com.TmOrderChannelConfigBean;
import com.voyageone.service.dao.com.TmOrderChannelConfigDao;
import com.voyageone.service.dao.com.TmOrderChannelDao;
import com.voyageone.service.daoext.com.TmOrderChannelDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.cart.CartService;
import com.voyageone.service.model.com.CtCartModel;
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmOrderChannelConfigKey;
import com.voyageone.service.model.com.TmOrderChannelConfigModel;
import com.voyageone.service.model.com.TmOrderChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/10
 */
@Service("AdminChannelService")
public class ChannelService extends BaseService {
	
	@Autowired
	private TmOrderChannelDao channelDao;
	
	@Autowired
	private TmOrderChannelDaoExt channelDaoExt;
	
	@Autowired
	private TmOrderChannelConfigDao channelConfigDao;
	
	@Resource(name = "AdminCartService")
	private CartService cartService;
	
	public List<TmOrderChannelModel> getAllChannel() {
		return channelDao.selectList(Collections.emptyMap());
	}
	
	public TmOrderChannelBean searchChannelByChannelId(String channelId) {
		List<TmOrderChannelBean> result = searchChannelByPage(channelId, null, null, null, null, null).getResult();
		if (result == null) {
			return null;
		} else {
			return result.get(0);
		}
	}
	
	public List<TmOrderChannelBean> searchChannel(String channelId, String channelName, Integer isUsjoi, Integer active) {
		return searchChannelByPage(channelId, channelName, isUsjoi, active, null, null).getResult();
	}
	
	public PageModel<TmOrderChannelBean> searchChannelByPage(String channelId, String channelName, Integer isUsjoi,
			Integer active, Integer pageNum, Integer pageSize) {
		PageModel<TmOrderChannelBean> pageModel = new PageModel<TmOrderChannelBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("channelName", channelName);
		params.put("isUsjoi", isUsjoi);
		params.put("active", active);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(channelDaoExt.selectChannelCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		
		// 查询渠道信息
		List<TmOrderChannelModel> channels = channelDaoExt.selectChannelByPage(params);
		if (CollectionUtils.isNotEmpty(channels)) {
			// 复制渠道信息，并添加渠道店铺对象。
			List<TmOrderChannelBean> newChannels = new ArrayList<TmOrderChannelBean>();
			for (int i = 0; i < channels.size(); i++) {
				TmOrderChannelBean newChannel = new TmOrderChannelBean();
				BeanUtils.copyProperties(channels.get(i), newChannel);
				// 取得渠道店铺ID对应的店铺对象
				if (StringUtils.isNotBlank(newChannel.getCartIds())) {
					List<CtCartModel> carts = cartService.getCartByIds(Arrays.asList(newChannel.getCartIds()
							.split(",")));
					newChannel.setCarts(carts);
				}
				newChannels.add(newChannel);
			}
			pageModel.setResult(newChannels);	
		}
		
		return pageModel;
	}

	public List<Map<String, Object>> getAllCompany() {
		return channelDaoExt.selectAllCompany();
	}

	public void addOrUpdateChannel(TmOrderChannelModel model, String username, boolean append) {
		TmOrderChannelModel channel = channelDao.select(model.getOrderChannelId());
		// 保存渠道信息
		boolean success = false;
		if (append) {
			// 添加渠道信息
			if (channel != null) {
				throw new BusinessException("添加的渠道信息已存在");
			}
			model.setCreater(username);
			model.setModifier(username);
			success = channelDao.insert(model) > 0;
		} else {
			// 更新渠道信息
			if (channel == null) {
				throw new BusinessException("更新的渠道信息不存在");
			}
			model.setModifier(username);
			success = channelDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存渠道信息失败");
		}
	}

	@VOTransactional
	public void deleteChannel(List<String> channelIds, String username) {
		for (String channelId : channelIds) {
			TmOrderChannelModel model = new TmOrderChannelModel();
			model.setOrderChannelId(channelId);
			model.setActive(0);
			model.setModifier(username);
			if (channelDao.update(model) <= 0) {
				throw new BusinessException("删除渠道信息失败");
			}
		}
	}

	public List<TmOrderChannelConfigBean> searchChannelConfig(String channelId, String cfgName, String cfgVal) {
		return searchChannelConfigByPage(channelId, cfgName, cfgVal, 0, 0).getResult();
	}
	
	public PageModel<TmOrderChannelConfigBean> searchChannelConfigByPage(String channelId, String cfgName,
			String cfgVal, Integer pageNum, Integer pageSize) {
		PageModel<TmOrderChannelConfigBean> pageModel = new PageModel<TmOrderChannelConfigBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("cfgName", cfgName);
		params.put("cfgVal", cfgVal);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(channelDaoExt.selectChannelConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道配置信息
		pageModel.setResult(channelDaoExt.selectChanneConfigByPage(params));
		
		return pageModel;
	}

	public void addOrUpdateChannelConfig(TmOrderChannelConfigModel model, boolean append) {
		// 查询渠道配置信息
		TmOrderChannelConfigModel channelConfig = channelConfigDao.select(model);
		
		// 保存渠道配置信息
		boolean success = false;
		if (append) {
			// 添加渠道配置信息
			if (channelConfig != null) {
				throw new BusinessException("添加的渠道配置信息已存在");
			}
			success = channelConfigDao.insert(model) > 0;
		} else {
			// 更新渠道配置信息
			if (channelConfig == null) {
				throw new BusinessException("更新的渠道配置信息不存在");
			}
			success = channelConfigDao.update(model) > 0;
		}

		
		if (!success) {
			throw new BusinessException("保存渠道配置信息失败");
		}
	}

	public void deleteChannelConfig(List<TmOrderChannelConfigKey> configKeys) {
		for (TmOrderChannelConfigKey configKey : configKeys) {
			// 删除渠道配置信息
			if (channelConfigDao.delete(configKey) <= 0) {
				throw new BusinessException("删除渠道配置信息失败");
			}
		}
	}

}
