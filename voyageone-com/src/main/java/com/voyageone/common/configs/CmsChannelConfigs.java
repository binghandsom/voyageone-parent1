package com.voyageone.common.configs;

import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.dao.CmsChannelConfigDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * CmsChannelConfig 配置文件的专用配置访问类
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CmsChannelConfigs {

	private static Log logger = LogFactory.getLog(CmsChannelConfigs.class);

	private static Map<String, CmsChannelConfigBean> channelConfigMap = new HashMap<>();

	/**
	 * init方法
	 * @param dao DAO
	 */
	public static void init(CmsChannelConfigDao dao) {
		logger.info("cms_mt_channel_config 取得开始...");
		List<CmsChannelConfigBean> channelConfigs = dao.selectALl();

		if (channelConfigs != null) {
			for (CmsChannelConfigBean channelConfig : channelConfigs) {
				String key = buildKey(channelConfig.getChannelId(), channelConfig.getConfigKey(), channelConfig.getConfigCode());
				channelConfigMap.put(key, channelConfig);
			}
		}
		logger.info("cms_mt_channel_config 取得结束...");
	}

	private static String buildKey(String channelId, String configKey, String configCode) {
		return channelId + "|" + configKey + "|" + configCode;
	}

	/**
	 * get one ConfigBean
	 * @param channelId channel Id
	 * @param configKey config Key
	 * @param configCode config Code
	 * @return CmsChannelConfigBean
	 */
	public static CmsChannelConfigBean getConfigBean(String channelId, String configKey, String configCode) {
		String key = buildKey(channelId, configKey, configCode);
		return channelConfigMap.get(key);
	}

	/**
	 * get ConfigBeans list
	 * @param channelId channel Id
	 * @param configKey config Key
	 * @return List<CmsChannelConfigBean>
	 */
	public static List<CmsChannelConfigBean> getConfigBeans(String channelId, String configKey) {
		List<CmsChannelConfigBean> result = new ArrayList<>();

		String key = buildKey(channelId, configKey, "");
		for(Map.Entry<String, CmsChannelConfigBean> entry : channelConfigMap.entrySet()) {
			if (entry.getKey().startsWith(key)) {
				result.add(entry.getValue());
			}
		}
		return result;
	}
}
