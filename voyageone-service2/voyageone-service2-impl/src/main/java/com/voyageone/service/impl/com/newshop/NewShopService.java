package com.voyageone.service.impl.com.newshop;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
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
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;
import com.voyageone.service.bean.com.CtStoreConfigBean;
import com.voyageone.service.bean.com.NewShopBean;
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
import com.voyageone.service.dao.com.TmNewShopDataDao;
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
import com.voyageone.service.model.com.TmNewShopDataModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/26
 */
@Service
public class NewShopService extends BaseService {
	
	@Autowired
	private TmNewShopDataDao newShopDao;
	
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

	public void saveChannelSeries(Long newShopId, NewShopBean bean, String username) {
		TmOrderChannelBean channel = bean.getChannel();
		// 设置开新店信息
		TmNewShopDataModel model = new TmNewShopDataModel();
		model.setChannelId(channel.getOrderChannelId());
		model.setChannelName(channel.getName());
		model.setData(JacksonUtil.bean2Json(bean));
		// 保存开新店信息
		boolean success = false;
		if (newShopId == null) {
			// 添加开新店信息
			model.setCreater(username);
			model.setModifier(username);
			success = newShopDao.insert(model) > 0;
		} else {
			model.setId(newShopId);
			model.setModifier(username);
			TmNewShopDataModel newShop = newShopDao.select(newShopId);
			if (newShop == null) {
				throw new BusinessException("更新的开新店信息不存在");
			}
			success = newShopDao.update(model) > 0;
		}
		
		if (!success) {
			throw new BusinessException("保存开新店信息失败");
		}
	}

	public File downloadNewShopSql(Long newShopId, String username) throws Exception {
		TmNewShopDataModel newShop = newShopDao.select(newShopId);
		if (newShop == null) {
			throw new BusinessException("开新店信息不存在");
		}
		if (StringUtils.isBlank(newShop.getData())) {
			throw new BusinessException("开新店数据信息为空");
		}
		NewShopBean bean = JacksonUtil.json2Bean(newShop.getData(), NewShopBean.class);
		
		return handleChannelSeriesSql(bean, username);
	}

