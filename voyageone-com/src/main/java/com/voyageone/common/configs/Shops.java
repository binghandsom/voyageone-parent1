package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import com.voyageone.common.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aooer 2016/3/24.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Shops {
    private static final Log logger = LogFactory.getLog(Shops.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_ShopConfigs.toString();

    private static HashOperations<String, String, ShopBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            Map<String, ShopBean> shopBeanMap = new HashMap<>();
            ConfigDaoFactory.getShopDao().getAll().forEach(bean -> {
                        shopBeanMap.put(
                                buildKey(bean.getCart_id(), bean.getOrder_channel_id()),
                                bean
                        );
                    }
            );
            CacheHelper.reFreshSSB(KEY, shopBeanMap);
            logger.info("Shop 读取数量: " + hashOperations.size(KEY));
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String cart_id, String order_channel_id) {
        return cart_id + CacheHelper.SKIP + order_channel_id;
    }

    /**
     * 获取指定店铺的基本信息（类型重载）
     *
     * @return ShopBean
     */
    public static ShopBean getShop(String order_channel_id, int cart_id) {
        return getShop(order_channel_id, String.valueOf(cart_id));
    }

    public static ShopBean getShop(OrderChannelBean channel, CartBean cart) {
        return getShop(channel.getOrder_channel_id(), cart.getCart_id());
    }

    /**
     * 获取指定店铺的基本信息
     *
     * @return ShopBean
     */
    public static ShopBean getShop(String order_channel_id, String cart_id) {
        return hashOperations.get(KEY, buildKey(cart_id, order_channel_id));
    }

    /**
     * 获取指定渠道的店铺信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getChannelShopList(String order_channel_id) {
        Set<String> keySet = hashOperations.keys(KEY);
        List<String> keyList = new ArrayList<>();
        if (CollectionUtils.isEmpty(keySet)) return null;

        keySet.forEach(k -> {
            if (k.endsWith(buildKey("", order_channel_id))) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY, keyList);
    }


    /**
     * 获取指定店铺的渠道信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getCartShopList(String cart_id) {
        Set<String> keySet = hashOperations.keys(KEY);
        List<String> keyList = new ArrayList<>();
        if (CollectionUtils.isEmpty(keySet)) return null;

        keySet.forEach(k -> {
            if (k.startsWith(buildKey(cart_id, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return hashOperations.multiGet(KEY, keyList);
    }

    public static List<ShopBean> getShopListByPlatform(PlatFormEnums.PlatForm platform) {
        List<ShopBean> beans = getShopList();
        return CollectionUtils.isEmpty(beans) ? null : beans
                .stream()
                .filter(b -> b.getPlatform_id().equals(platform.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有渠道的店铺信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getShopList() {
        return hashOperations.values(KEY);
    }

    /**
     * 获取指定店铺名（带渠道）
     *
     * @return ShopNameDis
     */
    public static String getShopNameDis(String order_channel_id, String cart_id) {
        ShopBean shopBean = getShop(order_channel_id, cart_id);
        if (shopBean == null) return "";
        return StringUtils.isNullOrBlank2(shopBean.getComment())
                ? shopBean.getShop_name()
                : shopBean.getShop_name() + "(" + shopBean.getComment() + ")";
    }


}
