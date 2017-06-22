package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aooer 2016/3/24.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Shops {
    private static final Class selfClass = Shops.class;

    private static final Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_ShopConfigs.toString();

    public static void reload() {
        Map<String, ShopBean> shopBeanMap = new HashMap<>();
        ConfigDaoFactory.getShopDao().getAll().forEach(bean -> {
                    shopBeanMap.put(
                            buildKey(bean.getCart_id(), bean.getOrder_channel_id()),
                            bean
                    );
                }
        );
        CacheHelper.reFreshSSB(KEY, shopBeanMap);
        logger.info("Shop 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String cart_id, String order_channel_id) {
        return cart_id + CacheKeyEnums.SKIP + order_channel_id;
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
        return CacheHelper.getBean(KEY, buildKey(cart_id, order_channel_id), selfClass);
    }

    public static KoalaConfig getShopKoala(String order_channel_id, String cart_id) {
        KoalaConfig koalaConfig = new KoalaConfig();
        ShopBean shopBean = CacheHelper.getBean(KEY, buildKey(cart_id, order_channel_id), selfClass);
        if(shopBean != null) {
            koalaConfig.setAppkey(shopBean.getAppKey());
            koalaConfig.setAppsecret(shopBean.getAppSecret());
            koalaConfig.setAppUrl(shopBean.getApp_url());
            koalaConfig.setCartId(Integer.valueOf(shopBean.getCart_id()));
            koalaConfig.setChannelId(shopBean.getOrder_channel_id());
            koalaConfig.setName(shopBean.getShop_name());
            koalaConfig.setSessionkey(shopBean.getSessionKey());
            koalaConfig.setTokenUrl(shopBean.getToken_url());
        }
        return koalaConfig;
    }
    /**
     * 获取指定渠道的店铺信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getChannelShopList(String order_channel_id) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        List<String> keyList = new ArrayList<>();
        if (CollectionUtils.isEmpty(keySet)) return null;

        keySet.forEach(k -> {
            if (k.endsWith(buildKey("", order_channel_id))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
    }


    /**
     * 获取指定店铺的渠道信息
     *
     * @return List<ShopBean>
     */
    public static List<ShopBean> getCartShopList(String cart_id) {
        Set<String> keySet = CacheHelper.getKeySet(KEY, selfClass);
        List<String> keyList = new ArrayList<>();
        if (CollectionUtils.isEmpty(keySet)) return null;

        keySet.forEach(k -> {
            if (k.startsWith(buildKey(cart_id, ""))) keyList.add(k);
        });
        Collections.sort(keyList);
        return CacheHelper.getBeans(KEY, keyList, selfClass);
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
        return CacheHelper.getAllBeans(KEY, selfClass);
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
