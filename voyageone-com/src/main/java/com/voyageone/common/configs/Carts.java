package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.ShopDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;
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
    private static final Log logger = LogFactory.getLog(Carts.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_CartConfigs.toString();

    private static HashOperations<String, String, CartBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            ShopDao shopDao = ConfigDaoFactory.getShopDao();
            Map<String, CartBean> cartBeanMap = new HashMap<>();
            shopDao.getAllCart().forEach(bean -> {
                cartBeanMap.put(buildKey(bean.getCart_id()), bean);
            });
            CacheHelper.reFreshSSB(KEY, cartBeanMap);
            logger.info("Cart 读取数量: " + hashOperations.size(KEY));
        }
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
        return hashOperations.get(KEY, buildKey(cart_id));
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
        List<CartBean> beans=hashOperations.values(KEY);
        return CollectionUtils.isEmpty(beans)
                ?new ArrayList<CartBean>()
                :beans.stream().sorted((a,b)->a.getCart_id().compareTo(b.getCart_id()))
                .collect(Collectors.toList());
    }
}
