package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.DiscountRateBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.configs.dao.OrderChannelConfigDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.redis.CacheTemplateFactory;
import com.voyageone.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/3/17.
 * @version 2.0.0
 * @since 2.0.0
 */
public class DiscountRates {

    private static final Logger logger = LoggerFactory.getLogger(DiscountRates.class);

    /* redis key */
    private static final String KEY = CacheKeyEnums.ConfigData_DiscountRateConfigs.toString();

    private static HashOperations<String, String, DiscountRateBean> hashOperations = CacheTemplateFactory.getHashOperation();

    static {
        if (!CacheTemplateFactory.getCacheTemplate().hasKey(KEY)) {
            OrderChannelConfigDao orderChannelConfigDao = ConfigDaoFactory.getOrderChannelConfigDao();
            Map<String, DiscountRateBean> discountRateBeanMap = new HashMap<>();
            orderChannelConfigDao.getDiscountRate().forEach(bean -> {
                        discountRateBeanMap.put(
                                buildKey(bean.getOrder_channel_id(), bean.getShip_channel()),
                                bean
                        );
                    }
            );
            CacheHelper.reFreshSSB(KEY, discountRateBeanMap);
            logger.info("discount_rate 读取数量: " + hashOperations.size(KEY));
        }
    }

    /**
     * build redis hash Key
     *
     * @return key
     */
    private static String buildKey(String orderChannelId, String shipChannel) {
        return orderChannelId + CacheHelper.SKIP + shipChannel;
    }

    /**
     * 获取指定订单渠道、发货渠道的折扣信息
     *
     * @param orderChannelId 订单渠道
     * @param shipChannel    发货渠道
     * @return OrderChannel
     */
    public static BigDecimal getDiscountRate(String orderChannelId, String shipChannel) {
        DiscountRateBean bean = hashOperations.get(KEY, buildKey(orderChannelId, shipChannel));
        if (bean == null) {
            logger.warn("未查询到DiscountRateBean");
            return null;
        }

        if (StringUtils.isEmpty(bean.getDiscount_rate())) return null;
        return new BigDecimal(bean.getDiscount_rate());
    }
}
