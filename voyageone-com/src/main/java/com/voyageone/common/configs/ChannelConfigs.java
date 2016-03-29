package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.OrderChannelConfigDao;
import com.voyageone.common.redis.CacheHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author aooer 2016/3/17.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ChannelConfigs {

    private static final Class selfClass = ChannelConfigs.class;

    private static final Log logger = LogFactory.getLog(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_OrderChannelConfigConfigs.toString();

    public static void reload() {
        OrderChannelConfigDao orderChannelConfigDao = ConfigDaoFactory.getOrderChannelConfigDao();
        Map<String, OrderChannelConfigBean> orderChannelConfigBeanMap = new HashMap<>();
        orderChannelConfigDao.getAll().forEach(
                bean ->
                    orderChannelConfigBeanMap.put(
                        buildKey(bean.getOrder_channel_id(), bean.getCfg_name(), bean.getCfg_val1()),
                        bean
                    )
        );
        CacheHelper.reFreshSSB(KEY, orderChannelConfigBeanMap);
        logger.info("orderChannelConfig 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String id, String name, String cfgVal1) {
        return id + CacheKeyEnums.SKIP + name + CacheKeyEnums.SKIP + cfgVal1;
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
        }
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
        OrderChannelConfigBean bean = CacheHelper.getBean(KEY, buildKey(id, name.toString(), val1), selfClass);
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
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(id, name.toString(), ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
    }
}
