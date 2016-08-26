package com.voyageone.service.impl.com.openshop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;
import com.voyageone.service.bean.com.TmChannelShopBean;
import com.voyageone.service.bean.com.TmOrderChannelBean;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.com.cart.CartShopService;
import com.voyageone.service.impl.com.cart.CartTrackingService;
import com.voyageone.service.impl.com.channel.CarrierConfigService;
import com.voyageone.service.impl.com.channel.ChannelAttributeService;
import com.voyageone.service.impl.com.channel.ChannelService;
import com.voyageone.service.impl.com.channel.SmsConfigService;
import com.voyageone.service.impl.com.channel.ThirdPartyConfigService;
import com.voyageone.service.impl.com.store.StoreService;
import com.voyageone.service.impl.com.task.TaskService;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/26
 */
@Service
public class OpenShopService extends BaseService {
	
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private SmsConfigService smsConfigService;

	@Autowired
	private ThirdPartyConfigService thirdPartyConfigService;
	
	@Autowired
	private CarrierConfigService carrierConfigService;
	
	@Autowired
	private ChannelAttributeService channelAttributeService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private CartShopService cartShopService;

	@Autowired
	private CartTrackingService cartTrackingService;

	@Autowired
	private TaskService taskService;

	public Map<String, Object> getChannelSeries(String channelId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 渠道信息
		TmOrderChannelBean channel = channelService.searchChannelByChannelId(channelId);
		if (channel == null) {
			throw new BusinessException("选择复制的渠道信息不存在");
		}
		result.put("channel", channel);
		// 短信配置信息
		result.put("sms", smsConfigService.searchSmsConfigByChannelId(channelId));
		// 第三方配置信息
		result.put("thirdParty", thirdPartyConfigService.searchThirdPartyConfigByChannelId(channelId));
		// 快递配置信息
		result.put("carrier", carrierConfigService.searchCarrierConfigByChannelId(channelId));
		// 类型属性配置信息
		result.put("channelAttr", channelAttributeService.searchChannelAttributeByChannelId(channelId));
		// 仓库和配置信息
		result.put("store", storeService.searchStoreAndConfigByChannelId(channelId));
		// Cart相关信息
		List<TmChannelShopBean> allCartShops = new ArrayList<TmChannelShopBean>();
		List<ComMtTrackingInfoConfigBean> allCartTrackings = new ArrayList<ComMtTrackingInfoConfigBean>();
		// 取得Cart相关信息
		if (StringUtils.isNotBlank(channel.getCartIds())) {
			String[] cartIds = channel.getCartIds().split(",");
			if (ArrayUtils.isNotEmpty(cartIds)) {
				for (String cartId : cartIds) {
					// 渠道Cart和配置信息
					List<TmChannelShopBean> cartShops = cartShopService.searchCartShopAndConfigByKeys(channelId,
							Integer.valueOf(cartId));
					if (CollectionUtils.isNotEmpty(cartShops)) {
						allCartShops.addAll(cartShops);
					}
					
					// Cart物流信息
					List<ComMtTrackingInfoConfigBean> cartTrackings = cartTrackingService.searchCartTrackingByKeys(
							channelId, Integer.valueOf(cartId));
					if (CollectionUtils.isNotEmpty(cartTrackings)) {
						allCartTrackings.addAll(cartTrackings);
					}
				}
			}
		}
		// 设置Cart相关信息
		result.put("cartShop", allCartShops);
		result.put("cartTracking", allCartTrackings);
		// 任务信息
		result.put("task", taskService.searchTaskByChannelId(channelId));
		
		return result;
	}

}
