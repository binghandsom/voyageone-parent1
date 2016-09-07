package com.voyageone.service.impl.com.newshop;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;
import com.voyageone.service.bean.com.CtStoreConfigBean;
import com.voyageone.service.bean.com.NewShopBean;
import com.voyageone.service.bean.com.TmChannelShopBean;
import com.voyageone.service.bean.com.TmOrderChannelBean;
import com.voyageone.service.bean.com.TmTaskControlBean;
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
import com.voyageone.service.daoext.com.TmNewShopDataDaoExt;
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
import com.voyageone.service.model.com.PageModel;
import com.voyageone.service.model.com.TmNewShopDataModel;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/26
 */
@Service
public class NewShopService extends BaseService {
	
	private static final String SELECT_SQL_COLUMN_REGEX = "(select )[\\w`,\\(\\) ]+( from)";
	
	@Autowired
	private TmNewShopDataDao newShopDao;
	
	@Autowired
	private TmNewShopDataDaoExt newShopDaoExt;
	
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
		TmOrderChannelBean channel = channelService.searchChannelAndConfigByChannelId(channelId);
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
		// 开店sql模板参数
		Map<String, Object> sqlParams = new HashMap<String, Object>();
		// 渠道信息
		sqlParams.put("channel", getInsertMapperSql(TmOrderChannelDao.class, channel, overrides).get(0));
		// 渠道配置信息
		sqlParams.put("channelConfig", getInsertMapperSql(TmOrderChannelConfigDao.class, channel.getChannelConfig(), overrides));
		// 短信配置信息
		sqlParams.put("sms", getInsertMapperSql(TmSmsConfigDao.class, bean.getSms(), seqOverrides));
		// 第三方配置信息
		sqlParams.put("thirdParty", getInsertMapperSql(ComMtThirdPartyConfigDao.class, bean.getThirdParty(), seqOverrides));
		// 快递配置信息
		sqlParams.put("carrier", getInsertMapperSql(TmCarrierChannelDao.class, bean.getCarrier(), overrides));
		// 类型属性配置信息
		sqlParams.put("channelAttr", getInsertMapperSql(ComMtValueChannelDao.class, bean.getChannelAttr(), idOverrides));
		// 仓库和配置信息
		if (CollectionUtils.isNotEmpty(bean.getStore())) {
			Map<String, Object> storeIdOverrides = (Map<String, Object>) overrides.clone();
			storeIdOverrides.put("storeId", null);
			List<Object> storeParams = new ArrayList<Object>();
			for (WmsMtStoreBean store : bean.getStore()) {
				Map<String, Object> storeParam = new HashMap<String, Object>();
				storeParam.put("sql", getInsertMapperSql(WmsMtStoreDao.class, store, storeIdOverrides).get(0));
				if (CollectionUtils.isNotEmpty(store.getStoreConfig())) {
					// 设置自动生成的主键
					List<Map<String, Object>> storeConfig = new ArrayList<Map<String, Object>>();
					for (CtStoreConfigBean item : store.getStoreConfig()) {
						Map<String, Object> storeConfigMap = BeanUtils.describe(item);
						storeConfigMap.put("storeId", "@last_store_id");
						storeConfig.add(storeConfigMap);
					}
					storeParam.put("config", getInsertMapperSql(CtStoreConfigDao.class, storeConfig, overrides));
				}
				storeParams.add(storeParam);
			}
			sqlParams.put("store", storeParams);
		}
		// 渠道Cart和配置信息
		if (CollectionUtils.isNotEmpty(bean.getCartShop())) {
			List<Object> cartShopParams = new ArrayList<Object>();
			for (TmChannelShopBean cartShop : bean.getCartShop()) {
				Map<String, Object> cartShopParam = new HashMap<String, Object>();
				cartShopParam.put("sql", getInsertMapperSql(TmChannelShopDao.class, cartShop, overrides).get(0));
				cartShopParam.put("config", getInsertMapperSql(TmChannelShopConfigDao.class, cartShop.getCartShopConfig(), overrides));
				cartShopParams.add(cartShopParam);
			}
			sqlParams.put("cartShop", cartShopParams);
		}
		// Cart物流信息
		sqlParams.put("cartTracking", getInsertMapperSql(ComMtTrackingInfoConfigDao.class, bean.getCartTracking(), seqOverrides));
		// 任务信息
		if (CollectionUtils.isNotEmpty(bean.getTask())) {
			List<Object> taskParams = new ArrayList<Object>();
			// 任务信息参数
			Map<String, Object> taskSelectParams = new HashMap<String, Object>();
			Map<String, Object> taskInsertOverrides = (Map<String, Object>) overrides.clone();
			taskInsertOverrides.put("taskId", null);
			Map<String, Object> taskUpdateOverrides = (Map<String, Object>) overrides.clone();
			taskUpdateOverrides.put("taskId", "@task_id");
			// 任务配置参数
			Map<String, Object> taskConfigSqlParams = new HashMap<String, Object>();
			for (ComMtTaskBean task : bean.getTask()) {
				Map<String, Object> taskParam = new HashMap<String, Object>();
				// 任务信息的动态添加与更新
				taskSelectParams.put("taskType", task.getTaskType());
				taskSelectParams.put("taskName", task.getTaskName());
				String taskSql = getSelectMapperSql(ComMtTaskDao.class, taskSelectParams, "selectOne", null).get(0);
				taskParam.put("select", replacteSqlColumns(taskSql, "task_id into @task_id"));
				taskParam.put("insert", getInsertMapperSql(ComMtTaskDao.class, task, taskInsertOverrides).get(0));
				taskParam.put("update", getUpdateMapperSql(ComMtTaskDao.class, task, taskUpdateOverrides).get(0));
				// 任务配置信息的动态添加与更新
				List<Object> taskConfigParams = new ArrayList<Object>();
				for (TmTaskControlBean taskConfig : task.getTaskConfig()) {
					Map<String, Object> taskConfigParam = new HashMap<String, Object>();
					taskConfigSqlParams.put("taskId", taskConfig.getTaskId());
					taskConfigSqlParams.put("cfgName", taskConfig.getCfgName());
					taskConfigSqlParams.put("cfgVal1", taskConfig.getCfgVal1());
					taskConfigSqlParams.put("cfgVal2", taskConfig.getCfgVal2());
					String taskConfigSql = getSelectMapperSql(TmTaskControlDao.class, taskConfigSqlParams, "select", overrides).get(0);
					taskConfigParam.put("select", replacteSqlColumns(taskConfigSql, "task_id into @task_cfg_id"));
					taskConfigParam.put("insert", getInsertMapperSql(TmTaskControlDao.class, taskConfig, overrides).get(0));
					taskConfigParams.add(taskConfigParam);
				}
				taskParam.put("config", taskConfigParams);
				taskParams.add(taskParam);
			}
			sqlParams.put("task", taskParams);
		}
		
