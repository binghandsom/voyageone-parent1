package com.voyageone.service.impl.admin.channel;

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
import com.voyageone.service.bean.admin.TmOrderChannelBean;
import com.voyageone.service.dao.admin.TmOrderChannelDao;
import com.voyageone.service.daoext.admin.TmOrderChannelDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.admin.cart.CartService;
import com.voyageone.service.model.admin.CtCartModel;
import com.voyageone.service.model.admin.PageModel;
import com.voyageone.service.model.admin.TmOrderChannelConfigModel;
import com.voyageone.service.model.admin.TmOrderChannelModel;

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
	
	@Resource(name = "AdminCartService")
	private CartService cartService;
	
	public List<TmOrderChannelModel> getAllChannel() {
		return channelDao.selectList(Collections.emptyMap());
	}
	
	public List<TmOrderChannelBean> searchChannel(String channelId, String channelName, Integer isUsjoi) throws Exception {
		return searchChannelByPage(channelId, channelName, isUsjoi, 0, 0).getResult();
	}
	
	public PageModel<TmOrderChannelBean> searchChannelByPage(String channelId, String channelName, Integer isUsjoi,
			int pageNum, int pageSize) throws Exception {
		PageModel<TmOrderChannelBean> pageModel = new PageModel<TmOrderChannelBean>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("channelName", channelName);
		params.put("isUsjoi", isUsjoi);
		
		// 判断查询结果是否分页
		if (pageNum > 0 && pageSize > 0) {
			pageModel.setCount(channelDaoExt.selectChannelCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道信息
		List<TmOrderChannelModel> channels = channelDaoExt.selectChannelByPage(params);
		if (CollectionUtils.isNotEmpty(channels)) {
			// 复制渠道信息，并添加渠道店铺名。
			List<TmOrderChannelBean> newChannels = new ArrayList<TmOrderChannelBean>();
			for (int i = 0; i < channels.size(); i++) {
				TmOrderChannelBean newChannel = new TmOrderChannelBean();
				BeanUtils.copyProperties(channels.get(i), newChannel);
				
				// 取得渠道店铺ID对应的店铺名
				if (StringUtils.isNotBlank(newChannel.getCartIds())) {
					List<CtCartModel> carts = cartService.getCartByIds(Arrays.asList(newChannel.getCartIds().split(",")));
					if (CollectionUtils.isNotEmpty(carts)) {
						// 取出店铺的名
						String[] cartNames = new String[carts.size()];
						for (int j = 0; j < carts.size(); j++) {
							cartNames[j] = carts.get(j).getName();
						}
						newChannel.setCartNames(StringUtils.join(cartNames, "/"));
					}
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

	public boolean addOrUpdateChannel(TmOrderChannelModel model) {
		// 验证渠道ID唯一性
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", model.getOrderChannelId());
		if (channelDao.selectOne(params) != null) {
			throw new BusinessException("渠道ID[" + model.getOrderChannelId() + "]已存在！");
		}
		
		// 保存渠道信息，并返回保存结果。
		return channelDao.insert(model) > 0;
	}

	public List<TmOrderChannelConfigModel> searchChannelConfig(String channelId, String cfgName, String cfgVal) {
		return searchChannelConfigByPage(channelId, cfgName, cfgVal, 0, 0).getResult();
	}
	
	public PageModel<TmOrderChannelConfigModel> searchChannelConfigByPage(String channelId, String cfgName, String cfgVal,
			int pageNum, int pageSize) {
		PageModel<TmOrderChannelConfigModel> pageModel = new PageModel<TmOrderChannelConfigModel>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderChannelId", channelId);
		params.put("cfgName", cfgName);
		params.put("cfgVal", cfgVal);
		
		// 判断查询结果是否分页
		if (pageNum > 0 && pageSize > 0) {
			pageModel.setCount(channelDaoExt.selectChannelConfigCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询渠道信息
		pageModel.setResult(channelDaoExt.selectChanneConfigByPage(params));
		
		return pageModel;
	}

	public boolean addOrUpdateChannelConfig(TmOrderChannelConfigModel model) {
		return false;
	}

}
