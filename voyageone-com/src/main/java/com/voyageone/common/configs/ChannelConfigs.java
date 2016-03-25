package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.OrderChannelConfigDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author aooer 2016/3/17.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ChannelConfigs {

    private static final Logger logger = LoggerFactory.getLogger(ChannelConfigs.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_OrderChannelConfigConfigs.toString();

    private static HashOperations<String, String, OrderChannelConfigBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            OrderChannelConfigDao orderChannelConfigDao = ConfigDaoFactory.getOrderChannelConfigDao();
            Map<String, OrderChannelConfigBean> orderChannelConfigBeanMap = new HashMap<>();
            orderChannelConfigDao.getAll().forEach(
                    bean -> {
                        orderChannelConfigBeanMap.
                                put(
                                        buildKey(bean.getOrder_channel_id(), ChannelConfigEnums.Name.valueOf(bean.getCfg_name()), bean.getCfg_val1()),
                                        bean
                                );
                    }
            );
            CacheHelper.reFreshSSB(KEY, orderChannelConfigBeanMap);
            logger.info("orderChannelConfig 读取数量: " + hashOperations.size(KEY));
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String id, ChannelConfigEnums.Name name, String cfgVal1) {
        return id + CacheHelper.SKIP + name + CacheHelper.SKIP + cfgVal1;
    }

    /**
     * 获取指定渠道的第一个配置参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @return String
     */
    public static String getVal1(String id, ChannelConfigEnums.Name name) {
        List<OrderChannelConfigBean> beans = getConfigs(id, name);
        if (CollectionUtils.isEmpty(beans)) {
            logger.warn("未获取到beans");
            return "";
        };
        return beans.get(0).getCfg_val1();
    }

    /**
     * 获取指定渠道、配置名称、第一个参数的第二个参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @param val1 第一个参数
     * @return String
     */
    public static String getVal2(String id, ChannelConfigEnums.Name name, String val1) {
        OrderChannelConfigBean bean = hashOperations.get(KEY, buildKey(id, name, val1));
        return (bean == null) ? "" : bean.getCfg_val2();
    }

    /**
     * 获取指定渠道的一个配置的所有参数
     *
     * @param id   渠道ID
     * @param name 配置名称
     * @return List<OrderChannelConfig>
     */
    public static List<OrderChannelConfigBean> getConfigs(String id, ChannelConfigEnums.Name name) {
        Set<String> keySet = hashOperations.keys(KEY);
        if (CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(id, name, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY, keyList);
    }
}