	@SuppressWarnings({ "unchecked" })
	protected File handleChannelSeriesSql(NewShopBean bean, String username) throws Exception {
		TmOrderChannelBean channel = bean.getChannel();
		// 覆盖的参数
		HashMap<String, Object> overrides = new HashMap<String, Object>();
		overrides.put("creater", username);
		overrides.put("created", null);
		overrides.put("modifier", username);
		overrides.put("modified", null);
		overrides.put("channelId", channel.getOrderChannelId());
		overrides.put("orderChannelId", channel.getOrderChannelId());
		Map<String, Object> seqOverrides = (Map<String, Object>) overrides.clone();
		seqOverrides.put("seq", null);
		Map<String, Object> idOverrides = (Map<String, Object>) overrides.clone();
		idOverrides.put("id", null);
		// 生成开店sql
		List<String> sqls = new ArrayList<String>();
		sqls.add("delimiter $$");
		sqls.add("drop procedure if exists open_new_shop $$");
		sqls.add("create procedure open_new_shop()");
		sqls.add("begin");
		sqls.add("  declare errno integer default 0;");
		sqls.add("  declare continue handler for sqlexception set errno = 1;");
		sqls.add("  set autocommit = 0;");
		// 渠道信息
		sqls.add("  /* tm_order_channel */");
		sqls.addAll(getInsertMapperSql(TmOrderChannelDao.class, channel, overrides));
		// 渠道配置信息
		sqls.add("  /* tm_order_channel_config */");
		sqls.addAll(getInsertMapperSql(TmOrderChannelConfigDao.class, channel.getChannelConfig(), overrides));
		// 短信配置信息
		sqls.add("  /* tm_sms_config */");
		sqls.addAll(getInsertMapperSql(TmSmsConfigDao.class, bean.getSms(), seqOverrides));
		// 第三方配置信息
		sqls.add("  /* com_mt_third_party_config */");
		sqls.addAll(getInsertMapperSql(ComMtThirdPartyConfigDao.class, bean.getThirdParty(), seqOverrides));
		// 快递配置信息
		sqls.add("  /* tm_carrier_channel */");
		sqls.addAll(getInsertMapperSql(TmCarrierChannelDao.class, bean.getCarrier(), overrides));
		// 类型属性配置信息
		sqls.add("  /* com_mt_value_channel */");
		sqls.addAll(getInsertMapperSql(ComMtValueChannelDao.class, bean.getChannelAttr(), idOverrides));
		// 仓库和配置信息
		if (CollectionUtils.isNotEmpty(bean.getStore())) {
			Map<String, Object> storeIdOverrides = (Map<String, Object>) overrides.clone();
			storeIdOverrides.put("storeId", null);
			for (WmsMtStoreBean store : bean.getStore()) {
				sqls.add("  /* wms_mt_store */");
				sqls.addAll(getInsertMapperSql(WmsMtStoreDao.class, store, storeIdOverrides));
				if (CollectionUtils.isNotEmpty(store.getStoreConfig())) {
					sqls.add("  /* ct_store_config */");
					sqls.add("  select last_insert_id() into @last_store_id;");
					// 设置自动生成的主键
					List<Map<String, Object>> storeConfig = new ArrayList<Map<String, Object>>();
					for (CtStoreConfigBean item : store.getStoreConfig()) {
						Map<String, Object> storeConfigMap = BeanUtils.describe(item);
						storeConfigMap.put("storeId", "@last_store_id");
						storeConfig.add(storeConfigMap);
					}
					sqls.addAll(getInsertMapperSql(CtStoreConfigDao.class, storeConfig, overrides));
				}
			}	
		}
		// 渠道Cart和配置信息
		if (CollectionUtils.isNotEmpty(bean.getCartShop())) {
			for (TmChannelShopBean cartShop : bean.getCartShop()) {
				sqls.add("  /* tm_channel_shop */");
				sqls.addAll(getInsertMapperSql(TmChannelShopDao.class, cartShop, overrides));
				sqls.add("  /* tm_channel_shop_config */");
				sqls.addAll(getInsertMapperSql(TmChannelShopConfigDao.class, cartShop.getCartShopConfig(), overrides));
			}
		}
		// Cart物流信息
		sqls.add("  /* com_mt_tracking_info_config */");
		sqls.addAll(getInsertMapperSql(ComMtTrackingInfoConfigDao.class, bean.getCartTracking(), seqOverrides));
		// 任务信息
		if (CollectionUtils.isNotEmpty(bean.getTask())) {
			Map<String, Object> taskIdOverrides = (Map<String, Object>) overrides.clone();
			taskIdOverrides.put("taskId", null);
			for (ComMtTaskBean task : bean.getTask()) {
				sqls.add("  /* com_mt_task */");
				sqls.addAll(getInsertMapperSql(ComMtTaskDao.class, task, taskIdOverrides));
				sqls.add("  /* tm_task_control */");
				sqls.addAll(getInsertMapperSql(TmTaskControlDao.class, task.getTaskConfig(), overrides));
			}
		}
		sqls.add("  if errno = 0 then");
		sqls.add("    commit;");
		sqls.add("  else");
		sqls.add("    rollback;");
		sqls.add("  end if;");
		sqls.add("end$$");
		sqls.add("call open_new_shop() $$");
		sqls.add("delimiter ;");
		
		// 文件基本设置
		String sqlPath = Properties.readValue(AdminProperty.Props.ADMIN_SQL_PATH);
		String sqlFileName = UUID.randomUUID().toString() + ".sql";
		File sqlFile = new File(sqlPath, sqlFileName);
		// 保存sql文件
		FileUtils.writeLines(sqlFile, sqls);
		
		return sqlFile;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
				// Map类型
				if (Map.class.isAssignableFrom(model.getClass())) {
					Map<String, Object> target = (Map<String, Object>) model;
					for (String keyName : overrides.keySet()) {
						if (target.containsKey(keyName)) {
							target.put(keyName, overrides.get(keyName));
						}
					}
				} else {
					// POJO类型
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
			}
			return Arrays.asList("  " + MybatisSqlHelper.getMapperSql(clazz, "insert", model) + ";");
		}
	}

	public void deleteNewShop(Long newShopId) {
		if (newShopDao.delete(newShopId) <= 0) {
			throw new BusinessException("删除开新店信息失败");
		}
	}
	
}
