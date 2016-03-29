package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.dao.CmsChannelConfigDao;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.redis.CacheHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * CmsChannelConfig 配置文件的专用配置访问类
 *
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsChannelConfigs {

    private static final Class selfClass = CmsChannelConfigs.class;

    private static final Log logger = LogFactory.getLog(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_CmsChannelConfigs.toString();

    public static void reload() {
        CmsChannelConfigDao cmsChannelConfigDao = ConfigDaoFactory.getCmsChannelConfigDao();
        Map<String, CmsChannelConfigBean> cmsChannelConfigBeanMap = new HashMap<>();
        cmsChannelConfigDao.selectALl()
                .forEach(bean ->
                            cmsChannelConfigBeanMap.put(
                                buildKey(bean.getChannelId(), bean.getConfigKey(), bean.getConfigCode()),
                                bean
                            )
                );
        CacheHelper.reFreshSSB(KEY, cmsChannelConfigBeanMap);
        logger.info("cmsChannelConfig 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String channelId, String configKey, String configCode) {
        return channelId + CacheKeyEnums.SKIP + configKey + CacheKeyEnums.SKIP + configCode;
    }

    /**
     * get one ConfigBean
     *
     * @param channelId  channel Id
     * @param configKey  config Key
     * @param configCode config Code
     * @return CmsChannelConfigBean
     */
    public static CmsChannelConfigBean getConfigBean(String channelId, String configKey, String configCode) {
        return CacheHelper.getBean(KEY, buildKey(channelId, configKey, configCode), selfClass);
    }

    /**
     * get ConfigBeans list
     *
     * @param channelId channel Id
     * @param configKey config Key
     * @return List<CmsChannelConfigBean>
     */
    public static List<CmsChannelConfigBean> getConfigBeans(String channelId, String configKey) {
        Set<String> keyset = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keyset)) return null;
        List<String> keyList = new ArrayList<>();
        keyset.forEach(k -> {
            if (k.startsWith(buildKey(channelId, configKey, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
    }
}