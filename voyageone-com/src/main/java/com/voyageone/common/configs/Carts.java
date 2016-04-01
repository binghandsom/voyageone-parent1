package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ShopDao;
import com.voyageone.common.redis.CacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aooer 2016/3/18.
 * @version 2.0.0
 * @since 2.0.0
 */
public class Carts {
    private static final Class selfClass = Carts.class;

    private final static Logger logger = LoggerFactory.getLogger(selfClass);

    /* redis key */
    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_CartConfigs.toString();

    public static void reload() {
        ShopDao shopDao = ConfigDaoFactory.getShopDao();
        Map<String, CartBean> cartBeanMap = new HashMap<>();
        shopDao.getAllCart().forEach(bean ->cartBeanMap.put(buildKey(bean.getCart_id()), bean));
        CacheHelper.reFreshSSB(KEY, cartBeanMap);
        logger.info("Cart 读取数量: " + CacheHelper.getSize(KEY));
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String cart_id) {
        return cart_id;
    }

    /**
     * 获取指定渠道信息
     *
     * @return CartBean
     */
    public static CartBean getCart(String cart_id) {
        return CacheHelper.getBean(KEY, buildKey(cart_id), selfClass);
    }

    public static CartBean getCart(int cart_id) {
        return getCart(String.valueOf(cart_id));
    }

    /**
     * 获取指定平台的渠道信息
     *
     * @return List<CartBean>
     */
    public static List<CartBean> getCartList(PlatFormEnums.PlatForm platform) {
        return getAllCartList()
                .stream()
                .filter(bean -> bean.getPlatform_id().equals(platform.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有的渠道信息
     *
     * @return List<CartBean>
     */
    public static List<CartBean> getAllCartList() {
        List<CartBean> beans= CacheHelper.getAllBeans(KEY, selfClass);
        return CollectionUtils.isEmpty(beans)
                ?new ArrayList<>()
                :beans.stream().sorted((a,b)->a.getCart_id().compareTo(b.getCart_id()))
                .collect(Collectors.toList());
    }
}
