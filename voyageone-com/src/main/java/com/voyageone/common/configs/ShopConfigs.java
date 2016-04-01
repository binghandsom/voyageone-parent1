package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ShopConfigDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author aooer 2016/3/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ShopConfigs {
    private static final Class selfClass = ShopConfigs.class;

    private final static Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_ShopConfigConfigs.toString();

    public static void reload() {
        ShopConfigDao shopConfigDao = ConfigDaoFactory.getShopConfigDao();
        Map<String, ShopConfigBean> shopConfigBeanMap = new HashMap<>();
        shopConfigDao.getAll().forEach(bean ->
                    shopConfigBeanMap.put(
                            buildKey(bean.getOrder_channel_id(), bean.getCart_id(),bean.getCfg_name(), bean.getCfg_val1()),
                            bean
                    )
        );
        CacheHelper.reFreshSSB(KEY, shopConfigBeanMap);
        logger.info("shopConfig 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String order_channel_id, String cart_id, String name, String val1) {
        return order_channel_id + CacheKeyEnums.SKIP + cart_id + CacheKeyEnums.SKIP+ name + CacheKeyEnums.SKIP + val1;
    }

    /**
     * 获取指定店铺的第一个配置参数
     *
     * @param shopBean 店铺
     * @param name     配置名
     * @return String
     */
    public static String getVal1(ShopBean shopBean, ShopConfigEnums.Name name) {
        return getVal1(shopBean.getOrder_channel_id(), shopBean.getCart_id(), name);
    }

    /**
     * 获取指定店铺、配置名称、第一个参数的第二个参数
     *
     * @param shopBean 店铺
     * @param name     配置名
     * @param val1     第一参数
     * @return String
     */
    public static String getVal2(ShopBean shopBean, ShopConfigEnums.Name name, String val1) {
        return getVal2(shopBean.getOrder_channel_id(), shopBean.getCart_id(), name, val1);
    }

    /**
     * 获取指定店铺的第一个配置参数
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @param name             配置名
     * @return String
     */
    public static String getVal1(String order_channel_id, String cart_id, ShopConfigEnums.Name name) {
        List<ShopConfigBean> beans = getConfigs(order_channel_id, cart_id, name);
        return (CollectionUtils.isEmpty(beans)) ? "" : beans.get(0).getCfg_val1();
    }

    /**
     * 获取指定店铺、配置名称、第一个参数的第二个参数
     *
     * @param order_channel_id 渠道
     * @param cart_id          店铺
     * @param name             配置名
     * @param val1             第一参数
     * @return String
     */
    public static String getVal2(String order_channel_id, String cart_id, ShopConfigEnums.Name name, String val1) {
        ShopConfigBean bean = CacheHelper.getBean(KEY, buildKey(order_channel_id, cart_id, name.toString(), val1), selfClass);
        return (bean == null) ? "" : bean.getCfg_val2();
    }

    /**
     * 获取指定店铺的一个配置的所有参数
     *
     * @return List<ShopConfigBean>
     */
    public static List<ShopConfigBean> getConfigs(String order_channel_id, String cart_id, ShopConfigEnums.Name name) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        if (CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(order_channel_id, cart_id, name.toString(), ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
    }
}