		// 文件基本设置
		String sqlPath = Properties.readValue(AdminProperty.Props.ADMIN_SQL_PATH);
		String sqlFileName = UUID.randomUUID().toString() + ".sql";
		File sqlFile = new File(sqlPath, sqlFileName);
		// 生成开店的sql文件
		try (
			Writer output = new OutputStreamWriter(new FileOutputStream(sqlFile));
		) {
			FileUtils.forceMkdir(new File(sqlPath));
			renderConfigTemplate(AdminProperty.Tpls.PROC_NEW_SHOP_SQL, sqlParams, output);
		} 
		
		return sqlFile;
	}
	
	protected void renderConfigTemplate(String ftlName, Object dataModel, Writer out) throws Exception {
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(getClass(), "/config/");
		Template template = config.getTemplate(ftlName);
		template.process(dataModel, out);
	}
	
	protected List<String> getInsertMapperSql(Class<?> clazz, Object model, Map<String, Object> overrides) {
		return getMyBatisMapperSql(clazz, model, "insert", overrides);
	}

	protected List<String> getSelectMapperSql(Class<?> clazz, Object model, String sqlKey, Map<String, Object> overrides) {
		return getMyBatisMapperSql(clazz, model, sqlKey, overrides);
	}

	protected List<String> getUpdateMapperSql(Class<?> clazz, Object model, Map<String, Object> overrides) {
		return getMyBatisMapperSql(clazz, model, "update", overrides);
	}
	
	private String replacteSqlColumns(String sql, String columns) {
		return sql.replaceFirst(SELECT_SQL_COLUMN_REGEX, "$1" + columns + "$2");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<String> getMyBatisMapperSql(Class<?> clazz, Object model, String sqlKey, Map<String, Object> overrides) {
		if (model == null) {
			return Collections.emptyList();
		} else if (List.class.isAssignableFrom(model.getClass())) {
			// List的简单对象
			List target = (List) model;
			if (CollectionUtils.isNotEmpty(target)) {
				List<String> sqls = new ArrayList<String>(target.size());
				for (int i = 0; i < target.size(); i++) {
					sqls.addAll(getMyBatisMapperSql(clazz, target.get(i), sqlKey, overrides));
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
							Method writer = propDesc.getWriteMethod();
							if (writer != null) {
								writer.invoke(model, overrides.get(keyName));
							}
						} catch (Exception e) {
							logger.debug("忽略覆盖对象属性时的失败，" + e.getMessage());
						}
					}
				}
			}
			return Arrays.asList(MybatisSqlHelper.getMapperSql(clazz, sqlKey, model));
		}
	}

	public void deleteNewShop(Long newShopId) {
		if (newShopDao.delete(newShopId) <= 0) {
			throw new BusinessException("删除开新店信息失败");
		}
	}

	public TmNewShopDataModel getNewShopById(Long newShopId) {
		return newShopDao.select(newShopId);
	}

	public PageModel<TmNewShopDataModel> searchNewShopByPage(String channelId, String channelName, String modifiedFrom,
			String modifiedTo, Integer pageNum, Integer pageSize) {
		PageModel<TmNewShopDataModel> pageModel = new PageModel<TmNewShopDataModel>();
		// 设置查询参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		params.put("channelName", channelName);
		params.put("modifiedFrom", modifiedFrom);
		params.put("modifiedTo", modifiedTo);
		
		// 判断查询结果是否分页
		if (pageNum != null && pageSize != null) {
			pageModel.setCount(newShopDaoExt.selectNewShopCount(params));
			params = MySqlPageHelper.build(params).page(pageNum).limit(pageSize).toMap();
		}
		// 查询开店脚本信息
		pageModel.setResult(newShopDaoExt.selectNewShopByPage(params));
		
		return pageModel;
	}
	
}
