package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.ShopConfigEnums;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.beans.ShopConfigBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ShopConfigDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author aooer 2016/3/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ShopConfigs {
    private static final Log logger = LogFactory.getLog(ShopConfigs.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_ShopConfigConfigs.toString();

    private static HashOperations<String, String, ShopConfigBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            ShopConfigDao shopConfigDao = ConfigDaoFactory.getShopConfigDao();
            Map<String, ShopConfigBean> shopConfigBeanMap = new HashMap<>();
            shopConfigDao.getAll().forEach(bean -> {
                        shopConfigBeanMap.put(
                                buildKey(bean.getOrder_channel_id(), bean.getCart_id(), ShopConfigEnums.Name.valueOf(bean.getCfg_name()), bean.getCfg_val1()),
                                bean
                        );
                    }
            );
            CacheHelper.reFreshSSB(KEY, shopConfigBeanMap);
            logger.info("shopConfig 读取数量: " + hashOperations.size(KEY));
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String order_channel_id, String cart_id, ShopConfigEnums.Name name, String val1) {
        return order_channel_id + CacheHelper.SKIP + cart_id + CacheHelper.SKIP+ name + CacheHelper.SKIP + val1;
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
        ShopConfigBean bean = hashOperations.get(KEY, buildKey(order_channel_id, cart_id, name, val1));
        return (bean == null) ? "" : bean.getCfg_val2();
    }

    /**
     * 获取指定店铺的一个配置的所有参数
     *
     * @return List<ShopConfigBean>
     */
    public static List<ShopConfigBean> getConfigs(String order_channel_id, String cart_id, ShopConfigEnums.Name name) {
        Set<String> keySet = hashOperations.keys(KEY);
        if (CollectionUtils.isEmpty(keySet)) return null;

        List<String> keyList = new ArrayList<>();
        keySet.forEach(k -> {
            if (k.startsWith(buildKey(order_channel_id, cart_id, name, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY, keyList);
    }
}
