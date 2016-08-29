package com.voyageone.service.impl.com.openshop;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.base.dao.mysql.MybatisSqlHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Properties;
import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;
import com.voyageone.service.bean.com.OpenShopBean;
import com.voyageone.service.bean.com.TmChannelShopBean;
import com.voyageone.service.bean.com.TmOrderChannelBean;
import com.voyageone.service.bean.com.WmsMtStoreBean;
import com.voyageone.service.dao.com.ComMtTaskDao;
import com.voyageone.service.dao.com.ComMtThirdPartyConfigDao;
import com.voyageone.service.dao.com.ComMtTrackingInfoConfigDao;
import com.voyageone.service.dao.com.ComMtValueChannelDao;
import com.voyageone.service.dao.com.CtStoreConfigDao;
import com.voyageone.service.dao.com.TmCarrierChannelDao;
import com.voyageone.service.dao.com.TmChannelShopConfigDao;
import com.voyageone.service.dao.com.TmChannelShopDao;
import com.voyageone.service.dao.com.TmOrderChannelConfigDao;
import com.voyageone.service.dao.com.TmOrderChannelDao;
import com.voyageone.service.dao.com.TmSmsConfigDao;
import com.voyageone.service.dao.com.TmTaskControlDao;
import com.voyageone.service.dao.com.WmsMtStoreDao;
import com.voyageone.service.impl.AdminProperty;
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

	public void handleChannelSeriesSql(OpenShopBean bean, String username) throws IOException {
		TmOrderChannelBean channel = bean.getChannel();
		// 覆盖的参数
		Map<String, Object> overrides = new HashMap<String, Object>();
		overrides.put("seq", null);
		overrides.put("creater", username);
		overrides.put("created", null);
		overrides.put("modifier", username);
		overrides.put("modified", null);
		overrides.put("channelId", channel.getOrderChannelId());
		overrides.put("orderChannelId", channel.getOrderChannelId());
		// 生成开店sql
		List<String> sqls = new ArrayList<String>();
		// 渠道信息
		sqls.addAll(getInsertMapperSql(TmOrderChannelDao.class, channel, overrides));
		// 渠道配置信息
		sqls.addAll(getInsertMapperSql(TmOrderChannelConfigDao.class, channel.getChannelConfig(), overrides));
		// 短信配置信息
		sqls.addAll(getInsertMapperSql(TmSmsConfigDao.class, bean.getSms(), overrides));
		// 第三方配置信息
		sqls.addAll(getInsertMapperSql(ComMtThirdPartyConfigDao.class, bean.getThirdParty(), overrides));
		// 快递配置信息
		sqls.addAll(getInsertMapperSql(TmCarrierChannelDao.class, bean.getCarrier(), overrides));
		// 类型属性配置信息
		sqls.addAll(getInsertMapperSql(ComMtValueChannelDao.class, bean.getChannelAttr(), overrides));
		// 仓库和配置信息
		if (CollectionUtils.isNotEmpty(bean.getStore())) {
			sqls.addAll(getInsertMapperSql(WmsMtStoreDao.class, bean.getStore(), overrides));
			for (WmsMtStoreBean store : bean.getStore()) {
				sqls.addAll(getInsertMapperSql(CtStoreConfigDao.class, store.getStoreConfig(), overrides));	
			}	
		}
		// 渠道Cart和配置信息
		if (CollectionUtils.isNotEmpty(bean.getCartShop())) {
			sqls.addAll(getInsertMapperSql(TmChannelShopDao.class, bean.getCartShop(), overrides));
			for (TmChannelShopBean cartShop : bean.getCartShop()) {
				sqls.addAll(getInsertMapperSql(TmChannelShopConfigDao.class, cartShop.getCartShopConfig(), overrides));
			}
		}
		// Cart物流信息
		sqls.addAll(getInsertMapperSql(ComMtTrackingInfoConfigDao.class, bean.getCartTracking(), overrides));
		// 任务信息
		if (CollectionUtils.isNotEmpty(bean.getTask())) {
			sqls.addAll(getInsertMapperSql(ComMtTaskDao.class, bean.getTask(), overrides));
			for (ComMtTaskBean task : bean.getTask()) {
				sqls.addAll(getInsertMapperSql(TmTaskControlDao.class, task.getTaskConfig(), overrides));
			}
		}
		
		// 文件基本设置
		String sqlPath = Properties.readValue(AdminProperty.Props.ADMIN_SQL_PATH);
		String sqlFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".sql";
		File sqlFile = new File(sqlPath, sqlFileName);
		// 保存sql文件
		FileUtils.writeLines(sqlFile, sqls);
	}

	@SuppressWarnings("rawtypes")
	private List<String> getInsertMapperSql(Class<?> clazz, Object model, Map<String, Object> overrides) {
		if (model == null) {
			return Collections.emptyList();
		} else if (List.class.isAssignableFrom(model.getClass())) {
			// List的简单对象
			List target = (List) model;
			if (CollectionUtils.isNotEmpty(target)) {
				List<String> sqls = new ArrayList<String>();
				for (int i = 0; i < target.size(); i++) {
					sqls.addAll(getInsertMapperSql(clazz, target.get(i), overrides));
				}
				return sqls;
			}
			return Collections.emptyList();
		} else {
			// 简单对象
			if (MapUtils.isNotEmpty(overrides)) {
				for (String keyName : overrides.keySet()) {
					try {
						PropertyDescriptor propDesc = new PropertyDescriptor(keyName, model.getClass());
						Method writter = propDesc.getWriteMethod();
						if (writter != null) {
							writter.invoke(model, overrides.get(keyName));
						}
					} catch (Exception e) {
						logger.debug("覆盖对象属性失败，" + e.getMessage());
					}
				}
			}
			return Arrays.asList(MybatisSqlHelper.getMapperSql(clazz, "insert", model) + ";");
		}
	}
	
}
