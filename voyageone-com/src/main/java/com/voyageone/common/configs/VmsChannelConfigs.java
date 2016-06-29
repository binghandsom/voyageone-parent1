package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.VmsChannelConfigDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * VmsChannelConfig 配置文件的专用配置访问类
 *
 * @author chuanyu.liang, 16/06/25
 * @version 2.0.0
 * @since 2.0.0
 */
public class VmsChannelConfigs {

    private static final Class selfClass = VmsChannelConfigs.class;

    private final static Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_VmsChannelConfigs.toString();

    public static void reload() {
        VmsChannelConfigDao vmsChannelConfigDao = ConfigDaoFactory.getVmsChannelConfigDao();
        Map<String, VmsChannelConfigBean> vmsChannelConfigBeanMap = new HashMap<>();
        vmsChannelConfigDao.selectALl()
                .forEach(bean ->
                                vmsChannelConfigBeanMap.put(
                                buildKey(bean.getChannelId(), bean.getConfigKey(), bean.getConfigCode()),
                                bean
                            )
                );
        CacheHelper.reFreshSSB(KEY, vmsChannelConfigBeanMap);
        logger.info("vmsChannelConfig 读取数量: " + CacheHelper.getSize(KEY));
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
     * @return VmsChannelConfigBean
     */
    public static VmsChannelConfigBean getConfigBean(String channelId, String configKey, String configCode) {
        return CacheHelper.getBean(KEY, buildKey(channelId, configKey, configCode), selfClass);
    }

    /**
     * get one ConfigBean
     *
     * @param channelId  channel Id
     * @param configKey  config Key
     * @return VmsChannelConfigBean
     */
    public static VmsChannelConfigBean getConfigBeanNoCode(String channelId, String configKey) {
        return CacheHelper.getBean(KEY, buildKey(channelId, configKey, "0"), selfClass);
    }

    /**
     * get ConfigBeans list
     *
     * @param channelId channel Id
     * @param configKey config Key
     * @return List<VmsChannelConfigBean>
     */
    public static List<VmsChannelConfigBean> getConfigBeans(String channelId, String configKey) {
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